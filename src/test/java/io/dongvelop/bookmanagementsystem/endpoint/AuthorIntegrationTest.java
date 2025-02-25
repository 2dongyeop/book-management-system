package io.dongvelop.bookmanagementsystem.endpoint;

import autoparams.AutoSource;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.dongvelop.bookmanagementsystem.entity.Author;
import io.dongvelop.bookmanagementsystem.exception.APIException;
import io.dongvelop.bookmanagementsystem.exception.ErrorType;
import io.dongvelop.bookmanagementsystem.payload.request.CreateAuthorRequest;
import io.dongvelop.bookmanagementsystem.payload.request.UpdateAuthorRequest;
import io.dongvelop.bookmanagementsystem.service.AuthorService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AuthorIntegrationTest extends IntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private AuthorService authorService;

    @Nested
    @DisplayName("저자 생성 API 통합 테스트")
    class CreateAuthor {

        @AutoSource
        @ParameterizedTest
        @DisplayName("이메일이 중복되지 않았을 경우, 저자 생성에 성공한다.")
        void success(CreateAuthorRequest request) throws Exception {

            // given

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
            authorService.createAuthor(request);

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
    @DisplayName("저자 목록 조회 API 통합 테스트")
    class GetAuthorList {

        @AutoSource
        @ParameterizedTest
        @DisplayName("저자 목록을 조회한다.")
        void success(List<CreateAuthorRequest> requests) throws Exception {

            // given
            requests.forEach(request -> {
                try {
                    authorService.createAuthor(request);
                } catch (APIException e) {
                    throw new RuntimeException(e);
                }
            });

            // when
            var result = mockMvc.perform(get("/authors"));

            // then
            result.andExpect(status().isOk());
        }
    }

    @Nested
    @DisplayName("저자 상세 조회 API 통합 테스트")
    class getAuthorDetail {

        @AutoSource
        @ParameterizedTest
        @DisplayName("저자 아이디가 유효할 경우, 상세 조회에 성공한다.")
        void success(CreateAuthorRequest request) throws Exception {

            // given
            final Author author = authorService.createAuthor(request);

            // when
            var result = mockMvc.perform(get("/authors/{id}", author.getId()));

            // then
            result.andExpect(status().isOk());
        }

        @AutoSource
        @ParameterizedTest
        @DisplayName("저자 아이디가 유효하지 않을 경우, 상세 조회에 실패한다.")
        void fail() throws Exception {

            // given
            final Long notExistAuthorId = 10_000_000L;

            // when
            var result = mockMvc.perform(get("/authors/{id}", notExistAuthorId));

            // then
            result.andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.error_code").value(ErrorType.NOT_EXIST_DATA.getValue()));
        }
    }

    @Nested
    @DisplayName("저자 수정 API 통합 테스트")
    class UpdateAuthor {

        @AutoSource
        @ParameterizedTest
        @DisplayName("저자 아이디가 유효할 경우, 저자 수정에 성공한다.")
        void fail1(CreateAuthorRequest createAuthorRequest, UpdateAuthorRequest updateAuthorRequest) throws Exception {

            // given
            final Author author = authorService.createAuthor(createAuthorRequest);

            // when
            var result = mockMvc.perform(patch("/authors/{id}", author.getId())
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(objectMapper.writeValueAsString(updateAuthorRequest))
            );

            // then
            result.andExpect(status().isNoContent());
        }

        @AutoSource
        @ParameterizedTest
        @DisplayName("저자 아이디가 유효하지 않을 경우, 저자 수정에 실패한다.")
        void fail1(UpdateAuthorRequest request) throws Exception {

            // given
            final Long notExistAuthorId = 10_000_000L;

            // when
            var result = mockMvc.perform(patch("/authors/{id}", notExistAuthorId)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(objectMapper.writeValueAsString(request))
            );

            // then
            result.andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.error_code").value(ErrorType.NOT_EXIST_DATA.getValue()));
        }

        @AutoSource
        @ParameterizedTest
        @DisplayName("수정 요청 형태가 유효하지 않을 경우, 저자 수정에 실패한다.")
        void fail2() throws Exception {

            // given
            final CreateAuthorRequest createAuthorRequest = new CreateAuthorRequest("name", "example@email.com");
            final Author author = authorService.createAuthor(createAuthorRequest);

            final UpdateAuthorRequest request = new UpdateAuthorRequest("");

            // when
            var result = mockMvc.perform(patch("/authors/{id}", author.getId())
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(objectMapper.writeValueAsString(request))
            );

            // then
            result.andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.error_code").value(ErrorType.INVALID_INPUT.getValue()));
        }
    }

    @Nested
    @DisplayName("저자 삭제 API 통합 테스트")
    class DeleteAuthor {

        @AutoSource
        @ParameterizedTest
        @DisplayName("저자 아이디가 유효할 경우, 저자 삭제에 성공한다.")
        void success(CreateAuthorRequest request) throws Exception {

            // given
            final Author author = authorService.createAuthor(request);

            // when
            var result = mockMvc.perform(delete("/authors/{id}", author.getId()));

            // then
            result.andExpect(status().isNoContent());
        }

        @AutoSource
        @ParameterizedTest
        @DisplayName("저자 아이디가 유효하지 경우, 저자 삭제에 실패한다.")
        void fail1() throws Exception {

            // given
            final Long notExistAuthorId = 10_000_000L;

            // when
            var result = mockMvc.perform(delete("/authors/{id}", notExistAuthorId));

            // then
            result.andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.error_code").value(ErrorType.NOT_EXIST_DATA.getValue()));
        }
    }
}