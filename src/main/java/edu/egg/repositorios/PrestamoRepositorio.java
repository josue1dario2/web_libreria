package edu.egg.repositorios;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import edu.egg.entidades.Prestamo;

@Repository
public interface PrestamoRepositorio extends JpaRepository<Prestamo,String>{

	@Query("SELECT l FROM Prestamo l WHERE l.id = :id")
	public Optional<Prestamo> buscarPrestamoPorId(@Param("id") String id);
 
	@Query(value="SELECT * FROM Prestamo p WHERE p.cliente_documento LIKE :documento", nativeQuery=true)
    public List<Prestamo> buscarPrestamosPorDNICliente(@Param("documento") String documento);
}
