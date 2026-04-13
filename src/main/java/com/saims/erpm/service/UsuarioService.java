
package com.saims.erpm.service;

import java.util.List;

import com.saims.erpm.dto.UsuarioDtoResponse;
import com.saims.erpm.enums.Estado;
import com.saims.erpm.model.PersonaModel;
import com.saims.erpm.model.UsuarioModel;

public interface UsuarioService{
	UsuarioModel createOrGet(PersonaModel persona) throws Exception;
	UsuarioDtoResponse getUsuarioResponse(Long id);
	UsuarioModel getUsuarioModel(Long id);
	List<UsuarioDtoResponse> findAllResponse(Estado estado);
	List<UsuarioModel>findAllModel(Estado estado);
	
	void setAllEstado(boolean estado);
}
