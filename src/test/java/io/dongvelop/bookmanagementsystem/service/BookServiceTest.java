package io.dongvelop.bookmanagementsystem.service;

import autoparams.AutoSource;
import io.dongvelop.bookmanagementsystem.entity.Author;
import io.dongvelop.bookmanagementsystem.entity.Book;
import io.dongvelop.bookmanagementsystem.excepiton.APIException;
import io.dongvelop.bookmanagementsystem.excepiton.ErrorType;
import io.dongvelop.bookmanagementsystem.payload.request.CreateAuthorRequest;
import io.dongvelop.bookmanagementsystem.payload.request.CreateBookRequest;
import io.dongvelop.bookmanagementsystem.payload.request.UpdateBookRequest;
import io.dongvelop.bookmanagementsystem.repository.BookRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @InjectMocks
    private BookService bookService;

    @Mock
    private AuthorService authorService;
    @Mock
    private BookRepository bookRepository;

    @Nested
    @DisplayName("도서 생성 테스트")
    class CreateBook {

        @AutoSource
        @ParameterizedTest
        @DisplayName("요청 값이 유효할 경우, 도서 생성에 성공한다.")
        void success(CreateAuthorRequest createAuthorRequest) throws Exception {

            // given
            final CreateBookRequest request = new CreateBookRequest("title", "description", "123-456789-0", LocalDate.now(), 1L);
            final Author author = createAuthorRequest.toEntity();
            final Book mockedBook = request.toEntity(author);

            given(bookRepository.existsByIsbn(request.isbn())).willReturn(false);
            given(authorService.getAuthorDetail(request.authorId())).willReturn(author);
            given(bookRepository.save(any(Book.class))).willReturn(mockedBook);

            // when
            final Book book = bookService.createBook(request);

            // then
            Assertions.assertThat(book).isNotNull();
            Assertions.assertThat(book.getTitle()).isEqualTo(request.title());
            Assertions.assertThat(book.getIsbn()).isEqualTo(request.isbn());
            Assertions.assertThat(book.getAuthor()).isEqualTo(author);
        }

        @AutoSource
        @ParameterizedTest
        @DisplayName("이미 존재하는 ISBN일 경우, 도서 생성에 실패한다.")
        void fail1() {

            // given
            final CreateBookRequest request = new CreateBookRequest("title", "description", "123-456789-0", LocalDate.now(), 1L);

            given(bookRepository.existsByIsbn(request.isbn())).willReturn(true);

            // when
            final APIException apiException = assertThrowsExactly(APIException.class, () -> bookService.createBook(request));

            // then
            Assertions.assertThat(apiException.getHttpStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
            Assertions.assertThat(apiException.getErrorType()).isEqualTo(ErrorType.EXIST_DATA);
        }

        @AutoSource
        @ParameterizedTest
        @DisplayName("존재하지 않는 저자 아이디일 경우, 도서 생성에 실패한다.")
        void fail2() throws APIException {

            // given
            final Long notExistAuthorId = 1L;
            final CreateBookRequest request = new CreateBookRequest("title", "description", "123-456789-0", LocalDate.now(), notExistAuthorId);

            given(authorService.getAuthorDetail(request.authorId())).willThrow(
                    new APIException(
                            HttpStatus.BAD_REQUEST,
                            ErrorType.NOT_EXIST_DATA
                    ));

            // when
            final APIException apiException = assertThrowsExactly(APIException.class, () -> bookService.createBook(request));

            // then
            Assertions.assertThat(apiException.getHttpStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
            Assertions.assertThat(apiException.getErrorType()).isEqualTo(ErrorType.NOT_EXIST_DATA);
        }
    }

    @Nested
    @DisplayName("도서 목록 조회 테스트")
    class GetBookList {

        @ParameterizedTest
        @DisplayName("도서 목록 조회에 성공한다.")
        @CsvSource({"0, 10"})
        void success(int page, int size) throws APIException {

            // given
            final Pageable pageable = PageRequest.of(page, size);
            final CreateAuthorRequest createAuthorRequest = new CreateAuthorRequest("Author Name", "author@example.com");
            final Author author = createAuthorRequest.toEntity();

            final CreateBookRequest request1 = new CreateBookRequest("title1", "description", "123-456789-0", LocalDate.now(), author.getId());
            final CreateBookRequest request2 = new CreateBookRequest("title2", "description", "223-456789-0", LocalDate.now(), author.getId());
            final CreateBookRequest request3 = new CreateBookRequest("title3", "description", "323-456789-0", LocalDate.now(), author.getId());
            final PageImpl<Book> mockedBooks = new PageImpl<>(List.of(request1.toEntity(author), request2.toEntity(author), request3.toEntity(author)));

            given(bookRepository.findAll(pageable)).willReturn(mockedBooks);

            // when
            final Page<Book> result = bookService.getBookList(pageable);

            // then
            Assertions.assertThat(result.getSize()).isEqualTo(mockedBooks.getSize());
        }
    }

    @Nested
    @DisplayName("도서 상세 조회 테스트")
    class GetBookDetails {

        @AutoSource
        @ParameterizedTest
        @DisplayName("도서 아이디가 유효할 경우, 도서 상세 조회에 성공한다.")
        void success(CreateAuthorRequest createAuthorRequest) throws APIException {

            // given
            final Author author = createAuthorRequest.toEntity();

            final CreateBookRequest request1 = new CreateBookRequest("title1", "description", "123-456789-0", LocalDate.now(), author.getId());
            final Book mockedBook = request1.toEntity(author);

            given(bookRepository.findById(any())).willReturn(Optional.of(mockedBook));

            // when
            final Book result = bookService.getBookDetails(mockedBook.getId());

            // then
            Assertions.assertThat(result.getTitle()).isEqualTo(mockedBook.getTitle());
            Assertions.assertThat(result.getIsbn()).isEqualTo(mockedBook.getIsbn());
            Assertions.assertThat(result.getDescription()).isEqualTo(mockedBook.getDescription());
        }

        @AutoSource
        @ParameterizedTest
        @DisplayName("도서 아이디가 유효하지 않을 경우, 도서 상세 조회에 실패한다.")
        void fail(CreateAuthorRequest createAuthorRequest) throws APIException {

            // given
            final Author author = createAuthorRequest.toEntity();

            final CreateBookRequest request1 = new CreateBookRequest("title1", "description", "123-456789-0", LocalDate.now(), author.getId());
            final Book mockedBook = request1.toEntity(author);

            given(bookRepository.findById(any()))
                    .willAnswer(invocation -> {
                        throw new APIException(HttpStatus.NOT_FOUND, ErrorType.NOT_EXIST_DATA);
                    });

            // when
            final APIException apiException = assertThrowsExactly(APIException.class, () -> bookService.getBookDetails(mockedBook.getId()));

            // then
            Assertions.assertThat(apiException.getHttpStatus()).isEqualTo(HttpStatus.NOT_FOUND);
            Assertions.assertThat(apiException.getErrorType()).isEqualTo(ErrorType.NOT_EXIST_DATA);
        }
    }

    @Nested
    @DisplayName("도서 수정 테스트")
    class UpdateBook {

        @AutoSource
        @ParameterizedTest
        @DisplayName("도서 아이디와 수정할 정보가 유효할 경우, 수정에 성공한다.")
        void success(CreateAuthorRequest createAuthorRequest) throws Exception {

            // given
            final UpdateBookRequest request = new UpdateBookRequest("title", "description", LocalDate.now());
            final Author author = createAuthorRequest.toEntity();

            final CreateBookRequest request1 = new CreateBookRequest("title1", "description", "123-456789-0", LocalDate.now(), author.getId());
            final Book mockedBook = request1.toEntity(author);

            given(bookRepository.findById(any())).willReturn(Optional.of(mockedBook));

            // when
            bookService.updateBook(author.getId(), request);

            // then
            Assertions.assertThat(mockedBook.getTitle()).isEqualTo((request.title()));
            Assertions.assertThat(mockedBook.getDescription()).isEqualTo((request.description()));
            Assertions.assertThat(mockedBook.getPublicationDate()).isEqualTo((request.publicationDate()));
        }

        @Test
        @DisplayName("도서 아이디가 유효하지 않을 경우, 수정에 실패한다.")
        void fail1() {

            // given
            final UpdateBookRequest request = new UpdateBookRequest("title", "description", null);
            final Long notExistBookId = 1L;

            given(bookRepository.findById(any()))
                    .willAnswer(invocation -> {
                        throw new APIException(HttpStatus.NOT_FOUND, ErrorType.NOT_EXIST_DATA);
                    });

            // when
            final APIException apiException = assertThrowsExactly(APIException.class, () -> bookService.updateBook(notExistBookId, request));

            // then
            Assertions.assertThat(apiException.getHttpStatus()).isEqualTo(HttpStatus.NOT_FOUND);
            Assertions.assertThat(apiException.getErrorType()).isEqualTo(ErrorType.NOT_EXIST_DATA);
        }

        @AutoSource
        @ParameterizedTest
        @DisplayName("도서 수정 정보가 유효하지 않을 경우, 수정에 실패한다.")
        void fail2(CreateAuthorRequest createAuthorRequest) throws APIException {

            // given
            final UpdateBookRequest request = new UpdateBookRequest(" ", " ", null);
            final Author author = createAuthorRequest.toEntity();

            final CreateBookRequest request1 = new CreateBookRequest("title1", "description", "123-456789-0", LocalDate.now(), author.getId());
            final Book mockedBook = request1.toEntity(author);

            given(bookRepository.findById(any())).willReturn(Optional.of(mockedBook));

            // when
            bookService.updateBook(author.getId(), request);

            // then
            Assertions.assertThat(mockedBook.getTitle()).isNotEqualTo((request.title()));
            Assertions.assertThat(mockedBook.getDescription()).isNotEqualTo((request.description()));
            Assertions.assertThat(mockedBook.getPublicationDate()).isNotEqualTo((request.publicationDate()));
        }
    }

    @Nested
    @DisplayName("도서 삭제 테스트")
    class DeleteBook {

        @AutoSource
        @ParameterizedTest
        @DisplayName("도서 아이디가 유효할 경우, 삭제에 성공한다.")
        void success(CreateAuthorRequest createAuthorRequest) throws Exception {

            // given
            final Author author = createAuthorRequest.toEntity();

            final CreateBookRequest request1 = new CreateBookRequest("title1", "description", "123-456789-0", LocalDate.now(), author.getId());
            final Book mockedBook = request1.toEntity(author);

            given(bookRepository.findById(any())).willReturn(Optional.of(mockedBook));

            // when
            bookService.deleteBook(author.getId());

            // then
            BDDMockito.verify(bookRepository, Mockito.times(1)).deleteById(any());
        }

        @Test
        @DisplayName("도서 아이디가 유효하지 않을 경우, 삭제에 실패한다.")
        void fail() {

            // given
            final Long notFoundBookId = 1L;
            given(bookRepository.findById(any()))
                    .willAnswer(invocation -> {
                        throw new APIException(HttpStatus.NOT_FOUND, ErrorType.NOT_EXIST_DATA);
                    });

            // when
            final APIException apiException = assertThrowsExactly(APIException.class,
                    () -> bookService.deleteBook(notFoundBookId));

            // then
            Assertions.assertThat(apiException.getHttpStatus()).isEqualTo(HttpStatus.NOT_FOUND);
            Assertions.assertThat(apiException.getErrorType()).isEqualTo(ErrorType.NOT_EXIST_DATA);
        }
    }
}