package edu.egg.service;

import java.util.List;
import java.util.Optional;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.egg.entidades.Editorial;
import edu.egg.error.ErrorService;
import edu.egg.repositorios.EditorialRepositorio;

@Service
public class EditorialService {
	@Autowired
	private EditorialRepositorio editorialRepositorio;
	
	@Transactional(readOnly = true)
	public Editorial buscarEditorialPorNombre(String nombre) throws ErrorService {
		
		Optional<Editorial> respuesta = editorialRepositorio.buscarEditorialPorNombre(nombre);
		
		if(respuesta.isPresent()) {
			
			Editorial editorial = respuesta.get();
			
			return editorial;
		}else {
			throw new ErrorService("No existe editorial con ese nombre");
		}
		
		
	}
	@Transactional(readOnly = true)
	public Editorial buscarEditorialPorId(String id)throws ErrorService {
		
		Optional<Editorial> respuesta = editorialRepositorio.findById(id);
		
		if(respuesta.isPresent()) {
			
			Editorial editorial = respuesta.get();
			
			return editorial;
			
		}else {
			throw new ErrorService("No existe editorial con ese id");
		}
		
	}
	@Transactional(readOnly = true)
	public List<Editorial>listarEditoriales() throws ErrorService{
		
		List<Editorial> respuesta = editorialRepositorio.findAll();
		
		if(respuesta != null) {
			
			return respuesta;
		
		}else {
			throw new ErrorService("No hay editoriales para mostrar");
		}
	}
	
	
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = {ErrorService.class, Exception.class})
	public void registrarEditorial(String nombre) throws ErrorService{
		
		validar(nombre);
		
		Editorial editorial = new Editorial();
		
		editorial.setNombre(nombre);
		
		editorialRepositorio.save(editorial);
	}
	
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = {ErrorService.class, Exception.class})
    public void modificarEditorial(String id,String nombre) throws ErrorService{
		
		validar(nombre);
		
		Optional<Editorial> respuesta = editorialRepositorio.findById(id);
		
		if(respuesta.isPresent()) {
		
			Editorial editorial = respuesta.get();
			
			editorial.setNombre(nombre);
			
			editorialRepositorio.save(editorial);
		
		
		}else {
			throw new ErrorService("No se encontro la editorial solicitada");
		}
		
	
	}
    
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = {ErrorService.class, Exception.class})
    public void eliminarEditorial(String id) throws ErrorService {
		
        Optional<Editorial> respuesta = editorialRepositorio.findById(id);
		
		if(respuesta.isPresent()) {
		
		Editorial editorial = respuesta.get();
		
		editorialRepositorio.delete(editorial);
		
		
		}else {
			throw new ErrorService("No se encontro la editorial solicitada");
		}
	}
	
	public void validar(String nombre) throws ErrorService{
		
        if(nombre == null || nombre.isEmpty()) {
			
			throw new ErrorService("El nombre de la editorial no puede ser nulo.");
		}
		
	}
	
	
}
