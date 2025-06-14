package com.application.projectweb1.projectweb1.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
//pojo class

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeDTO {

    private long id;

    private String name;

    private String email;

    private int age;

    private LocalDate dateofjoining;

    private Boolean isActive;


}
