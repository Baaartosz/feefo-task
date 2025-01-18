package dev.baaart.feefo.task.service.implementation;

import dev.baaart.feefo.task.exception.NoMatchException;
import dev.baaart.feefo.task.service.NormaliseTitle;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class TokenNormaliseTitleImpl implements NormaliseTitle {

    private static final double MINIMUM_THRESHOLD = 0.5;
    private static final String UNMATCHED = "";

    @Override
    public String normalise(String jobTitle, List<String> titles) {
        Set<String> inputTokens = tokenize(jobTitle);

        MatchResult bestMatch = findBestMatchingTitle(inputTokens, titles);

        if (bestMatch.score() < MINIMUM_THRESHOLD) {
            throw new NoMatchException(String.format(
                    "No match found that meets threshold %.2f. Best score was %.2f", MINIMUM_THRESHOLD, bestMatch.score()));
        }

        log.debug("Matched `{}` to `{}` with score `{}`", jobTitle, bestMatch.title(), bestMatch.score());
        return bestMatch.title();
    }

    private MatchResult findBestMatchingTitle(Set<String> inputTokens, List<String> titles) {
        MatchResult bestMatch = new MatchResult(UNMATCHED, 0.0);

        for (String title : titles) {
            Set<String> titleTokens = tokenize(title);
            double score = calculateScore(inputTokens, titleTokens);

            if (score > bestMatch.score()) {
                bestMatch = new MatchResult(title, score);
            }
        }

        return bestMatch;
    }

    private double calculateScore(Set<String> tokensA, Set<String> tokensB) {
        if (tokensA.isEmpty() || tokensB.isEmpty()) {
            return 0.0;
        }

        Set<String> intersection = new HashSet<>(tokensA);
        intersection.retainAll(tokensB);

        return (double) intersection.size() / Math.max(tokensA.size(), tokensB.size());
    }

    private Set<String> tokenize(String text) {
        if (text == null || text.isBlank()) {
            return Collections.emptySet();
        }
        return Arrays
                .stream(text.toLowerCase().trim().split("\\s+"))
                .collect(Collectors.toSet());
    }

    private record MatchResult(String title, double score) { }
}
