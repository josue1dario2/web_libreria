package edu.egg.controladores;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


import edu.egg.entidades.Autor;
import edu.egg.error.ErrorService;
import edu.egg.service.AutorService;

@Controller
@RequestMapping("/autor")
public class AutorController {

	@Autowired
	private AutorService autorService;
	
	@GetMapping("")
	public String autor() {
		
		return "autor.html";
	}
	@PreAuthorize("hasAnyRole('ROLE_CLIENTE_REGISTRADO')")
	@GetMapping ("/listarAutor")
	public String listar(ModelMap modelo) {
		
		try {
			
		List<Autor> autores = autorService.listarAutores();
		
			modelo.put("autores", autores);
			return "listarAutor.html";
			
		}catch(ErrorService e) {
			modelo.put("error", e.getMessage());
			
			return "listarAutor.html";
		}
	}

	@PreAuthorize("hasAnyRole('ROLE_CLIENTE_REGISTRADO')")
	@GetMapping("/registrarAutor")
	public String registrarAutor() {
		return "registrarAutor.html";
	}
	@PostMapping("/registrarAutor")
	public String cargarAutor(ModelMap modelo,@RequestParam String nombre) throws ErrorService{
		
		try {
			
			autorService.registrarAutor(nombre);
			
		}catch(ErrorService ex) {
			
			modelo.put("error", ex.getMessage());
			return "registrarAutor.html";
		}catch(Exception e) {
			
			modelo.put("error", e.getMessage());
			modelo.put("nombre", nombre);
			
			return "registrarAutor.html";
		}
		
		modelo.addAttribute("mensaje", "Atributo");
		modelo.put("mensaje", "Has registrado el Autor exitosamente");
		
		return "registrarAutor.html";
		
		//return "autor.html";
	}
	@GetMapping("/modificar-autor")
	public String modificarAutor(@RequestParam String id,ModelMap modelo) {
		
		try {
			
			Autor autor = autorService.buscarAutorPorId(id);
			modelo.addAttribute("autor", autor);
			
		}catch(ErrorService e) {
			
			modelo.addAttribute("error", e.getMessage());
		}
		
		return "modificarAutor.html";
	}
	@PreAuthorize("hasAnyRole('ROLE_CLIENTE_REGISTRADO')")
	@PostMapping("/actualizar-autor")
	public String actualizarAutor(ModelMap modelo,
			HttpSession session,
			@RequestParam String id,
			@RequestParam String nombre) {
		
		Autor autor = null;
		
		try {
			
			autor = autorService.buscarAutorPorId(id);
			autorService.modificarAutor(id, nombre);
			session.setAttribute("autorsession", autor);
			modelo.put("mensaje", "Has modificado el Autor exitosamente");
			
			
		}catch(ErrorService e) {
			modelo.addAttribute("error", e.getMessage());
			return "listarAutor.html";
		}
		return "autor.html";
		
	}
	
	
	@PreAuthorize("hasAnyRole('ROLE_CLIENTE_REGISTRADO')")
	@PostMapping("/eliminar-autor")
	public String eliminarAutor(ModelMap modelo,
			HttpSession session,
			@RequestParam String id) {
		
		Autor autor = null;
		
		try {
			
			autor = autorService.buscarAutorPorId(id);
			autorService.eliminarAutor(id);
			modelo.put("mensaje", "Has eliminado el Autor exitosamente");
			
			
		}catch(ErrorService e) {
			modelo.addAttribute("error", e.getMessage());
			return "listarAutor.html";
		}
		return "autor.html";
		
	}
	
	//Pruba
	
	@PreAuthorize("hasAnyRole('ROLE_CLIENTE_REGISTRADO')")
	@GetMapping("/eliminar-autor-get/{id}")
	public String eliminarAutorGet(ModelMap modelo,
			@PathVariable String id) throws ErrorService {
		
		autorService.eliminarAutor(id);
		
		modelo.put("mensaje", "Has eliminado el Autor exitosamente");
		
		
		//Llamar al metodo listar y pasarle el modelo para que me imprima el mensaje
		return listar(modelo);
		
	}
	
}
