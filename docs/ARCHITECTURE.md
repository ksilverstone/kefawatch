# Kefawatch — Mimari (TBL324)

Kefawatch; katalog, izleme listesi ve izleme ilerlemesi sunan bir **Spring Boot 3 (Java 17)** REST API ve **Native Android (Java)** istemciden oluşur. Kalıcı veri **PostgreSQL + JDBC** ile, popülerlik / trend verisi **Redis (sorted set)** ile tutulur.

## Bileşenler

```mermaid
flowchart LR
  subgraph android [Android_Java]
    MainActivity
    RetrofitClient
  end
  subgraph api [SpringBoot]
    Controllers[REST_controllers]
    Services[Domain_services]
    Jdbc[JDBC_repositories]
    Redis[Redis_trending]
  end
  MainActivity --> RetrofitClient
  RetrofitClient -->|HTTPS_JSON| Controllers
  Controllers --> Services
  Services --> Jdbc
  Services --> Redis
```

## Veri modeli (özet ER)

```mermaid
erDiagram
  USERS ||--o{ WATCHLIST : saves
  USERS ||--o{ WATCH_PROGRESS : tracks
  TITLES ||--o{ EPISODES : contains
  TITLES ||--o{ WATCHLIST : referenced_by
  TITLES ||--o{ WATCH_PROGRESS : referenced_by
  USERS {
    bigint id PK
    varchar username
    varchar password_hash
  }
  TITLES {
    bigint id PK
    varchar type
    varchar name
    text description
  }
  EPISODES {
    bigint id PK
    bigint title_id FK
    int season_number
    int episode_number
  }
  WATCHLIST {
    bigint user_id PK_FK
    bigint title_id PK_FK
  }
  WATCH_PROGRESS {
    bigint id PK
    bigint user_id FK
    bigint title_id FK
    bigint episode_id FK
    int position_seconds
    boolean completed
  }
```

## API yüzeyi (özet)

| Metot | Yol | Açıklama |
|-------|-----|----------|
| POST | `/api/v1/auth/register` | Kullanıcı + JWT |
| POST | `/api/v1/auth/login` | JWT |
| GET | `/api/v1/titles` | Sayfalı katalog |
| GET | `/api/v1/titles/{id}` | Detay + bölümler |
| POST | `/api/v1/titles/{id}/views` | Trend sayacı (Redis) |
| GET | `/api/v1/catalog/trending` | Trend sıralaması |
| GET/POST/DELETE | `/api/v1/watchlist` | Kimlik doğrulamalı |
| GET/PUT | `/api/v1/progress` | İzleme ilerlemesi |

## Tasarım notları

- **Generic:** `ApiResponse<T>`, `PageResult<T>` ortak JSON zarfı ve sayfalama.
- **SOLID:** Controller → servis → port arayüzleri (`TitleRepository`, `TrendingTitleStore`, …); `TitleMetadataSource` strateji soyutlaması.
- **Hata yönetimi:** `@ControllerAdvice` ile `ApiResponse` ve uygun **HTTP 4xx/5xx** kodları.
- **Swagger / OpenAPI:** `springdoc-openapi` — `/api/swagger-ui.html` ve `/api/v3/api-docs`.
