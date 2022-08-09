package com.book.book.domain;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;

import static javax.persistence.GenerationType.AUTO;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Role {
    @Id @GeneratedValue(strategy = AUTO)
    private Long id;
    @Column(name = "Name")
    private String name;
    @JsonIgnore
    @ManyToMany(mappedBy = "roles")
    private List<User> userList = new ArrayList<>();
    public Role(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}