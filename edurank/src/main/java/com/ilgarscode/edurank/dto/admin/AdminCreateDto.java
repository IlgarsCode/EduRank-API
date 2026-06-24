package com.ilgarscode.edurank.dto.admin;

import lombok.Data;

@Data
public class AdminCreateDto {

    private String id;
    private String name;
    private String surname;
    private String email;
    private String password;
}