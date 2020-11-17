package edu.egg.repositorios;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import edu.egg.entidades.Autor;



@Repository
public interface AutorRepositorio extends JpaRepository<Autor,String>{

    
    @Query("SELECT l FROM Autor l WHERE l.nombre = :nombre")
	public Optional<Autor> buscarAutorPorNombre(@Param("nombre") String nombre);
    
    
    
    
}

