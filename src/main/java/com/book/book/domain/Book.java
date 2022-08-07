package com.book.book.domain;
import javax.persistence.*;

@Entity
@Table
public class Book {
    @Id
    @SequenceGenerator(
            name ="sequence",
            sequenceName = "sequence",
            allocationSize = 1
    )
    @GeneratedValue(
        strategy =GenerationType.SEQUENCE,
        generator = "sequence"
    )
    private Long id;
    private String name;
    private String author;
    private Integer number_of_pages;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    public Book(Long id, String name, String author, Integer number_of_pages) {
        this.id = id;
        this.name = name;
        this.author = author;
        this.number_of_pages = number_of_pages;
    }


    public Book(String name, String author, Integer number_of_pages) {
        this.name = name;
        this.author = author;
        this.number_of_pages = number_of_pages;
    }
    public Book() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Integer getNumber_of_pages() {
        return number_of_pages;
    }

    public void setNumber_of_pages(Integer number_of_pages) {
        this.number_of_pages = number_of_pages;
    }
}
