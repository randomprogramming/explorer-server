package com.randomprogramming.explorer.entities;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

// Lombok hashcode breaks everything, do not use
@ToString
@Getter
@Setter
@Entity
@NoArgsConstructor
public class Media {
    @Id
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "uuid2")
    private String id;

    private String url;

    public Media(String url) {
        this.url = url;
    }
}
