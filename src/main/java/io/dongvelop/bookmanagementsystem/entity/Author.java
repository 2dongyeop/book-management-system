package io.dongvelop.bookmanagementsystem.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 이동엽(Lee Dongyeop)
 * @date 2025. 02. 23
 * @description 저자 Entity Model.
 */
@Entity
@Getter
@Table(name = "author")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Author implements Serializable {

    @Serial
    private static final long serialVersionUID = 7107027513806946728L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * [필수] 이름
     */
    @Column(name = "name", nullable = false)
    private String name;

    /**
     * [필수, 고유] 이메일
     */
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    /**
     * 도서 목록
     */
    @OneToMany(mappedBy = "author")
    private final List<Book> books = new ArrayList<>();

    public Author(String name, String email) {
        this.name = name;
        this.email = email;
    }
}
