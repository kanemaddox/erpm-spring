package com.saims.erpm.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.saims.erpm.model.CuentaBancariaModel;

public interface CuentaBancariaDao extends JpaRepository<CuentaBancariaModel,Long> {
	
	@Query(value = "select * from cuentabancaria where id_persona=?",nativeQuery=true)
	List<CuentaBancariaModel> getListaCuentaBancaria(Long id_persona);
	
	@Query(value = "select * from cuentabancaria where id=?",nativeQuery=true)
	CuentaBancariaModel getId(Long id);

}
