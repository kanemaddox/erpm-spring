package com.saims.erpm.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.saims.erpm.dao.BancoDao;
import com.saims.erpm.dao.CuentaBancariaDao;
import com.saims.erpm.dao.PersonaDao;
import com.saims.erpm.dto.CargoDtoResponse;
import com.saims.erpm.dto.CuentaBancariaDtoRequest;
import com.saims.erpm.dto.CuentaBancariaDtoResponse;
import com.saims.erpm.dto.DatosDtoRequest;
import com.saims.erpm.model.BancoModel;
import com.saims.erpm.model.CargoModel;
import com.saims.erpm.model.CuentaBancariaModel;
import com.saims.erpm.model.PersonaModel;
import com.saims.erpm.service.CuentaBancariaService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CuentaBancariaServiceImpl implements CuentaBancariaService {

    private final CuentaBancariaDao cuentaBancariaDao;
    private final PersonaDao personaDao;
    private final BancoDao bancoDao;
    private final ModelMapper modelMapper;

    /**
     * Registra o reutiliza una cuenta bancaria para una persona.
     * 
     * Reglas:
     * - Una persona puede tener múltiples cuentas, pero solo una activa.
     * - Si la cuenta ya existe (mismo banco + número), se reactiva.
     * - Si no existe, se crea una nueva y se desactivan las anteriores.
     *
     * @param personaModel Persona asociada
     * @param bancoModel Banco asociado
     * @param datosDtoRequest Datos de la cuenta
     * @return Cuenta bancaria creada o existente
     */
    @Transactional
    public CuentaBancariaModel createOrGet(PersonaModel personaModel,BancoModel bancoModel,DatosDtoRequest datosDtoRequest) {

        Optional<CuentaBancariaModel> cuentaExistente = findCuentaExistente(personaModel.getId(), bancoModel.getId(), datosDtoRequest.getNumero());

        // Desactivar todas las cuentas previas
        desactivarCuentas(personaModel.getId());

        if (cuentaExistente.isPresent()) {
            CuentaBancariaModel cuenta = cuentaExistente.get();
            cuenta.setEstado(true);
            return this.cuentaBancariaDao.save(cuenta);
        }

        // Crear nueva cuenta
        CuentaBancariaModel nuevaCuenta = buildCuenta(personaModel, bancoModel,datosDtoRequest.getNumero(), datosDtoRequest.getTipoCuenta());

        this.cuentaBancariaDao.save(nuevaCuenta);
        return nuevaCuenta;
    }

    /**
     * Crea una cuenta bancaria a partir de un DTO.
     *
     * @param request DTO de entrada
     * @return DTO de respuesta
     */
    @Transactional
    public CuentaBancariaDtoResponse createCuentaBancaria(CuentaBancariaDtoRequest request) {

        PersonaModel persona = this.personaDao.getId(request.getId_persona());
        BancoModel banco = this.bancoDao.getId(request.getId_banco());
        
        DatosDtoRequest datos = new DatosDtoRequest();
        datos.setNumero(request.getNumero());
        datos.setTipoCuenta(request.getTipoCuenta());

        CuentaBancariaModel cuenta = createOrGet(persona,banco,datos);
        
        return this.mapToDto(cuenta);
    }

    /**
     * Obtiene una cuenta bancaria por ID.
     *
     * @param id identificador
     * @return cuenta bancaria
     */
    @Transactional
    public CuentaBancariaDtoResponse getCuentaBancaria(Long id) {
        CuentaBancariaModel cuenta = cuentaBancariaDao.getId(id);
        return this.mapToDto(cuenta);
    }

    /**
     * Lista cuentas bancarias por persona.
     *
     * @param idPersona ID de la persona
     * @return lista de cuentas
     */
    @Transactional
    public List<CuentaBancariaDtoResponse> getCuentasBancarias(Long idPersona) {
        return cuentaBancariaDao.getListaCuentaBancaria(idPersona)
                .stream()
                .map(cuenta -> this.mapToDto(cuenta))
                .collect(Collectors.toList());
    }

    /**
     * Lista todas las cuentas bancarias del sistema.
     *
     * @return lista completa
     */
    @Transactional
    public List<CuentaBancariaDtoResponse> getCuentasBancarias() {
        return this.cuentaBancariaDao.findAll()
                .stream()
                .map(cuenta -> this.mapToDto(cuenta))
                .collect(Collectors.toList());
    }

    // =========================
    // 🔧 MÉTODOS PRIVADOS
    // =========================

    /**
     * Busca si ya existe una cuenta con mismo banco y número.
     */
    private Optional<CuentaBancariaModel> findCuentaExistente(Long idPersona,Long idBanco,String numero) {

        return this.cuentaBancariaDao.getListaCuentaBancaria(idPersona)
                .stream()
                .filter(c -> c.getBanco().getId().equals(idBanco)
                        && c.getNumero().equals(numero))
                .findFirst();
    }

    /**
     * Desactiva todas las cuentas de una persona.
     */
    private void desactivarCuentas(Long idPersona) {
        List<CuentaBancariaModel> cuentas = this.cuentaBancariaDao.getListaCuentaBancaria(idPersona);

        cuentas.forEach(c -> {
            c.setEstado(false);
            this.cuentaBancariaDao.save(c);
        });
    }

    /**
     * Construye una nueva cuenta bancaria.
     */
    private CuentaBancariaModel buildCuenta(PersonaModel persona,BancoModel banco,String numero,String tipoCuenta) {

        CuentaBancariaModel cuenta = new CuentaBancariaModel();
        cuenta.setPersona(persona);
        cuenta.setBanco(banco);
        cuenta.setNumero(numero);
        cuenta.setTipoCuenta(tipoCuenta);
        cuenta.setEstado(true);

        return cuenta;
    }

    // =========================
    // 🚀 MÉTODOS NUEVOS (FUTURO)
    // =========================

    /**
     * Activa una cuenta específica y desactiva las demás.
     */
    @Transactional
    public void activarCuenta(Long idCuenta) {
        CuentaBancariaModel cuenta = cuentaBancariaDao.getId(idCuenta);

        desactivarCuentas(cuenta.getPersona().getId());

        cuenta.setEstado(true);
        this.cuentaBancariaDao.save(cuenta);
    }

    /**
     * Elimina (lógicamente) una cuenta bancaria.
     */
    @Transactional
    public void desactivarCuenta(Long idCuenta) {
        CuentaBancariaModel cuenta = cuentaBancariaDao.getId(idCuenta);
        cuenta.setEstado(false);
        this.cuentaBancariaDao.save(cuenta);
    }

    /**
     * Obtiene la cuenta activa de una persona.
     */
    @Transactional
    public Optional<CuentaBancariaDtoResponse> getCuentaActiva(Long idPersona) {
        return cuentaBancariaDao.getListaCuentaBancaria(idPersona)
                .stream()
                .filter(CuentaBancariaModel::getEstado)
                .findFirst()
                .map(c -> this.mapToDto(c));
    }

    /**
     * Verifica si una persona tiene cuentas registradas.
     */
    @Transactional
    public boolean tieneCuentas(Long idPersona) {
        return !this.cuentaBancariaDao.getListaCuentaBancaria(idPersona).isEmpty();
    }
    
    /**
     * 📌 Convierte Entity → DTO
     */
    private CuentaBancariaDtoResponse mapToDto(CuentaBancariaModel cuentaBancariaModel) {
        return this.modelMapper.map(cuentaBancariaModel, CuentaBancariaDtoResponse.class);
    }
}