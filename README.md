# Kefawatch

TBL324 kapsamında **Java 17** ile geliştirilmiş dizi/film **katalog**, **izleme listesi** ve **izleme ilerlemesi** uygulaması. Çekirdek iş mantığı **Spring Boot 3** REST API üzerinde; kalıcı veri **PostgreSQL + JDBC**, trend/popülerlik tarafı **Redis** ile tutulur. Dağıtık bileşenler **Docker Compose** ile ayağa kalkar: **Spring Cloud Gateway** (dış giriş), çekirdek **API**, **analytics** mikroservisi, **JavaFX** masaüstü ve **Android (Java)** istemci.

## Modüller

| Klasör | Açıklama |
|--------|----------|
| [`backend/`](backend/) | Ana REST API: Flyway, JWT, JDBC repository’ler, Redis, OpenAPI (springdoc), Testcontainers ile testler |
| [`api-gateway/`](api-gateway/) | Spring Cloud Gateway: `/api/v1/**` → çekirdek API, `/api/v1/analytics/**` → analytics servisi |
| [`analytics-service/`](analytics-service/) | Ayrı Spring Boot servisi (analitik uçları) |
| [`desktop/`](desktop/) | **JavaFX** masaüstü istemci; HTTP API tabanı `http://localhost:8080/api/v1` |
| [`android/`](android/) | Retrofit + Material; debug’da `API_BASE_URL` (`10.0.2.2:8080`) |
| [`docs/`](docs/) | [`ARCHITECTURE.md`](docs/ARCHITECTURE.md) (Mermaid), [`PERFORMANCE.md`](docs/PERFORMANCE.md), [`docs/k6/smoke.js`](docs/k6/smoke.js) |

Grup / ders teslimi için metin özetine [`readme.txt`](readme.txt) dosyasına bakabilirsiniz.

## Gereksinimler

- **Java 17**, **Maven**
- **Docker** + Docker Compose (tam yığın ve CI’de Testcontainers için)

## Hızlı başlangıç (Docker)

```bash
docker compose up --build
```

- **Giriş noktası (Gateway):** `http://localhost:8080` — REST öneki: `/api/v1/...`, analitik: `/api/v1/analytics/...`
- **PostgreSQL:** `localhost:5432` (db: `kefawatch`, kullanıcı/şifre: `kefa` / `kefa`)
- **Redis:** `localhost:6379`

`api` ve `analytics` konteynerleri compose içinde yalnızca ağ üzerinden erişilir; dışarıdan tek yayınlanan HTTP portu gateway’dir.

## Swagger / OpenAPI

Springdoc yolları backend’de tanımlıdır (ör. Swagger UI: `/api/swagger-ui.html`, OpenAPI: `/api/v3/api-docs`). Gateway rotaları şu an **`/api/v1/**`** ile sınırlı olduğundan, **yalnızca `docker compose` + gateway** kullanırken bu arayüz otomatik olarak 8080 üzerinden açılmaz.

- **Swagger’ı görmek için:** `cd backend && mvn spring-boot:run` (önce PostgreSQL + Redis ayakta olmalı) veya compose’da `api` servisine geçici bir `ports` eşlemesi ekleyin / gateway kurallarını genişletin.

## Yerel geliştirme (yalnız çekirdek API)

```bash
cd backend
mvn spring-boot:run
```

Önkoşul: PostgreSQL ve Redis (ör. `docker compose up postgres redis`). Varsayılan port **8080**; gateway ile çakışmaması için biri farklı portta çalıştırılabilir (`SERVER_PORT=8081`).

## Masaüstü (JavaFX)

```bash
cd desktop
mvn javafx:run
```

İstemci varsayılan olarak `http://localhost:8080/api/v1` adresine gider; bu, yerelde çalışan gateway veya doğrudan backend ile uyumludur.

## Android

Android Studio ile [`android/`](android/) modülünü açın. Emülatörden makineye `localhost` için taban adres: `http://10.0.2.2:8080` (debug `build.gradle` içindeki `API_BASE_URL`). Release yapılandırması ayrı bir taban URL kullanır; üretim adresinize göre güncelleyin.

## Performans testleri

Bkz. [`docs/PERFORMANCE.md`](docs/PERFORMANCE.md). Örnek duman testi:

```bash
k6 run docs/k6/smoke.js
```

## CI

[`.github/workflows/ci.yml`](.github/workflows/ci.yml) — `backend` modülünde `mvn test` (Testcontainers için Docker destekli bir runner kullanılır).
