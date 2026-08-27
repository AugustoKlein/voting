package com.voting.pauta.scheduler;

import com.voting.pauta.service.PautaService;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class PautaScheduler {

    private PautaService pautaService;

    @Scheduled(fixedDelay = 5000)
    public void checkInProgressPautas() {
        pautaService.checkOpenPautas();
    }
}
