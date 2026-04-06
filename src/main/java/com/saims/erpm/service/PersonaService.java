package com.saims.erpm.service;

import java.util.List;

import com.saims.erpm.dto.DatosDtoRequest;
import com.saims.erpm.dto.PersonaDtoRequest;
import com.saims.erpm.dto.PersonaDtoResponse;
import com.saims.erpm.model.PersonaModel;

public interface PersonaService {
	
	PersonaModel addDatos(DatosDtoRequest datosDtoRequest);
	PersonaDtoResponse addPersona(PersonaDtoRequest personaDtoRequest);
	PersonaDtoResponse getPersona(Long id);
	PersonaDtoResponse updatePersona(PersonaDtoResponse personaDtoResponse);
	List<PersonaDtoResponse>getPersonas();
	

}
