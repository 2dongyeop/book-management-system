package io.dongvelop.bookmanagementsystem.endpoint;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.dongvelop.bookmanagementsystem.entity.Author;
import io.dongvelop.bookmanagementsystem.entity.Book;
import io.dongvelop.bookmanagementsystem.excepiton.APIException;
import io.dongvelop.bookmanagementsystem.excepiton.ErrorType;
import io.dongvelop.bookmanagementsystem.payload.request.CreateAuthorRequest;
import io.dongvelop.bookmanagementsystem.payload.request.CreateBookRequest;
import io.dongvelop.bookmanagementsystem.payload.request.UpdateBookRequest;
import io.dongvelop.bookmanagementsystem.repository.AuthorRepository;
import io.dongvelop.bookmanagementsystem.repository.BookRepository;
import io.dongvelop.bookmanagementsystem.service.BookService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class BookIntegrationTest extends IntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private BookService bookService;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private AuthorRepository authorRepository;

    private Author author;
    private Book book;

    @BeforeEach
    public void setup() throws APIException {
        author = authorRepository.save(new CreateAuthorRequest("name", "example@email.com").toEntity());

        final CreateBookRequest request1 = new CreateBookRequest("title", "description", "789-456789-0", LocalDate.now(), author.getId());
        book = bookRepository.save(request1.toEntity(author));
    }

    @AfterEach
    public void tearDown() {
        bookRepository.deleteAll();
        authorRepository.deleteAll();
    }

    @Nested
    @DisplayName("도서 생성 API 통합 테스트")
    class CreateBook {

        @Test
        @DisplayName("ISBN이 중복되지 않았을 경우, 도서 생성에 성공한다.")
        void success() throws Exception {

            // given
            final CreateBookRequest request = new CreateBookRequest("title", "description", "123-456789-0", LocalDate.now(), author.getId());

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
            final CreateBookRequest request = new CreateBookRequest("title", "description", "123-456789-0", LocalDate.now(), author.getId());
            bookService.createBook(request);

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

        @Test
        @DisplayName("도서 목록을 조회한다.")
        void success() throws Exception {

            // given
            final CreateBookRequest request1 = new CreateBookRequest("title", "description", "123-456789-0", LocalDate.now(), author.getId());
            final CreateBookRequest request2 = new CreateBookRequest("title", "description", "223-456789-0", LocalDate.now(), author.getId());

            bookService.createBook(request1);
            bookService.createBook(request2);

            // when
            var result = mockMvc.perform(get("/books"));

            // then
            result.andExpect(status().isOk());
        }
    }

    @Nested
    @DisplayName("도서 상세 조회 API 테스트")
    class getBookDetail {

        @Test
        @DisplayName("도서 아이디가 유효할 경우, 상세 조회에 성공한다.")
        void success() throws Exception {

            // given
            final CreateBookRequest request1 = new CreateBookRequest("title", "description", "223-456789-0", LocalDate.now(), author.getId());
            final Book book = bookService.createBook(request1);

            // when
            var result = mockMvc.perform(get("/books/{id}", book.getId()));

            // then
            result.andExpect(status().isOk());
        }

        @Test
        @DisplayName("도서 아이디가 유효하지 않을 경우, 상세 조회에 실패한다.")
        void fail() throws Exception {

            // given
            final Long notExistBookId = 10_000_000L;

            // when
            var result = mockMvc.perform(get("/books/{id}", notExistBookId));

            // then
            result.andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.error_code").value(ErrorType.NOT_EXIST_DATA.getValue()));
        }
    }

    @Nested
    @DisplayName("도서 수정 API 테스트")
    class UpdateBook {

        @Test
        @DisplayName("도서 아이디와 수정할 정보가 유효할 경우, 수정에 성공한다.")
        void success() throws Exception {

            // given
            final UpdateBookRequest request = new UpdateBookRequest("title", "description", LocalDate.now());

            // when
            var result = mockMvc.perform(patch("/books/{id}", book.getId())
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(objectMapper.writeValueAsString(request))
            );

            // then
            result.andExpect(status().isNoContent());
        }

        @Test
        @DisplayName("도서 아이디가 유효하지 않을 경우, 수정에 실패한다.")
        void fail1() throws Exception {

            // given
            final Long notExistBookId = 10_000_000L;
            final UpdateBookRequest request = new UpdateBookRequest("title", "description", LocalDate.now());

            // when
            var result = mockMvc.perform(patch("/books/{id}", notExistBookId)
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
    class DeleteBook {

        @Test
        @DisplayName("도서 아이디가 유효할 경우, 삭제에 성공한다.")
        void success() throws Exception {

            // given

            // when
            var result = mockMvc.perform(delete("/books/{id}", book.getId()));

            // then
            result.andExpect(status().isNoContent());
        }

        @Test
        @DisplayName("도서 아이디가 유효하지 않을 경우, 삭제에 실패한다.")
        void fail1() throws Exception {

            // given
            final Long notExistBookId = 10_000_000L;

            // when
            var result = mockMvc.perform(delete("/books/{id}", notExistBookId));

            // then
            result.andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.error_code").value(ErrorType.NOT_EXIST_DATA.getValue()));
        }
    }
}