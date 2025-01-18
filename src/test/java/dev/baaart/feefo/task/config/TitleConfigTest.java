package dev.baaart.feefo.task.config;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TitleConfigTest {

    private final ApplicationContextRunner contextRunner =
            new ApplicationContextRunner().withUserConfiguration(TitleConfig.class);

    @Test
    void whenJobTitlesAreProvided_thenTheyShouldBeReturned() {
        contextRunner
                // Simulate providing "job.titles" in application.properties
                .withPropertyValues("job.titles=Software engineer,Accountant")
                .run(context -> {
                    TitleConfig config = context.getBean(TitleConfig.class);
                    List<String> jobTitles = config.getJobTitles();

                    assertNotNull(jobTitles, "Job titles list should not be null");
                    assertEquals(2, jobTitles.size(), "Expected exactly two job titles");
                    assertEquals("Software engineer", jobTitles.get(0));
                    assertEquals("Accountant", jobTitles.get(1));
                });
    }

    @Test
    void whenJobTitlesWithAdditionalWhiteSpaceAreProvided_thenTheyShouldBeReturned() {
        contextRunner
                // Simulate providing "job.titles" in application.properties
                .withPropertyValues("job.titles=    Software engineer ,    Accountant    ")
                .run(context -> {
                    TitleConfig config = context.getBean(TitleConfig.class);
                    List<String> jobTitles = config.getJobTitles();

                    assertNotNull(jobTitles, "Job titles list should not be null");
                    assertEquals(2, jobTitles.size(), "Expected exactly two job titles");
                    assertEquals("Software engineer", jobTitles.get(0));
                    assertEquals("Accountant", jobTitles.get(1));
                });
    }

    @Test
    void whenJobTitlesIsEmpty_thenThrowIllegalStateException() {
        contextRunner
                .withPropertyValues("job.titles=")
                .run(context -> {
                    assertThrows(IllegalStateException.class, () -> {
                        context.getBean(TitleConfig.class);
                    });
                });
    }
}
