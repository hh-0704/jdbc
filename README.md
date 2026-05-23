# 스프링 DB 1편 - 데이터 접근 핵심 원리

> 인프런 김영한님의 [스프링 DB 1편 - 데이터 접근 핵심 원리](https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81-db-1) 강의 클론 코딩 레포지토리입니다.

---

## 🛠 기술 스택

| 구분 | 기술 |
|------|------|
| Language | Java 25 |
| Framework | Spring Boot 4.0.6 |
| Database | H2 (In-Memory) |
| Library | Lombok, Spring JDBC |
| Build | Gradle |

---

## 📚 학습 내용

### 1. JDBC 이해

- **JDBC 등장 배경**: DB마다 커넥션 연결 방식, SQL 전달 방식, 응답 방식이 달라 발생하는 문제 해결
- **JDBC 표준 인터페이스**: `java.sql.Connection`, `java.sql.Statement`, `java.sql.ResultSet`을 표준화하여 DB 종류에 관계없이 동일한 코드 사용 가능
- **최신 데이터 접근 기술**
  - `SQL Mapper` (JdbcTemplate, MyBatis): SQL은 직접 작성하되, JDBC 반복 코드 제거 및 결과를 객체로 변환
  - `ORM` (JPA, Hibernate): SQL을 자동 생성하여 객체와 관계형 DB를 매핑

### 2. 커넥션 풀과 DataSource

- **커넥션 풀 필요성**: DB 커넥션 생성 시 TCP/IP 연결, 인증 등 복잡한 과정으로 인한 성능 문제 해결
- **커넥션 풀 동작**: 애플리케이션 시작 시 커넥션을 미리 생성해두고 재사용 (기본값 10개)
- **HikariCP**: 스프링 부트 기본 커넥션 풀, 성능과 안정성이 검증된 오픈소스
- **DataSource 추상화**: `javax.sql.DataSource` 인터페이스를 통해 커넥션 획득 방법을 추상화하여 `DriverManager` ↔ 커넥션 풀 변경 시 코드 수정 최소화

### 3. 트랜잭션 이해

- **트랜잭션 ACID 원칙**
  - `원자성(Atomicity)`: 트랜잭션 내 작업은 모두 성공하거나 모두 실패
  - `일관성(Consistency)`: 일관성 있는 DB 상태 유지
  - `격리성(Isolation)`: 동시 실행 트랜잭션 간 상호 영향 차단
  - `지속성(Durability)`: 커밋된 트랜잭션 결과는 항상 영구 기록
- **DB 세션**: 커넥션 연결 시 DB 내부에 세션이 생성되며, 트랜잭션은 세션 단위로 관리
- **자동/수동 커밋**: 수동 커밋 모드 설정이 곧 트랜잭션 시작을 의미하며, 이후 반드시 `commit` 또는 `rollback` 필요
- **DB 락**: 트랜잭션이 데이터를 수정하는 동안 다른 세션의 수정을 막아 정합성 보장

### 4. 스프링의 트랜잭션 문제 해결

- **기존 문제점**
  - 트랜잭션 적용을 위해 JDBC 구현 기술이 서비스 계층에 누수
  - 같은 커넥션 유지를 위한 파라미터 전달의 번거로움
  - 반복적인 try-catch-finally 코드

- **트랜잭션 매니저** (`PlatformTransactionManager`): 다양한 데이터 접근 기술을 추상화하여 서비스 계층이 특정 기술에 의존하지 않도록 처리
- **트랜잭션 동기화 매니저**: 쓰레드 로컬을 사용하여 같은 커넥션을 파라미터 없이 안전하게 동기화
- **트랜잭션 AOP** (`@Transactional`): 트랜잭션 로직을 AOP 프록시로 분리하여 서비스 계층을 순수한 비즈니스 로직만 남김

### 5. 자바 예외

- **예외 계층 구조**: `Throwable` → `Exception` / `Error`
- **체크 예외** (`Exception` 하위): 컴파일러가 체크, `throws` 선언 필수 → 예외 누락 방지가 장점이지만 불필요한 의존 관계 발생
- **언체크 예외** (`RuntimeException` 하위): 컴파일러 체크 없음, `throws` 생략 가능 → 복구 불가능한 예외에 적합
- **실무 권장**: 대부분 언체크 예외 사용, 비즈니스상 의도적으로 잡아야 하는 경우에만 체크 예외 사용

### 6. 스프링 예외 추상화

- `SQLException` 같은 특정 기술 종속 예외를 스프링의 예외 계층(`DataAccessException`)으로 변환
- 체크 예외 → 런타임 예외로 전환하여 서비스 계층의 순수성 유지
- DB 종류가 바뀌어도 서비스/컨트롤러 계층 코드 변경 불필요

---

## 📁 프로젝트 구조

```
src
├── main
│   ├── java/hello/jdbc
│   │   └── JdbcApplication.java
│   └── resources
└── test
    └── java/hello/jdbc
```

> 본 강의는 학습 개념 확인을 위한 테스트 코드 중심으로 진행됩니다.

---

## ▶️ 실행 방법

```bash
# 프로젝트 클론
git clone https://github.com/hh-0704/jdbc.git
cd jdbc

# 빌드 및 테스트
./gradlew test
```

> H2 인메모리 DB를 사용하므로 별도 DB 설치 없이 바로 실행 가능합니다.
