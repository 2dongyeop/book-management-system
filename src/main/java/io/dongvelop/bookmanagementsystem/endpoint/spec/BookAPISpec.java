package io.dongvelop.bookmanagementsystem.endpoint.spec;

import io.dongvelop.bookmanagementsystem.exception.APIException;
import io.dongvelop.bookmanagementsystem.payload.request.CreateBookRequest;
import io.dongvelop.bookmanagementsystem.payload.request.UpdateBookRequest;
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
 * @description 도서 API 명세
 */
@Tag(name = "Book", description = "도서 API Endpoint")
public interface BookAPISpec {

    @Operation(summary = "도서 생성 API", description = "도서의 ISBN은 고유해야 합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "요청 성공"),
            @ApiResponse(responseCode = "400", description = "요청 실패. 상세 에러 코드 참고.",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = {
                                    @ExampleObject(name = "제목/ISBN/저자 아이디 중 빈 값이 존재할 경우", value = """
                                            {
                                              "error_code": "101",
                                              "error_message": "입력값이 올바르지 않습니다.[[title : 공백일 수 없습니다]]"
                                            }
                                            """),
                                    @ExampleObject(name = "출판일이 미래 날짜일 경우", value = """
                                            {
                                              "error_code": "101",
                                              "error_message": "입력값이 올바르지 않습니다.[[publicationDate : 출판일은 미래 날짜가 될 수 없습니다.]]"
                                            }
                                            """),
                                    @ExampleObject(name = "출판일의 날짜 포맷이 잘못되었을 경우", value = """
                                            {
                                              "error_code": "101",
                                              "error_message": "입력값이 올바르지 않습니다.[Text '2025-02-23T01:01:01' could not be parsed, unparsed text found at index 10]"
                                            }
                                            """),
                                    @ExampleObject(name = "ISBN-10 포맷이 일치하지 않을 경우", value = """
                                            {
                                              "error_code": "101",
                                              "error_message": "입력값이 올바르지 않습니다.[[유효하지 않은 ISBN-10 포맷입니다. 예시 : 123-456789-0]]"
                                            }
                                            """),
                                    @ExampleObject(name = "중복된 ISBN일 경우", value = """
                                            {
                                              "error_code": "103",
                                              "error_message": "이미 데이터가 존재합니다.[isbn[123-456780-0] is already exist]"
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
                                              "error_message": "데이터가 존재하지 않습니다.[authorId[2] not found]"
                                            }
                                            """),
                            }
                    )
            ),
    })
    ResponseEntity<?> createBook(CreateBookRequest request) throws APIException;

    @Operation(summary = "도서 목록 조회 API", description = "제목을 포함하여 검색이 가능하며, 결과는 페이징 처리되어 반환합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "요청 성공"),
    })
    ResponseEntity<?> getBookList(int page, int size, String sortBy, String title) throws APIException;

    @Operation(summary = "도서 상세 조회 API", description = "")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "요청 성공"),
            @ApiResponse(responseCode = "404", description = "요청 실패. 상세 에러 코드 참고.",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = {
                                    @ExampleObject(name = "존재하지 않는 도서 아이디일 경우", value = """
                                            {
                                              "error_code": "104",
                                              "error_message": "데이터가 존재하지 않습니다.[bookId[1] not found]"
                                            }
                                            """),
                            }
                    )
            ),
    })
    ResponseEntity<?> getBookDetails(Long id) throws APIException;

    @Operation(summary = "도서 수정 API", description = "도서 정보는 제목/설명/출판일만 수정할 수 있습니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "요청 성공"),
            @ApiResponse(responseCode = "400", description = "요청 실패. 상세 에러 코드 참고.",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = {
                                    @ExampleObject(name = "출판일이 미래 날짜일 경우", value = """
                                            {
                                              "error_code": "101",
                                              "error_message": "입력값이 올바르지 않습니다.[[publicationDate : 출판일은 미래 날짜가 될 수 없습니다.]]"
                                            }
                                            """),
                                    @ExampleObject(name = "출판일의 날짜 포맷이 잘못되었을 경우", value = """
                                            {
                                              "error_code": "101",
                                              "error_message": "입력값이 올바르지 않습니다.[Text '2025-02-23T01:01:01' could not be parsed, unparsed text found at index 10]"
                                            }
                                            """),
                            }
                    )
            ),
            @ApiResponse(responseCode = "404", description = "요청 실패. 상세 에러 코드 참고.",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = {
                                    @ExampleObject(name = "존재하지 않는 도서 아이디일 경우", value = """
                                            {
                                              "error_code": "104",
                                              "error_message": "데이터가 존재하지 않습니다.[bookId[1] not found]"
                                            }
                                            """),
                            }
                    )
            ),
    })
    ResponseEntity<?> updateBook(Long id, UpdateBookRequest request) throws APIException;

    @Operation(summary = "도서 삭제 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "요청 성공"),
            @ApiResponse(responseCode = "404", description = "요청 실패. 상세 에러 코드 참고.",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = {
                                    @ExampleObject(name = "존재하지 않는 도서 아이디일 경우", value = """
                                            {
                                              "error_code": "104",
                                              "error_message": "데이터가 존재하지 않습니다.[bookId[1] not found]"
                                            }
                                            """),
                            }
                    )
            ),
    })
    ResponseEntity<?> deleteBook(Long id) throws APIException;
}
