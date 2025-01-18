package dev.baaart.feefo.task.service;

import java.util.List;

public interface NormaliseTitle {
    String normalise(String jobTitle, List<String> titles);
}
