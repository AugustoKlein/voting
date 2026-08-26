package com.voting.voter.service;

import com.voting.voter.dto.VoterDto;

public interface VoterService {
    Long create(VoterDto voterDto);
}
