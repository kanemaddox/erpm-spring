package com.saims.erpm.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.saims.erpm.model.PersonaProfesionModel;

public interface PersonaProfesionDao extends JpaRepository<PersonaProfesionModel,Long> {
	
	@Query(value = "SELECT * FROM persona_profesion WHERE id_persona = ? AND id_profesion = ?", nativeQuery = true)
	Optional<PersonaProfesionModel> getPersonaProfesion(Long idPersona,Long idProfesion);

}
