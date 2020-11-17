package edu.egg.entidades;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

@Entity
public class Prestamo {
	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid",strategy = "uuid2")
	private String id;
	
	@Temporal(TemporalType.DATE)
	private Date fecha;
	
	@Temporal(TemporalType.DATE)
	private Date devolucion;
	
	@Temporal(TemporalType.DATE)
	private Date fechaEntrega;
	
	public Date getFechaEntrega() {
		return fechaEntrega;
	}



	public void setFechaEntrega(Date fechaEntrega) {
		this.fechaEntrega = fechaEntrega;
	}

	private double multa;
	
	@ManyToOne
	private Cliente cliente;
	
	@ManyToOne
	private Libro libro;
	
	public Prestamo() {
		
		
	}
	
	

	public Cliente getCliente() {
		return cliente;
	}



	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}



	public Libro getLibro() {
		return libro;
	}



	public void setLibro(Libro libro) {
		this.libro = libro;
	}



	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public Date getDevolucion() {
		return devolucion;
	}

	public void setDevolucion(Date devolucion) {
		this.devolucion = devolucion;
	}

	public double getMulta() {
		return multa;
	}

	public void setMulta(double multa) {
		this.multa = multa;
	}
	
	
	
}
