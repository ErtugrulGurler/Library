package com.book.book.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import javax.persistence.*;
import static javax.persistence.GenerationType.*;

@Entity@Getter@Setter@NoArgsConstructor
@Table
public class Book {
    @Id
    @GeneratedValue(strategy = AUTO)@Column(name = "ID")
    private Long id;
    @Column(name = "Name")
    private String name;
    @Column(name = "Author")
    private String author;
    @Column(name = "Number_of_Pages")
    private Integer number_of_pages;
    @Column(name = "Price")
    private Integer price;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;


    public Book(String name, String author, Integer number_of_pages, Integer price) {
        this.name = name;
        this.author = author;
        this.number_of_pages = number_of_pages;
        this.price=price;
    }
}

