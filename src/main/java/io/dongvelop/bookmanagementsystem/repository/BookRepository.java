package io.dongvelop.bookmanagementsystem.repository;

import io.dongvelop.bookmanagementsystem.entity.Book;
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
}
