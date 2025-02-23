package io.dongvelop.bookmanagementsystem.endpoint;

import autoparams.AutoSource;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.dongvelop.bookmanagementsystem.entity.Author;
import io.dongvelop.bookmanagementsystem.excepiton.APIException;
import io.dongvelop.bookmanagementsystem.excepiton.ErrorType;
import io.dongvelop.bookmanagementsystem.payload.request.CreateAuthorRequest;
import io.dongvelop.bookmanagementsystem.payload.request.UpdateAuthorRequest;
import io.dongvelop.bookmanagementsystem.repository.AuthorRepository;
import io.dongvelop.bookmanagementsystem.service.AuthorService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AuthorEndpoint.class)
class AuthorEndpointTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AuthorService authorService;
    @MockitoBean
    private AuthorRepository authorRepository;

    @Nested
    @DisplayName("저자 생성 API 테스트")
    class CreateAuthor {

        @AutoSource
        @ParameterizedTest
        @DisplayName("이메일이 중복되지 않았을 경우, 저자 생성에 성공한다.")
        void success(CreateAuthorRequest request, Author author) throws Exception {

            // given
            given(authorService.createAuthor(request)).willReturn(author);

            // when
            var result = mockMvc.perform(post("/authors")
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON_VALUE));

            // then
            result.andExpect(status().isCreated());
        }

        @AutoSource
        @ParameterizedTest
        @DisplayName("이메일이 중복되었을 경우, 저자 생성에 실패한다.")
        void fail(CreateAuthorRequest request) throws Exception {

            // given
            given(authorService.createAuthor(request)).willThrow(
                    new APIException(
                            HttpStatus.BAD_REQUEST,
                            ErrorType.EXIST_DATA,
                            request.email()
                    )
            );

            // when
            var result = mockMvc.perform(post("/authors")
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON_VALUE));

            // then
            result.andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.error_code").value(ErrorType.EXIST_DATA.getValue()));
        }
    }

    @Nested
    @DisplayName("저자 목록 조회 API 테스트")
    class GetAuthorList {

        @AutoSource
        @ParameterizedTest
        @DisplayName("저자 목록을 조회한다.")
        void success(List<Author> responses) throws Exception {

            // given
            given(authorService.getAuthorList()).willReturn(responses);

            // when
            var result = mockMvc.perform(get("/authors"));

            // then
            result.andExpect(status().isOk());
        }
    }

    @Nested
    @DisplayName("저자 상세 조회 API 테스트")
    class getAuthorDetail {

        @AutoSource
        @ParameterizedTest
        @DisplayName("저자 아이디가 유효할 경우, 상세 조회에 성공한다.")
        void success(Long authorId, Author response) throws Exception {

            // given
            given(authorService.getAuthorDetail(authorId)).willReturn(response);

            // when
            var result = mockMvc.perform(get("/authors/{id}", authorId));

            // then
            result.andExpect(status().isOk());
        }

        @AutoSource
        @ParameterizedTest
        @DisplayName("저자 아이디가 유효하지 않을 경우, 상세 조회에 실패한다.")
        void fail(Long authorId) throws Exception {

            // given
            given(authorService.getAuthorDetail(authorId)).willThrow(
                    new APIException(
                            HttpStatus.NOT_FOUND,
                            ErrorType.NOT_EXIST_DATA,
                            "authorId[" + authorId + "] not found"
                    )
            );

            // when
            var result = mockMvc.perform(get("/authors/{id}", authorId));

            // then
            result.andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.error_code").value(ErrorType.NOT_EXIST_DATA.getValue()));
        }
    }

    @Nested
    @DisplayName("저자 수정 API 테스트")
    class UpdateAuthor {

        @AutoSource
        @ParameterizedTest
        @DisplayName("저자 아이디가 유효하지 않을 경우, 저자 수정에 실패한다.")
        void fail1(Long authorId, UpdateAuthorRequest request) throws Exception {

            // given
            doThrow(new APIException(
                    HttpStatus.NOT_FOUND,
                    ErrorType.NOT_EXIST_DATA,
                    "authorId[" + authorId + "] not found"
            )).when(authorService).updateAuthor(authorId, request);

            // when
            var result = mockMvc.perform(patch("/authors/{id}", authorId)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(objectMapper.writeValueAsString(request))
            );

            // then
            result.andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.error_code").value(ErrorType.NOT_EXIST_DATA.getValue()));
        }
    }

    @Nested
    @DisplayName("저자 삭제 API 테스트")
    class DeleteAuthor {

        @AutoSource
        @ParameterizedTest
        @DisplayName("저자 아이디가 유효하지 않을 경우, 저자 삭제에 실패한다.")
        void fail1(Long authorId) throws Exception {

            // given
            doThrow(new APIException(
                    HttpStatus.NOT_FOUND,
                    ErrorType.NOT_EXIST_DATA,
                    "authorId[" + authorId + "] not found"
            )).when(authorService).deleteAuthor(authorId);

            // when
            var result = mockMvc.perform(delete("/authors/{id}", authorId));

            // then
            result.andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.error_code").value(ErrorType.NOT_EXIST_DATA.getValue()));
        }
    }
}