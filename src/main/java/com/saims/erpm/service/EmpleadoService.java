package com.saims.erpm.service;

import com.saims.erpm.dto.DatosDtoRequest;
import com.saims.erpm.model.CargoModel;
import com.saims.erpm.model.EmpleadoModel;
import com.saims.erpm.model.PersonaModel;
import com.saims.erpm.model.ProfesionModel;
import com.saims.erpm.model.SucursalModel;

public interface EmpleadoService {
	EmpleadoModel addDatos(PersonaModel personaModel,CargoModel cargoModel, ProfesionModel profesionModel, SucursalModel sucursalModel, DatosDtoRequest datosDtoRequest);

}
