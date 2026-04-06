package com.saims.erpm.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.saims.erpm.dao.AreaDao;
import com.saims.erpm.dto.AreaDtoRequest;
import com.saims.erpm.dto.AreaDtoResponse;
import com.saims.erpm.dto.DatosDtoRequest;
import com.saims.erpm.model.AreaModel;
import com.saims.erpm.service.AreaService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AreaServiceImpl implements AreaService{

	private final AreaDao areaDao; 
	private final ModelMapper modelMapper;
	
	@Transactional
	public AreaModel addDatos(DatosDtoRequest datosDtoRequest) {
		/**
		 * registro de Area
		 * primero verificamos si existe el area
		 * si existe
		 * 		no realizamos nada 
		 * caso contrario 
		 * 		creamos una nueva area
		 */
		String nombreArea = datosDtoRequest.getNombreArea().toUpperCase().trim();
		
		AreaModel areaModel = this.areaDao.findByNombre(nombreArea)
				.orElseGet(()->{
					AreaModel nueva = new AreaModel();
					nueva.setNombre(nombreArea);
					return (this.areaDao.save(nueva));
				});
		
		return (areaModel);

	}
	
	@Transactional
	public AreaDtoResponse addArea(AreaDtoRequest areaDtoRequest) {
		/**
		 * registro de Area
		 * primero verificamos si existe el area
		 * si existe
		 * 		no realizamos nada 
		 * caso contrario 
		 * 		creamos una nueva area
		 */
		String nombreArea = areaDtoRequest.getNombre().toUpperCase();
		AreaModel areaModel = this.areaDao.findByNombre(nombreArea)
				.orElseGet(()->{
					AreaModel nueva = new AreaModel();
					nueva.setNombre(nombreArea);
					return (this.areaDao.save(nueva));
				});
		
		return (this.modelMapper.map(areaModel, AreaDtoResponse.class));

	}
	
	@Transactional
	public AreaDtoResponse getArea(Long id) {
		AreaModel areaModel = this.areaDao.getId(id);
		return(this.modelMapper.map(areaModel, AreaDtoResponse.class));
	}
	
	@Transactional
	public List<AreaDtoResponse> getAreas(){
		List<AreaDtoResponse> areas = this.areaDao.findAll().stream()
				.map(area -> this.modelMapper.map(area, AreaDtoResponse.class))
				.collect(Collectors.toList());
		return(areas);
	}
}
