package io.dongvelop.bookmanagementsystem.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author 이동엽(Lee Dongyeop)
 * @date 2025. 02. 23
 * @description Swagger 설정 클래스
 */
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .components(new Components())
                .info(new Info()
                        .title("도서관리 시스템 API Docs")
                        .description("코드 기술과제입니다. 각 에러 케이스 별 상태코드로 세분화하여 응답을 제공합니다.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Lee Dongyeop")
                                .url("https://github.com/2dongyeop/book-management-system")
                                .email("ldy_1204@naver.com")));
    }
}

