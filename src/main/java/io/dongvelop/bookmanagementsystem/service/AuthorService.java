package io.dongvelop.bookmanagementsystem.service;

import io.dongvelop.bookmanagementsystem.entity.Author;
import io.dongvelop.bookmanagementsystem.entity.Book;
import io.dongvelop.bookmanagementsystem.exception.APIException;
import io.dongvelop.bookmanagementsystem.exception.ErrorType;
import io.dongvelop.bookmanagementsystem.payload.request.CreateAuthorRequest;
import io.dongvelop.bookmanagementsystem.payload.request.UpdateAuthorRequest;
import io.dongvelop.bookmanagementsystem.repository.AuthorRepository;
import io.dongvelop.bookmanagementsystem.repository.BookRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author 이동엽(Lee Dongyeop)
 * @date 2025. 02. 23
 * @description 저자 Service
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthorService {

    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;

    /**
     * 저자 생성 메서드
     */
    @Transactional
    public Author createAuthor(final CreateAuthorRequest request) throws APIException {
        log.debug("request[{}]", request);

        if (isAlreadyExistEmail(request.email())) {
            throw new APIException(HttpStatus.BAD_REQUEST, ErrorType.EXIST_DATA, "email[" + request.email() + "] is already exist");
        }

        return authorRepository.save(request.toEntity());
    }

    /**
     * 저자 목록 조회 메서드
     *
     * @return 저자 목록
     */
    public List<Author> getAuthorList() {
        // 저자 목록이 많아졌을 때 한번에 조회할 경우, 메모리를 많이 차지할 수 있음.
        return authorRepository.findAuthors();
    }

    /**
     * 저자 상세 조회 메서드
     */
    public Author getAuthorDetail(final Long authorId) throws APIException {
        log.debug("authorId[{}]", authorId);

        return authorRepository.findById(authorId)
                .orElseThrow(() -> new APIException(HttpStatus.NOT_FOUND, ErrorType.NOT_EXIST_DATA, "authorId[" + authorId + "] not found"));
    }

    /**
     * 저자 수정 메서드
     *
     * @param authorId 수정할 저자 아이디
     * @param request  수정할 정보
     */
    @Transactional
    public void updateAuthor(final Long authorId, @Valid final UpdateAuthorRequest request) throws APIException {
        log.debug("authorId[{}], request[{}]", authorId, request);

        final Author author = getAuthorDetail(authorId);
        author.update(request);
    }

    /**
     * 저자 삭제 메서드 <br/>
     * 저자를 삭제할 경우, 저자가 등록한 도서까지 모두 삭제한다.
     *
     * @param authorId 저자 아이디
     */
    @Transactional
    public void deleteAuthor(final Long authorId) throws APIException {
        log.debug("authorId[{}]", authorId);

        final Author author = getAuthorDetail(authorId);

        final List<Book> books = author.getBooks();
        bookRepository.deleteAllInBatch(books);

        authorRepository.deleteById(authorId);
    }

    private boolean isAlreadyExistEmail(final String email) {
        return authorRepository.existsByEmail(email);
    }
}
