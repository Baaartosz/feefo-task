package dev.baaart.feefo.task.service;

import dev.baaart.feefo.task.exception.NoMatchException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
public class NormaliseServiceTest {

    @Autowired
    private NormaliseService normaliseService;

    @ParameterizedTest
    @CsvSource({
            "'c# EnginEEr', 'Software engineer'",
            "'JAVA ENGINEER', 'Software engineer'",
            "' go Engineer ', 'Software engineer'",
            "' Python engineer', 'Software engineer'",
            "'Software engineer', 'Software engineer'",
            "'solution architect', 'Architect'",
            "'Solution Architect', 'Architect'",
            "' enterprise architect ', 'Architect'",
            "'Enterprise ARCHITECT', 'Architect'",
            "'TECHNICAL architect', 'Architect'",
            "' Technical Architect ', 'Architect'",
            "'technical ARCHITECT', 'Architect'",
            "'quantity surveyor', 'Quantity surveyor'",
            "'Quantity Surveyor', 'Quantity surveyor'",
            "' senior quantity surveyor ', 'Quantity surveyor'",
            "'management accountant', 'Accountant'",
            "' Management Accountant ', 'Accountant'",
            "'accountant', 'Accountant'"
    })
    void shouldReturnNormalizedTitle(String inputTitle, String expectedNormalizedTitle) {
        String result = normaliseService.normalise(inputTitle);
        assertEquals(expectedNormalizedTitle, result);
    }
    
    @Test
    void shouldThrowNoMatchExceptionForNoMatch(){
        assertThrows(NoMatchException.class, () -> normaliseService.normalise("CEO"));
    }
    
    @Test
    void shouldThrowExceptionForInvalidInput() {
        assertThrows(IllegalArgumentException.class, () -> normaliseService.normalise(""));
    }

    @Test
    void shouldThrowExceptionForNullInput() {
        assertThrows(IllegalArgumentException.class, () -> normaliseService.normalise(null));
    }
    
}