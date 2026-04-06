package com.saims.erpm.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.saims.erpm.dao.ProfesionDao;
import com.saims.erpm.dto.DatosDtoRequest;
import com.saims.erpm.dto.ProfesionDtoRequest;
import com.saims.erpm.dto.ProfesionDtoResponse;
import com.saims.erpm.model.ProfesionModel;
import com.saims.erpm.service.ProfesionService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProfesionServiceImpl implements ProfesionService{
	private final ProfesionDao profesionDao;
	private final ModelMapper modelMapper;
	
	@Transactional
	public ProfesionModel addProfesion(DatosDtoRequest datosDtoRequest) {
		
		/**
		 * registro para aprofesion
		 * primero verificamos si la profesion existe
		 * si existe 
		 * 		no realizamos nada
		 * caso contrario 
		 * 		creamos una nueva profesion
		 */
		
		String nombreProfesion = datosDtoRequest.getProfesionNombre().toUpperCase().trim();
		ProfesionModel profesionModel = this.profesionDao.findByNombre(nombreProfesion)
				.orElseGet(()->{
					ProfesionModel nuevo = new ProfesionModel();
					nuevo.setNombre(nombreProfesion);
					return (this.profesionDao.save(nuevo));
				});
		
		return (profesionModel);
		
	}
	@Transactional
	public ProfesionDtoResponse addProfesion(ProfesionDtoRequest profesionDtoRequest) {
		String nombreProfesion = profesionDtoRequest.getNombre().toUpperCase().trim();
		
		ProfesionModel profesionModel = this.profesionDao.findByNombre(nombreProfesion)
				.orElseGet(()->{
					ProfesionModel nuevo = new ProfesionModel();
					nuevo.setNombre(nombreProfesion);
					return (this.profesionDao.save(nuevo));
				});
		
		return (this.modelMapper.map(profesionModel, ProfesionDtoResponse.class));
	}
	
	@Transactional
	public List<ProfesionDtoResponse> getProfesion(){
		List<ProfesionDtoResponse> profesiones = this.profesionDao.findAll().stream()
				.map(profesion -> this.modelMapper.map(profesion, ProfesionDtoResponse.class))
				.collect(Collectors.toList());
		return(profesiones);
	}

}
