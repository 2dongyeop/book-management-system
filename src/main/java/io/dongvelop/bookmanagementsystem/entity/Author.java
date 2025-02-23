package io.dongvelop.bookmanagementsystem.entity;

import io.dongvelop.bookmanagementsystem.excepiton.APIException;
import io.dongvelop.bookmanagementsystem.excepiton.ErrorType;
import io.dongvelop.bookmanagementsystem.payload.request.UpdateAuthorRequest;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 이동엽(Lee Dongyeop)
 * @date 2025. 02. 23
 * @description 저자 Entity Model.
 */
@Entity
@Getter
@Table(name = "author")
@ToString(exclude = {"books"})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Author extends BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 7107027513806946728L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * [필수] 이름
     */
    @Column(name = "name", nullable = false)
    private String name;

    /**
     * [필수, 고유] 이메일
     */
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    /**
     * 도서 목록
     */
    @OneToMany(mappedBy = "author")
    private final List<Book> books = new ArrayList<>();

    public Author(final String name, final String email) {
        this.name = name;
        this.email = email;
    }

    public void update(final UpdateAuthorRequest request) throws APIException {
        if (!StringUtils.hasText(request.name())) {
            throw new APIException(HttpStatus.BAD_REQUEST, ErrorType.REQUIRED_INPUT, "name[" + request.name() + "] is required.");
        }
        this.name = request.name();
    }
}
