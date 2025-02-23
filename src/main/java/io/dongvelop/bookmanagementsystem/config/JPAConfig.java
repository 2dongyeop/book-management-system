package io.dongvelop.bookmanagementsystem.config;

import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * @author 이동엽(Lee Dongyeop)
 * @date 2025. 02. 23
 * @description JPA 설정 클래스 <br/>
 * - `@WebMvcTest`를 이용해 컨트롤러 단위 테스트 시에, 필요한 빈들을 불러오기 위함.
 */
@EnableJpaAuditing
@EnableJpaRepositories(basePackages = "io.dongvelop.bookmanagementsystem.repository")
public class JPAConfig {
}
