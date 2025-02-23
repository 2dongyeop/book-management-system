package io.dongvelop.bookmanagementsystem.payload.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.dongvelop.bookmanagementsystem.common.ValidISBN10;
import io.dongvelop.bookmanagementsystem.entity.Author;
import io.dongvelop.bookmanagementsystem.entity.Book;
import io.dongvelop.bookmanagementsystem.excepiton.APIException;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDate;

/**
 * @author 이동엽(Lee Dongyeop)
 * @date 2025. 02. 23
 * @description 도서 생성 API 요청 클래스
 */
public record CreateBookRequest(
        @NotBlank
        String title,

        @Nullable
        String description,

        @NotBlank
        @ValidISBN10
        String isbn,

        @Nullable
        @JsonProperty("publication_date")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        @PastOrPresent(message = "출판일은 미래 날짜가 될 수 없습니다.")
        LocalDate publicationDate,

        @JsonProperty("author_id")
        Long authorId
) {
    public Book toEntity(Author author) throws APIException {
        return new Book(title, description, isbn, publicationDate, author);
    }
}
