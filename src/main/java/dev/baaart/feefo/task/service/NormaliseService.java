package dev.baaart.feefo.task.service;

import dev.baaart.feefo.task.config.TitleConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class NormaliseService {

    private final TitleConfig titleConfig;
    private final NormaliseTitle titleNormaliser;

    public String normalise(String jobTitle) {
        if (jobTitle == null || jobTitle.isEmpty()) {
            throw new IllegalArgumentException("Job title cannot be null or empty");
        }
        log.debug("Normalising job title '{}'", jobTitle);
        return titleNormaliser.normalise(jobTitle, titleConfig.getJobTitles());
    }
}
