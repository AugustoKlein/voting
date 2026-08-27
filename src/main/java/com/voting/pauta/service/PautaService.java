package com.voting.pauta.service;

import com.voting.voter.dto.VoterDto;
import com.voting.pauta.dto.PautaDto;

import java.time.LocalDateTime;

public interface PautaService {

    Long create(PautaDto pautaDto);

    void vote(Long id, VoterDto voter);

    PautaDto find(Long id);

    void checkOpenPautas();

    PautaDto closeById(Long id);

    void openById(Long id, LocalDateTime endsAt);
}
