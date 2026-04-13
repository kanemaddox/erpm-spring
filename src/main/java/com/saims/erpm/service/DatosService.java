package com.saims.erpm.service;

import java.util.List;

import com.saims.erpm.dto.DatosDtoRequest;
import com.saims.erpm.dto.DatosDtoResponse;

public interface DatosService {
	
	DatosDtoResponse AddDatos(DatosDtoRequest datosDtoRequest) throws Exception;
	List<DatosDtoResponse>getEmpleadosFile();
	void setAllEstado(boolean estado);

}
