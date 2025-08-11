# Swagger документация для Reactive Rates Users API

## Описание

В проекте настроена автоматическая генерация API документации с помощью SpringDoc OpenAPI 3 (Swagger).

## Доступ к документации

После запуска приложения документация будет доступна по следующим URL:

- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **OpenAPI JSON**: http://localhost:8080/api-docs
- **OpenAPI YAML**: http://localhost:8080/api-docs.yaml

## Основные возможности

### 1. Интерактивная документация
- Полное описание всех API endpoints
- Возможность тестирования API прямо из браузера
- Автоматическая валидация запросов
- Примеры запросов и ответов

### 2. Описание моделей данных
- Детальное описание всех DTO классов
- Примеры значений для каждого поля
- Валидационные правила
- Описание enum значений

### 3. Коды ответов
- Описание всех возможных HTTP статус кодов
- Примеры успешных и ошибочных ответов
- Документация исключений

## API Endpoints

### Пользователи (Users)

#### GET /api/users
Получить список всех пользователей

#### GET /api/users/{id}
Получить пользователя по ID

#### GET /api/users/username/{username}
Получить пользователя по имени пользователя

#### GET /api/users/email/{email}
Получить пользователя по email

#### GET /api/users/role/{role}
Получить пользователей по роли

#### GET /api/users/active
Получить активных пользователей

#### GET /api/users/search?q={query}
Поиск пользователей по запросу

#### POST /api/users
Создать нового пользователя

#### PUT /api/users/{id}
Обновить существующего пользователя

#### DELETE /api/users/{id}
Удалить пользователя

#### PATCH /api/users/{id}/deactivate
Деактивировать пользователя

#### PATCH /api/users/{id}/activate
Активировать пользователя

## Модели данных

### UserDto
DTO для представления пользователя в API

### CreateUserRequest
Запрос на создание нового пользователя

### UpdateUserRequest
Запрос на обновление существующего пользователя

### UserRole
Enum с возможными ролями пользователей:
- ADMIN - Администратор
- USER - Обычный пользователь
- MODERATOR - Модератор

## Конфигурация

Настройки Swagger находятся в файле `application.properties`:

```properties
# Swagger UI Configuration
springdoc.api-docs.path=/api-docs
springdoc.swagger-ui.path=/swagger-ui.html
springdoc.swagger-ui.operationsSorter=method
springdoc.swagger-ui.tagsSorter=alpha
springdoc.swagger-ui.doc-expansion=none
springdoc.swagger-ui.disable-swagger-default-url=true
```

## Аннотации

В проекте используются следующие аннотации OpenAPI:

- `@Tag` - группировка endpoints
- `@Operation` - описание операции
- `@Parameter` - описание параметров
- `@Schema` - описание моделей данных
- `@ApiResponse` - описание ответов
- `@ApiResponses` - описание множественных ответов

## Примеры использования

### Создание пользователя
```json
POST /api/users
{
  "username": "john_doe",
  "email": "john@example.com",
  "password": "password123",
  "firstName": "John",
  "lastName": "Doe",
  "phoneNumber": "+7 (999) 123-45-67",
  "role": "USER"
}
```

### Обновление пользователя
```json
PUT /api/users/1
{
  "firstName": "John",
  "lastName": "Smith",
  "isActive": true
}
```

## Зависимости

Для работы Swagger используется:
```xml
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
    <version>2.3.0</version>
</dependency>
```

## Примечания

- Документация автоматически генерируется на основе аннотаций в коде
- При изменении API необходимо обновлять соответствующие аннотации
- Swagger UI доступен только в режиме разработки (можно отключить в продакшене)
