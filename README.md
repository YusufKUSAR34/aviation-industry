# Aviation Industry System

## Proje Hakkında
Aviation Industry System, havayolu ve ulaşım rotalarını yönetmek için geliştirilmiş kapsamlı bir API sistemidir. Bu sistem, lokasyonlar arasındaki ulaşım seçeneklerini yönetmeyi, optimum rotalar bulmayı ve çeşitli transfer seçenekleriyle seyahat planlaması yapmayı sağlar.

## Özellikler
- 🔐 JWT tabanlı güvenli kimlik doğrulama
- 👥 Rol tabanlı yetkilendirme (USER ve ADMIN rolleri)
- 📍 Lokasyon yönetimi
- 🚆 Ulaşım seçenekleri yönetimi
- 🛫 Rota optimizasyonu ve arama
- 🔄 Aktarmalı uçuş seçenekleri
- 📊 Swagger UI ile API dökümantasyonu
- 🚀 Redis cache desteği
- 🗄️ Soft delete mekanizması
- 📝 Detaylı loglama sistemi (AOP Log,Slf4j)

## Teknolojiler

### Backend
- Java 17
- Spring Boot 3.x
- Spring Security
- Spring Data JPA
- Spring Cache
- JWT (JSON Web Token)
- MapStruct
- Lombok
- Docker

### Veritabanı & Cache
- PostgreSQL
- Redis

### API Dökümantasyonu
- OpenAPI 3.0
- Swagger UI

### Güvenlik
- Spring Security
- JWT Authentication
- Role-Based Access Control (RBAC)

### Build & Dependency Management
- Gradle

## Kurulum

### Gereksinimler
- Java 17 veya üzeri
- PostgreSQL 12 veya üzeri
- Redis 6.x veya üzeri
- Gradle 7.x veya üzeri

### Uygulama Kurulumu
### 1. Projeyi Klonlayın
```bash
git clone https://github.com/YusufKUSAR34/aviation-industry.git
cd aviation.industry
```

### 2. Docker Compose ile Çalıştırma
```bash
# Tüm servisleri başlatın
docker-compose up -d

# Logları görüntüleyin
docker-compose logs -f

# Belirli bir servisin loglarını görüntüleyin
docker-compose logs -f app
docker-compose logs -f postgres
docker-compose logs -f redis
```

### 3. Kontrol Edin
```bash
# Tüm containerların çalıştığını kontrol edin
docker-compose ps

# PostgreSQL bağlantısını kontrol edin
docker exec -it aviation-postgres psql -U postgres -d aviation

# Redis bağlantısını kontrol edin
docker exec -it aviation-redis redis-cli ping
```

### 4. Swagger UI'a Erişin

### 4. Uygulama Kontrolü

1. Swagger UI'a erişin:
```
http://localhost:8080/swagger-ui.html
```
2 .Redis UI'a erişin:
```
http://localhost:8081/
```

3.Sağlık kontrolü yapın:
```bash
curl http://localhost:8080/actuator/health
```

## API Kullanımı(Dokümantasyonu)

### Kimlik Doğrulama (Authentication) Endpointleri

#### Kayıt Olma
```http
POST /api/v1/auth/register
Content-Type: application/json

{
    "username": "testuser",
    "email": "test@example.com",
    "password": "password123"
}
```

#### Giriş Yapma
```http
POST /api/v1/auth/login
Content-Type: application/json

{
    "username": "testuser",
    "password": "password123"
}

Yanıt:
{
    "token": "jwt_token_değeri"
}
```
### Lokasyon Endpointleri

#### Lokasyon Oluşturma
```http
POST /api/v1/locations
Authorization: Bearer jwt_token_değeri
Content-Type: application/json

{
    "name": "İstanbul Havalimanı",
    "type": "AIRPORT",
    "latitude": 41.275278,
    "longitude": 28.751944,
    "city": "İstanbul",
    "country": "Türkiye"
}
```

#### Tüm Lokasyonları Getirme
```http
GET /api/v1/locations
Authorization: Bearer jwt_token_değeri

Yanıt:
[
    {
        "id": 1,
        "name": "İstanbul Havalimanı",
        "type": "AIRPORT",
        "latitude": 41.275278,
        "longitude": 28.751944,
        "city": "İstanbul",
        "country": "Türkiye"
    }
]
```

#### ID ile Lokasyon Getirme
```http
GET /api/v1/locations/{id}
Authorization: Bearer jwt_token_değeri

Yanıt:
{
    "id": 1,
    "name": "İstanbul Havalimanı",
    "type": "AIRPORT",
    "latitude": 41.275278,
    "longitude": 28.751944,
    "city": "İstanbul",
    "country": "Türkiye"
}
```

#### Lokasyon Güncelleme
```http
PUT /api/v1/locations/{id}
Authorization: Bearer jwt_token_değeri
Content-Type: application/json

{
    "name": "Güncellenmiş İstanbul Havalimanı",
    "type": "AIRPORT",
    "latitude": 41.275278,
    "longitude": 28.751944,
    "city": "İstanbul",
    "country": "Türkiye"
}
```

#### Lokasyon Silme
```http
DELETE /api/v1/locations/{id}
Authorization: Bearer jwt_token_değeri
```

### Ulaşım Endpointleri

#### Ulaşım Oluşturma
```http
POST /api/v1/transportations
Authorization: Bearer jwt_token_değeri
Content-Type: application/json

{
    "type": "FLIGHT",
    "originLocationId": 1,
    "destinationLocationId": 2,
    "price": 500.00,
    "duration": 120
}
```

#### Tüm Ulaşımları Getirme
```http
GET /api/v1/transportations
Authorization: Bearer jwt_token_değeri

Yanıt:
[
    {
        "id": 1,
        "type": "FLIGHT",
        "originLocation": {
            "id": 1,
            "name": "İstanbul Havalimanı",
            "type": "AIRPORT"
        },
        "destinationLocation": {
            "id": 2,
            "name": "Ankara Havalimanı",
            "type": "AIRPORT"
        },
        "price": 500.00,
        "duration": 120
    }
]
```

#### ID ile Ulaşım Getirme
```http
GET /api/v1/transportations/{id}
Authorization: Bearer jwt_token_değeri

Yanıt:
{
    "id": 1,
    "type": "FLIGHT",
    "originLocation": {
        "id": 1,
        "name": "İstanbul Havalimanı",
        "type": "AIRPORT"
    },
    "destinationLocation": {
        "id": 2,
        "name": "Ankara Havalimanı",
        "type": "AIRPORT"
    },
    "price": 500.00,
    "duration": 120
}
```

#### Ulaşım Güncelleme
```http
PUT /api/v1/transportations/{id}
Authorization: Bearer jwt_token_değeri
Content-Type: application/json

{
    "type": "FLIGHT",
    "originLocationId": 1,
    "destinationLocationId": 2,
    "price": 550.00,
    "duration": 120
}
```

#### Ulaşım Silme
```http
DELETE /api/v1/transportations/{id}
Authorization: Bearer jwt_token_değeri
```

### Rota Endpointleri

#### Rota Arama
```http
POST /api/v1/routes/search
Authorization: Bearer jwt_token_değeri
Content-Type: application/json

{
    "originLocationId": 1,
    "destinationLocationId": 2
}

Yanıt:
[
    {
        "transportations": [
            {
                "id": 1,
                "type": "FLIGHT",
                "originLocation": {
                    "id": 1,
                    "name": "İstanbul Şehir Merkezi",
                    "type": "CITY_POINT"
                },
                "destinationLocation": {
                    "id": 3,
                    "name": "İstanbul Havalimanı",
                    "type": "AIRPORT"
                },
                "price": 10.00,
                "duration": 45
            },
            {
                "id": 2,
                "type": "FLIGHT",
                "originLocation": {
                    "id": 3,
                    "name": "İstanbul Havalimanı",
                    "type": "AIRPORT"
                },
                "destinationLocation": {
                    "id": 4,
                    "name": "Ankara Havalimanı",
                    "type": "AIRPORT"
                },
                "price": 500.00,
                "duration": 120
            },
            {
                "id": 3,
                "type": "OTHER",
                "originLocation": {
                    "id": 4,
                    "name": "Ankara Havalimanı",
                    "type": "AIRPORT"
                },
                "destinationLocation": {
                    "id": 2,
                    "name": "Ankara Şehir Merkezi",
                    "type": "CITY_POINT"
                },
                "price": 8.00,
                "duration": 30
            }
        ],
        "totalPrice": 518.00,
        "totalDuration": 195
    }
]
```
## Güvenlik Özellikleri

### JWT Yapılandırması
- Token Süresi: 24 saat
- Token İçeriği: Kullanıcı adı ve roller
- Güvenlik Başlığı: Bearer token

### Rol Tabanlı Erişim
- ROLE_USER: Temel işlemler
- ROLE_ADMIN: Yönetimsel işlemler

## Cache Stratejisi
- Redis kullanılarak implemente edilmiştir
- Key Prefix: "aviation_"
- Null değerler cache'lenmez

## Hata Yönetimi
- Merkezi hata yönetimi
- Özelleştirilmiş exception sınıfları
- Detaylı hata mesajları

## Veritabanı Şeması
```
users
- id (PK)
- username (unique)
- email (unique)
- password
- role
- created_at
- updated_at
- version
- deleted

locations
- id (PK)
- name
- type (AIRPORT, CITY_POINT)
- latitude
- longitude
- city
- country
- created_at
- updated_at
- version
- deleted

transportations
- id (PK)
- origin_id (FK)
- destination_id (FK)
- transportationType
- duration
- price
- created_at
- updated_at
- version
- deleted
```






## Tasarım Desenleri (Design Patterns)

### Rota Doğrulama Sistemi

Rota doğrulama sistemi, temiz, bakımı kolay ve genişletilebilir kod sağlamak için birden fazla tasarım desenini kullanmaktadır:

#### 1. Kompozit (Composite) Deseni
- **Amaç:** Tekil doğrulayıcıları ve doğrulayıcı gruplarını tek tip olarak ele almayı sağlar
- **Uygulama:** `CompositeRouteValidator` birden fazla `RouteValidator` implementasyonunu içeren bir konteyner görevi görür
- **Faydaları:**
    - Doğrulayıcılar gruplanabilir ve iç içe kullanılabilir
    - Mevcut kodu değiştirmeden yeni doğrulayıcılar eklenebilir
    - Doğrulama sırası kontrol edilebilir

#### 2. Sorumluluk Zinciri (Chain of Responsibility) Deseni
- **Amaç:** Doğrulama isteğini doğrulayıcılar zinciri boyunca iletir
- **Uygulama:** Her `RouteValidator` kendi özel doğrulamasını yapar ve bağlamı bir sonraki doğrulayıcıya iletir
- **Faydaları:**
    - Doğrulayıcıları birbirinden bağımsız hale getirir
    - Doğrulayıcıları değiştirmeden doğrulama sırası değiştirilebilir
    - Doğrulama adımları kolayca eklenip çıkarılabilir

#### 3. İnşaatçı (Builder) Deseni
- **Amaç:** Doğrulama bağlamını oluşturmak için esnek bir yol sağlar
- **Uygulama:** `ValidationContext` Lombok'un `@Builder` anotasyonunu kullanır
- **Faydaları:**
    - Net ve okunabilir bağlam oluşturma
    - Opsiyonel parametreler kolayca yönetilebilir
    - Değişmez (immutable) doğrulama bağlam nesneleri

### Doğrulayıcı Uygulamaları

Sistem, rota doğrulamasının belirli yönlerinden sorumlu olan çeşitli doğrulayıcıları içerir:

1. **TransportationCountValidator (Ulaşım Sayısı Doğrulayıcı)**
    - İzin verilen maksimum ulaşım sayısını aşmamayı sağlar
    - Sıra: 1 (ilk çalışır)

2. **ConnectionValidator (Bağlantı Doğrulayıcı)**
    - Ardışık ulaşımlar arasındaki bağlantıları doğrular
    - Sıra: 2

3. **FlightRequirementValidator (Uçuş Gereksinimi Doğrulayıcı)**
    - Rotada tam olarak bir uçuş olmasını sağlar
    - Sıra: 3

4. **FlightLocationValidator (Uçuş Lokasyonu Doğrulayıcı)**
    - Uçuşun başlangıç ve varış noktalarını doğrular
    - Sıra: 4

5. **TransferTypeValidator (Transfer Tipi Doğrulayıcı)**
    - Transfer tiplerini ve uçuşa göre konumlarını doğrular
    - Sıra: 5

### Doğrulama Süreci

1. Rota kombinasyonları Stream API kullanılarak oluşturulur
2. Her kombinasyon kompozit doğrulayıcı üzerinden doğrulanır
3. Doğrulama sonuçları toplanır ve işlenir
4. Geçerli rotalar döndürülür veya uygun hatalar fırlatılır
