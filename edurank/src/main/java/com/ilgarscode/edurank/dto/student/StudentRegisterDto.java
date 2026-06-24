package com.ilgarscode.edurank.dto.student;

import lombok.Data;

@Data
public class StudentRegisterDto {
    private String id;
    private String name;
    private String surname;
    private String email;
    private String password;
}