package edu.egg.controladores;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import edu.egg.error.ErrorService;
import edu.egg.service.ClienteService;

@Controller
@RequestMapping("/")
public class PortalController {
	
	@Autowired
	private ClienteService clienteService;
	
	@GetMapping("/")
	public String index() {
		
		return "index.html";
	}
	
	@GetMapping("/login")
	public String login(@RequestParam(required = false) String error, ModelMap model) {
		
		if(error != null) {
		model.put("error", "Número de documento o clave incorrectos");
		}
		
		return "login.html";
	}
	@PreAuthorize("hasAnyRole('ROLE_CLIENTE_REGISTRADO')")
	@GetMapping("/prestamo")
	public String prestamo() {
		return "prestamo.html";
	}
	
	@GetMapping("/registro")
	public String registro() {
		
		return "registro.html";
	}
	
	@PostMapping("/registro")
	public String registro(ModelMap modelo, 
			@RequestParam String documento,
			@RequestParam String nombre,
			@RequestParam String apellido,
			@RequestParam String domicilio,
			@RequestParam String telefono,
			@RequestParam String mail,
			@RequestParam String clave,
			@RequestParam String repetir) throws ErrorService {
		
	
		
		try {
			
			clienteService.crearCliente(Long.parseLong(documento), nombre, apellido, domicilio, telefono,mail, clave,repetir);
			
			modelo.put("mensaje", "Se ha registrado exitosamente");
		
		}catch(ErrorService ex) {
			
			modelo.put("error",ex.getMessage());
			
			return "registro.html";
		}catch(Exception e) {
			
			modelo.put("error",e.getMessage());
			modelo.put("documento",documento);
			modelo.put("nombre", nombre);
			modelo.put("apellido", apellido);
			modelo.put("domicilio", domicilio);
			modelo.put("telefono", telefono);
			modelo.put("mail", mail);
			modelo.put("clave", clave);
			modelo.put("repetir", repetir);
			
			
			return "registro.html";
		}
		
		modelo.put("titulo","Bienvenido");
		modelo.put("descripcion", "Te registrarse de manera satisfactoria");
		
		return "index.html";
	}
	

}
