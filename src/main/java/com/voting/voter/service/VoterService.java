package com.voting.voter.service;

import com.voting.voter.dto.VoterDto;

import java.util.List;

public interface VoterService {
    void create(VoterDto voterDto);

    List<VoterDto> findAllByPautaId(Long id);
}
