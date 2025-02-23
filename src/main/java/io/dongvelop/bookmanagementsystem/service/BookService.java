package io.dongvelop.bookmanagementsystem.service;

import io.dongvelop.bookmanagementsystem.entity.Author;
import io.dongvelop.bookmanagementsystem.entity.Book;
import io.dongvelop.bookmanagementsystem.excepiton.APIException;
import io.dongvelop.bookmanagementsystem.excepiton.ErrorType;
import io.dongvelop.bookmanagementsystem.payload.request.CreateBookRequest;
import io.dongvelop.bookmanagementsystem.payload.request.UpdateBookRequest;
import io.dongvelop.bookmanagementsystem.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author 이동엽(Lee Dongyeop)
 * @date 2025. 02. 23
 * @description 도서 Service
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookService {

    private final AuthorService authorService;
    private final BookRepository bookRepository;

    /**
     * 도서 생성 메서드
     *
     * @param request 생성할 도서 정보
     */
    @Transactional
    public Book createBook(final CreateBookRequest request) throws APIException {
        log.debug("request[{}]", request);

        if (isAlreadyExistISBN(request.isbn())) {
            throw new APIException(HttpStatus.BAD_REQUEST, ErrorType.EXIST_DATA, "isbn[" + request.isbn() + "] is already exist");
        }

        final Author author = authorService.getAuthorDetail(request.authorId());
        log.debug("author[{}]", author);

        return bookRepository.save(request.toEntity(author));
    }

    /**
     * 도서 목록 조회 <br/>
     *
     * @param pageable 페이지 번호 및 크기 등의 페이징 정보
     */
    public Page<Book> getBookList(final Pageable pageable) {
        log.debug("pageable[{}]", pageable);

        return bookRepository.findAll(pageable);
    }

    /**
     * 도서 상세 조회 API
     *
     * @param bookId 조회할 도서 아이디
     */
    public Book getBookDetails(final Long bookId) throws APIException {
        log.debug("bookId[{}]", bookId);

        return bookRepository.findById(bookId).orElseThrow(
                () -> new APIException(HttpStatus.NOT_FOUND, ErrorType.NOT_EXIST_DATA, "bookId[" + bookId + "] not found"));
    }

    /**
     * 도서 수정 API
     *
     * @param bookId  수정할 도서 아이디
     * @param request 수정할 도서 정보
     */
    @Transactional
    public void updateBook(final Long bookId, final UpdateBookRequest request) throws APIException {
        log.debug("bookId[{}], request[{}]", bookId, request);

        final Book book = getBookDetails(bookId);
        log.debug("book[{}]", book);

        book.update(request);
    }

    /**
     * 도서 삭제 API
     *
     * @param bookId 삭제할 도서 아이디
     */
    @Transactional
    public void deleteBook(Long bookId) throws APIException {
        log.debug("bookId[{}]", bookId);

        getBookDetails(bookId);
        bookRepository.deleteById(bookId);
    }

    private boolean isAlreadyExistISBN(final String isbn) {
        return bookRepository.existsByIsbn(isbn);
    }
}
