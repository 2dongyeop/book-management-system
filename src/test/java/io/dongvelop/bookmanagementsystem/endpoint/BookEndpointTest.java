package io.dongvelop.bookmanagementsystem.endpoint;

import autoparams.AutoSource;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.dongvelop.bookmanagementsystem.entity.Author;
import io.dongvelop.bookmanagementsystem.entity.Book;
import io.dongvelop.bookmanagementsystem.exception.APIException;
import io.dongvelop.bookmanagementsystem.exception.ErrorType;
import io.dongvelop.bookmanagementsystem.payload.request.CreateBookRequest;
import io.dongvelop.bookmanagementsystem.payload.request.UpdateBookRequest;
import io.dongvelop.bookmanagementsystem.service.BookService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BookEndpoint.class)
class BookEndpointTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private BookService bookService;

    @Nested
    @DisplayName("도서 생성 API 테스트")
    class CreateBook {
        private final CreateBookRequest request = new CreateBookRequest("title", "description", "123-456789-0", LocalDate.now(), 1L);

        @AutoSource
        @ParameterizedTest
        @DisplayName("ISBN이 중복되지 않았을 경우, 도서 생성에 성공한다.")
        void success(Author author) throws Exception {

            // given
            given(bookService.createBook(request)).willReturn(request.toEntity(author));

            // when
            var result = mockMvc.perform(post("/books")
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON_VALUE));

            // then
            result.andExpect(status().isCreated());
        }

        @Test
        @DisplayName("ISBN이 중복되지 않았을 경우, 도서 생성에 실패한다.")
        void fail() throws Exception {

            // given
            given(bookService.createBook(request)).willThrow(
                    new APIException(
                            HttpStatus.BAD_REQUEST,
                            ErrorType.EXIST_DATA
                    )
            );

            // when
            var result = mockMvc.perform(post("/books")
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON_VALUE));

            // then
            result.andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.error_code").value(ErrorType.EXIST_DATA.getValue()));
        }
    }

    @Nested
    @DisplayName("도서 목록 조회 API 테스트")
    class GetBookList {

        @AutoSource
        @ParameterizedTest
        @DisplayName("도서 목록을 조회한다.")
        void success(Author author) throws Exception {

            // given
            final CreateBookRequest request1 = new CreateBookRequest("title", "description", "123-456789-0", LocalDate.now(), author.getId());
            final CreateBookRequest request2 = new CreateBookRequest("title", "description", "223-456789-0", LocalDate.now(), author.getId());

            final PageImpl<Book> response = new PageImpl<>(List.of(request1.toEntity(author), request2.toEntity(author)));
            given(bookService.getBookList(any(), any())).willReturn(response);

            // when
            var result = mockMvc.perform(get("/books"));

            // then
            result.andExpect(status().isOk());
        }
    }

    @Nested
    @DisplayName("도서 상세 조회 API 테스트")
    class getBookDetail {

        @AutoSource
        @ParameterizedTest
        @DisplayName("도서 아이디가 유효할 경우, 상세 조회에 성공한다.")
        void success(Long bookId, Author author) throws Exception {

            // given
            final CreateBookRequest request1 = new CreateBookRequest("title", "description", "123-456789-0", LocalDate.now(), author.getId());
            final Book mockedBook = request1.toEntity(author);

            given(bookService.getBookDetails(bookId)).willReturn(mockedBook);

            // when
            var result = mockMvc.perform(get("/books/{id}", bookId));

            // then
            result.andExpect(status().isOk());
        }

        @AutoSource
        @ParameterizedTest
        @DisplayName("도서 아이디가 유효하지 않을 경우, 상세 조회에 실패한다.")
        void fail(Long bookId) throws Exception {

            // given
            given(bookService.getBookDetails(bookId)).willThrow(
                    new APIException(
                            HttpStatus.NOT_FOUND,
                            ErrorType.NOT_EXIST_DATA
                    )
            );

            // when
            var result = mockMvc.perform(get("/books/{id}", bookId));

            // then
            result.andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.error_code").value(ErrorType.NOT_EXIST_DATA.getValue()));
        }
    }

    @Nested
    @DisplayName("도서 수정 API 테스트")
    class UpdateBook {

        @AutoSource
        @ParameterizedTest
        @DisplayName("도서 아이디가 유효하지 않을 경우, 수정에 실패한다.")
        void fail1(Long bookId) throws Exception {

            // given
            final UpdateBookRequest request = new UpdateBookRequest("title", "description", null);

            doThrow(new APIException(
                    HttpStatus.NOT_FOUND,
                    ErrorType.NOT_EXIST_DATA
            )).when(bookService).updateBook(bookId, request);

            // when
            var result = mockMvc.perform(patch("/books/{id}", bookId)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(objectMapper.writeValueAsString(request))
            );

            // then
            result.andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.error_code").value(ErrorType.NOT_EXIST_DATA.getValue()));
        }
    }

    @Nested
    @DisplayName("도서 삭제 API 테스트")
    class DeleteAuthor {

        @AutoSource
        @ParameterizedTest
        @DisplayName("도서 아이디가 유효하지 않을 경우, 삭제에 실패한다.")
        void fail1(Long bookId) throws Exception {

            // given
            doThrow(new APIException(
                    HttpStatus.NOT_FOUND,
                    ErrorType.NOT_EXIST_DATA
            )).when(bookService).deleteBook(bookId);

            // when
            var result = mockMvc.perform(delete("/books/{id}", bookId));

            // then
            result.andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.error_code").value(ErrorType.NOT_EXIST_DATA.getValue()));
        }
    }
}