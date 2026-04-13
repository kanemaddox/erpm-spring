package com.saims.erpm.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.saims.erpm.model.UsuarioModel;

public interface UsuarioDao extends JpaRepository<UsuarioModel,Long>{
	
	@Query(value = "select * from usuario where id=?",nativeQuery=true)
	UsuarioModel getId(Long id);
	
	Optional<UsuarioModel>findByUsuario(String usuario);
	Optional<UsuarioModel>findByEstado(boolean estado);
	
	@Query(value = "select * from usuario where estado=?",nativeQuery=true)
	List<UsuarioModel>findEstado(boolean estado);

}
