package org.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

@Entity
@Table(name="Available-Contacts")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Contacts {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name="name")
    private String personName;

    @Column(name="email")
    private String email;

    @Column(name="no")
    private String hpNo;

    public Contacts(String personName, String email, String hpNo) {
        this.personName = personName;
        this.email = email;
        this.hpNo = hpNo;
    }


}
