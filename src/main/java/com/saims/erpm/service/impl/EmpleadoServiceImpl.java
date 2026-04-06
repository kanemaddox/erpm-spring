package com.saims.erpm.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.saims.erpm.dao.EmpleadoDao;
import com.saims.erpm.dto.DatosDtoRequest;
import com.saims.erpm.enums.TipoEmpleado;
import com.saims.erpm.model.CargoModel;
import com.saims.erpm.model.EmpleadoModel;
import com.saims.erpm.model.PersonaModel;
import com.saims.erpm.model.ProfesionModel;
import com.saims.erpm.model.SucursalModel;
import com.saims.erpm.service.EmpleadoService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmpleadoServiceImpl implements EmpleadoService {
	
	private final EmpleadoDao empleadoDao;
	private final ModelMapper modelMapper;
	
	@Transactional
	public EmpleadoModel addDatos(PersonaModel personaModel,CargoModel cargoModel, ProfesionModel profesionModel, SucursalModel sucursalModel, DatosDtoRequest datosDtoRequest) {
		/**
		 * registro de emplado
		 * primero verificamos si el empleado existe 
		 * si existe 
		 * 		verificamos si sus datos estan correctos y actualizamos
		 * caso contrario 
		 * 		creamos un nuevo empleado
		 */
		
		EmpleadoModel empleadoModel = this.empleadoDao.findByPersona_Id(personaModel.getId())
				.orElseGet(()->{
					EmpleadoModel nuevo = new EmpleadoModel();
					nuevo.setPersona(personaModel);
					nuevo.setCargo(cargoModel);
					nuevo.setProfesion(profesionModel);
					nuevo.setSucursal(sucursalModel);
					nuevo.setEstado(true);
					nuevo.setIdpJefe(datosDtoRequest.getIdpJefe());
					nuevo.setTipo(TipoEmpleado.SUBORDINADO);
					return(this.empleadoDao.save(nuevo));
				});
		
		return (empleadoModel);
	}

}
