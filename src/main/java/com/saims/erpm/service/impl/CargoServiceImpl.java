package com.saims.erpm.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.saims.erpm.dao.CargoDao;
import com.saims.erpm.dto.AreaDtoResponse;
import com.saims.erpm.dto.CargoDtoRequest;
import com.saims.erpm.dto.CargoDtoResponse;
import com.saims.erpm.dto.DatosDtoRequest;
import com.saims.erpm.model.AreaModel;
import com.saims.erpm.model.CargoModel;
import com.saims.erpm.service.CargoService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

/**
 * 📌 Servicio de implementación para la gestión de Cargos
 * 
 * Contiene la lógica de negocio para:
 * ✔ Crear cargos
 * ✔ Obtener cargos
 * ✔ Buscar por nombre
 * ✔ Paginación
 * ✔ Actualización y eliminación
 */
@Service
@RequiredArgsConstructor
public class CargoServiceImpl implements CargoService {

    private final CargoDao cargoDao;
    private final ModelMapper modelMapper;

    /**
     * 📌 Crea o recupera un Cargo basado en DatosDtoRequest
     * 
     * 🔎 Flujo:
     * 1. Normaliza el nombre (uppercase + trim)
     * 2. Busca si ya existe
     * 3. Si no existe → lo crea
     */
    @Transactional
    public CargoModel createOrGet(DatosDtoRequest datosDtoRequest) {

        String nombreCargo = normalize(datosDtoRequest.getCargoNombre());

        return this.cargoDao.findByNombre(nombreCargo)
                .orElseGet(() -> {
                    CargoModel nuevo = new CargoModel();
                    nuevo.setNombre(nombreCargo);
                    return this.cargoDao.save(nuevo);
                });
    }

    /**
     * 📌 Crea un nuevo Cargo o devuelve uno existente
     */
    @Transactional
    public CargoDtoResponse createCargo(CargoDtoRequest cargoDtoRequest) {

        String nombreCargo = normalize(cargoDtoRequest.getNombre());
        
        if (existsByNombre(nombreCargo)) {
            throw new RuntimeException("El área ya existe");
        }

        CargoModel cargo = new CargoModel();
        cargo.setNombre(nombreCargo);
        cargo.setEstado(true);

        this.cargoDao.save(cargo);

        return mapToDto(cargo);
    }
    
    /**
     * 📌 Lista todas las cargos activas
     */
    @Transactional
    public List<CargoDtoResponse> getAllActive() {

        return this.cargoDao.findAll().stream()
                .filter(CargoModel::getEstado) // 🔥 solo activos
                .map(this:: mapToDto)
                .collect(Collectors.toList());
    }

    /**
     * 📌 Obtiene todos los cargos
     */
    @Transactional
    public List<CargoDtoResponse> getAll() {

        return this.cargoDao.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    /**
     * 📌 Obtiene todos los cargos con paginación
     */
    @Transactional
    public Page<CargoDtoResponse> getAllPaginated(Pageable pageable) {

        return this.cargoDao.findAll(pageable)
                .map(this::mapToDto);
    }

    /**
     * 📌 Busca un cargo por ID
     */
    @Transactional
    public CargoDtoResponse getById(Long id) {

        CargoModel cargo = this.cargoDao.findById(id)
                .orElseThrow(() -> new RuntimeException("Cargo no encontrado con ID: " + id));

        return mapToDto(cargo);
    }

    /**
     * 📌 Busca un cargo por nombre
     */
    @Transactional
    public CargoDtoResponse findByNombre(String nombre) {

        String nombreCargo = normalize(nombre);

        CargoModel cargo = this.cargoDao.findByNombre(nombreCargo)
                .orElseThrow(() -> new RuntimeException("Cargo no encontrado: " + nombre));

        return mapToDto(cargo);
    }

    /**
     * 📌 Actualiza el nombre de un cargo
     */
    @Transactional
    public CargoDtoResponse updateNombre(Long id, String nuevoNombre) {

        CargoModel cargo = this.cargoDao.findById(id)
                .orElseThrow(() -> new RuntimeException("Cargo no encontrado con ID: " + id));

        cargo.setNombre(normalize(nuevoNombre));

        return mapToDto(this.cargoDao.save(cargo));
    }

    /**
     * 📌 Elimina un cargo por ID
     */
    @Transactional
    public void delete(Long id) {

        if (!this.cargoDao.existsById(id)) {
            throw new RuntimeException("Cargo no existe con ID: " + id);
        }

        this.cargoDao.deleteById(id);
    }

    // =========================
    // 🔧 MÉTODOS PRIVADOS
    // =========================

    /**
     * 📌 Verifica existencia por nombre
     */
    public boolean existsByNombre(String nombre) {
        return this.cargoDao.findByNombre(nombre).isPresent();
    }
    /**
     * 📌 Normaliza texto (evita duplicados)
     */
    private String normalize(String value) {
        return value.toUpperCase().trim();
    }

    /**
     * 📌 Convierte Entity → DTO
     */
    private CargoDtoResponse mapToDto(CargoModel cargoModel) {
        return this.modelMapper.map(cargoModel, CargoDtoResponse.class);
    }
}