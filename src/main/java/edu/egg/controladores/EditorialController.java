package edu.egg.controladores;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner.Mode;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import edu.egg.entidades.Editorial;
import edu.egg.error.ErrorService;
import edu.egg.service.EditorialService;

@Controller
@RequestMapping("/editorial")
public class EditorialController {

	@Autowired
	private EditorialService editorialService;
	
	@GetMapping("")
	public String editorial() {
		
		return "editorial.html";
	}
	
	@PreAuthorize("hasAnyRole('ROLE_CLIENTE_REGISTRADO')")
	@GetMapping("/listarEditorial")
	public String listar(ModelMap modelo) {
		
		try {
			
			List<Editorial> editoriales = editorialService.listarEditoriales();
			
			modelo.put("editoriales", editoriales);
			return "listarEditorial.html";
			
		}catch(ErrorService e) {
			modelo.put("error", e.getMessage());
			
			return "listarEditorial.html";
		}
	}
	
	@PreAuthorize("hasAnyRole('ROLE_CLIENTE_REGISTRADO')")
	@GetMapping("/registrarEditorial")
	public String registrarEditorial() {
		return "registrarEditorial.html";
	}
	
	@PostMapping("/registrarEditorial")
	public String cargarEditorial(ModelMap modelo,@RequestParam String nombre) throws ErrorService{
		
		try {
			
			editorialService.registrarEditorial(nombre);
			
			modelo.put("mensaje", "Ha registrado la editorial exitosamente");
			
		}catch(ErrorService ex) {
			
			modelo.put("error", ex.getMessage());
			return "registrarEditoria.html";
			
		}catch(Exception e) {
			
			modelo.put("error",e.getMessage());
			modelo.put("nombre", nombre);
			
			return  "registrarEditorial.html";
		}
		
		return"registrarEditorial.html";
	}
	
	@GetMapping("/modificar-editorial")
	public String modificarEditorial(@RequestParam String id,ModelMap modelo) {
		
		try {
			
			Editorial editorial = editorialService.buscarEditorialPorId(id);
			modelo.addAttribute("editorial",editorial);
			
		}catch(ErrorService e) {
			
			modelo.addAttribute("error",e.getMessage());
		}
		
		return "modificarEditorial.html";
	}
	@PreAuthorize("hasAnyRole('ROLE_CLIENTE_REGISTRADO')")
	@PostMapping("/actualizar-editorial")
	public String actualizarEditorial(ModelMap modelo,
			HttpSession session,
			@RequestParam String id,
			@RequestParam String nombre) {
		
		Editorial editorial = null;
		
		try {
			
			editorial = editorialService.buscarEditorialPorId(id);
			editorialService.modificarEditorial(id, nombre);
			session.setAttribute("editorialsession", editorial);
			modelo.put("mensaje", "Se ha modificado la editorial exitosamente");
			
			
			
		}catch(ErrorService e) {
			modelo.addAttribute("error", e.getMessage());
			return "listarEditorial.html";
		}
		return "editorial.html";
		
	}
	
	@PreAuthorize("hasAnyRole('ROLE_CLIENTE_REGISTRADO')")
	@PostMapping("/eliminar-editorial")
	public String eliminarEditorial(ModelMap modelo,
			HttpSession session,
			@RequestParam String id) {
	
		Editorial editorial = null;
		
		try {
			
			editorial = editorialService.buscarEditorialPorId(id);
			editorialService.eliminarEditorial(id);
			modelo.put("mensaje", "Ha eliminado la editorial exitosamente");
			
			
		}catch(ErrorService e) {
			
			modelo.addAttribute("error", e.getMessage());
			return "listarEditorial.html";
		}
		return "editorial.html";
		
	}
	
	@PreAuthorize("hasAnyRole('ROLE_CLIENTE_REGISTRADO')")
	@GetMapping("/eliminar-editorial-get/{id}")
	public String eliminarEditorialGet(ModelMap modelo,
			@PathVariable String id) throws ErrorService {
		
		editorialService.eliminarEditorial(id);
		
		modelo.put("mensaje", "Se ha eliminado la editorial exitosamente");
		
		//return "redirect:/editorial/listarEditorial";
		
		return listar(modelo);
		
	}
	
	
	
}
