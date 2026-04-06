package com.saims.erpm.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.saims.erpm.dto.DatosDtoRequest;
import com.saims.erpm.dto.DatosDtoResponse;
import com.saims.erpm.service.DatosService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/erpm/datos")
@RequiredArgsConstructor
public class DatosController {
	private final DatosService datosService;
	
	@PostMapping("/addDatos")
	public ResponseEntity<DatosDtoResponse> addDatos(@RequestBody DatosDtoRequest datosDtoRequest){
		try {
			return new ResponseEntity<>(this.datosService.AddDatos(datosDtoRequest),HttpStatus.CREATED);
		}catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
