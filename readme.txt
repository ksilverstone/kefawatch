
KOCAELİ ÜNİVERSİTESİ — TEKNOLOJİ FAKÜLTESİ — BİLİŞİM SİSTEMLERİ MÜHENDİSLİĞİ
TBL324 — İLERİ JAVA UYGULAMALARI

GRUP BİLGİSİ
------------
Proje adı (kod deposu) : Kefawatch
Grup üye sayısı       : 2

EKİP ÜYELERİ
------------
1) 231307050 — Faruk Öztürk
2) 231307084 — Kerem Emre Gümüştaş

PROJE KONUSU
------------
Kefawatch; dizi ve film kataloğu ile kullanıcı izleme ilerlemesi (watch progress)
yönetimini hedefleyen Java tabanlı bir uygulamadır. Sistem; Spring Boot ile
REST API (iş mantığı, JWT kimlik doğrulama), PostgreSQL üzerinde JDBC ile
kalıcılık, Redis ile NoSQL tarafı, isteğe bağlı mikroservis bileşenleri
(analytics servisi), API Gateway üzerinden trafik yönlendirmesi, masaüstü
(JavaFX) ve Android (Java) istemcileri ile uçtan uca bir mimari sunar.
Docker Compose ile temel altyapı ve servisler ayağa kaldırılabilir;
performans testleri için k6 senaryoları ve teknik dokümantasyon (docs/)
bulunmaktadır.

Faruk Öztürk (231307050)
  • Backend: domain servisleri, REST controller’lar, güvenlik (JWT), Flyway
  • Kalıcılık: JDBC repository implementasyonları, PostgreSQL şeması
  • Masaüstü: JavaFX (desktop/) custom arayüz ve API istemcisi
  • Birim/entegrasyon testleri (backend odaklı)

Kerem Emre Gümüştaş (231307084)
  • Android istemci (Retrofit, ekranlar, Material bileşenleri)
  • Mikroservis: analytics-service ve API Gateway entegrasyonu
  • DevOps: Docker imajları, docker-compose düzeni, CI (GitHub Actions)
  • Performans: k6 senaryoları, docs/PERFORMANCE.md ve teknik raporlama

Ortak / her iki üye
  • Kod incelemesi, SOLID ve hata yönetimi standartlarının korunması
  • GitHub commit dengesi ve dokümantasyonun güncelliği (NOT-2 ile uyum)

----------------
Kod deposu: https://github.com/ksilverstone/kefawatch

Dosya sonu — readme.txt
