package com.saims.erpm.service;

import java.util.List;

import com.saims.erpm.dto.CargoDtoRequest;
import com.saims.erpm.dto.CargoDtoResponse;
import com.saims.erpm.dto.DatosDtoRequest;
import com.saims.erpm.model.CargoModel;

public interface CargoService {
	
	CargoModel addDatos(DatosDtoRequest datosDtoRequest);
	CargoDtoResponse addCargo(CargoDtoRequest cargoDtoRequest);
	List<CargoDtoResponse> getCargos();

}
