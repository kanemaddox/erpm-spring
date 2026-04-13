package com.saims.erpm.service;

import java.util.List;

import com.saims.erpm.model.PersonaModel;
import com.saims.erpm.model.PersonaProfesionModel;
import com.saims.erpm.model.ProfesionModel;

public interface PersonaProfesionService {
	PersonaProfesionModel createOrGet(PersonaModel persona, ProfesionModel profesion);
	List<PersonaProfesionModel> findAllModel();

}
