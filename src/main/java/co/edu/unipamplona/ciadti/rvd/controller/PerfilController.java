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
import org.springframework.web.bind.annotation.RequestParam;


import co.edu.unipamplona.ciadti.rvd.model.dto.PerfilDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.PerfilFormularioDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.EstadoDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.PerfilEstadoFormularioDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.PerfilPersonaDTO;
import co.edu.unipamplona.ciadti.rvd.model.dto.PerfilPersonaFormularioDTO;
import co.edu.unipamplona.ciadti.rvd.model.service.PerfilService;

import io.swagger.v3.oas.annotations.Operation;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/configuration/administration/profiles")
public class PerfilController {

    private final PerfilService perfilService;

    @Operation(
            summary = "Lista todos los perfiles",
            description = "Obtiene los perfiles registrados "
                    + "en el sistema"
    )
    @GetMapping({"", "/list"})
    public ResponseEntity<List<PerfilDTO>> listProfiles() {

        return ResponseEntity.ok(
                perfilService.listProfiles()
        );
    }

    @Operation(
            summary = "Obtiene un perfil por ID"
    )
    @GetMapping("/{id}")
    public ResponseEntity<PerfilDTO> getProfileById(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                perfilService.getProfileById(id)
        );
    }

    @Operation(
            summary = "Crea un nuevo perfil"
    )
    @PostMapping
    public ResponseEntity<Void> saveProfile(
            @RequestBody PerfilFormularioDTO dto
    ) {

        perfilService.saveProfile(dto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
    }

    @Operation(
            summary = "Actualiza un perfil"
    )
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateProfile(
            @PathVariable Long id,
            @RequestBody PerfilFormularioDTO dto
    ) {

        perfilService.updateProfile(id, dto);

        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Elimina un perfil"
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProfile(
            @PathVariable Long id
    ) {

        perfilService.deleteProfile(id);

        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Elimina varios perfiles"
    )
    @DeleteMapping("/batch")
    public ResponseEntity<Void> deleteProfiles(
            @RequestBody List<Long> ids
    ) {

        perfilService.deleteProfiles(ids);

        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Lista los estados asociados al perfil",
            description = "Obtiene los estados relacionados "
                    + "con el perfil seleccionado"
    )
    @GetMapping("/{id}/states")
    public ResponseEntity<List<EstadoDTO>> listProfileStates(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                perfilService.listProfileStates(id)
        );
    }

    @Operation(
            summary = "Guarda los estados asociados al perfil",
            description = "Actualiza las relaciones entre "
                    + "el perfil y los estados seleccionados"
    )
    @PutMapping("/{id}/states")
    public ResponseEntity<Void> saveProfileStates(
            @PathVariable Long id,
            @RequestBody PerfilEstadoFormularioDTO dto
    ) {

        perfilService.saveProfileStates(id, dto);

        return ResponseEntity.ok().build();
    }

    @Operation(

            summary = "Busca personas para asociar a perfiles",

            description = "Permite buscar personas institucionales "
                    + "por documento de identidad o nombre"

    )
    @GetMapping("/persons/search")
    public ResponseEntity<List<PerfilPersonaDTO>> searchPersons(

            @RequestParam(
                    name = "term",
                    required = false
            ) String term

    ) {

        return ResponseEntity.ok(

                perfilService.searchPersons(term)

        );
    }

    @Operation(

            summary = "Lista las personas asociadas al perfil"

    )
    @GetMapping("/{id}/persons")
    public ResponseEntity<List<PerfilPersonaDTO>> listProfilePersons(

            @PathVariable Long id

    ) {

        return ResponseEntity.ok(

                perfilService.listProfilePersons(id)

        );
    }

    @Operation(

            summary = "Guarda las personas asociadas al perfil",

            description = "Actualiza las relaciones entre "
                    + "el perfil y las personas seleccionadas"

    )
    @PutMapping("/{id}/persons")
    public ResponseEntity<Void> saveProfilePersons(

            @PathVariable Long id,

            @RequestBody PerfilPersonaFormularioDTO dto

    ) {

        perfilService.saveProfilePersons(

                id,

                dto

        );

        return ResponseEntity.ok().build();
    }

}