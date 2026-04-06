package com.saims.erpm.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.saims.erpm.model.BancoModel;

public interface BancoDao extends JpaRepository<BancoModel, Long>{
	@Query(value = "select * from banco where nombre=?",nativeQuery=true)
	BancoModel getBanco(String nombre);
	
	Optional<BancoModel> findByNombre(String nombre);
	
	@Query(value = "select * from banco where id=?",nativeQuery=true)
	BancoModel getId(Long id);

}
