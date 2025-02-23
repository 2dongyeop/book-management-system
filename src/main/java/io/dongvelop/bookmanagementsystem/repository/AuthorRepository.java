package io.dongvelop.bookmanagementsystem.repository;

import io.dongvelop.bookmanagementsystem.entity.Author;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author 이동엽(Lee Dongyeop)
 * @date 2025. 02. 23
 * @description 저자 JPA Repository
 */
public interface AuthorRepository extends JpaRepository<Author, Long> {

    /**
     * 이메일 존재 여부 확인
     *
     * @param email 이메일
     * @return 존재 여부
     */
    boolean existsByEmail(final String email);

    /**
     * 페이징을 적용한 저자 목록 조회 <br/>
     * FETCH JOIN 과 페이징이 함께 동작할 수 없기에, `EntityGraph`를 활용하여 저자 조회시에 도서 목록을 함께 조회
     */
    @EntityGraph(attributePaths = "books")
    @Query("SELECT a FROM Author a")
    List<Author> findAuthorsWithPaging(Pageable pageable);
}
