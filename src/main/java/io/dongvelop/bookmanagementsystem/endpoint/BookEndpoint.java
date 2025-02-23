package io.dongvelop.bookmanagementsystem.endpoint;

import io.dongvelop.bookmanagementsystem.endpoint.spec.BookAPISpec;
import io.dongvelop.bookmanagementsystem.excepiton.APIException;
import io.dongvelop.bookmanagementsystem.payload.request.CreateBookRequest;
import io.dongvelop.bookmanagementsystem.payload.request.UpdateBookRequest;
import io.dongvelop.bookmanagementsystem.payload.response.BookDetailResponse;
import io.dongvelop.bookmanagementsystem.payload.response.BookListResponse;
import io.dongvelop.bookmanagementsystem.payload.response.CreateBookResponse;
import io.dongvelop.bookmanagementsystem.service.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author 이동엽(Lee Dongyeop)
 * @date 2025. 02. 23
 * @description 도서 관련 API Endpoint
 */
@Slf4j
@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookEndpoint implements BookAPISpec {

    private final BookService bookService;

    /**
     * 도서 생성 API
     *
     * @param request 생성할 도서 정보
     */
    @Override
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createBook(@RequestBody @Valid final CreateBookRequest request) throws APIException {
        log.info("request[{}]", request);

        return new ResponseEntity<>(
                new CreateBookResponse(bookService.createBook(request).getId()),
                HttpStatus.CREATED
        );
    }

    /**
     * 도서 목록 조회 <br/>
     *
     * @param page 페이지 번호
     * @param size 페이지 크기
     */
    @Override
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<BookListResponse>> getBookList(
            @RequestParam(required = false, defaultValue = "0") final int page,
            @RequestParam(required = false, defaultValue = "10") final int size,
            @RequestParam(required = false, defaultValue = "id") final String sort,
            @RequestParam(required = false, defaultValue = "") final String title
    ) {
        log.info("page[{}], size[{}], sort[{}]", page, size, sort);
        final PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, sort));

        return ResponseEntity.ok(bookService.getBookList(pageRequest, title).map(BookListResponse::of));
    }

    /**
     * 도서 상세 조회 API
     *
     * @param id 조회할 도서 아이디
     */
    @Override
    @GetMapping("/{id}")
    public ResponseEntity<BookDetailResponse> getBookDetails(@PathVariable final Long id) throws APIException {
        log.info("id[{}]", id);

        return ResponseEntity.ok(BookDetailResponse.of(bookService.getBookDetails(id)));
    }

    /**
     * 도서 수정 API
     *
     * @param id      수정할 도서 아이디
     * @param request 수정할 도서 정보
     */
    @Override
    @PatchMapping("/{id}")
    public ResponseEntity<?> updateBook(@PathVariable final Long id, @RequestBody @Valid final UpdateBookRequest request) throws APIException {
        log.info("id[{}], request[{}]", id, request);

        bookService.updateBook(id, request);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * 도서 삭제 API
     *
     * @param id 삭제할 도서 아이디
     */
    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBook(@PathVariable final Long id) throws APIException {
        log.info("id[{}]", id);

        bookService.deleteBook(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
