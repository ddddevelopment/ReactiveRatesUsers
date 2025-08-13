# Микросервис управления пользователями

Микросервис для управления пользователями в системе Reactive Rates с поддержкой JWT аутентификации.

## Возможности

- CRUD операции с пользователями
- JWT аутентификация и авторизация
- gRPC API
- REST API с Swagger документацией
- Поддержка ролей пользователей (ADMIN, USER)

## Технологии

- Spring Boot 3.5.4
- Spring Security с JWT
- Spring Data JPA
- H2 Database (для разработки)
- PostgreSQL (для продакшена)
- gRPC
- Swagger/OpenAPI 3

## Быстрый старт

### Предварительные требования

- Java 21
- Maven 3.6+

### Запуск

1. Клонируйте репозиторий
2. Перейдите в директорию проекта
3. Запустите приложение:

```bash
./mvnw spring-boot:run
```

Приложение будет доступно по адресу: http://localhost:8080

### Swagger UI

Документация API доступна по адресу: http://localhost:8080/swagger-ui.html

## JWT Аутентификация

### Конфигурация

Настройки JWT находятся в `application.properties`:

```properties
jwt.secret=your-super-secret-jwt-key-that-should-be-at-least-256-bits-long-for-production
jwt.expiration=86400000
```

**Важно**: В продакшене обязательно замените `jwt.secret` на безопасный ключ длиной не менее 256 бит.

### Структура JWT токена

JWT токен должен содержать следующие claims:

```json
{
  "type": "access",
  "roles": ["USER", "ADMIN"],
  "sub": "username",
  "iat": 1234567890,
  "exp": 1234567890
}
```

- `type` - тип токена (обычно "access")
- `roles` - массив строк с ролями пользователя
- `sub` - имя пользователя
- `exp` - время истечения токена
- `iat` - время создания токена

**Поддерживаемые роли:**
- `USER` - обычный пользователь
- `ADMIN` - администратор (доступ ко всем endpoints)
- `MODERATOR` - модератор

### Доступ к endpoints

#### Публичные endpoints (доступны всем)

- `GET /api/users` - получить всех пользователей
- `GET /api/users/{id}` - получить пользователя по ID
- `GET /api/users/username/{username}` - получить пользователя по username
- `GET /api/users/email/{email}` - получить пользователя по email
- `GET /api/users/role/{role}` - получить пользователей по роли
- `GET /api/users/active` - получить активных пользователей
- `GET /api/users/search?q={query}` - поиск пользователей
- `POST /api/users` - создать нового пользователя

#### Административные endpoints (требуют роль ADMIN)

- `PUT /api/users/{id}` - обновить пользователя
- `DELETE /api/users/{id}` - удалить пользователя
- `PATCH /api/users/{id}/deactivate` - деактивировать пользователя
- `PATCH /api/users/{id}/activate` - активировать пользователя

### Использование

Добавьте JWT токен в заголовок `Authorization`:

```
Authorization: Bearer <your-jwt-token>
```

#### Примеры запросов

Публичный запрос (не требует токена):
```bash
curl -X GET http://localhost:8080/api/users
```

Административный запрос (требует токен с ролью ADMIN):
```bash
curl -X PUT http://localhost:8080/api/users/1 \
  -H "Authorization: Bearer <your-jwt-token>" \
  -H "Content-Type: application/json" \
  -d '{
    "username": "updated_user",
    "email": "updated@example.com",
    "firstName": "Updated",
    "lastName": "User"
  }'
```

### Примеры JWT токенов для тестирования

Для тестирования можно использовать следующие примеры токенов (созданные с тем же секретным ключом):

**Токен для пользователя с ролью USER:**
```
eyJhbGciOiJIUzI1NiJ9.eyJ0eXBlIjoiYWNjZXNzIiwicm9sZXMiOlsiVVNFUiJdLCJzdWIiOiJqb2huX2RvZSIsImlhdCI6MTczNDQ5NzQ0NCwiZXhwIjoxNzM0NTAxMDQ0fQ.example_signature
```

**Токен для администратора с ролью ADMIN:**
```
eyJhbGciOiJIUzI1NiJ9.eyJ0eXBlIjoiYWNjZXNzIiwicm9sZXMiOlsiQURNSU4iXSwic3ViIjoiYWRtaW4iLCJpYXQiOjE3MzQ0OTc0NDQsImV4cCI6MTczNDUwMTA0NH0.example_signature
```

**Пример curl с токеном:**
```bash
# Получить всех пользователей (публичный endpoint)
curl -X GET http://localhost:8080/api/users

# Обновить пользователя (требует роль ADMIN)
curl -X PUT http://localhost:8080/api/users/1 \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJ0eXBlIjoiYWNjZXNzIiwicm9sZXMiOlsiQURNSU4iXSwic3ViIjoiYWRtaW4iLCJpYXQiOjE3MzQ0OTc0NDQsImV4cCI6MTczNDUwMTA0NH0.example_signature" \
  -H "Content-Type: application/json" \
  -d '{
    "username": "updated_user",
    "email": "updated@example.com",
    "firstName": "Updated",
    "lastName": "User"
  }'
```

## Интеграция с внешним микросервисом аутентификации

Убедитесь, что ваш внешний микросервис аутентификации:

1. Использует тот же секретный ключ для подписи JWT токенов
2. Включает роли пользователя в claims токена
3. Устанавливает корректное время истечения токена

## Тестирование

Запуск тестов:

```bash
./mvnw test
```

Запуск конкретного теста:

```bash
./mvnw test -Dtest=JwtServiceTest
```

## Структура проекта

```
src/
├── main/
│   ├── java/com/reactiverates/users/
│   │   ├── api/controller/          # REST контроллеры
│   │   ├── application/service/     # Сервисы приложения
│   │   ├── domain/                  # Доменная модель
│   │   │   ├── model/              # Модели данных
│   │   │   ├── service/            # Доменные сервисы
│   │   │   └── exception/          # Доменные исключения
│   │   └── infrastructure/         # Инфраструктурный слой
│   │       ├── config/             # Конфигурации
│   │       ├── security/           # JWT аутентификация
│   │       ├── persistence/        # Репозитории и сущности
│   │       └── grpc/               # gRPC сервисы
│   ├── proto/                      # Protocol Buffers файлы
│   └── resources/                  # Конфигурационные файлы
└── test/                           # Тесты
```

## Безопасность

1. **Секретный ключ**: Используйте криптографически стойкий секретный ключ в продакшене
2. **HTTPS**: Всегда используйте HTTPS в продакшене
3. **Время жизни токена**: Настройте разумное время жизни токена
4. **Валидация**: Токены проверяются на валидность и время жизни

## Обработка ошибок

### 401 Unauthorized
Возвращается когда:
- JWT токен отсутствует
- JWT токен недействителен
- JWT токен истек

### 403 Forbidden
Возвращается когда:
- У пользователя недостаточно прав для доступа к endpoint

## Лицензия

Этот проект является частью системы Reactive Rates.
