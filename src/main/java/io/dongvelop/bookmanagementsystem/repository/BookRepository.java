package io.dongvelop.bookmanagementsystem.repository;

import io.dongvelop.bookmanagementsystem.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author 이동엽(Lee Dongyeop)
 * @date 2025. 02. 23
 * @description 도서 JPA Repository
 */
public interface BookRepository extends JpaRepository<Book, Long> {

    /**
     * ISBN 존재 여부 확인
     *
     * @param isbn 국제 표준 도서번호
     * @return 존재 여부
     */
    boolean existsByIsbn(String isbn);

    /**
     * 제목을 포함한 도서 목록 조회
     *
     * @param title 제목
     * @return 도서 목록
     */
    Page<Book> findBooksByTitleContainsIgnoreCase(String title, Pageable pageable);
}
