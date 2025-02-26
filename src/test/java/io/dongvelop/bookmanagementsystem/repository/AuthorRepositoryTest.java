package io.dongvelop.bookmanagementsystem.repository;

import autoparams.AutoSource;
import io.dongvelop.bookmanagementsystem.entity.Author;
import io.dongvelop.bookmanagementsystem.entity.Book;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.List;

@DataJpaTest
class AuthorRepositoryTest {

    @Autowired
    private AuthorRepository authorRepository;
    @Autowired
    private BookRepository bookRepository;

    @Nested
    @DisplayName("이메일 존재 여부 검증 테스트")
    class ExistByEmail {

        @AutoSource
        @ParameterizedTest
        @DisplayName("이미 존재하는 이메일일 경우, 반환 값이 참으로 나온다.")
        void resultTrue(Author author) {

            // given
            authorRepository.save(author);

            // when
            boolean result = authorRepository.existsByEmail(author.getEmail());

            // then
            Assertions.assertThat(result).isTrue();
        }

        @Test
        @DisplayName("존재하지 않는 이메일일 경우, 반환 값이 거짓으로 나온다.")
        void resultFalse() {

            // given

            // when
            boolean result = authorRepository.existsByEmail("not_exist@email.com");

            // then
            Assertions.assertThat(result).isFalse();
        }
    }

    @Nested
    @DisplayName("저자 목록 조회 테스트")
    class FindAuthorsWithPaging {

        @AutoSource
        @ParameterizedTest
        @DisplayName("저자 목록 조회 시, 연관된 도서 목록을 함께 불러온다.")
        void success(Author author) throws Exception {

            // given
            authorRepository.save(author);

            final Book book1 = new Book("title1", "description", "123-456789-0", LocalDate.now(), author);
            final Book book2 = new Book("title2", "description", "124-456789-0", LocalDate.now(), author);
            bookRepository.saveAll(List.of(book1, book2));

            // when
            final List<Author> authors = authorRepository.findAuthors();

            // then
            SoftAssertions.assertSoftly(softly -> {
                softly.assertThat(authors).hasSize(1);
                softly.assertThat(authors.get(0).getName()).isEqualTo(author.getName());
                softly.assertThat(authors.get(0).getBooks()).hasSize(2);
            });
        }
    }
}