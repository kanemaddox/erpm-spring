package com.saims.erpm.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.saims.erpm.dao.PersonaDao;
import com.saims.erpm.dto.DatosDtoRequest;
import com.saims.erpm.dto.PersonaDtoRequest;
import com.saims.erpm.dto.PersonaDtoResponse;
import com.saims.erpm.model.PersonaModel;
import com.saims.erpm.service.PersonaService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor

public class PersonaServiceImpl implements PersonaService{
	
	private final PersonaDao personaDao;
	private final ModelMapper modelMapper;
	
	
	@Transactional
	public PersonaModel addDatos(DatosDtoRequest datosDtoRequest) {
		
		/**
		 * Registro para persona
		 * primero verificamos la existencia del IDP, 
		 * si no existe el IDP
		 * 		Creamos el modelo de persona con los datos, retorna el numero de ID en la
		 * 		tabla de la base de datos
		 * caso contrario
		 * 		actualizamos los datos de persona
		 */
		String idp = datosDtoRequest.getIdp().toUpperCase().trim();
		
		PersonaModel personaModel = this.personaDao.findByIdp(idp)
				.orElseGet(()->{
					PersonaModel nuevo = new PersonaModel();
					nuevo.setIdp(idp);
					nuevo.setNombre(datosDtoRequest.getNombre());
					nuevo.setPaterno(datosDtoRequest.getPaterno());
					nuevo.setMaterno(datosDtoRequest.getMaterno());
					nuevo.setEmail(datosDtoRequest.getEmail().toLowerCase().trim());
					return (this.personaDao.save(nuevo));
				});
		
		if(!personaModel.getEmail().equals(datosDtoRequest.getEmail().toLowerCase().trim()))
		{
			personaModel.setEmail(datosDtoRequest.getEmail().toLowerCase().trim());
			this.personaDao.save(personaModel);
		}
		
		return(personaModel);
	}
	
	@Transactional
	public PersonaDtoResponse addPersona(PersonaDtoRequest personaDtoRequest) {
		
		/**
		 * Registro para persona
		 * primero verificamos la existencia del IDP, 
		 * si no existe el IDP
		 * 		Creamos el modelo de persona con los datos, retorna el numero de ID en la
		 * 		tabla de la base de datos
		 * caso contrario
		 * 		actualizamos los datos de persona
		 */
		
		String idp = personaDtoRequest.getIdp().toUpperCase().trim();
		
		PersonaModel personaModel = this.personaDao.findByIdp(idp)
				.orElseGet(()->{
					PersonaModel nuevo = new PersonaModel();
					nuevo.setIdp(idp);
					nuevo.setNombre(personaDtoRequest.getNombre());
					nuevo.setPaterno(personaDtoRequest.getPaterno());
					nuevo.setMaterno(personaDtoRequest.getMaterno());
					nuevo.setEmail(personaDtoRequest.getEmail().toLowerCase().trim());
					return (this.personaDao.save(nuevo));
				});
		
		return(this.modelMapper.map(personaModel, PersonaDtoResponse.class));
	}
	
	@Transactional
	public PersonaDtoResponse getPersona(Long id) {
		
		PersonaModel personaModel = personaDao.getId(id);
		
		return(this.modelMapper.map(personaModel, PersonaDtoResponse.class));
	}
	
	@Transactional
	public PersonaDtoResponse updatePersona(PersonaDtoResponse personaDtoResponse) {
	
		PersonaModel personaModel = this.modelMapper.map(personaDtoResponse, PersonaModel.class);
		personaDao.save(personaModel);
		return (this.modelMapper.map(personaModel, PersonaDtoResponse.class));
	}
	
	@Transactional
	public List<PersonaDtoResponse>getPersonas(){
		
		List<PersonaDtoResponse>personas = this.personaDao.findAll().stream()
				.map(persona -> this.modelMapper.map(persona, PersonaDtoResponse.class))
				.collect(Collectors.toList());
		return(personas);
	}
	
	

}
