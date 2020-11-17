package edu.egg.controladores;

import java.util.Date;
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
import edu.egg.entidades.Cliente;
import edu.egg.entidades.Editorial;
import edu.egg.entidades.Libro;
import edu.egg.entidades.Prestamo;
import edu.egg.error.ErrorService;
import edu.egg.repositorios.AutorRepositorio;
import edu.egg.repositorios.EditorialRepositorio;
import edu.egg.repositorios.LibroRepositorio;
import edu.egg.repositorios.PrestamoRepositorio;
import edu.egg.service.ClienteService;
import edu.egg.service.LibroService;
import edu.egg.service.PrestamoService;

@Controller
@RequestMapping("/libro")
public class LibroController {

	@Autowired
	private LibroService libroService;
	
	@Autowired
	private AutorRepositorio autorRepositorio;
	@Autowired
	private EditorialRepositorio editorialRepositorio;
	
	@Autowired
	private LibroRepositorio libroRepositorio;
	
	@Autowired
	private PrestamoRepositorio prestamoRepositorio;
	
	@Autowired
	private PrestamoService prestamoService;

	
	@GetMapping("")
	public String libro() {
		
		return "libro.html";
	}
	
	

	@PreAuthorize("hasAnyRole('ROLE_CLIENTE_REGISTRADO')")
	@GetMapping("/registrarLibro")
	public String agregarLibro(ModelMap modelo) {

		List<Autor> autores = autorRepositorio.findAll();
		
		List<Editorial> editoriales = editorialRepositorio.findAll();
		
		modelo.put("autores", autores);
		modelo.put("editoriales", editoriales);
		
		return "registrarLibro.html";
	}
	
	@PostMapping("/registrarLibro")
	public String registro(ModelMap modelo,  
			@RequestParam String isbn,
			@RequestParam String titulo,
			@RequestParam String anio,
			@RequestParam String ejemplares,
			@RequestParam String idAutor,
			@RequestParam String idEditorial) throws ErrorService {
		
			
		
		try {
			
			libroService.agregarLibro(idEditorial, idAutor, Long.parseLong(isbn), titulo,Integer.valueOf(anio), Integer.valueOf(ejemplares),Integer.valueOf(0));
			
		
		}catch(Exception e) {
			
			List<Autor> autores = autorRepositorio.findAll();
			
			List<Editorial> editoriales = editorialRepositorio.findAll();
			
			modelo.put("editoriales", editoriales);
			modelo.put("autores", autores);
			modelo.put("error",e.getMessage());
			modelo.put("isbn", isbn);
			modelo.put("titulo", titulo);
			modelo.put("anio", anio);
			modelo.put("ejemplares", ejemplares);
			modelo.put("prestados", 0);
			
			
			return "registrarLibro.html";
		}
		
		modelo.put("mensaje", "Has registrado el libro exitosamente :P");
		
		
		return "registrarLibro.html";
	}
	
	
	@PreAuthorize("hasAnyRole('ROLE_CLIENTE_REGISTRADO')")
	@GetMapping("/modificarLibro")
	public String modificarLibro(@RequestParam String isbn ,ModelMap modelo) throws NumberFormatException, ErrorService {
		
		
		Libro libro = libroService.buscarLibroPorISBN(Long.parseLong(isbn));
		modelo.put("libro",libro);

		List<Autor> autores = autorRepositorio.findAll();
		
		List<Editorial> editoriales = editorialRepositorio.findAll();
		
		modelo.put("autores", autores);
		modelo.put("editoriales", editoriales);
	
		
		return "modificarLibro.html";
	}
	
	
	@PostMapping("/modificarLibroPost")
	public String modificar(ModelMap modelo,
			@RequestParam String isbn,
			@RequestParam String titulo,
			@RequestParam String anio,
			@RequestParam String ejemplares,
			@RequestParam String prestados,
			@RequestParam String idAutor,
			@RequestParam String idEditorial) throws ErrorService {
		
				Libro libro = null;
		
		try {
			
			
			
			libro = libroService.buscarLibroPorISBN(Long.parseLong(isbn));
			
			libroService.modificarLibro(idEditorial, idAutor, Long.parseLong(isbn), titulo,Integer.valueOf(anio), Integer.valueOf(ejemplares),Integer.valueOf(prestados));
			
			modelo.put("mensaje", "Se ha modificado el Libro exitosamente");
		
		}catch(Exception e) {
			
			
			
			List<Autor> autores = autorRepositorio.findAll();
			
			List<Editorial> editoriales = editorialRepositorio.findAll();
			
			modelo.put("editoriales", editoriales);
			modelo.put("autores", autores);
			modelo.put("error",e.getMessage());
			modelo.put("isbn", isbn);
			modelo.put("titulo", titulo);
			modelo.put("anio", anio);
			modelo.put("ejemplares", ejemplares);
			modelo.put("prestados", prestados);
			modelo.put("libro", libro);
			
			return "modificarLibro.html";
		}
		
		return "libro.html";
	}
	
	
	@PreAuthorize("hasAnyRole('ROLE_CLIENTE_REGISTRADO')")
	@PostMapping("/eliminar-libro")
	public String eliminarLibro(ModelMap modelo,
			HttpSession session,
			@RequestParam String isbn) {
		
		Libro libro = null;
		
		try {
			
			libro = libroService.buscarLibroPorISBN(Long.parseLong(isbn));
			libroService.eliminarLibro(Long.parseLong(isbn));
	
			modelo.put("mensaje", "Se ha eliminado el libro exitosamente");
			
		}catch(ErrorService e) {
			modelo.addAttribute("error", e.getMessage());
			return "listarLibro.html";
		}
		return "libro.html";
		
	}
	@PreAuthorize("hasAnyRole('ROLE_CLIENTE_REGISTRADO')")
	@GetMapping("/eliminar-libro-get/{isbn}")
	public String eliminarLibroGet(ModelMap modelo,
			@PathVariable String isbn) throws NumberFormatException, ErrorService {
		
		libroService.eliminarLibro(Long.parseLong(isbn));
		modelo.put("mensaje", "Se ha eliminado el libro exitosamente");
		
		return listarLibro(modelo);
		
	}
	
	@PreAuthorize("hasAnyRole('ROLE_CLIENTE_REGISTRADO')")
	@GetMapping("/listarLibro")
	public String listarLibro(ModelMap modelo) {	
		
		List<Libro> libro= libroRepositorio.findAll();	
		
		modelo.put("libro",libro);
		
		List<Autor> autor= autorRepositorio.findAll();	
		
		modelo.put("autor",autor);
		
		List<Editorial> editorial= editorialRepositorio.findAll();	
		
		modelo.put("editorial",editorial);
		
		return "listarLibro.html";
	}
	
	@PreAuthorize("hasAnyRole('ROLE_CLIENTE_REGISTRADO')")
	@GetMapping("/realizarPrestamo")
	public String listarLibroPrestamo(ModelMap modelo) throws NumberFormatException, ErrorService {	
		
		
		
		List<Libro> libro = libroRepositorio.findAll();
		modelo.put("libro", libro);
		
		List<Autor> autores= autorRepositorio.findAll();	
		
		modelo.put("autores",autores);
		
		List<Editorial> editoriales= editorialRepositorio.findAll();	
		
		modelo.put("editoriales",editoriales);
		
		return "realizarPrestamo.html";
	}
	
	@PreAuthorize("hasAnyRole('ROLE_CLIENTE_REGISTRADO')")
	@GetMapping("/realizarPrestamo1")
	public String listarLibroPrestamo1(ModelMap modelo,@RequestParam String isbn) throws NumberFormatException, ErrorService {
		
		Libro libro = libroService.buscarLibroPorISBN(Long.parseLong(isbn));
		modelo.put("libro", libro);
		
		return "realizarAlquiler.html";
	}	
	
	
	@PreAuthorize("hasAnyRole('ROLE_CLIENTE_REGISTRADO')")
	@PostMapping("/realizarPrestamoPOST")
	public String realizarPrestamoPost(ModelMap modelo, 
		@RequestParam String isbn,
		@RequestParam String documento,
		@RequestParam String devolucion,
		HttpSession sesion) {
		
		Date nuevaDevolucion = libroService.conversorStringDate(devolucion);
		
		try {
			
			 prestamoService.agregarPrestamo(Long.parseLong(isbn),Long.parseLong(documento), nuevaDevolucion);
			 
			 modelo.put("mensaje","Se ha realizado el prestamo exitosamente");
			 
			 return "prestamo.html";
			
		}catch(ErrorService e) {
			modelo.addAttribute("error", e.getMessage());
			
			return "realizarPrestamo.html";
		}
		
	}
	
	@PreAuthorize("hasAnyRole('ROLE_CLIENTE_REGISTRADO')")
	@GetMapping("/listar-prestamo")
	public String listarprestamo(ModelMap modelo) {		
		List<Prestamo> prestamo= prestamoRepositorio.findAll();	
		modelo.put("prestamo",prestamo);
		
		return "listarPrestamo.html";
	}
	
	@PreAuthorize("hasAnyRole('ROLE_CLIENTE_REGISTRADO')")
	@GetMapping("/devolverPuntuacion/{id}")
		public String devolverPuntuacion(@PathVariable String id,ModelMap modelo) throws NumberFormatException, ErrorService {
		
		Prestamo prestamo = prestamoService.buscarPrestamoPorId(id);
		modelo.put("prestamoId", prestamo.getId());
		Libro libro = libroService.buscarLibroPorISBN(prestamo.getLibro().getIsbn());
		modelo.put("libro", libro);
		
		
			return "devolucionPuntuacion.html";
		}
	
	
	@PostMapping("/devolverlibro")
	public String devolverLibro(ModelMap modelo,
			@RequestParam String isbn,
			@RequestParam String documento,
			@RequestParam String puntuacion,
			@RequestParam String id) {
		
		
		
		try {
			
			
			prestamoService.devolucion(Long.parseLong(isbn),Long.parseLong(documento),id,Integer.parseInt(puntuacion));
			
			modelo.put("mensaje", "Has puntuado exitosamente el libro");
	
			
		}catch(ErrorService e) {
			modelo.addAttribute("error", e.getMessage());
			
			return "listarPrestamo.html";
		}
		return "prestamo.html";
		
	
		
	}
		
	
	
}


	
	