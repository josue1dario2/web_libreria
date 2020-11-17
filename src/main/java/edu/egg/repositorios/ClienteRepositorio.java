package edu.egg.repositorios;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import edu.egg.entidades.Cliente;


@Repository
public interface ClienteRepositorio extends JpaRepository<Cliente,Long>{

	
    @Query("SELECT l FROM Cliente l WHERE l.documento = :documento")
	public Optional<Cliente> buscarClientePorDocumento(@Param("documento")Long documento);
    
    @Query("SELECT l FROM Cliente l WHERE l.nombre = :nombre")
	public Optional<Cliente> buscarClientePorNombre(@Param("nombre") String nombre);
    
   
}
