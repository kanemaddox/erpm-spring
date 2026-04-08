package com.saims.erpm.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.saims.erpm.dto.DatosDtoRequest;
import com.saims.erpm.dto.DatosDtoResponse;
import com.saims.erpm.model.AreaModel;
import com.saims.erpm.model.BancoModel;
import com.saims.erpm.model.CargoModel;
import com.saims.erpm.model.CentroCostosModel;
import com.saims.erpm.model.CuentaBancariaModel;
import com.saims.erpm.model.EmpleadoModel;
import com.saims.erpm.model.PersonaModel;
import com.saims.erpm.model.ProfesionModel;
import com.saims.erpm.model.SucursalAreaCentroCostosModel;
import com.saims.erpm.model.SucursalModel;
import com.saims.erpm.service.AreaService;
import com.saims.erpm.service.BancoService;
import com.saims.erpm.service.CargoService;
import com.saims.erpm.service.CentroCostosService;
import com.saims.erpm.service.CuentaBancariaService;
import com.saims.erpm.service.DatosService;
import com.saims.erpm.service.EmpleadoService;
import com.saims.erpm.service.PersonaService;
import com.saims.erpm.service.ProfesionService;
import com.saims.erpm.service.SucursalAreaCentroCostosService;
import com.saims.erpm.service.SucursalService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DatosServiceImpl implements DatosService{
	
	private final ModelMapper modelMapper;
	
	private final PersonaService personaService;
	private final BancoService bancoService;
	private final CuentaBancariaService cuentaBancariaService;
	private final CargoService cargoService;
	private final ProfesionService profesionService;
	private final AreaService areaServise;
	private final CentroCostosService centroCostosService;
	private final SucursalService sucursalService;
	private final SucursalAreaCentroCostosService sucursalAreaCentroCostosService; 
	private final EmpleadoService empleadoService;
	
	@Transactional
	public DatosDtoResponse AddDatos(DatosDtoRequest datosDtoRequest) {
		
		/**
		 * Registro para persona
		 */
		PersonaModel personaModel = this.personaService.createOrGet(datosDtoRequest);
		//System.out.println(personaModel.getNombre());
		
		/**
		 * Registro para banco
		 */
		BancoModel bancoModel = this.bancoService.createOrGet(datosDtoRequest); 
		//System.out.println(bancoModel.getNombre());
		/**
		 * Registro de cuenta Bancaria
		 */
		CuentaBancariaModel cuentaBancariaModel = this.cuentaBancariaService.createOrGet(personaModel, bancoModel, datosDtoRequest);
		//System.out.println(cuentaBancariaModel.getNumero());
		/**
		 * registro para cargo
		 */
		CargoModel cargoModel = this.cargoService.createOrGet(datosDtoRequest);
		//System.out.println(cargoModel.getNombre());
		/**
		 * registro para aprofesion
		 */
		ProfesionModel profesionModel = this.profesionService.createOrGet(datosDtoRequest);
		//System.out.println(profesionModel.getNombre());
		/**
		 * registro de Area
		 */
		AreaModel areaModel = this.areaServise.createOrGet(datosDtoRequest);
		//System.out.println(areaModel.getNombre());
	

		/**
		 * registro de Centro de costos
		 * verificamos si en el request el nombre es ninguno si es ninguno mandamos un centro de costos nulo
		 * caso contrario mandamos a la funcion addDatos 
		 */
		CentroCostosModel centroCostosModel = null;
		if(!datosDtoRequest.getNombreCentroCostos().equals("Ninguno"))
			centroCostosModel = this.centroCostosService.createOrGet(datosDtoRequest);
		//System.out.println(centroCostosModel.getNombre());
		/**
		 * registro de Sucursal
		 */
		
		SucursalModel sucursalModel = this.sucursalService.createOrGet(datosDtoRequest);
		//System.out.println(sucursalModel.getNombre());

		/**
		 * registro de relasion entre Sucursal y Area
		 */
		
		
		SucursalAreaCentroCostosModel sucursalAreaCentroCostosModel = this.sucursalAreaCentroCostosService.createOrGet(sucursalModel, areaModel, centroCostosModel);
		
		//System.out.println(sucursalAreaCentroCostosModel.getFechaCreacion());
		/**
		 * registro de emplado de 
		 */
		EmpleadoModel empleadoModel = this.empleadoService.addDatos(personaModel, cargoModel, profesionModel, sucursalModel, datosDtoRequest); 
		//System.out.println(empleadoModel.getEstado());
		return null;
	}

}
