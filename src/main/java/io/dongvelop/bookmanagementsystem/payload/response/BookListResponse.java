package io.dongvelop.bookmanagementsystem.payload.response;

import io.dongvelop.bookmanagementsystem.entity.Book;

import java.time.LocalDate;

/**
 * @author 이동엽(Lee Dongyeop)
 * @date 2025. 02. 23
 * @description 책 목록 조회 API 응답 클래스
 */
public record BookListResponse(
        Long id,
        String title,
        String description,
        LocalDate publicationDate
) {
    public static BookListResponse of(Book book) {
        return new BookListResponse(book.getId(), book.getTitle(), book.getDescription(), book.getPublicationDate());
    }
}
