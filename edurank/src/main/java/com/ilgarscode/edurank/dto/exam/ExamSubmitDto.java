package com.ilgarscode.edurank.dto.exam;

import lombok.Data;
import java.util.Map;

@Data
public class ExamSubmitDto {
    private Map<Long, String> answers;
}