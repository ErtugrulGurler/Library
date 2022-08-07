package com.book.book.domain;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Token {


    @SequenceGenerator(
            name ="sequence",
            sequenceName = "sequence",
            allocationSize = 1
    )
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "sequence"
    )
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private LocalDateTime createdAt;
    private LocalDateTime expiresAt;

    public Token(User user, LocalDateTime createdAt, LocalDateTime expiresAt) {
        this.user = user;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
    }

}
