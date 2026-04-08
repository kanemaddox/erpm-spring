package com.saims.erpm.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.saims.erpm.model.AreaModel;

public interface AreaDao extends JpaRepository<AreaModel, Long>{
	// nativo
	@Query(value = "select * from area where nombre=?",nativeQuery=true)
	AreaModel getArea(String nombre);
	
	// Jpa automatizado
	Optional<AreaModel>findByNombre(String nombre);
	
	@Query(value = "select * from area where id=?",nativeQuery=true)
	AreaModel getId(Long id);
	
}
