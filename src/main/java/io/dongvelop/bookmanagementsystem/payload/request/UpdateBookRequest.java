package io.dongvelop.bookmanagementsystem.payload.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDate;

/**
 * @author 이동엽(Lee Dongyeop)
 * @date 2025. 02. 23
 * @description 도서 수정 API 요청 클래스
 */
public record UpdateBookRequest(
        @Nullable
        String title,

        @Nullable
        String description,

        @Nullable
        @JsonProperty("publication_date")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        @PastOrPresent(message = "출판일은 미래 날짜가 될 수 없습니다.")
        LocalDate publicationDate
) {
}
