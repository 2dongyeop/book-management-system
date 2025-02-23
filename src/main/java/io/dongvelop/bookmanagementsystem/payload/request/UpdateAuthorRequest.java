package io.dongvelop.bookmanagementsystem.payload.request;

import jakarta.validation.constraints.NotBlank;

/**
 * @author 이동엽(Lee Dongyeop)
 * @date 2025. 02. 23
 * @description 저자 수정 API 요청 클래스
 */
public record UpdateAuthorRequest(
        @NotBlank
        String name
) {
}
