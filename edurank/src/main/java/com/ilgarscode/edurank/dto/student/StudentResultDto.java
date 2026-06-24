package com.ilgarscode.edurank.dto.student;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class git StudentResultDto {

    private String studentId;
    private String fullName;
    private Integer score;
    private String admittedMajor;
    private String status;
}