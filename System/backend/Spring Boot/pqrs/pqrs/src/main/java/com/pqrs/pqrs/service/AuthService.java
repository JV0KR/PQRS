package com.pqrs.pqrs.service;

import com.pqrs.pqrs.dto.*;
import com.pqrs.pqrs.entity.*;
import com.pqrs.pqrs.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.security.MessageDigest;
import java.nio.charset.StandardCharsets;
import java.util.HexFormat;

@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final ClienteRepository clienteRepository;
    private final GestorRepository gestorRepository;
    private final PerfilRepository perfilRepository;
    private final TipoIdentificacionRepository tipoIdentificacionRepository;

    public AuthService(UsuarioRepository usuarioRepository,
                       ClienteRepository clienteRepository,
                       GestorRepository gestorRepository,
                       PerfilRepository perfilRepository,
                       TipoIdentificacionRepository tipoIdentificacionRepository) {
        this.usuarioRepository = usuarioRepository;
        this.clienteRepository = clienteRepository;
        this.gestorRepository = gestorRepository;
        this.perfilRepository = perfilRepository;
        this.tipoIdentificacionRepository = tipoIdentificacionRepository;
    }

    public LoginResponseDTO login(LoginDTO dto) {
        Usuario usuario = usuarioRepository.findByCorreo(dto.getCorreo())
                .orElseThrow(() -> new RuntimeException("Credenciales invalidas"));

        String hashedInput = hashPassword(dto.getPassword());
        if (!usuario.getPasswordHash().equals(hashedInput)) {
            throw new RuntimeException("Credenciales invalidas");
        }

        if (!usuario.getActivo()) {
            throw new RuntimeException("Usuario inactivo");
        }

        String perfilNombre = usuario.getPerfil().getNombre();
        LoginResponseDTO response = new LoginResponseDTO();
        response.setCorreo(usuario.getCorreo());
        response.setPerfil(perfilNombre);

        if ("CLIENTE".equals(perfilNombre)) {
            Cliente cliente = clienteRepository.findByUsuario(usuario)
                    .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
            response.setId(cliente.getId());
            response.setNombresCompletos(cliente.getNombresCompletos());
        } else if ("GESTOR".equals(perfilNombre)) {
            Gestor gestor = gestorRepository.findByUsuario(usuario)
                    .orElseThrow(() -> new RuntimeException("Gestor no encontrado"));
            response.setId(gestor.getId());
            response.setNombresCompletos(gestor.getNombresCompletos());
        } else {
            response.setId(usuario.getId());
            response.setNombresCompletos(usuario.getUsername());
        }

        return response;
    }

    @Transactional
    public ClienteResponseDTO registrar(RegistroClienteDTO dto) {
        if (clienteRepository.existsByCorreo(dto.getCorreo())) {
            throw new RuntimeException("Ya existe un cliente con ese correo");
        }
        if (clienteRepository.existsByNumeroIdentificacion(dto.getNumeroIdentificacion())) {
            throw new RuntimeException("Ya existe un cliente con ese numero de identificacion");
        }
        if (usuarioRepository.existsByCorreo(dto.getCorreo())) {
            throw new RuntimeException("Ya existe un usuario con ese correo");
        }

        Perfil perfilCliente = perfilRepository.findByNombre("CLIENTE")
                .orElseThrow(() -> new RuntimeException("Perfil CLIENTE no encontrado"));

        TipoIdentificacion tipoId = tipoIdentificacionRepository.findById(dto.getIdTipoIdentificacion())
                .orElseThrow(() -> new RuntimeException("Tipo de identificacion no encontrado"));

        Usuario usuario = new Usuario();
        usuario.setUsername(dto.getCorreo());
        usuario.setPasswordHash(hashPassword(dto.getNumeroIdentificacion()));
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
        cliente = clienteRepository.save(cliente);

        return toClienteResponse(cliente);
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

    private ClienteResponseDTO toClienteResponse(Cliente cliente) {
        ClienteResponseDTO response = new ClienteResponseDTO();
        response.setIdCliente(cliente.getId());
        response.setNombresCompletos(cliente.getNombresCompletos());
        response.setCorreo(cliente.getCorreo());
        response.setNumeroIdentificacion(cliente.getNumeroIdentificacion());
        response.setTipoIdentificacion(cliente.getTipoIdentificacion().getNombre());
        response.setTelefonoMovil(cliente.getTelefonoMovil());
        return response;
    }
}
