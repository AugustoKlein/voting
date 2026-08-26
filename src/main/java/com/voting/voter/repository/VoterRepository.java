package com.voting.voter.repository;

import com.voting.voter.repository.entity.Voter;
import org.springframework.data.repository.CrudRepository;

public interface VoterRepository extends CrudRepository<Voter, Long> {

    boolean existsByIdAndPautaId(Long id, Long pautaId);
}
