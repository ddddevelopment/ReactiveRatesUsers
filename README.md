# CRUD Приложение для Управления Пользователями

Это Spring Boot приложение для управления пользователями с полным CRUD функционалом.

## Технологии

- **Spring Boot 3.5.4**
- **Spring Data JPA**
- **Spring Security**
- **H2 Database** (для разработки)
- **PostgreSQL** (для продакшена)
- **Lombok**
- **Maven**

## Запуск приложения

1. Клонируйте репозиторий
2. Убедитесь, что у вас установлена Java 21
3. Запустите приложение:
   ```bash
   mvn spring-boot:run
   ```

Приложение будет доступно по адресу: `http://localhost:8080`

## База данных

### H2 Console (для разработки)
- URL: `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:testdb`
- Username: `sa`
- Password: `password`

### Тестовые пользователи
При первом запуске автоматически создаются тестовые пользователи:
- **admin/admin123** (Администратор)
- **user/user123** (Пользователь)
- **moderator/mod123** (Модератор)

## API Endpoints

### Получение пользователей

#### Получить всех пользователей
```
GET /api/users
```

#### Получить пользователя по ID
```
GET /api/users/{id}
```

#### Получить пользователя по имени пользователя
```
GET /api/users/username/{username}
```

#### Получить пользователя по email
```
GET /api/users/email/{email}
```

#### Получить пользователей по роли
```
GET /api/users/role/{role}
```
Роли: `ADMIN`, `USER`, `MODERATOR`

#### Получить активных пользователей
```
GET /api/users/active
```

#### Поиск пользователей
```
GET /api/users/search?q={searchTerm}
```

### Создание пользователя

```
POST /api/users
Content-Type: application/json

{
  "username": "newuser",
  "email": "newuser@example.com",
  "password": "password123",
  "firstName": "Имя",
  "lastName": "Фамилия",
  "phoneNumber": "+7-999-123-45-67",
  "role": "USER"
}
```

### Обновление пользователя

```
PUT /api/users/{id}
Content-Type: application/json

{
  "username": "updateduser",
  "email": "updated@example.com",
  "firstName": "Обновленное имя",
  "lastName": "Обновленная фамилия",
  "role": "MODERATOR",
  "isActive": true
}
```

### Удаление пользователя

```
DELETE /api/users/{id}
```

### Активация/деактивация пользователя

```
PATCH /api/users/{id}/activate
PATCH /api/users/{id}/deactivate
```

## Модель данных

### User
```java
{
  "id": 1,
  "username": "user",
  "email": "user@example.com",
  "firstName": "Иван",
  "lastName": "Иванов",
  "phoneNumber": "+7-999-123-45-67",
  "role": "USER",
  "isActive": true,
  "createdAt": "2024-01-01T10:00:00",
  "updatedAt": "2024-01-01T10:00:00"
}
```

### Роли пользователей
- **ADMIN** - Администратор системы
- **USER** - Обычный пользователь
- **MODERATOR** - Модератор

## Валидация

Приложение включает валидацию данных:
- Username: 3-50 символов, уникальный
- Email: корректный формат, уникальный
- Password: минимум 6 символов
- Обязательные поля: username, email, password

## Безопасность

- Пароли хешируются с помощью BCrypt
- CSRF защита отключена для API
- Все API endpoints доступны без аутентификации (для демонстрации)

## Архитектура

Приложение использует **Domain-Driven Design (DDD)** архитектуру с четким разделением слоев:

### Слои архитектуры:

1. **Domain Layer** - доменная модель и бизнес-логика
   - `domain/model/User.java` - доменная модель пользователя
   - `domain/service/UserDomainService.java` - доменные сервисы
   - `domain/repository/UserRepository.java` - интерфейс репозитория
   - `domain/exception/` - доменные исключения

2. **Infrastructure Layer** - инфраструктурный слой
   - `infrastructure/persistence/entity/UserEntity.java` - JPA Entity
   - `infrastructure/persistence/repository/UserJpaRepository.java` - JPA репозиторий
   - `infrastructure/persistence/repository/UserRepositoryImpl.java` - реализация доменного репозитория
   - `infrastructure/config/` - конфигурации

3. **Application Layer** - прикладной слой
   - `application/service/UserApplicationService.java` - прикладные сервисы

4. **API Layer** - слой представления
   - `api/controller/UserController.java` - REST контроллеры
   - `api/exception/GlobalExceptionHandler.java` - обработка исключений

5. **DTO Layer** - объекты передачи данных
   - `dto/UserDto.java` - DTO для пользователя
   - `dto/CreateUserRequest.java` - DTO для создания
   - `dto/UpdateUserRequest.java` - DTO для обновления

### Преимущества архитектуры:

- **Разделение ответственности** - каждый слой имеет свою зону ответственности
- **Независимость домена** - доменная модель не зависит от фреймворков
- **Тестируемость** - легко тестировать каждый слой отдельно
- **Гибкость** - можно легко заменить реализацию репозитория
- **Масштабируемость** - легко добавлять новые функции

## Структура проекта

```
src/main/java/com/reactiverates/users/
├── api/
│   ├── controller/
│   │   └── UserController.java
│   └── exception/
│       └── GlobalExceptionHandler.java
├── application/
│   └── service/
│       └── UserApplicationService.java
├── domain/
│   ├── exception/
│   │   ├── UserNotFoundException.java
│   │   └── UserAlreadyExistsException.java
│   ├── model/
│   │   └── User.java
│   ├── repository/
│   │   └── UserRepository.java
│   └── service/
│       └── UserDomainService.java
├── dto/
│   ├── CreateUserRequest.java
│   ├── UpdateUserRequest.java
│   └── UserDto.java
└── infrastructure/
    ├── config/
    │   ├── DataInitializer.java
    │   └── SecurityConfig.java
    └── persistence/
        ├── entity/
        │   └── UserEntity.java
        └── repository/
            ├── UserJpaRepository.java
            └── UserRepositoryImpl.java
```

## Тестирование

Для тестирования API можно использовать:
- Postman
- curl
- H2 Console для просмотра базы данных

### Примеры curl команд

```bash
# Получить всех пользователей
curl -X GET http://localhost:8080/api/users

# Создать пользователя
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "email": "test@example.com",
    "password": "password123",
    "firstName": "Тест",
    "lastName": "Пользователь"
  }'

# Получить пользователя по ID
curl -X GET http://localhost:8080/api/users/1

# Обновить пользователя
curl -X PUT http://localhost:8080/api/users/1 \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "Обновленное имя"
  }'

# Удалить пользователя
curl -X DELETE http://localhost:8080/api/users/1
```
