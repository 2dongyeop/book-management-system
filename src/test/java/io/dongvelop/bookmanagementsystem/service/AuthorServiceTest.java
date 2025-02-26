package io.dongvelop.bookmanagementsystem.service;

import autoparams.AutoSource;
import io.dongvelop.bookmanagementsystem.entity.Author;
import io.dongvelop.bookmanagementsystem.exception.APIException;
import io.dongvelop.bookmanagementsystem.exception.ErrorType;
import io.dongvelop.bookmanagementsystem.payload.request.CreateAuthorRequest;
import io.dongvelop.bookmanagementsystem.payload.request.UpdateAuthorRequest;
import io.dongvelop.bookmanagementsystem.repository.AuthorRepository;
import io.dongvelop.bookmanagementsystem.repository.BookRepository;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class AuthorServiceTest {

    @InjectMocks
    private AuthorService authorService;

    @Mock
    private AuthorRepository authorRepository;
    @Mock
    private BookRepository bookRepository; // 저자 삭제 테스트에서 모킹을 위해 선언. 삭제 금지

    @Nested
    @DisplayName("저자 생성 테스트")
    class CreateAuthor {

        @AutoSource
        @ParameterizedTest
        @DisplayName("요청 값이 유효할 경우, 저자 생성에 성공한다.")
        void success(CreateAuthorRequest request) throws Exception {

            // given
            given(authorRepository.existsByEmail(request.email())).willReturn(false);
            given(authorRepository.save(any(Author.class))).willReturn(new Author("name", "email@example.com"));

            // when
            final Author result = authorService.createAuthor(request);

            // then
            Assertions.assertThat(result).isNotNull();
        }

        @AutoSource
        @ParameterizedTest
        @DisplayName("이메일이 중복될 경우, 저자 생성에 실패한다.")
        void fail(CreateAuthorRequest request) {

            // given
            given(authorRepository.existsByEmail(request.email())).willReturn(true);

            // when
            final APIException apiException = assertThrowsExactly(APIException.class, () -> authorService.createAuthor(request));

            // then
            SoftAssertions.assertSoftly(softly -> {
                softly.assertThat(apiException.getHttpStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
                softly.assertThat(apiException.getErrorType()).isEqualTo(ErrorType.EXIST_DATA);
            });
        }
    }

    @Nested
    @DisplayName("저자 목록 조회 테스트")
    class GetAuthorList {

        @AutoSource
        @ParameterizedTest
        @DisplayName("저자 목록 조회에 성공한다.")
        void success(List<Author> mockedAuthors) {

            // given
            given(authorRepository.findAuthors()).willReturn(mockedAuthors);

            // when
            final List<Author> result = authorService.getAuthorList();

            // then
            Assertions.assertThat(result.size()).isEqualTo(mockedAuthors.size());
        }
    }

    @Nested
    @DisplayName("저자 상세 조회 테스트")
    class GetAuthorDetail {

        @AutoSource
        @ParameterizedTest
        @DisplayName("저자 아이디가 유효할 경우, 저자 상세 조회에 성공한다.")
        void success(Author mockedAuthor, Long authorId) throws APIException {

            // given
            given(authorRepository.findById(authorId)).willReturn(Optional.ofNullable(mockedAuthor));

            // when
            final Author result = authorService.getAuthorDetail(authorId);

            // then
            SoftAssertions.assertSoftly(softly -> {
                softly.assertThat(result.getEmail()).isEqualTo(Objects.requireNonNull(mockedAuthor).getEmail());
                softly.assertThat(result.getName()).isEqualTo(mockedAuthor.getName());
            });
        }

        @AutoSource
        @ParameterizedTest
        @DisplayName("저자 아이디가 유효하지 않을 경우, 저자 상세 조회에 실패한다.")
        void fail(Long authorId) {

            // given
            given(authorRepository.findById(authorId)).willReturn(Optional.empty());

            // when
            final APIException apiException = assertThrowsExactly(APIException.class, () -> authorService.getAuthorDetail(authorId));

            // then
            SoftAssertions.assertSoftly(softly -> {
                softly.assertThat(apiException.getHttpStatus()).isEqualTo(HttpStatus.NOT_FOUND);
                softly.assertThat(apiException.getErrorType()).isEqualTo(ErrorType.NOT_EXIST_DATA);
            });
        }
    }

    @Nested
    @DisplayName("저자 수정 테스트")
    class UpdateAuthor {

        @AutoSource
        @ParameterizedTest
        @DisplayName("요청 정보가 유효할 경우, 저자 수정에 성공한다.")
        void success(Author author, Long authorId, UpdateAuthorRequest request) throws APIException {

            // given
            given(authorRepository.findById(authorId)).willReturn(Optional.ofNullable(author));

            // when
            authorService.updateAuthor(authorId, request);

            // then
            Assertions.assertThat(Objects.requireNonNull(author).getName()).isEqualTo(request.name());
        }

        @AutoSource
        @ParameterizedTest
        @DisplayName("저자 아이디가 유효하지 않을 경우, 저자 수정에 실패한다.")
        void fail1(Long authorId, UpdateAuthorRequest request) {

            // given
            given(authorRepository.findById(authorId)).willReturn(Optional.empty());

            // when
            final APIException apiException = assertThrowsExactly(APIException.class, () -> authorService.updateAuthor(authorId, request));

            // then
            SoftAssertions.assertSoftly(softly -> {
                softly.assertThat(apiException.getHttpStatus()).isEqualTo(HttpStatus.NOT_FOUND);
                softly.assertThat(apiException.getErrorType()).isEqualTo(ErrorType.NOT_EXIST_DATA);
            });
        }

        @AutoSource
        @ParameterizedTest
        @DisplayName("요청 정보가 유효하지 않을 경우, 저자 수정에 실패한다.")
        void fail2(Author author, Long authorId) {

            // given
            final UpdateAuthorRequest request = new UpdateAuthorRequest("");
            given(authorRepository.findById(authorId)).willReturn(Optional.ofNullable(author));

            // when
            final APIException apiException = assertThrowsExactly(APIException.class, () -> authorService.updateAuthor(authorId, request));

            // then
            SoftAssertions.assertSoftly(softly -> {
                softly.assertThat(apiException.getHttpStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
                softly.assertThat(apiException.getErrorType()).isEqualTo(ErrorType.REQUIRED_INPUT);
            });
        }
    }

    @Nested
    @DisplayName("저자 삭제 테스트")
    class DeleteAuthor {

        @AutoSource
        @ParameterizedTest
        @DisplayName("저자 아이디가 유효할 경우, 저자 삭제에 성공한다.")
        void success(Author author, Long authorId) throws APIException {

            // given
            given(authorRepository.findById(authorId)).willReturn(Optional.ofNullable(author));

            // when
            authorService.deleteAuthor(authorId);

            // then
            BDDMockito.verify(authorRepository, Mockito.times(1)).deleteById(authorId);
        }

        @AutoSource
        @ParameterizedTest
        @DisplayName("저자 아이디가 유효하지 않을 경우, 저자 삭제에 실패한다.")
        void fail(Long authorId) {

            // given
            given(authorRepository.findById(authorId)).willReturn(Optional.empty());

            // when
            final APIException apiException = assertThrowsExactly(APIException.class, () -> authorService.deleteAuthor(authorId));

            // then
            SoftAssertions.assertSoftly(softly -> {
                softly.assertThat(apiException.getHttpStatus()).isEqualTo(HttpStatus.NOT_FOUND);
                softly.assertThat(apiException.getErrorType()).isEqualTo(ErrorType.NOT_EXIST_DATA);
            });
        }
    }
}