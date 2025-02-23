package io.dongvelop.bookmanagementsystem.payload.response;

import io.dongvelop.bookmanagementsystem.entity.Author;

import java.util.List;

/**
 * @author 이동엽(Lee Dongyeop)
 * @date 2025. 02. 23
 * @description 저자 상세 조회 API 응답 클래스
 */
public record AuthorDetailResponse(
        Long id,
        String name,
        String email,
        List<BookResponse> books
) {
    public static AuthorDetailResponse of(final Author author) {
        return new AuthorDetailResponse(
                author.getId(),
                author.getName(),
                author.getEmail(),
                author.getBooks().stream().map(BookResponse::of).toList()
        );
    }
}
