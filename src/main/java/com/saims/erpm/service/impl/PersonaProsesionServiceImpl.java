package com.saims.erpm.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.saims.erpm.dao.PersonaProfesionDao;
import com.saims.erpm.model.PersonaModel;
import com.saims.erpm.model.PersonaProfesionModel;
import com.saims.erpm.model.ProfesionModel;
import com.saims.erpm.service.PersonaProfesionService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PersonaProsesionServiceImpl implements PersonaProfesionService{
	private final PersonaProfesionDao personaProfesionDao;
	
	@Transactional
	public PersonaProfesionModel createOrGet(PersonaModel persona, ProfesionModel profesion) {
		PersonaProfesionModel personaProfesion = this.personaProfesionDao.getPersonaProfesion(persona.getId(), profesion.getId())
				.orElseGet(()->{
					PersonaProfesionModel nuevo = new PersonaProfesionModel();
					nuevo.setPersona(persona);
					nuevo.setProfesion(profesion);
					this.personaProfesionDao.save(nuevo);
					return nuevo; 
				});
		return personaProfesion;
	}
	
	@Transactional
	public List<PersonaProfesionModel> findAllModel() {
		List<PersonaProfesionModel> personaProfesion = this.personaProfesionDao.findAll();
		return personaProfesion;
	}

}
