package com.saims.erpm.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.saims.erpm.dao.SucursalDao;
import com.saims.erpm.dto.DatosDtoRequest;
import com.saims.erpm.model.SucursalModel;
import com.saims.erpm.service.SucursalService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SucursalServiceImpl implements SucursalService{
	
	private final SucursalDao sucursalDao;
	private final ModelMapper modelMapper;
	
	@Transactional
	public SucursalModel addDatos(DatosDtoRequest datosDtoRequest) {
		/**
		 * registro de Sucursal
		 * primero verificamos la existensia de una Sucursal mediante el codigo de sucursal
		 * si existe 
		 * 		no realizamos nada
		 * caso contrario
		 * 		creamos una nueva sucursal
		 */
		
		String codigoSucursal = datosDtoRequest.getCodigoSucursal().toUpperCase().trim();
		
		SucursalModel sucursalModel = this.sucursalDao.findByCodigo(codigoSucursal)
				.orElseGet(()->{
					SucursalModel nuevo = new SucursalModel();
					nuevo.setCodigo(codigoSucursal);
					nuevo.setNombre(datosDtoRequest.getNombreSucursal().toUpperCase().trim());
					nuevo.setPrefijo(codigoSucursal.substring(0, 2));
					return (this.sucursalDao.save(nuevo));
				});
		
		return (sucursalModel);
		
	}

}
