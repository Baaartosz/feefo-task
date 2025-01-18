package dev.baaart.feefo.task.config;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Slf4j
@Getter
@Configuration
public class TitleConfig {

    @Value("#{'${job.titles}'.split(',')}")
    private List<String> jobTitles;

    @PostConstruct
    public void validate() {
        List<String> trimmed = jobTitles.stream()
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .toList();

        if (trimmed.isEmpty()) {
            log.error("Job titles cannot be empty. Please provide at least one job title in 'job.titles'");
            throw new IllegalArgumentException(
                    "Job titles cannot be empty. "
                            + "Please provide at least one job title in 'job.titles'."
            );
        }

        this.jobTitles = trimmed;
    }
}