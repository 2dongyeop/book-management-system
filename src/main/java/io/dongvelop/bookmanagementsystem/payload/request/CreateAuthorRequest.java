package io.dongvelop.bookmanagementsystem.payload.request;

import io.dongvelop.bookmanagementsystem.entity.Author;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * @author 이동엽(Lee Dongyeop)
 * @date 2025. 02. 23
 * @description 저자 생성 API 요청 클래스
 */
public record CreateAuthorRequest(
        @NotBlank
        String name,

        @NotBlank
        @Email
        String email
) {

    public Author toEntity() {
        return new Author(name, email);
    }
}
