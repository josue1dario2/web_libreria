package edu.egg.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.egg.entidades.Autor;
import edu.egg.entidades.Editorial;
import edu.egg.entidades.Libro;
import edu.egg.error.ErrorService;
import edu.egg.repositorios.AutorRepositorio;
import edu.egg.repositorios.EditorialRepositorio;
import edu.egg.repositorios.LibroRepositorio;

@Service
public class LibroService {
	@Autowired
	private EditorialRepositorio editorialRepositorio;
	@Autowired
	private AutorRepositorio autorRepositorio;
	@Autowired
	private LibroRepositorio libroRepositorio;
	
	
	@Transactional(readOnly = true)
	public Libro buscarLibroPorNombre(String titulo) throws ErrorService {
		
		Optional<Libro> respuesta = libroRepositorio.buscarLibroPorTitulo(titulo);
		
		if(respuesta.isPresent()) {
			
			Libro libro = respuesta.get();
			
			return libro;
			
		}else {
			throw new ErrorService("No existe libro con ese nombre");
		}
	}
	@Transactional(readOnly = true)
	public Libro buscarLibroPorISBN(long isbn) throws ErrorService {
	
		Optional<Libro> respuesta = libroRepositorio.findById(isbn);
		
		if(respuesta.isPresent()) {
			
			Libro libro  = respuesta.get();
			
			return libro;
		}else {
			throw new ErrorService("No existe libro con ese isbn");
		}
		
	}
	@Transactional(readOnly = true)
	public Collection<Libro>listarLibros() throws ErrorService{
		
		List<Libro> respuesta = libroRepositorio.findAll();
		
		if(respuesta != null) {
			
			return respuesta;
			
		}else {
			throw new ErrorService("No hay libro para mostrar");
		}
		
	}

	
	
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = {ErrorService.class, Exception.class})
	public void agregarLibro(String idEditorial,String idAutor,long isbn,String titulo,Integer anio,Integer ejemplares,Integer prestados) throws ErrorService {
		
		
		Editorial editorial = editorialRepositorio.getOne(idEditorial);
		
		Autor autor = autorRepositorio.getOne(idAutor);
		
		if(editorial != null && autor != null) {
			
			
			
			validar(isbn,titulo,anio,ejemplares,prestados);
			
			Libro libro = new Libro();
		
			libro.setAutor(autor);
			libro.setEditorial(editorial);
			libro.setIsbn(isbn);
			libro.setTitulo(titulo);
			libro.setAnio(anio);
			libro.setEjemplares(ejemplares);
			libro.setPrestados(prestados);
		
		libroRepositorio.save(libro);
		
		}else {
			throw new ErrorService("No se encontro la editorial ni el autor para este libro");
		}
		
	}
	
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = {ErrorService.class, Exception.class})
	public void modificarLibro(String idEditorial,String idAutor,long isbn,String titulo,Integer anio,Integer ejemplares,Integer prestados)throws ErrorService{
		
		Editorial editorial = editorialRepositorio.getOne(idEditorial);
        
        Autor autor = autorRepositorio.getOne(idAutor); 
		
		
		validar(isbn,titulo,anio,ejemplares,prestados);
		
		
		Optional<Libro> respuesta = libroRepositorio.findById(isbn);
		
		if(respuesta.isPresent()) {
			
			Libro libro = respuesta.get();
			
			//if(libro.getAutor().getId().equals(idAutor) && libro.getEditorial().getId().equals(idEditorial)) {
				libro.setAutor(autor);
				libro.setEditorial(editorial);
				libro.setTitulo(titulo);
				libro.setAnio(anio);
				libro.setEjemplares(ejemplares);
				libro.setPrestados(prestados);
				
				libroRepositorio.save(libro);
				
			//}else {
				//throw new ErrorService("No tiene permisos suficientes para realizar la operación");
			//}
			
		}else {
			throw new ErrorService("No existe una editorial con el identificador solicitado");
		}
		
	}
	
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = {ErrorService.class, Exception.class})
	public void eliminarLibro(long isbn)throws ErrorService{
		
		Optional<Libro> respuesta = libroRepositorio.findById(isbn);
		
		if(respuesta.isPresent()) {
			
			Libro libro = respuesta.get();
			
			if(libro !=null) {
				
			
				libroRepositorio.delete(libro);
				
			}
			
		}else {
			throw new ErrorService("No existe un libro con el identificador solicitado");
		}
		
	}
	
	public Date conversorStringDate(String fecha) {

        try {
            DateFormat fechaHora = new SimpleDateFormat("yyyy-MM-dd");
            Date convertido = fechaHora.parse(fecha);
            return convertido;
        } catch (java.text.ParseException ex) {
            Logger.getLogger(LibroService.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
	
	public void validar(long isbn,String titulo,Integer anio,Integer ejemplares,Integer prestados) throws ErrorService {
		
		if(isbn == 0) {
			throw new ErrorService("El isbn del libro no debe estar nulo.");
		}
		
		if(titulo == null || titulo.isEmpty()) {
			throw new ErrorService("El título del libro no debe estar nulo.");
		}
		
		if(anio == 0) {
			throw new ErrorService("El año del libro no debe estar nulo.");
		}
		
		if(ejemplares == 0) {
			throw new ErrorService("La cantidad de libros no puede ser 0.");
		}
		
		if(prestados > 0) {
			throw new ErrorService("No se puede realizar un prestamos aun.");
		}
	}

}
