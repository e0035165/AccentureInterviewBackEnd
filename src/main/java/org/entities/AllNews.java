package org.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name="All_News")
@AllArgsConstructor
@Data
public class AllNews {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "Headlines")
    private String title;

    @Column(name="Type")
    private String type;

    @Column(name="News_Document")
    private String document;

//    @ManyToMany(mappedBy = "allNews")
//    private List<CustomUserDetails> allUsers;

    public AllNews(String title, String type, String document) {
        this.title = title;
        this.type = type;
        this.document = document;
    }

    public AllNews() {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDocument() {
        return document;
    }
    public void setDocument(String document) {
        this.document = document;
    }

//    public List<CustomUserDetails> getAllUsers() {
//        return allUsers;
//    }
//    public void setAllUsers(List<CustomUserDetails> allUsers) {
//        this.allUsers = allUsers;
//    }
//
//    public void addAllUsers(List<CustomUserDetails> allUsers) {
//        this.allUsers.addAll(allUsers);
//    }



}
