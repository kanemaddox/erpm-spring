package com.saims.erpm.service.impl;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.saims.erpm.dao.EmpleadoDao;
import com.saims.erpm.dto.DatosDtoRequest;
import com.saims.erpm.dto.EmpleadoDtoResponse;
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

    /**
     * Método principal para registrar o recuperar un empleado.
     * 
     * Flujo:
     * 1. Verifica si ya existe un empleado asociado a la persona.
     * 2. Si existe:
     *    - Retorna el empleado existente (opcionalmente se podría actualizar).
     * 3. Si no existe:
     *    - Crea un nuevo empleado con los datos proporcionados.
     * 
     * @param personaModel   Persona asociada al empleado
     * @param cargoModel     Cargo del empleado
     * @param profesionModel Profesión del empleado
     * @param sucursalModel  Sucursal donde trabaja
     * @param datosDtoRequest Datos adicionales
     * @return EmpleadoModel persistido o existente
     */
    @Transactional
    public EmpleadoModel addDatos(PersonaModel personaModel,CargoModel cargoModel,ProfesionModel profesionModel,SucursalModel sucursalModel,DatosDtoRequest datosDtoRequest) {

        return this.empleadoDao.findByPersona_Id(personaModel.getId())
                .map(empleadoExistente -> actualizarEmpleado(empleadoExistente, cargoModel, profesionModel, sucursalModel, datosDtoRequest))
                .orElseGet(() -> crearEmpleado(personaModel, cargoModel, profesionModel, sucursalModel, datosDtoRequest));
    }

    /**
     * Crea un nuevo empleado con valores por defecto.
     */
    private EmpleadoModel crearEmpleado(PersonaModel personaModel,CargoModel cargoModel,ProfesionModel profesionModel,SucursalModel sucursalModel,DatosDtoRequest datosDtoRequest) {

        EmpleadoModel nuevo = new EmpleadoModel();
        nuevo.setPersona(personaModel);
        nuevo.setCargo(cargoModel);
        nuevo.setProfesion(profesionModel);
        nuevo.setSucursal(sucursalModel);
        nuevo.setEstado(true);
        nuevo.setIdpJefe(datosDtoRequest.getIdpJefe());
        nuevo.setTipo(TipoEmpleado.SUBORDINADO);

        return empleadoDao.save(nuevo);
    }

    /**
     * Actualiza los datos de un empleado existente.
     */
    private EmpleadoModel actualizarEmpleado(EmpleadoModel empleado,CargoModel cargoModel,ProfesionModel profesionModel,SucursalModel sucursalModel,DatosDtoRequest datosDtoRequest) {

        empleado.setCargo(cargoModel);
        empleado.setProfesion(profesionModel);
        empleado.setSucursal(sucursalModel);
        empleado.setIdpJefe(datosDtoRequest.getIdpJefe());

        return this.empleadoDao.save(empleado);
    }

    /**
     * Obtiene un empleado por ID.
     */
    @Transactional
    public EmpleadoDtoResponse getById(Long id) {
        EmpleadoModel empleado = empleadoDao.findById(id)
                .orElseThrow(() -> new RuntimeException("Empleado no encontrado con ID: " + id));

        return this.mapToDto(empleado);
    }

    /**
     * Lista todos los empleados.
     */
    @Transactional
    public List<EmpleadoDtoResponse> getAll() {
        return empleadoDao.findAll()
                .stream()
                .map(emp -> this.mapToDto(emp))
                .toList();
    }

    /**
     * Lista empleados paginados.
     */
    @Transactional
    public Page<EmpleadoDtoResponse> getAllPaginated(Pageable pageable) {
        return empleadoDao.findAll(pageable)
                .map(emp -> this.mapToDto(emp));
    }

    /**
     * Busca un empleado por ID de persona.
     */
    @Transactional
    public EmpleadoDtoResponse findByPersonaId(Long personaId) {
        EmpleadoModel empleado = empleadoDao.findByPersona_Id(personaId)
                .orElseThrow(() -> new RuntimeException("Empleado no encontrado para la persona"));

        return this.mapToDto(empleado);
    }

    /**
     * Cambia el estado (activo/inactivo) de un empleado.
     */
    @Transactional
    public void cambiarEstado(Long id, boolean estado) {
        EmpleadoModel empleado = this.empleadoDao.findById(id)
                .orElseThrow(() -> new RuntimeException("Empleado no encontrado"));

        empleado.setEstado(estado);
        this.empleadoDao.save(empleado);
    }

    /**
     * Elimina un empleado (borrado lógico recomendado).
     */
    @Transactional
    public void delete(Long id) {
        EmpleadoModel empleado = this.empleadoDao.findById(id)
                .orElseThrow(() -> new RuntimeException("Empleado no encontrado"));

        empleado.setEstado(false); // borrado lógico
        this.empleadoDao.save(empleado);
    }
    
    /**
     * 📌 Convierte Entity → DTO
     */
    private EmpleadoDtoResponse mapToDto(EmpleadoModel empleado) {
        return this.modelMapper.map(empleado, EmpleadoDtoResponse.class);
    }
}