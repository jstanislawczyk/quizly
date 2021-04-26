package com.quizly.models.entities;

import lombok.Builder;
import lombok.Data;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
@Builder
public class Category {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private String description;
}
