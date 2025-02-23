package io.dongvelop.bookmanagementsystem.endpoint;

import io.dongvelop.bookmanagementsystem.endpoint.spec.AuthAPISpec;
import io.dongvelop.bookmanagementsystem.excepiton.APIException;
import io.dongvelop.bookmanagementsystem.payload.request.CreateAuthorRequest;
import io.dongvelop.bookmanagementsystem.payload.request.UpdateAuthorRequest;
import io.dongvelop.bookmanagementsystem.payload.response.AuthorDetailResponse;
import io.dongvelop.bookmanagementsystem.payload.response.AuthorListResponse;
import io.dongvelop.bookmanagementsystem.payload.response.CreateAuthorResponse;
import io.dongvelop.bookmanagementsystem.service.AuthorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author 이동엽(Lee Dongyeop)
 * @date 2025. 02. 23
 * @description 저자 관련 API Endpoint
 */
@Slf4j
@RestController
@RequestMapping("/authors")
@RequiredArgsConstructor
public class AuthorEndpoint implements AuthAPISpec {

    private final AuthorService authorService;

    /**
     * 저자 생성 API
     */
    @Override
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CreateAuthorResponse> createAuthor(@RequestBody @Valid final CreateAuthorRequest request) throws APIException {
        log.info("request[{}]", request);

        return new ResponseEntity<>(authorService.createAuthor(request), HttpStatus.CREATED);
    }

    /**
     * 저자 목록 조회 API
     *
     * @param pageSize [선택] 페이징 크기
     * @param pageNum  [선택] 페이지 번호
     * @return 저자 목록
     */
    @Override
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<AuthorListResponse>> getAuthorList(@RequestParam(required = false, defaultValue = "30") final int pageSize,
                                                                  @RequestParam(required = false, defaultValue = "0") final int pageNum) {
        log.info("pageSize[{}], pageNum[{}]", pageSize, pageNum);

        return ResponseEntity.ok(authorService.getAuthorList(pageSize, pageNum)
                .stream()
                .map(AuthorListResponse::from)
                .toList()
        );
    }

    /**
     * 저자 상세 조회 API
     *
     * @param id 저자 아이디
     * @return 저자 정보
     */
    @Override
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AuthorDetailResponse> getAuthorDetail(@PathVariable final Long id) throws APIException {
        log.info("id[{}]", id);

        return ResponseEntity.ok(AuthorDetailResponse.of(authorService.getAuthorDetail(id)));
    }

    /**
     * 저자 수정 API
     *
     * @param id      수정할 저자 아이디
     * @param request 수정할 정보
     */
    @Override
    @PatchMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateAuthor(@PathVariable final Long id, @RequestBody @Valid final UpdateAuthorRequest request) throws APIException {
        log.info("id[{}], request[{}]", id, request);

        authorService.updateAuthor(id, request);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * 저자 삭제 API
     *
     * @param id 삭제할 저자 아이디
     */
    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAuthor(@PathVariable final Long id) throws APIException {
        log.info("id[{}]", id);

        authorService.deleteAuthor(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
