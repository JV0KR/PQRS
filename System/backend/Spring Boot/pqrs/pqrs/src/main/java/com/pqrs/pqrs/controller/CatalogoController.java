package com.pqrs.pqrs.controller;

import com.pqrs.pqrs.entity.TipoIdentificacion;
import com.pqrs.pqrs.entity.TipoRadicado;
import com.pqrs.pqrs.repository.TipoIdentificacionRepository;
import com.pqrs.pqrs.repository.TipoRadicadoRepository;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/catalogos")
@CrossOrigin("*")
public class CatalogoController {

    private final TipoIdentificacionRepository tipoIdentificacionRepository;
    private final TipoRadicadoRepository tipoRadicadoRepository;

    public CatalogoController(TipoIdentificacionRepository tipoIdentificacionRepository,
                              TipoRadicadoRepository tipoRadicadoRepository) {
        this.tipoIdentificacionRepository = tipoIdentificacionRepository;
        this.tipoRadicadoRepository = tipoRadicadoRepository;
    }

    @GetMapping("/tipos-identificacion")
    public List<TipoIdentificacion> listarTiposIdentificacion() {
        return tipoIdentificacionRepository.findAll();
    }

    @GetMapping("/tipos-radicado")
    public List<TipoRadicado> listarTiposRadicado() {
        return tipoRadicadoRepository.findAll();
    }
}
