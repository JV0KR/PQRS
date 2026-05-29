package com.pqrs.pqrs.service;

import com.pqrs.pqrs.dto.CambiarEstadoDTO;
import com.pqrs.pqrs.dto.PqrsResponseDTO;
import com.pqrs.pqrs.entity.*;
import com.pqrs.pqrs.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.core.io.Resource;

@Service
public class GestorService {

    private final PqrsRepository pqrsRepository;
    private final EstadoPqrsRepository estadoPqrsRepository;
    private final PqrsAnexoRepository pqrsAnexoRepository;
    private final FileStorageService fileStorageService;

    public GestorService(PqrsRepository pqrsRepository,
                         EstadoPqrsRepository estadoPqrsRepository, PqrsAnexoRepository pqrsAnexoRepository, FileStorageService fileStorageService) {
        this.pqrsRepository = pqrsRepository;
        this.estadoPqrsRepository = estadoPqrsRepository;
        this.pqrsAnexoRepository = pqrsAnexoRepository;
        this.fileStorageService = fileStorageService;
    }

    public List<PqrsResponseDTO> listarTodas() {
        return pqrsRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public PqrsResponseDTO cambiarEstado(CambiarEstadoDTO dto) {
        Pqrs pqrs = pqrsRepository.findById(dto.getIdPqrs())
                .orElseThrow(() -> new RuntimeException("PQRS no encontrada"));

        EstadoPqrs nuevoEstado = estadoPqrsRepository.findById(dto.getIdEstado())
                .orElseThrow(() -> new RuntimeException("Estado no encontrado"));

        pqrs.setEstadoActual(nuevoEstado);
        pqrs.setJustificacionEstado(dto.getJustificacion());
        pqrs.setFechaActualizacion(java.time.LocalDateTime.now());
        pqrs = pqrsRepository.save(pqrs);

        System.out.println("========================================");
        System.out.println("CAMBIO DE ESTADO (SIMULADO)");
        System.out.println("PQRS: " + pqrs.getNumeroRadicado());
        System.out.println("Nuevo Estado: " + nuevoEstado.getNombre());
        System.out.println("Justificacion: " + dto.getJustificacion());
        System.out.println("========================================");

        return toResponse(pqrs);
    }

    
    public PqrsAnexo obtenerAnexo(Long idPqrs) {
        Pqrs pqrs = pqrsRepository.findById(idPqrs)
                .orElseThrow(() -> new RuntimeException("PQRS no encontrada"));
        return pqrsAnexoRepository.findByPqrs(pqrs)
                .orElseThrow(() -> new RuntimeException("Anexo no encontrado"));
    }

    public Resource cargarArchivoComoRecurso(String ruta) {
        return fileStorageService.loadFileAsResource(ruta);
    }
    public byte[] generarReportePdf() {
        List<Pqrs> lista = pqrsRepository.findAll();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try {
            Document document = new Document(PageSize.A4.rotate());
            PdfWriter.getInstance(document, baos);
            document.open();

            Font titleFont = new Font(Font.HELVETICA, 18, Font.BOLD);
            Font headerFont = new Font(Font.HELVETICA, 10, Font.BOLD, java.awt.Color.WHITE);
            Font cellFont = new Font(Font.HELVETICA, 9);

            Paragraph title = new Paragraph("Reporte de PQRS - SuperMarket", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(20);
            document.add(title);

            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            Paragraph fecha = new Paragraph("Generado: " + java.time.LocalDateTime.now().format(fmt), cellFont);
            fecha.setAlignment(Element.ALIGN_RIGHT);
            fecha.setSpacingAfter(15);
            document.add(fecha);

            PdfPTable table = new PdfPTable(6);
            table.setWidthPercentage(100);
            table.setWidths(new float[]{15, 12, 10, 10, 20, 15});

            String[] headers = {"N Radicado", "Fecha", "Tipo", "Estado", "Comentarios", "Cliente"};
            java.awt.Color headerBg = new java.awt.Color(63, 81, 181);
            for (String h : headers) {
                PdfPCell cell = new PdfPCell(new Phrase(h, headerFont));
                cell.setBackgroundColor(headerBg);
                cell.setPadding(8);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);
            }

            for (Pqrs p : lista) {
                table.addCell(new PdfPCell(new Phrase(p.getNumeroRadicado(), cellFont)));
                table.addCell(new PdfPCell(new Phrase(p.getFechaRadicado() != null ? p.getFechaRadicado().format(fmt) : "-", cellFont)));
                table.addCell(new PdfPCell(new Phrase(p.getTipoRadicado().getNombre(), cellFont)));
                table.addCell(new PdfPCell(new Phrase(p.getEstadoActual().getNombre(), cellFont)));
                String comentario = p.getComentarios();
                if (comentario != null && comentario.length() > 80) {
                    comentario = comentario.substring(0, 80) + "...";
                }
                table.addCell(new PdfPCell(new Phrase(comentario != null ? comentario : "", cellFont)));
                table.addCell(new PdfPCell(new Phrase(p.getCliente().getNombresCompletos(), cellFont)));
            }

            document.add(table);

            Paragraph total = new Paragraph("Total de radicados: " + lista.size(), cellFont);
            total.setSpacingBefore(10);
            document.add(total);

            document.close();
        } catch (Exception e) {
            throw new RuntimeException("Error al generar reporte PDF", e);
        }

        return baos.toByteArray();
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
        dto.setJustificacionEstado(pqrs.getJustificacionEstado());
        pqrsAnexoRepository.findByPqrs(pqrs).ifPresent(anexo -> {
            dto.setTieneAnexo(true);
            dto.setIdAnexo(anexo.getId());
        });
        return dto;
    }
}
