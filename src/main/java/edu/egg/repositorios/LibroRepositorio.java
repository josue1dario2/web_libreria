package edu.egg.repositorios;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;




import edu.egg.entidades.Libro;

@Repository
public interface LibroRepositorio extends JpaRepository<Libro,Long>{

	@Query("SELECT l FROM Libro l WHERE l.titulo = :titulo")
	public Optional<Libro> buscarLibroPorTitulo(@Param("titulo") String titulo);
	

	@Query("SELECT l FROM Libro l WHERE l.autor.id = :id")
	public List<Libro> buscarLibroPorAutor(@Param("id") String id);
	
	@Query("SELECT l FROM Libro l ")
	public List<Libro>listarLibros();
	
	
	
}
