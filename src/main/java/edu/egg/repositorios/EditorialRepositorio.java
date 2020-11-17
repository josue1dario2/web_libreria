package edu.egg.repositorios;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import edu.egg.entidades.Editorial;


@Repository

public interface EditorialRepositorio extends JpaRepository<Editorial,String>{

	
    @Query("SELECT l FROM Editorial l WHERE l.nombre = :nombre")
	public Optional<Editorial> buscarEditorialPorNombre(@Param("nombre") String nombre);
}
