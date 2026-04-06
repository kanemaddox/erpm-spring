package com.saims.erpm.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.saims.erpm.model.CargoModel;

public interface CargoDao extends JpaRepository<CargoModel,Long>{
	
	@Query(value = "select * from cargo where nombre=?",nativeQuery=true)
	CargoModel getCargo(String nombre);
	
	Optional<CargoModel> findByNombre(String nombre);

}
