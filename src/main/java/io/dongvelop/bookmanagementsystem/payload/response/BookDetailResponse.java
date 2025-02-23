package io.dongvelop.bookmanagementsystem.payload.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dongvelop.bookmanagementsystem.entity.Book;

import java.time.LocalDate;

/**
 * @author 이동엽(Lee Dongyeop)
 * @date 2025. 02. 23
 * @description 도서 상세 조회 API 응답 클래스
 */
public record BookDetailResponse(
        Long id,
        String title,
        String description,
        String isbn,
        @JsonProperty("publication_date")
        LocalDate publicationDate,
        @JsonProperty("author_id")
        Long authorId
) {
    public static BookDetailResponse of(final Book book) {
        return new BookDetailResponse(
                book.getId(),
                book.getTitle(),
                book.getDescription(),
                book.getIsbn(),
                book.getPublicationDate(),
                book.getAuthor().getId()
        );
    }
}
