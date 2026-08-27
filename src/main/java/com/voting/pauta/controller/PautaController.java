package com.voting.pauta.controller;

import com.voting.pauta.controller.request.OpenPautaRequest;
import com.voting.pauta.controller.request.PautaRequest;
import com.voting.pauta.controller.request.PautaVoteRequest;
import com.voting.pauta.controller.response.PautaResponse;
import com.voting.pauta.mapper.PautaMapper;
import com.voting.pauta.service.PautaService;
import com.voting.voter.mapper.VoterMapper;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDateTime;


@RestController
@AllArgsConstructor
@RequestMapping("/pauta")
public class PautaController {

    private PautaService pautaService;

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody PautaRequest pautaRequest) {
        Long id = pautaService.create(PautaMapper.toDto(pautaRequest));
        return ResponseEntity.created(URI.create(String.format("/pauta/%s", id))).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PautaResponse> find(@PathVariable("id") Long id) {
        return ResponseEntity.ok(PautaMapper.toResponse(pautaService.find(id)));
    }

    @PutMapping("/{id}/vote")
    public ResponseEntity<Void> vote(@PathVariable("id") Long id, @RequestBody PautaVoteRequest pautaVoteRequest) {
        pautaService.vote(id, VoterMapper.toVoterDto(id, pautaVoteRequest));
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/open-session")
    public ResponseEntity<Void> openSession(@PathVariable("id") Long id, @Valid @RequestBody OpenPautaRequest openPautaRequest) {
        pautaService.openById(id, openPautaRequest.getEndsAt());
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/close-session")
    public ResponseEntity<PautaResponse> closeSession(@PathVariable("id") Long id) {
        return ResponseEntity.ok(PautaMapper.toResponse(pautaService.closeById(id)));
    }
}