package io.dongvelop.bookmanagementsystem.payload.response;

import io.dongvelop.bookmanagementsystem.entity.Author;

import java.util.List;

/**
 * @author 이동엽(Lee Dongyeop)
 * @date 2025. 02. 23
 * @description 저자 목록 조회 API 응답 클래스
 */
public record AuthorListResponse(
        String name,
        String email,
        List<BookResponse> books
) {
    public static AuthorListResponse from(final Author author) {
        return new AuthorListResponse(
                author.getName(),
                author.getEmail(),
                author.getBooks().stream().map(BookResponse::of).toList()
        );
    }
}
