import http from "k6/http";
import { check, sleep } from "k6";

export const options = {
  vus: 20,
  duration: "30s",
  thresholds: {
    http_req_failed: ["rate<0.05"],
    http_req_duration: ["p(95)<800"],
  },
};

const BASE = __ENV.BASE_URL || "http://localhost:8080";

export default function () {
  const titles = http.get(`${BASE}/api/v1/titles?page=0&size=20`);
  check(titles, { "titles 200": (r) => r.status === 200 });

  const detail = http.get(`${BASE}/api/v1/titles/1`);
  check(detail, { "detail 200": (r) => r.status === 200 });

  const view = http.post(`${BASE}/api/v1/titles/1/views`);
  check(view, { "view 200": (r) => r.status === 200 });

  const trending = http.get(`${BASE}/api/v1/catalog/trending?limit=5`);
  check(trending, { "trending 200": (r) => r.status === 200 });

  sleep(0.3);
}
