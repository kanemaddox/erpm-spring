package com.saims.erpm.service.impl;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.saims.erpm.dao.UsuarioDao;
import com.saims.erpm.dto.UsuarioDtoResponse;
import com.saims.erpm.enums.Estado;
import com.saims.erpm.model.PersonaModel;
import com.saims.erpm.model.UsuarioModel;
import com.saims.erpm.service.UsuarioService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {
	private final UsuarioDao usuarioDao;
	private final ModelMapper modelMapper;
	
	@Transactional
	public UsuarioModel createOrGet(PersonaModel persona) throws Exception {
		
		String pass = password(persona.getIdp());
		
		UsuarioModel usuario = this.usuarioDao.findByUsuario(persona.getIdp())
				.map(e->{
					e.setEstado(true);
					return(e);
				})
				.orElseGet(()->{
					UsuarioModel nuevo = new UsuarioModel();
					nuevo.setUsuario(persona.getIdp());
					nuevo.setPassword(pass);
					nuevo.setEstado(true);
					nuevo.setPersona(persona);
					return (nuevo);
				});
		this.usuarioDao.save(usuario);		
		return usuario;
	}
	/**
	 * funciones para retornar el usuario mediante su id
	 * retorno mediante Response y Model 
	 */
	@Transactional
	public UsuarioDtoResponse getUsuarioResponse(Long id) {
		UsuarioModel usuario = this.usuarioDao.getId(id);
		return (this.modelToResponse(usuario));
	}
	
	@Transactional
	public UsuarioModel getUsuarioModel(Long id) {
		UsuarioModel usuario = this.usuarioDao.getId(id);
		return (usuario);
	}
	
	@Transactional
	public void setAllEstado(boolean estado) {
		List<UsuarioModel>usuarios = this.usuarioDao.findAll();
		usuarios.forEach(u ->{
			u.setEstado(estado);
		});
		
		this.usuarioDao.saveAll(usuarios);
	}
	/**
	 * funsion para el retorno de una lista de Usuarios 
	 * @param estado
	 * @return lista de Usuarios Response y Model
	 */
	@Transactional
	public List<UsuarioDtoResponse> findAllResponse(Estado estado){
		List<UsuarioDtoResponse> usuarios = null;
		switch(estado) {
			case ACTIVO:
				usuarios  = this.usuarioDao.findByEstado(true).stream()
				.map(usuario -> this.modelToResponse(usuario))
				.collect(Collectors.toList());
			break;
			case INACTIVO:
				usuarios  = this.usuarioDao.findByEstado(false).stream()
				.map(usuario -> this.modelToResponse(usuario))
				.collect(Collectors.toList());
				break;
			case TODOS:
				usuarios = this.usuarioDao.findAll().stream()
				.map(usuario->this.modelToResponse(usuario))
				.collect(Collectors.toList());
				break;
		}
		return usuarios;
	}
	@Transactional
	public List<UsuarioModel>findAllModel(Estado estado){
		List<UsuarioModel> usuarios = null;
		switch(estado) {
			case ACTIVO:
				usuarios  = this.usuarioDao.findEstado(true);
				break;
			case INACTIVO:
				usuarios  = this.usuarioDao.findEstado(false);
				break;
			case TODOS:
				usuarios = this.usuarioDao.findAll();
				break;
		}
		return usuarios;
	}
	/**
	 * funsion de creacion de password 
	 * @param value dato para contrasena 
	 * @return value decodificado
	 * @throws Exception
	 */
	private String password(String value) throws Exception{
		MessageDigest md = MessageDigest.getInstance("SHA-256");
		byte[] digest = md.digest(value.getBytes(StandardCharsets.UTF_8));
		StringBuilder hexString = new StringBuilder();
        for (byte b : digest) {
            hexString.append(String.format("%02x", b));
        }
		return hexString.toString();
	}
	
	/**
	 * 
	 * @param usuario
	 * @return Response de Usuario
	 */
	private UsuarioDtoResponse modelToResponse(UsuarioModel usuario) {
		return (this.modelMapper.map(usuario, UsuarioDtoResponse.class));
	}
}
