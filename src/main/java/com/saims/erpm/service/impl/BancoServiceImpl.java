package com.saims.erpm.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.saims.erpm.dao.BancoDao;
import com.saims.erpm.dto.BancoDtoRequest;
import com.saims.erpm.dto.BancoDtoResponse;
import com.saims.erpm.dto.DatosDtoRequest;
import com.saims.erpm.model.BancoModel;
import com.saims.erpm.service.BancoService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BancoServiceImpl implements BancoService {
	
	private final BancoDao bancoDao;
	private final ModelMapper modelMapper;
	
	@Transactional
	public BancoModel addDatos(DatosDtoRequest datosDtoRequest) {
		/**
		 * Registro para banco
		 * primero verificamos la existencia del banco 
		 * si existe
		 * 	recuperamos su ID
		 * caso contrario
		 * 	creamos un nuevo banco
		 */
		
		String nombreBanco = datosDtoRequest.getBancoNombre().toUpperCase().trim();
		
		BancoModel bancoModel = this.bancoDao.findByNombre(nombreBanco)
				.orElseGet(()->{
					BancoModel nuevo = new BancoModel();
					nuevo.setNombre(nombreBanco);
					return this.bancoDao.save(nuevo);
				});
		
		return (bancoModel);
	}
	
	@Transactional
	public BancoDtoResponse addBanco(BancoDtoRequest bancoDtoRequest) {
		/**
		 * Registro para banco
		 * primero verificamos la existencia del banco 
		 * si existe
		 * 	recuperamos su ID
		 * caso contrario
		 * 	creamos un nuevo banco
		 */
		
		String nombreBanco = bancoDtoRequest.getNombre().toUpperCase().trim();
		
		BancoModel bancoModel = this.bancoDao.findByNombre(nombreBanco)
				.orElseGet(()->{
					BancoModel nuevo = new BancoModel();
					nuevo.setNombre(nombreBanco);
					return this.bancoDao.save(nuevo);
				});
		
		return (this.modelMapper.map(bancoModel,BancoDtoResponse.class));
	}
	
	@Transactional
	public BancoDtoResponse getBanco(Long id) {
		BancoModel bancoModel = bancoDao.getId(id);
		return (this.modelMapper.map(bancoModel, BancoDtoResponse.class));
	}
	
	
	
	@Transactional
	public List<BancoDtoResponse> getBancos(){
		List<BancoDtoResponse>bancos = this.bancoDao.findAll().stream()
				.map(banco -> this.modelMapper.map(banco, BancoDtoResponse.class))
				.collect(Collectors.toList());
		return (bancos);
	}

}
