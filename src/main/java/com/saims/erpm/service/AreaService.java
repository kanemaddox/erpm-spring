package com.saims.erpm.service;

import java.util.List;

import com.saims.erpm.dto.AreaDtoRequest;
import com.saims.erpm.dto.AreaDtoResponse;
import com.saims.erpm.dto.DatosDtoRequest;
import com.saims.erpm.model.AreaModel;

public interface AreaService {
	AreaModel addDatos(DatosDtoRequest datosDtoRequest);
	AreaDtoResponse addArea(AreaDtoRequest areaDtoRequest);
	AreaDtoResponse getArea(Long id);
	List<AreaDtoResponse> getAreas();

}
