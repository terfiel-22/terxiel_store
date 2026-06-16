package com.terxiel.store.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
@ToString
@Entity
@Table(name = "categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Byte id;

    @Column(name="name")
    private String name;

    @OneToMany(mappedBy = "category")
    @ToString.Exclude
    public Set<Product> products = new HashSet<Product>();

    public Category(String name) {
        this.name = name;
    }
}
