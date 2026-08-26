package com.voting.pauta.service;

import com.voting.voter.dto.VoterDto;
import com.voting.pauta.dto.PautaDto;

public interface PautaService {
    Long create(PautaDto pautaDto);
    void vote(Long id, VoterDto voter);
    PautaDto find(Long id);
}
