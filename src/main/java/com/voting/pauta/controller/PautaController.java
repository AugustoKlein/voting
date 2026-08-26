package com.voting.pauta.controller;

import com.voting.pauta.controller.request.PautaRequest;
import com.voting.pauta.mapper.PautaMapper;
import com.voting.pauta.service.PautaService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;


@RestController
@AllArgsConstructor
public class PautaController {
    private PautaService pautaService;

    @PostMapping("/pauta")
    public ResponseEntity<Void> post(@RequestBody PautaRequest pautaRequest) {
        Long id = pautaService.create(PautaMapper.toDto(pautaRequest));
        return ResponseEntity.created(URI.create(String.format("/pauta/%s", id))).build();
    }

}