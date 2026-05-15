# Performans testi (k6)

## Senaryo

[`docs/k6/smoke.js`](k6/smoke.js) betiği şu uçları yük altında çağırır:

- `GET /api/v1/titles`
- `GET /api/v1/titles/1`
- `POST /api/v1/titles/1/views`
- `GET /api/v1/catalog/trending`

## Çalıştırma

Önkoşul: API ayakta (`docker compose up` veya yerel `mvn spring-boot:run` + Postgres + Redis).

```bash
k6 run docs/k6/smoke.js
```

Özel taban URL:

```bash
BASE_URL=http://localhost:8080 k6 run docs/k6/smoke.js
```

## Eşikler (betik içi)

| Eşik | Hedef |
|------|--------|
| `http_req_failed` | Under 5% failure rate |
| `http_req_duration` p(95) | Under 800 ms |

## Örnek çıktı kaydı

CI veya yerel koşudan sonra `k6` özet tablosunun ekran görüntüsünü rapora ekleyin (throughput, p95, başarısız istek oranı).

JMeter kullanmak isterseniz benzer thread grubu ile aynı uç noktaları HTTP Request olarak ekleyebilirsiniz; değerlendirme için sonuç özeti ve grafik yeterlidir.
