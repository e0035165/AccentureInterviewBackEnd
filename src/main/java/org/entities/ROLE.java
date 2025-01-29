package org.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name="Role")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ROLE {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name="name")
    private String name;

    @ManyToMany(mappedBy = "roles")
    List<CustomUserDetails> users;

    public ROLE(String name) {
        this.name = name;
    }
}
