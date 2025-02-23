package io.dongvelop.bookmanagementsystem.entity;

import autoparams.AutoSource;
import io.dongvelop.bookmanagementsystem.excepiton.APIException;
import io.dongvelop.bookmanagementsystem.excepiton.ErrorType;
import io.dongvelop.bookmanagementsystem.payload.request.UpdateAuthorRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;

class AuthorTest {

    @Nested
    @DisplayName("저자 수정 테스트")
    class UpdateAuthor {

        @AutoSource
        @ParameterizedTest
        @DisplayName("요청 값이 유효할 경우, 저자의 정보가 수정된다.")
        void updateSuccess(Author author) {

            // given
            final UpdateAuthorRequest request = new UpdateAuthorRequest("");

            // when
            final APIException exception = Assertions.assertThrows(APIException.class, () -> author.update(request));

            // then
            assertThat(exception.getHttpStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
            assertThat(exception.getErrorType()).isEqualTo(ErrorType.REQUIRED_INPUT);
        }

        @AutoSource
        @ParameterizedTest
        @DisplayName("요청 값이 비어있을 경우, 예외가 발생한다.")
        void updateFail(Author author) throws Exception {

            // given
            final UpdateAuthorRequest request = new UpdateAuthorRequest("updateName");

            // when
            author.update(request);

            // then
            assertThat(author.getName()).isEqualTo(request.name());
        }
    }
}