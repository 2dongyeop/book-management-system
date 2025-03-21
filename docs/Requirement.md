# 도서 관리 시스템 CRUD 과제

## 과제 개요

이 과제의 목적은 주니어 백엔드 개발자로서 기본적인 CRUD 기능 구현, 데이터 모델링, RESTful API 설계, 그리고 간단한 비즈니스 로직(유효성 검사 등)을 평가하는 데 있습니다.

본 과제는 도서와 저자 정보를 관리하는 간단한 시스템을 구현하는 것입니다.

<br/>

## 요구사항

### **1. 기술 스택 및 구현 방식**

- 언어 및 프레임워크:
    - Java 17 이상 혹은 Kotlin 1.9 이상
    - Spring Boot 3 이상
    - Gradle 사용
- 데이터베이스:
    - H2 데이터베이스를 사용하여 로컬에서 실행 가능하도록 개발
- ORM 사용 여부:
    - 자유롭게 선택 (예: Spring Data JPA 등)
- API 문서:
    - Swagger 문서 작성은 필수입니다. Swagger 또는 OpenAPI 스펙을 활용하여 API 문서를 작성해 주세요.
- 배포:
    - 배포는 고려하지 않습니다. 로컬에서 실행이 가능해야합니다.

<br/>

### **2. 데이터 모델**

- 저자 (Author)
    - id: 고유 식별자 (자동 생성)
    - name: 저자 이름 (필수)
    - email: 이메일 (필수, 고유)

- 도서 (Book)
    - id: 고유 식별자 (자동 생성)
    - title: 도서 제목 (필수)
    - description: 도서 설명 (선택)
    - isbn: 국제 표준 도서번호 (필수, 고유)
    - publication_date: 출판일 (선택)
    - author_id: 해당 도서의 저자 (필수, Author와의 관계)
- 참고:
    - 관계: 한 명의 저자는 여러 도서를 가질 수 있으며, 각 도서는 하나의 저자에 속합니다.

<br/>

### **3. API 엔드포인트**

각 엔티티(Author, Book)에 대해 기본 CRUD 기능을 구현합니다.

> **저자(Author) API**

- 생성:
    - POST /authors
    - 요청 본문 예시:
      ```json
        {
        "name": "홍길동",
        "email": "hong@example.com"
        }
      ```
- 목록 조회:
    - GET /authors
    - 모든 저자 목록을 반환
- 상세 조회:
    - GET /authors/{id}
    - 특정 저자의 상세 정보를 반환
- 수정:
    - PUT /authors/{id} 또는 PATCH /authors/{id}
    - 요청 본문을 통해 저자 정보 수정
- 삭제:
    - DELETE /authors/{id}
    - 삭제 전, 해당 저자와 연관된 도서 처리(삭제 불가 혹은 연관 도서 삭제 여부에 대한 정책 명시 필요)

<br/>

> **도서(Book) API**

- 생성:
    - POST /books
    - 요청 본문 예시:
      ```json
      {
        "title": "예제 도서",
        "description": "도서에 대한 설명",
        "isbn": "1234567890123",
        "publication_date": "2025-01-01",
        "author_id": 1
      }
      ```
- 목록 조회:
    - GET /books
    - 모든 도서 목록을 반환
    - (선택) Pagination 및 간단한 필터링(예: 출판일 기준) 기능 추가 가능
- 상세 조회:
    - GET /books/{id}
    - 특정 도서의 상세 정보를 반환
- 수정:
    - PUT /books/{id} 또는 PATCH /books/{id}
    - 요청 본문을 통해 도서 정보 수정
- 삭제:
    - DELETE /books/{id}
    - 도서 삭제

<br/>

### **4. 유효성 및 비즈니스 로직**

> **고유성 체크:**

- isbn은 도서마다 유일해야 합니다.
- ISBN-10 규칙을 사용
    - 10자리 숫자로 구성되었습니다. 이는 다음과 같은 형식을 따릅니다:
    - 국가, 언어 식별 번호: 첫 번째 세 자리
        - 100~900 사이의 숫자 허용
    - 출판사 식별 번호: 다음 4~7자리
    - 책 식별 번호: 다음 1~3자리
    - 체크 디지트: 마지막 자리. 0을 사용.
- email은 저자마다 유일해야 합니다.

<br/>

> **필수 필드:**

- 필수 항목(예: 저자 이름, 이메일, 도서 제목, ISBN 등)은 요청 시 반드시 포함되어야 하며, 누락 시 적절한 에러 메시지를 반환해야 합니다.

<br/>

> **에러 처리:**

- 잘못된 입력, 존재하지 않는 리소스 접근 등 다양한 상황에 대해 적절한 HTTP 상태 코드(예: 400, 404, 500 등)를 반환하세요.

<br/>

### 5. 제출 및 평가 기준

- API 설계:
    - RESTful한 엔드포인트 구성 및 HTTP 상태 코드 활용
- 기능 완성도:
    - CRUD 기능이 정상적으로 동작하며, 데이터 모델 및 관계가 적절하게 구현되었는지
- 에러 처리:
    - 유효성 검사 및 예외 상황 처리
- API 문서 (Swagger):
    - Swagger를 활용한 API 문서의 완성도와 정확성
    - Swagger UI를 통해 API 확인이 가능하도록 구현
- README 파일:
    - 프로젝트 실행 방법, API 사용법, Swagger 문서 접근 방법, 기타 주의 사항 등을 명시

<br/>

## 추가 사항 (선택)

- 테스트 케이스:
    - 단위 테스트 또는 통합 테스트 작성 (가능한 경우)
- 기타:
    - 로컬 실행 환경 구성이 명확해야 합니다.
