package co.edu.unipamplona.ciadti.rvd.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.edu.unipamplona.ciadti.rvd.model.dto.EstadoDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.EstadoFormularioDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.EstadoSiguienteFormularioDTO;
import co.edu.unipamplona.ciadti.rvd.model.service.EstadosService;

import io.swagger.v3.oas.annotations.Operation;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/configuration/administration/states")
public class EstadosController {

    private final EstadosService estadosService;

    @Operation(
            summary = "Lista todos los estados",
            description = "Obtiene los estados registrados en el sistema"
    )
    @GetMapping({"", "/list"})
    public ResponseEntity<List<EstadoDTO>> listStates() {

        return ResponseEntity.ok(
                estadosService.listStates()
        );
    }

    @Operation(summary = "Obtiene un estado por ID")
    @GetMapping("/{id}")
    public ResponseEntity<EstadoDTO> getStateById(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                estadosService.getStateById(id)
        );
    }

    @Operation(summary = "Crea un nuevo estado")
    @PostMapping
    public ResponseEntity<Void> saveState(
            @RequestBody EstadoFormularioDTO dto
    ) {

        estadosService.saveState(dto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
    }

    @Operation(summary = "Actualiza un estado")
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateState(
            @PathVariable Long id,
            @RequestBody EstadoFormularioDTO dto
    ) {

        estadosService.updateState(id, dto);

        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Elimina un estado")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteState(
            @PathVariable Long id
    ) {

        estadosService.deleteState(id);

        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Elimina varios estados")
    @DeleteMapping("/batch")
    public ResponseEntity<Void> deleteStates(
            @RequestBody List<Long> ids
    ) {

        estadosService.deleteStates(ids);

        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Lista los estados siguientes",
            description = "Obtiene los estados de destino "
                    + "asociados al estado de origen"
    )
    @GetMapping("/{id}/next-states")
    public ResponseEntity<List<EstadoDTO>> listNextStates(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                estadosService.listNextStates(id)
        );
    }

    @Operation(
            summary = "Guarda los estados siguientes",
            description = "Actualiza las relaciones de estados "
                    + "siguientes del estado de origen"
    )
    @PutMapping("/{id}/next-states")
    public ResponseEntity<Void> saveNextStates(
            @PathVariable Long id,
            @RequestBody EstadoSiguienteFormularioDTO dto
    ) {

        estadosService.saveNextStates(id, dto);

        return ResponseEntity.ok().build();
    }
}