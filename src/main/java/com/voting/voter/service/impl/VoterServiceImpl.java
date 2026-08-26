package com.voting.voter.service.impl;

import com.voting.client.service.service.DocumentValidationClientService;
import com.voting.infra.exception.MemberUnableToVoteException;
import com.voting.voter.dto.VoterDto;
import com.voting.voter.enums.VoterStatusEnum;
import com.voting.voter.mapper.VoterMapper;
import com.voting.voter.repository.VoterRepository;
import com.voting.voter.repository.entity.Voter;
import com.voting.voter.service.VoterService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class VoterServiceImpl implements VoterService {

    private DocumentValidationClientService documentValidationService;
    private VoterRepository voterRepository;

    @Override
    public Long create(VoterDto voterDto) {
        log.info("Create voter for pauta: pautaId - {}", voterDto.pautaId());

        if (voterRepository.existsByIdAndPautaId(voterDto.id(), voterDto.pautaId())) {
            log.error("Unable to vote because he/she already voted");
            throw new MemberUnableToVoteException(VoterStatusEnum.ALREADY_VOTED.name());
        }

        VoterStatusEnum status = documentValidationService.validateDocumentId(voterDto.cpf()).status();
        if (VoterStatusEnum.UNABLE_TO_VOTE.equals(status)) {
            log.error("Unable to vote because he/she is not valid");
            throw new MemberUnableToVoteException(VoterStatusEnum.UNABLE_TO_VOTE.name());
        }

        Voter voter = voterRepository.save(VoterMapper.toVoter(voterDto));
        return voter.getId();
    }

}
