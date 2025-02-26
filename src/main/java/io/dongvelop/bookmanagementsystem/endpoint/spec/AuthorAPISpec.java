package io.dongvelop.bookmanagementsystem.endpoint.spec;

import io.dongvelop.bookmanagementsystem.exception.APIException;
import io.dongvelop.bookmanagementsystem.payload.request.CreateAuthorRequest;
import io.dongvelop.bookmanagementsystem.payload.request.UpdateAuthorRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

/**
 * @author 이동엽(Lee Dongyeop)
 * @date 2025. 02. 23
 * @description 저자 API 명세
 */
@Tag(name = "Author", description = "저자 API Endpoint")
public interface AuthorAPISpec {

    @Operation(summary = "저자 생성 API", description = "저자의 이메일은 고유해야 합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "요청 성공"),
            @ApiResponse(responseCode = "400", description = "요청 실패. 상세 에러 코드 참고.",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = {
                                    @ExampleObject(name = "이메일 혹은 이름이 빈 값일 경우", value = """
                                            {
                                              "error_code": "101",
                                              "error_message": "입력값이 올바르지 않습니다.[[name : 공백일 수 없습니다, email : 공백일 수 없습니다]]"
                                            }
                                            """),
                                    @ExampleObject(name = "이메일 형식이 아닐 경우", value = """
                                            {
                                              "error_code": "101",
                                              "error_message": "입력값이 올바르지 않습니다.[[email : 올바른 형식의 이메일 주소여야 합니다]]"
                                            }
                                            """),
                                    @ExampleObject(name = "중복된 이메일인 경우", value = """
                                            {
                                              "error_code": "103",
                                              "error_message": "이미 데이터가 존재합니다.[email[example@email.com] is already exist]"
                                            }
                                            """),
                            }
                    )
            ),
    })
    ResponseEntity<?> createAuthor(CreateAuthorRequest request) throws APIException;

    @Operation(summary = "저자 목록 조회 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "요청 성공"),
    })
    ResponseEntity<?> getAuthorList();

    @Operation(summary = "저자 상세 조회 API", description = "")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "요청 성공"),
            @ApiResponse(responseCode = "404", description = "요청 실패. 상세 에러 코드 참고.",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = {
                                    @ExampleObject(name = "존재하지 않는 저자 아이디일 경우", value = """
                                            {
                                              "error_code": "104",
                                              "error_message": "데이터가 존재하지 않습니다.[authorId[1] not found]"
                                            }
                                            """),
                            }
                    )
            ),
    })
    ResponseEntity<?> getAuthorDetail(Long id) throws APIException;

    @Operation(summary = "저자 수정 API", description = "저자 정보는 이름만 수정할 수 있습니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "요청 성공"),
            @ApiResponse(responseCode = "400", description = "요청 실패. 상세 에러 코드 참고.",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = {
                                    @ExampleObject(name = "이름이 빈 값일 경우", value = """
                                            {
                                              "error_code": "101",
                                              "error_message": "입력값이 올바르지 않습니다.[[name : 공백일 수 없습니다]]"
                                            }
                                            """),
                            }
                    )
            ),
            @ApiResponse(responseCode = "404", description = "요청 실패. 상세 에러 코드 참고.",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = {
                                    @ExampleObject(name = "존재하지 않는 저자 아이디일 경우", value = """
                                            {
                                              "error_code": "104",
                                              "error_message": "데이터가 존재하지 않습니다.[authorId[1] not found]"
                                            }
                                            """),
                            }
                    )
            ),
    })
    ResponseEntity<?> updateAuthor(Long id, UpdateAuthorRequest request) throws APIException;

    @Operation(summary = "저자 삭제 API", description = "저자를 삭제할 경우, 저자가 등록한 도서까지 모두 삭제됩니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "요청 성공"),
            @ApiResponse(responseCode = "404", description = "요청 실패. 상세 에러 코드 참고.",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = {
                                    @ExampleObject(name = "존재하지 않는 저자 아이디일 경우", value = """
                                            {
                                              "error_code": "104",
                                              "error_message": "데이터가 존재하지 않습니다.[authorId[1] not found]"
                                            }
                                            """),
                            }
                    )
            ),
    })
    ResponseEntity<?> deleteAuthor(Long id) throws APIException;
}
