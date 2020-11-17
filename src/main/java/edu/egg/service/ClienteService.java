package edu.egg.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import edu.egg.entidades.Cliente;
import edu.egg.entidades.Libro;
import edu.egg.entidades.Prestamo;
import edu.egg.error.ErrorService;
import edu.egg.repositorios.ClienteRepositorio;
import edu.egg.repositorios.PrestamoRepositorio;
@Service
public class ClienteService implements UserDetailsService{
	
	@Autowired
	private ClienteRepositorio clienteRepositorio;
	
	@Autowired
	private PrestamoRepositorio prestamoRepositorio;
	
	@Autowired
	private NotificacionService notificacionService;
	
	@Transactional(readOnly = true)
	public Cliente buscarClientePorNombre(String nombre) throws ErrorService {
		
		Optional<Cliente>respuesta = clienteRepositorio.buscarClientePorNombre(nombre);
		
		if(respuesta.isPresent()) {
			Cliente cliente = respuesta.get();
			
			return cliente;
		}else {
			throw new ErrorService("No hay clientes con ese nombre");
		}
		
	}
	@Transactional(readOnly = true)
	public Cliente buscarClientePorDocumento(Long documento) throws ErrorService {
	
		Optional<Cliente> respuesta = clienteRepositorio.findById(documento);
		
		if(respuesta.isPresent()) {
			
			Cliente cliente = respuesta.get();
			
			return cliente;
		}else {
			throw new ErrorService("No se encontro cliente con ese documento");
		}
	}
	@Transactional(readOnly = true)
	public List<Cliente>listarClientes() throws ErrorService{
		
		List<Cliente> respuesta = clienteRepositorio.findAll();
		
		if(respuesta != null) {
			
			return respuesta;
		}else {
			throw new ErrorService("No hay cliente para mostrar");
		}
	}
	
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = {ErrorService.class, Exception.class})
	public void crearCliente(Long documento,String nombre,String  apellido,String domicilio,String telefono,String mail,String clave,String repetir) throws ErrorService {
		
		Optional<Cliente> respuesta = clienteRepositorio.findById(documento);
		
		if(respuesta.isPresent()){
			
			throw new ErrorService("El cliente ya existe ");
		}
		
		
		validar(documento,nombre,apellido,domicilio,telefono,mail,clave,repetir);
		
		Cliente cliente = new Cliente();
		
		cliente.setDocumento(documento);
		cliente.setNombre(nombre);
		cliente.setApellido(apellido);
		cliente.setDomicilio(domicilio);
		cliente.setTelefono(telefono);
		cliente.setMail(mail);
		String encriptada = new BCryptPasswordEncoder().encode(clave);
		
		cliente.setClave(encriptada);
		
		clienteRepositorio.save(cliente);
		
		notificacionService.enviar("Bienvedidos a la Libreria Egg Se ha regitrardo exitosamente", "Libreria Egg", cliente.getMail());
		
		
	}
	
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = {ErrorService.class, Exception.class})
	public void modificarCliente(Long documento,String nombre,String  apellido,String domicilio,String telefono,String mail,String clave,String repetir)throws ErrorService{
		
		validar(documento,nombre,apellido,domicilio,telefono,mail,clave,repetir);
		
		Optional<Cliente> respuesta = clienteRepositorio.findById(documento);
		
		if(respuesta.isPresent()) {
			
			Cliente cliente = respuesta.get();
			cliente.setDocumento(documento);
			cliente.setNombre(nombre);
			cliente.setApellido(apellido);
			cliente.setDomicilio(domicilio);
			cliente.setTelefono(telefono);
			cliente.setMail(mail);
			String encriptada = new BCryptPasswordEncoder().encode(clave);
			cliente.setClave(encriptada);
			
			clienteRepositorio.save(cliente);
			
			notificacionService.enviar("Libreria Egg Se ha modificado su cuenta", "Libreria Egg", cliente.getMail());
			
		}else {
			throw new ErrorService("No se encontro el cliente solicitado");
		}
		
	}
	
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = {ErrorService.class, Exception.class})
	public void eliminarCliente(Long documento)throws ErrorService{
		
		Optional<Cliente> respuesta = clienteRepositorio.findById(documento);
		
		if(respuesta.isPresent()) {
			
			Cliente cliente = respuesta.get();
			
			clienteRepositorio.delete(cliente);
		
		}else {
			throw new ErrorService("No se encontro el cliente solicitado");
		}
		
	}
	
	@Transactional(readOnly = true)
	public void login(Long documento,String clave) throws ErrorService {
		
		buscarClientePorDocumento(documento);
		
		
	}
		
	public List<Libro> saberCuantosLibrosDebe(String documento) throws Error{
        
        List<Prestamo> prestamos = prestamoRepositorio.buscarPrestamosPorDNICliente(documento);
        List<Libro> libros = new ArrayList<>();
        System.out.println(prestamos.size());
        for (Prestamo prestamo : prestamos) {
            libros.add(prestamo.getLibro());
        }
                
        return libros;
    }
	
	public void validar(Long documento,String nombre,String  apellido,String domicilio,String telefono,String mail,String clave,String repetir) throws ErrorService{
		
		
		if(documento < 999999 || documento > 99999999) {
			throw new ErrorService("El numero de documento tiene que contener de 6 a 8 digitos");
		}
		
		if(nombre == null || nombre.isEmpty()) {
			throw new ErrorService("El nombre del cliente no puede ser nulo.");
		}
		
		if(apellido == null || apellido.isEmpty()) {
			throw new ErrorService("El apellido del cliente no puede ser nulo.");
		}
		
		if(domicilio == null || domicilio.isEmpty()) {
			throw new ErrorService("El domicilio del cliente no puede ser nulo.");
		}
		
		if(telefono == null || telefono.isEmpty()) {
			throw new ErrorService("El telefono del cliente no puede ser nulo.");
		}
		if(mail == null || mail.isEmpty()) {
			throw new ErrorService("El mail del cliente no puede ser nulo.");
		}
		
		if(clave == null || clave.isEmpty() || clave.length() <= 6) {
			throw new ErrorService("La clave tiene que tener como mínimo 6 digitos");
		}
		
		if(!clave.equals(repetir)) {
			throw new ErrorService("Las claves deben ser iguales");
		}
		
		
	}
	@Override
	public UserDetails loadUserByUsername(String documento) {
		
		Optional<Cliente> respuesta = clienteRepositorio.buscarClientePorDocumento(Long.parseLong(documento));
		
		
		if(respuesta.isPresent()) {
			
			Cliente cliente = respuesta.get();
			
			List<GrantedAuthority> permisos = new ArrayList<>();
			
			GrantedAuthority p1 = new SimpleGrantedAuthority("ROLE_CLIENTE_REGISTRADO");
			permisos.add(p1);
			
			ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
			HttpSession session = attr.getRequest().getSession(true);
			session.setAttribute("clientesession", cliente);

			User user = new User(String.valueOf(cliente.getDocumento()), cliente.getClave(), permisos);

			return user;
			
		} else {
			
			return null;
		}
			
		
	}
		
	
	
	
}

