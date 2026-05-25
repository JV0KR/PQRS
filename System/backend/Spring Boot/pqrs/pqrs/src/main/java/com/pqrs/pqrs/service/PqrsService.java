package com.pqrs.pqrs.service;

import com.pqrs.pqrs.dto.PqrsResponseDTO;
import com.pqrs.pqrs.dto.RadicarPqrsDTO;
import com.pqrs.pqrs.dto.RadicacionPublicaDTO;
import com.pqrs.pqrs.entity.*;
import com.pqrs.pqrs.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import java.security.MessageDigest;
import java.nio.charset.StandardCharsets;
import java.util.HexFormat;

@Service
public class PqrsService {

    private final PqrsRepository pqrsRepository;
    private final ClienteRepository clienteRepository;
    private final UsuarioRepository usuarioRepository;
    private final TipoRadicadoRepository tipoRadicadoRepository;
    private final EstadoPqrsRepository estadoPqrsRepository;
    private final PerfilRepository perfilRepository;
    private final TipoIdentificacionRepository tipoIdentificacionRepository;

    public PqrsService(PqrsRepository pqrsRepository,
                       ClienteRepository clienteRepository,
                       UsuarioRepository usuarioRepository,
                       TipoRadicadoRepository tipoRadicadoRepository,
                       EstadoPqrsRepository estadoPqrsRepository,
                       PerfilRepository perfilRepository,
                       TipoIdentificacionRepository tipoIdentificacionRepository) {
        this.pqrsRepository = pqrsRepository;
        this.clienteRepository = clienteRepository;
        this.usuarioRepository = usuarioRepository;
        this.tipoRadicadoRepository = tipoRadicadoRepository;
        this.estadoPqrsRepository = estadoPqrsRepository;
        this.perfilRepository = perfilRepository;
        this.tipoIdentificacionRepository = tipoIdentificacionRepository;
    }

    @Transactional
    public PqrsResponseDTO radicarPublico(RadicacionPublicaDTO dto) {
        // 1. Validar Cliente (Si no existe, registrarlo)
        Cliente cliente = clienteRepository.findByCorreo(dto.getCorreo()).orElse(null);
        
        if (cliente == null) {
            // Verificar que no choque el numero de identificacion con otro correo
            if (clienteRepository.existsByNumeroIdentificacion(dto.getNumeroIdentificacion())) {
                throw new RuntimeException("El numero de identificacion ya esta registrado con otro correo.");
            }
            cliente = registrarNuevoCliente(dto);
        } else {
            // Validar que el numero de documento coincida si ya existe
            if (!cliente.getNumeroIdentificacion().equals(dto.getNumeroIdentificacion())) {
                throw new RuntimeException("El correo ya existe pero no coincide con el numero de identificacion proporcionado.");
            }
        }

        // 2. Radicar PQRS
        TipoRadicado tipoRadicado = tipoRadicadoRepository.findById(dto.getIdTipoRadicado())
                .orElseThrow(() -> new RuntimeException("Tipo de radicado no encontrado"));

        EstadoPqrs estadoNuevo = estadoPqrsRepository.findByNombre("Nuevo")
                .orElseThrow(() -> new RuntimeException("Estado 'Nuevo' no encontrado"));

        Pqrs pqrs = new Pqrs();
        pqrs.setNumeroRadicado(generarNumeroRadicado());
        pqrs.setComentarios(dto.getComentarios());
        pqrs.setCliente(cliente);
        pqrs.setTipoRadicado(tipoRadicado);
        pqrs.setEstadoActual(estadoNuevo);
        pqrs.setUsuarioCrea(cliente.getUsuario());

        pqrs = pqrsRepository.save(pqrs);

        // 3. Enviar Correo de Confirmacion
        enviarCorreoConfirmacion(cliente, pqrs);

        return toResponse(pqrs);
    }

    private Cliente registrarNuevoCliente(RadicacionPublicaDTO dto) {
        Perfil perfilCliente = perfilRepository.findByNombre("CLIENTE")
                .orElseThrow(() -> new RuntimeException("Perfil CLIENTE no encontrado"));

        TipoIdentificacion tipoId = tipoIdentificacionRepository.findById(dto.getIdTipoIdentificacion())
                .orElseThrow(() -> new RuntimeException("Tipo de identificacion no encontrado"));

        Usuario usuario = new Usuario();
        usuario.setUsername(dto.getCorreo());
        usuario.setPasswordHash(hashPassword(dto.getNumeroIdentificacion())); // Documento como password
        usuario.setCorreo(dto.getCorreo());
        usuario.setPerfil(perfilCliente);
        usuario = usuarioRepository.save(usuario);

        Cliente cliente = new Cliente();
        cliente.setTipoIdentificacion(tipoId);
        cliente.setNumeroIdentificacion(dto.getNumeroIdentificacion());
        cliente.setNombresCompletos(dto.getNombresCompletos());
        cliente.setCorreo(dto.getCorreo());
        cliente.setTelefonoMovil(dto.getTelefonoMovil());
        cliente.setUsuario(usuario);
        return clienteRepository.save(cliente);
    }

    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hash);
        } catch (Exception e) {
            throw new RuntimeException("Error al hashear contrasena", e);
        }
    }

    @Transactional
    public PqrsResponseDTO radicar(RadicarPqrsDTO dto) {
        Cliente cliente = clienteRepository.findById(dto.getIdCliente())
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        TipoRadicado tipoRadicado = tipoRadicadoRepository.findById(dto.getIdTipoRadicado())
                .orElseThrow(() -> new RuntimeException("Tipo de radicado no encontrado"));

        EstadoPqrs estadoNuevo = estadoPqrsRepository.findByNombre("Nuevo")
                .orElseThrow(() -> new RuntimeException("Estado 'Nuevo' no encontrado"));

        Pqrs pqrs = new Pqrs();
        pqrs.setNumeroRadicado(generarNumeroRadicado());
        pqrs.setComentarios(dto.getComentarios());
        pqrs.setCliente(cliente);
        pqrs.setTipoRadicado(tipoRadicado);
        pqrs.setEstadoActual(estadoNuevo);
        pqrs.setUsuarioCrea(cliente.getUsuario());

        pqrs = pqrsRepository.save(pqrs);
        enviarCorreoConfirmacion(cliente, pqrs);

        return toResponse(pqrs);
    }

    public List<PqrsResponseDTO> consultarPorCliente(Long idCliente) {
        Cliente cliente = clienteRepository.findById(idCliente)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        return pqrsRepository.findByClienteOrderByFechaRadicadoDesc(cliente)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<PqrsResponseDTO> listarTodas() {
        return pqrsRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private String generarNumeroRadicado() {
        String fecha = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String secuencia = String.format("%04d", (int)(Math.random() * 10000));
        return "PQRS-" + fecha + "-" + secuencia;
    }

    private void enviarCorreoConfirmacion(Cliente cliente, Pqrs pqrs) {
        System.out.println("========================================");
        System.out.println("CORREO DE CONFIRMACION (SIMULADO)");
        System.out.println("Para: " + cliente.getCorreo());
        System.out.println("Asunto: Confirmacion de radicacion PQRS");
        System.out.println("Estimado/a " + cliente.getNombresCompletos() + ",");
        System.out.println("Su PQRS ha sido radicada exitosamente.");
        System.out.println("Numero de radicado: " + pqrs.getNumeroRadicado());
        System.out.println("Fecha: " + pqrs.getFechaRadicado());
        System.out.println("========================================");
    }

    private PqrsResponseDTO toResponse(Pqrs pqrs) {
        PqrsResponseDTO dto = new PqrsResponseDTO();
        dto.setId(pqrs.getId());
        dto.setNumeroRadicado(pqrs.getNumeroRadicado());
        dto.setFechaRadicado(pqrs.getFechaRadicado());
        dto.setComentarios(pqrs.getComentarios());
        dto.setTipoRadicado(pqrs.getTipoRadicado().getNombre());
        dto.setEstado(pqrs.getEstadoActual().getNombre());
        dto.setNombreCliente(pqrs.getCliente().getNombresCompletos());
        dto.setCorreoCliente(pqrs.getCliente().getCorreo());
        return dto;
    }
}
