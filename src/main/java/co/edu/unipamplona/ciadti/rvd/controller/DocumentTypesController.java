/**
 * Aplicación: rvd
 * Archivo: DocumentTypesController.java
 * Paquete: co.edu.unipamplona.ciadti.rvd.controller
 * Autor: GRUPO DE DESARROLLO ESPECÍFICO - CIADTI - Universidad de Pamplona
 * Fecha de creación: 27/11/2026
 * Modificaciones:
 * 27/11/2026 - Andrés Hernández - Creación inicial
 */
package co.edu.unipamplona.ciadti.rvd.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.edu.unipamplona.ciadti.rvd.model.dto.EliminacionMasivaDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.TiposDocumentoContratacionFormularioDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.TiposDocumentoContratacionListadoDTO;
import co.edu.unipamplona.ciadti.rvd.model.service.TiposDocumentoContratacionService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/configuration/administration/document-types")
public class DocumentTypesController {
    
    private final TiposDocumentoContratacionService service;

    @Operation(
        summary = "Lista tipos de documentos",
        description = "Lista los tipos de documentos configurados para la contratación"
    )
    @GetMapping("/list")
    public ResponseEntity<List<TiposDocumentoContratacionListadoDTO>> list() {
        return ResponseEntity.ok(service.listDocumentTypes());
    }

    @Operation(
        summary = "Guarda tipos de documentos",
        description = "Registra los tipos de documentos para la contratación"
    )
    @PostMapping("/save")
    public ResponseEntity<Void> save(@RequestBody TiposDocumentoContratacionFormularioDTO dto) {
        service.saveDocumentType(dto);
        return ResponseEntity.ok().build();
    }

    @Operation(
        summary = "Actualiza tipos de documentos",
        description = "Actualiza los tipos de documentos configurados para la contratación"
    )
    @PutMapping("/update/{id}")
    public ResponseEntity<Void> update(@PathVariable Long id, @RequestBody TiposDocumentoContratacionFormularioDTO dto) {
        service.updateDocumentType(id, dto);
        return ResponseEntity.ok().build();
    }

    @Operation(
        summary = "Elimina tipos de documentos",
        description = "Elimina un tipo de documento registrado para la contratación"
    )
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteDocumentType(id);
        return ResponseEntity.ok().build();
    }

    @Operation(
        summary = "Elimina varios tipos de documentos",
        description = "Elimina uno o varios tipos de documentos registrados para la contratación"
    )
    @PostMapping("/delete-bulk")
    public ResponseEntity<Void> deleteBulk(@RequestBody EliminacionMasivaDTO dto) {
        service.deleteBulkDocumentTypes(dto.ids());
        return ResponseEntity.ok().build();
    }

}
