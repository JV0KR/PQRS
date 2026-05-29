package com.pqrs.pqrs.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.*;
import java.util.UUID;
import org.springframework.core.io.Resource;

@Service
public class FileStorageService {
    private final Path fileStorageLocation;

    public FileStorageService() {
        // Carpeta donde se guardaran los archivos mapeada al volumen de Docker
        this.fileStorageLocation = Paths.get("/app/uploads").toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            System.err.println("No se pudo crear el directorio donde se almacenaran los archivos subidos.");
        }
    }

    public String storeFile(MultipartFile file, String radicado) {
        try {
            // Normaliza el nombre del archivo
            String originalFileName = file.getOriginalFilename();
            String extension = "";
            if (originalFileName != null && originalFileName.contains(".")) {
                extension = originalFileName.substring(originalFileName.lastIndexOf("."));
            }
            
            // Generar nombre unico basado en radicado y UUID
            String fileName = radicado + "_" + UUID.randomUUID().toString() + extension;
            
            // Copiar el archivo al destino
            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            
            return targetLocation.toString();
        } catch (IOException ex) {
            throw new RuntimeException("No se pudo guardar el archivo " + file.getOriginalFilename(), ex);
        }
    }

    public Resource loadFileAsResource(String filePathStr) {
        try {
            Path filePath = Paths.get(filePathStr).normalize();
            org.springframework.core.io.Resource resource = new org.springframework.core.io.UrlResource(filePath.toUri());
            if (resource.exists()) {
                return resource;
            } else {
                throw new RuntimeException("Archivo no encontrado " + filePathStr);
            }
        } catch (java.net.MalformedURLException ex) {
            throw new RuntimeException("Archivo no encontrado " + filePathStr, ex);
        }
    }
}
