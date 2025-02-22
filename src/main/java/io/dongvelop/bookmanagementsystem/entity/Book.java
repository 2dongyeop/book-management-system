package io.dongvelop.bookmanagementsystem.entity;

import io.dongvelop.bookmanagementsystem.common.Utils;
import io.dongvelop.bookmanagementsystem.excepiton.APIException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author 이동엽(Lee Dongyeop)
 * @date 2025. 02. 23
 * @description 도서 Entity Model
 */
@Entity
@Getter
@Table(name = "book")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Book implements Serializable {

    @Serial
    private static final long serialVersionUID = -7890108862165975032L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * [필수] 제목
     */
    @Column(name = "title", nullable = false)
    private String title;

    /**
     * [선택] 설명
     */
    @Column(name = "description")
    private String description;

    /**
     * [필수. 고유] ISBN
     */
    @Column(name = "isbn", nullable = false, unique = true)
    private String isbn;

    /**
     * [선택] 출판일
     */
    @Column(name = "publication_date")
    private LocalDateTime publicationDate;

    /**
     * [필수] 저자
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private Author author;

    public Book(String title, String nullableDescription, String isbn,
                LocalDateTime nullablePublicationDate, Author author) throws APIException {

        Utils.validateISBN10(isbn);

        this.title = title;
        this.description = nullableDescription;
        this.isbn = isbn;
        this.publicationDate = nullablePublicationDate;
        this.author = author;
        author.getBooks().add(this);
    }
}
