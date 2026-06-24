package com.ilgarscode.edurank.dto.student;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StudentRankingDto {

    private Integer rank;
    private String studentId;
    private String fullName;
    private Integer score;
}