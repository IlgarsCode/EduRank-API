package com.ilgarscode.edurank.entity;

import jakarta.persistence.*;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonManagedReference;


import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Student {

    @Id
    private String id;

    private String name;
    private String surname;
    private String email;
    private String password;

    private String role;

    private Integer score;

    @ElementCollection
    private List<String> choices;

    private String admittedMajor;

    private String status;

    private Boolean approved;

    @OneToMany(
            mappedBy = "student",
            cascade = CascadeType.ALL
    )
    private List<ExamSession> examSessions;

}