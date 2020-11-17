package edu.egg.controladores;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import edu.egg.entidades.Cliente;
import edu.egg.error.ErrorService;
import edu.egg.service.ClienteService;

@Controller
@RequestMapping("/cliente")
public class ClienteController {

	@Autowired
	private ClienteService clienteService;
	
	@PreAuthorize("hasAnyRole('ROLE_CLIENTE_REGISTRADO')")
	@GetMapping("/editar-perfil")
	public String editarPerfil(@RequestParam Long documento, ModelMap model) {
		
		try {
			
			Cliente cliente = clienteService.buscarClientePorDocumento(documento);
			model.put("cliente", cliente);
			model.addAttribute("cliente",cliente);
			
		}catch(ErrorService e) {
			model.addAttribute("error", e.getMessage());
		}
		return "perfil.html";
		
	}
	
	@PreAuthorize("hasAnyRole('ROLE_CLIENTE_REGISTRADO')")
	@PostMapping("/actualizar-perfil")
	public String registro(ModelMap modelo,
			HttpSession session,
			@RequestParam String documento,
			@RequestParam String nombre,
			@RequestParam String apellido,
			@RequestParam String domicilio,
			@RequestParam String telefono,
			@RequestParam String mail,
			@RequestParam String clave,
			@RequestParam String repetir) throws ErrorService {
		
		
		try {
			
		    Cliente cliente = clienteService.buscarClientePorDocumento(Long.parseLong(documento));
			clienteService.modificarCliente(Long.parseLong(documento), nombre, apellido, domicilio, telefono,mail, clave, repetir);
			
			modelo.put("cliente", cliente);
			session.setAttribute("clientesession", cliente);
			modelo.put("mensaje", "Has modificado tu perfil exitosamente");
			
			return "prestamo.html";
		
		}catch(ErrorService ex) {
			
			Cliente cliente = clienteService.buscarClientePorDocumento(Long.parseLong(documento));
			modelo.addAttribute("cliente",cliente);
			modelo.put("error",ex.getMessage());
			modelo.put("documento",documento);
			modelo.put("nombre", nombre);
			modelo.put("apellido", apellido);
			modelo.put("domicilio", domicilio);
			modelo.put("telefono", telefono);
			modelo.put("mail", mail);
			modelo.put("clave", clave);
			modelo.put("repetir", repetir);
			
			return "perfil.html";
			
		}
	}
	
	@PreAuthorize("hasAnyRole('ROLE_CLIENTE_REGISTRADO')")
	@PostMapping("/eliminarCliente")
	public String eliminarCliente(ModelMap modelo,
			@RequestParam String documento) {
		
		Cliente cliente = null;
		
		try {
			
			cliente = clienteService.buscarClientePorDocumento(Long.parseLong(documento));
			clienteService.eliminarCliente(Long.parseLong(documento));
			modelo.put("mensaje", "Ha eliminado exitosamente");
			
			
		}catch(ErrorService e) {
			modelo.addAttribute("error", e.getMessage());
			return "listarCliente.html";
		}
		return "prestamo.html";
		
	}
	
	@PreAuthorize("hasAnyRole('ROLE_CLIENTE_REGISTRADO')")
	@GetMapping("/listarClientes")
	public String listarClientes(ModelMap modelo) {
	
		try {
			
			List<Cliente> clientes = clienteService.listarClientes();
			modelo.put("clientes", clientes);
			
		}catch(ErrorService e) {
			modelo.put("error", e.getMessage());
			
			return "prestamo.html";
		}
		
		return "listarCliente.html";
		
	}
}
