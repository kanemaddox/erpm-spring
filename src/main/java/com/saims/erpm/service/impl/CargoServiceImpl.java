package com.saims.erpm.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.saims.erpm.dao.CargoDao;
import com.saims.erpm.dto.CargoDtoRequest;
import com.saims.erpm.dto.CargoDtoResponse;
import com.saims.erpm.dto.DatosDtoRequest;
import com.saims.erpm.model.CargoModel;
import com.saims.erpm.service.CargoService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CargoServiceImpl implements CargoService {
	private final CargoDao cargoDao;
	private final ModelMapper modelMapper;
	
	@Transactional
	public CargoModel addDatos(DatosDtoRequest datosDtoRequest) {
		/**
		 * registro para cargo
		 * primero verificamos si el cargo existe
		 * si existe
		 * 		no realizamos nada
		 * caso contrario
		 * 		creamos un cargo nuevo
		 */
		String nombreCargo = datosDtoRequest.getCargoNombre().toUpperCase().trim();
		
		CargoModel cargoModel = this.cargoDao.findByNombre(nombreCargo)
				.orElseGet(()->{
					CargoModel nuevo = new CargoModel();
					nuevo.setNombre(nombreCargo);
					return (this.cargoDao.save(nuevo));
				});
		
		return (cargoModel);
	}
	
	@Transactional
	public CargoDtoResponse addCargo(CargoDtoRequest cargoDtoRequest) {
		
		String nombreCargo = cargoDtoRequest.getNombre().toUpperCase().trim();
		
		CargoModel cargoModel = this.cargoDao.findByNombre(nombreCargo)
				.orElseGet(()->{
					CargoModel nuevo = new CargoModel();
					nuevo.setNombre(nombreCargo);
					return (this.cargoDao.save(nuevo));
				});
		
		return (this.modelMapper.map(cargoModel, CargoDtoResponse.class));
	}
	
	@Transactional
	public List<CargoDtoResponse> getCargos(){
		List<CargoDtoResponse> cargos = this.cargoDao.findAll().stream()
				.map(cargo -> this.modelMapper.map(cargo, CargoDtoResponse.class))
				.collect(Collectors.toList());
		return (cargos);
	}
	

}
