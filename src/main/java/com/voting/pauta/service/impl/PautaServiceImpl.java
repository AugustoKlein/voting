package com.voting.pauta.service.impl;

import com.voting.infra.exception.ExpiredPautaException;
import com.voting.voter.dto.VoterDto;
import com.voting.voter.service.VoterService;
import com.voting.pauta.dto.PautaDto;
import com.voting.pauta.mapper.PautaMapper;
import com.voting.voter.repository.PautaVoterRepository;
import com.voting.pauta.repository.PautaRepository;
import com.voting.pauta.repository.entity.Pauta;
import com.voting.pauta.service.PautaService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
@Slf4j
public class PautaServiceImpl implements PautaService {

    private VoterService voterService;
    private PautaRepository pautaRepository;

    @Override
    public Long create(PautaDto pautaDto) {
        log.info("Creating pauta: name - {}", pautaDto.name());
        Pauta pauta = pautaRepository.save(PautaMapper.toEntity(pautaDto));
        return pauta.getId();
    }

    @Override
    public void vote(Long id, VoterDto voterDto) {
        Pauta pauta = pautaRepository.findById(id).orElseThrow(EntityNotFoundException::new);

        if (pauta.getExpirationTime().isBefore(LocalDateTime.now())) {
            log.error("Unable to vote because pauta is expired");
            throw new ExpiredPautaException();
        }

        voterService.create(voterDto);
    }

    @Override
    public PautaDto find(Long id) {
        log.info("");

        return null;
    }




}
