package com.saims.erpm.service;

import java.util.List;

import com.saims.erpm.dto.DatosDtoRequest;
import com.saims.erpm.dto.ProfesionDtoRequest;
import com.saims.erpm.dto.ProfesionDtoResponse;
import com.saims.erpm.model.ProfesionModel;

public interface ProfesionService {
	ProfesionModel addProfesion(DatosDtoRequest datosDtoRequest);
	ProfesionDtoResponse addProfesion(ProfesionDtoRequest profesionDtoRequest);
	List<ProfesionDtoResponse> getProfesion();
}
