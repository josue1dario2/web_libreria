package edu.egg.service;

import java.util.List;
import java.util.Optional;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import edu.egg.entidades.Autor;
import edu.egg.error.ErrorService;
import edu.egg.repositorios.AutorRepositorio;



@Service
public class AutorService {
	
	@Autowired
	private AutorRepositorio autorRepositorio;
	
	
	@Transactional(readOnly = true)
	public Autor buscarAutorPorNombre(String nombre)throws ErrorService {
		
		
		Optional<Autor> respuesta = autorRepositorio.buscarAutorPorNombre(nombre);
		
		if(respuesta.isPresent()) {
			
			Autor autor = respuesta.get();
			
			return autor;
		}else {
			throw new ErrorService("No existe autor con el nombre ingresado");
		}
		
		
	}
	
	
	@Transactional(readOnly = true)
	public Autor buscarAutorPorId(String id) throws ErrorService {
		
		Optional<Autor> respuesta = autorRepositorio.findById(id);
		
		if(respuesta.isPresent()) {
			Autor autor = respuesta.get();
			
			return autor;
		}else {
			throw new ErrorService("No se encuentra el autor");
		}
		
	}
	
	@Transactional(readOnly = true)
	public List<Autor>listarAutores() throws ErrorService{
		
		List<Autor> respuesta = autorRepositorio.findAll();
		
		if(respuesta != null) {
			
			return respuesta;
			
		}else {
			throw new ErrorService("No se encontro autores");
		}
		
	
		
	}
	

	
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = {ErrorService.class, Exception.class})
	public void registrarAutor(String nombre) throws ErrorService {
		
		validar(nombre);
		
		Autor autor = new Autor();
		
		autor.setNombre(nombre);
		
		autorRepositorio.save(autor);
		
		
	}
	
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = {ErrorService.class, Exception.class})
	public void modificarAutor(String id,String nombre) throws ErrorService{
		
		validar(nombre);
		
		Optional<Autor> respuesta = autorRepositorio.findById(id);
		
		if(respuesta.isPresent()) {
		
		Autor autor = respuesta.get();
		autor.setNombre(nombre);
		
		autorRepositorio.save(autor);
		
		}else {
			throw new ErrorService("No se encontro el autor solicitado");
		}
		
	
	}
	
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = {ErrorService.class, Exception.class})
	public void eliminarAutor(String id) throws ErrorService {
		
        Optional<Autor> respuesta = autorRepositorio.findById(id);
		
		if(respuesta.isPresent()) {
		
		Autor autor = respuesta.get();
		
		autorRepositorio.delete(autor);
	
		
		}else {
			throw new ErrorService("No se encontro el autor solicitado");
		}
	}
	
	public void validar(String nombre) throws ErrorService{
		
         if(nombre == null || nombre.isEmpty()) {
			
			throw new ErrorService("El nombre del autor no puede ser nulo.");
		}
		
	}

	
}
