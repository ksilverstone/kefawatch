# Kefawatch

TBL324 kapsamında **Java** tabanlı dizi/film katalog ve izleme ilerlemesi MVP’si: **Spring Boot** REST API (PostgreSQL + JDBC, Redis), **Native Android (Java)** istemci.

## Modüller

- [`backend/`](backend/) — REST API, Flyway, JWT, OpenAPI (springdoc), Testcontainers ile entegrasyon testleri
- [`android/`](android/) — Retrofit + Material bileşenleri; emülatörden `http://10.0.2.2:8080` (debug `API_BASE_URL`)
- [`docs/`](docs/) — Mimari (Mermaid) ve performans testi notları + [`docs/k6/smoke.js`](docs/k6/smoke.js)

## Hızlı başlangıç (Docker)

```bash
docker compose up --build
```

API: `http://localhost:8080` — Swagger UI: `http://localhost:8080/api/swagger-ui.html`

## Yerel geliştirme (backend)

Java **17** ve Maven gerekir.

```bash
cd backend
mvn spring-boot:run
```

Önkoşul: PostgreSQL ve Redis (ör. `docker compose up postgres redis` veya tam stack).

## Android

Android Studio ile `android/` klasörünü açın; Gradle wrapper yoksa IDE “Gradle Wrapper” oluşturmayı önerebilir. Emülatörde backend `localhost:8080` ise taban URL `http://10.0.2.2:8080` (debug `buildConfigField` ile tanımlı).

## Performans

Bkz. [`docs/PERFORMANCE.md`](docs/PERFORMANCE.md) ve `k6 run docs/k6/smoke.js`.

## CI

GitHub Actions: `.github/workflows/ci.yml` — `mvn test` (Testcontainers için Docker gerekir).
