package com.voting.pauta.controller;

import com.voting.pauta.controller.request.OpenPautaRequest;
import com.voting.pauta.controller.request.PautaRequest;
import com.voting.pauta.controller.request.PautaVoteRequest;
import com.voting.pauta.controller.response.PautaResponse;
import com.voting.pauta.mapper.PautaMapper;
import com.voting.pauta.service.PautaService;
import com.voting.voter.mapper.VoterMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;


@RestController
@AllArgsConstructor
@RequestMapping("/api/pauta")
@Tag(name = "Pauta", description = "APIs para a administração e votação de pautas")
public class PautaController {

    private PautaService pautaService;

    @Operation(
            summary = "Cria uma Pauta",
            description = "Cria uma pauta com o status CREATED."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Pauta successfully created",
                    headers = @Header(
                            name = "Location",
                            description = "URI da pauta criada",
                            schema = @Schema(
                                    type = "string",
                                    example = "/api/pauta/1"
                            ))),
            @ApiResponse(
                    responseCode = "404",
                    description = "Pauta não encontrada",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "status": "NOT_FOUND"
                                            }
                                            """
                            ))),
            @ApiResponse(
                    responseCode = "400",
                    description = "Erro na requisição",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "status": "BAD_REQUEST"
                                            }
                                            """
                            ))),
            @ApiResponse(
                    responseCode = "500",
                    description = "Falha Interna",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "status": "INTERNAL_ERROR"
                                            }
                                            """
                            )))
    })
    @PostMapping
    public ResponseEntity<Void> create(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Dados da criação",
                    required = true,
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(
                                    implementation = PautaRequest.class
                            )))
            @RequestBody PautaRequest pautaRequest) {
        Long id = pautaService.create(PautaMapper.toDto(pautaRequest));
        return ResponseEntity.created(URI.create(String.format("/api/pauta/%s", id))).build();
    }


    @Operation(
            summary = "Busca uma pauta",
            description = "Retorna uma pauta pelo seu id."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Pauta encontrada com sucesso",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = PautaResponse.class)
                    )),
            @ApiResponse(
                    responseCode = "404",
                    description = "Pauta não encontrada",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "status": "NOT_FOUND"
                                            }
                                            """
                            ))),
            @ApiResponse(
                    responseCode = "400",
                    description = "Erro na requisição",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "status": "BAD_REQUEST"
                                            }
                                            """
                            ))),
            @ApiResponse(
                    responseCode = "500",
                    description = "Erro interno do servidor",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "status": "INTERNAL_ERROR"
                                            }
                                            """
                            )))
    })
    @GetMapping("/{id}")
    public ResponseEntity<PautaResponse> find(
            @Parameter(
                    name = "id",
                    description = "Identificador da pauta",
                    required = true,
                    example = "1",
                    in = ParameterIn.PATH
            )
            @PathVariable("id") Long id) {
        return ResponseEntity.ok(PautaMapper.toResponse(pautaService.find(id)));
    }

    @Operation(
            summary = "Vota em uma pauta",
            description = "Registra um voto SIM ou NÃO para a pauta informada."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Voto registrado com sucesso"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Requisição de voto inválida",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "status": "INVALID_REQUEST"
                                            }
                                            """
                            ))),
            @ApiResponse(
                    responseCode = "404",
                    description = "Pauta não encontrada",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "status": "NOT_FOUND"
                                            }
                                            """
                            ))),
            @ApiResponse(
                    responseCode = "400",
                    description = "Não é possível votar pois a votação ainda não está aberta",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "status": "PAUTA_IS_CREATED_BUT_NOT_OPENED"
                                            }
                                            """
                            ))),
            @ApiResponse(
                    responseCode = "400",
                    description = "Não é possível votar pois a votação da pauta foi encerrada",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "status": "PAUTA_HAS_BEEN_CLOSED"
                                            }
                                            """
                            ))),
            @ApiResponse(
                    responseCode = "400",
                    description = "Não é possível votar porque o membro já votou",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "status": "ALREADY_VOTED"
                                            }
                                            """
                            ))),
            @ApiResponse(
                    responseCode = "400",
                    description = "Não é possível votar porque o membro não é válido",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "status": "UNABLE_TO_VOTE"
                                            }
                                            """
                            ))),
            @ApiResponse(
                    responseCode = "400",
                    description = "Erro na requisição",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "status": "BAD_REQUEST"
                                            }
                                            """
                            ))),
            @ApiResponse(
                    responseCode = "500",
                    description = "Erro interno do servidor",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "status": "INTERNAL_ERROR"
                                            }
                                            """
                            )))
    })
    @PutMapping("/{id}/vote")
    public ResponseEntity<Void> vote(
            @Parameter(
                    name = "id",
                    description = "Identificador da pauta",
                    required = true,
                    example = "1",
                    in = ParameterIn.PATH
            )
            @PathVariable("id") Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Dados do voto",
                    required = true,
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(
                                    implementation = PautaVoteRequest.class
                            )))
            @RequestBody PautaVoteRequest pautaVoteRequest) {
        pautaService.vote(id, VoterMapper.toVoterDto(id, pautaVoteRequest));
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Abrir uma sessão de votação",
            description = "Abre a sessão de votação da pauta e define a data e hora de encerramento."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Sessão de votação aberta com sucesso"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Requisição inválida",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "status": "INVALID_REQUEST"
                                            }
                                            """
                            ))),
            @ApiResponse(
                    responseCode = "400",
                    description = "Sessão já esta aberta",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "status": "PAUTA_IS_ALREADY_OPENED"
                                            }
                                            """
                            ))),
            @ApiResponse(
                    responseCode = "400",
                    description = "Erro na requisição",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "status": "BAD_REQUEST"
                                            }
                                            """
                            ))),
            @ApiResponse(
                    responseCode = "400",
                    description = "Sessão da pauta já foi encerrada",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "status": "PAUTA_HAS_BEEN_CLOSED"
                                            }
                                            """
                            ))),
            @ApiResponse(
                    responseCode = "404",
                    description = "Pauta não encontrada",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "status": "NOT_FOUND"
                                            }
                                            """
                            ))),
            @ApiResponse(
                    responseCode = "500",
                    description = "Erro interno do servidor",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "status": "INTERNAL_ERROR"
                                            }
                                            """
                            )))
    })
    @PutMapping("/{id}/open-session")
    public ResponseEntity<Void> openSession(
            @Parameter(
                    name = "id",
                    description = "Identificador da pauta",
                    required = true,
                    example = "1",
                    in = ParameterIn.PATH
            )
            @PathVariable("id") Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Configurações da sessão",
                    required = true,
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(
                                    implementation = OpenPautaRequest.class
                            )
                    )
            )
            @Valid @RequestBody OpenPautaRequest openPautaRequest) {
        pautaService.openById(id, openPautaRequest.getEndsAt());
        return ResponseEntity.ok().build();
    }


    @Operation(
            summary = "Encerrar uma sessão de votação",
            description = "Encerra a sessão de votação e retorna os dados finais da pauta."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Sessão de votação encerrada com sucesso",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(
                                    implementation = PautaResponse.class
                            )
                    )),
            @ApiResponse(
                    responseCode = "404",
                    description = "Pauta não encontrada",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "status": "NOT_FOUND"
                                            }
                                            """
                            ))),
            @ApiResponse(
                    responseCode = "400",
                    description = "Não é possível abrir a sessão pois a pauta ainda não está aberta",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "status": "PAUTA_IS_CREATED_BUT_NOT_OPENED"
                                            }
                                            """
                            ))),
            @ApiResponse(
                    responseCode = "500",
                    description = "Erro interno do servidor",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "status": "INTERNAL_ERROR"
                                            }
                                            """
                            )))
    })
    @PutMapping("/{id}/close-session")
    public ResponseEntity<PautaResponse> closeSession(
            @Parameter(
                    name = "id",
                    description = "Identificador da pauta",
                    required = true,
                    example = "1",
                    in = ParameterIn.PATH
            )
            @PathVariable("id") Long id) {
        return ResponseEntity.ok(PautaMapper.toResponse(pautaService.closeById(id)));
    }
}