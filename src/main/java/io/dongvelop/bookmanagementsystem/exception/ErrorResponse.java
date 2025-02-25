package io.dongvelop.bookmanagementsystem.exception;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * @author 이동엽(Lee Dongyeop)
 * @date 2025. 02. 23
 * @description 커스텀 에러 응답 형태
 */
@Getter
@ToString
@AllArgsConstructor
@JsonPropertyOrder({"error_code", "error_message"})
public class ErrorResponse {
    @JsonProperty("error_code")
    private String code;
    @JsonProperty("error_message")
    private String message;
}
