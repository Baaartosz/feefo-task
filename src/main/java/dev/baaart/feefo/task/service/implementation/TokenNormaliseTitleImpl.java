package dev.baaart.feefo.task.service.implementation;

import dev.baaart.feefo.task.exception.NoMatchException;
import dev.baaart.feefo.task.service.NormaliseTitle;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class TokenNormaliseTitleImpl implements NormaliseTitle {

    private static final double MINIMUM_THRESHOLD = 0.5;

    @Override
    public String normalise(String jobTitle, List<String> titles) {
        Set<String> inputTokens = getUniqueTokens(toLowercaseTrim(jobTitle));

        String bestMatch = null;
        double bestScore = 0.0;

        for (String title : titles) {
            Set<String> normalisedTitleTokens = getUniqueTokens(toLowercaseTrim(title));
            double score = computeOverlapScore(inputTokens, normalisedTitleTokens);

            if (score > bestScore) {
                bestScore = score;
                bestMatch = title;
            }
        }

        if (bestMatch == null || bestScore < MINIMUM_THRESHOLD) {
            throw new NoMatchException("No match found that meets threshold "
                    + MINIMUM_THRESHOLD + ". Best score was " + bestScore);
        }
        log.debug("Matched `{}` to `{}` with `{}` score", jobTitle, bestMatch, bestScore);
        return bestMatch;
    }

    private static String toLowercaseTrim(String text) {
        return text.toLowerCase().trim();
    }

    private static Set<String> getUniqueTokens(String text) {
        if (text.isEmpty()) {
            return Collections.emptySet();
        }
        return new HashSet<>(Arrays.asList(text.split("\\s+")));
    }

    private static double computeOverlapScore(Set<String> tokensA, Set<String> tokensB) {
        if (tokensA.isEmpty() && tokensB.isEmpty()) {
            return 0.0;
        }
        Set<String> intersection = new HashSet<>(tokensA);
        intersection.retainAll(tokensB);
        double intersectionCount = intersection.size();
        double maxSize = Math.max(tokensA.size(), tokensB.size());
        return intersectionCount / maxSize;
    }

}
