package edu.egg.service;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Optional;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.egg.entidades.Cliente;
import edu.egg.entidades.Libro;
import edu.egg.entidades.Prestamo;
import edu.egg.error.ErrorService;
import edu.egg.repositorios.ClienteRepositorio;
import edu.egg.repositorios.LibroRepositorio;
import edu.egg.repositorios.PrestamoRepositorio;
@Service
public class PrestamoService {
	@Autowired
	private LibroRepositorio libroRepositorio;
	@Autowired
	private ClienteRepositorio clienteRepositorio;
	@Autowired
	private PrestamoRepositorio prestamoRepositorio;
	
	
	@Transactional(readOnly = true)
	public Prestamo buscarPrestamoPorId(String id) throws ErrorService {
		
		Optional<Prestamo> respuesta = prestamoRepositorio.findById(id);
		
		if(respuesta.isPresent()) {
			
			Prestamo prestamo = respuesta.get();
			
			return prestamo;
		}else {
			throw new ErrorService("No se encontro prestamo ese id");
		}
		
	}
	@Transactional(readOnly = true)
	public Collection<Prestamo>listarPrestamos() throws ErrorService{
		
		List<Prestamo> respuesta = prestamoRepositorio.findAll();
		
		if(respuesta != null) {
			
			return  respuesta;
		}else {
			throw new ErrorService("No hay prestamos para mostrar");
		}
	}
	
	
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = {ErrorService.class, Exception.class})
	public void agregarPrestamo(long isbn,long documento,Date devolucion) throws ErrorService {
		
		Prestamo prestamo = new Prestamo();
		
		Optional<Libro> respuestalibro = libroRepositorio.findById(isbn);
		
		Optional<Cliente> respuestacliente = clienteRepositorio.findById(documento);
		
		
		
		if(respuestalibro.isPresent() && respuestacliente.isPresent()) {
			
			Libro libro1 = respuestalibro.get();
				
			Cliente cliente = respuestacliente.get();
			
			prestamo.setLibro(libro1);
			prestamo.setCliente(cliente);
			
		 if (libro1.getEjemplares() > 0) {

			 
			 Date fecha = new Date();
			 prestamo.setFecha(fecha);
			 prestamo.setDevolucion(devolucion);
			
		
		
           int pres1 = libro1.getPrestados() + 1;

           int eje1 = libro1.getEjemplares() - 1;
           
           libro1.setPrestados(pres1);

           libro1.setEjemplares(eje1);
           
           
           libroRepositorio.save(libro1);
           
           
           prestamoRepositorio.save(prestamo);
        
		 }else {
			 throw new ErrorService("No hay ejemplares para prestar");
		 }
		
		}else {
			throw new ErrorService("No se encontro libro ni cliente");
		}
		
	}
	

	@Transactional
	public void devolucion(long isbn,long documento,String id,Integer puntuacion) throws ErrorService {
		
		if(puntuacion < 0 || puntuacion > 5) {
			
			throw new ErrorService("La puntuación es del 1 al 5");
		}else {
		
		Libro libro = libroRepositorio.findById(isbn).get();
		
		Optional<Prestamo> respuesta = prestamoRepositorio.findById(id);
		
		Optional<Cliente> respuesta2 = clienteRepositorio.findById(documento);
		
		if(respuesta.isPresent() && respuesta2.isPresent()) {
			
			Prestamo prestamo = respuesta.get();
			
			if(prestamo.getLibro().getIsbn()==isbn && prestamo.getCliente().getDocumento()==documento) {
				
				
			prestamo.setFechaEntrega(new Date());
			
			int diferencia = (int) ((prestamo.getDevolucion().getTime()-prestamo.getFechaEntrega().getTime()));
			
			double plata= (diferencia*20);
			
			prestamo.setMulta(plata);
			
			prestamoRepositorio.delete(prestamo);
				
			int pres = libro.getPrestados() - 1;

            int eje = libro.getEjemplares() + 1;

            libro.setEjemplares(eje);
            libro.setPrestados(pres);
            
            
            if (libro.getPuntuacion()!= null) {
				libro.setPuntuacion((libro.getPuntuacion()+puntuacion)/2);
			} else {
				libro.setPuntuacion(puntuacion);
			}
            
            
            libroRepositorio.save(libro);
            
            
          
			
			}else {
				throw new ErrorService("No tiene permisos suficientes para realizar la operación");
			}
			
		}else {
			throw new ErrorService("No se encontro el prestamo solicitado");
		}
		
		}
	}
	
	public void validar(String id) throws ErrorService {
		
		if(id == null || id.isEmpty()) {
			
			throw new ErrorService("El id del prestamo no debe ser nulo");
		}
		
        
	}
}
