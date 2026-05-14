Собрал в один конспект по темам из файла: Spring, REST/OpenAPI, SQL/JPA/Hibernate, тесты, Docker, CI/CD, JWT, логи, метрики и архитектуры.

---

# Конспект к коллоквиуму по Backend / Kotlin / Spring

## 1. Общая схема backend-приложения

Backend обычно работает так:

```text
HTTP-запрос
↓
Controller
↓
Service
↓
Repository
↓
Database
↓
HTTP-ответ
```

Пример:

```text
GET /api/books/5
```

Что происходит:

```text
1. Клиент просит книгу с id = 5
2. Controller принимает запрос
3. Service выполняет бизнес-логику
4. Repository идёт в БД
5. БД возвращает данные
6. Backend отдаёт JSON
```

---

# 2. Spring

## 2.1. Зачем нужен Spring

Spring — это фреймворк, который берёт на себя рутину backend-приложения:

```text
создание объектов
связывание зависимостей
приём HTTP-запросов
превращение JSON ↔ Kotlin-объекты
работу с БД
транзакции
конфигурацию
security
логи
тесты
```

Мы пишем смысл приложения:

```text
Controller — принимает запросы
Service — бизнес-логика
Repository — работа с БД
```

Spring собирает это в рабочий pipeline.

---

## 2.2. IoC

**IoC** — Inversion of Control, инверсия управления.

Без Spring объект сам создаёт зависимости:

```kotlin
class BookService {
    private val repository = BookRepository()
}
```

Проблема: `BookService` жёстко привязан к конкретному `BookRepository`.

Со Spring/DI:

```kotlin
class BookService(
    private val repository: BookRepository
)
```

Теперь `BookService` не создаёт зависимость сам, а получает её снаружи.

```text
IoC = объект сам не управляет созданием зависимостей.
Этим занимается внешний контейнер, в нашем случае Spring.
```

Плюсы:

```text
код гибче
проще тестировать
легче менять реализации
меньше ручной настройки
```

---

## 2.3. DI

**DI** — Dependency Injection, внедрение зависимостей.

Пример:

```kotlin
@Service
class BookService(
    private val bookRepository: BookRepository
)
```

Spring видит, что `BookService` нужен `BookRepository`, и сам передаёт его в конструктор.

Лучший вариант — constructor injection:

```kotlin
class SomeService(
    private val dependency: Dependency
)
```

---

## 2.4. Bean

**Bean** — это объект, который создал и контролирует Spring.

```kotlin
@Service
class BookService
```

Spring создаёт объект `BookService` и кладёт его в контейнер.

```text
Обычный объект = мы создали сами.
Bean = объект создал Spring и хранит у себя.
```

Пример обычного объекта:

```kotlin
val book = Book("Clean Code")
```

Spring о нём ничего не знает.

Пример bean:

```kotlin
@Service
class BookService
```

---

## 2.5. Spring Container

Spring Container — хранилище bean-ов:

```text
Spring Container
├── BookController
├── BookService
├── BookRepository
├── AuthService
├── JwtService
└── PasswordEncoder
```

Если `BookController` требует `BookService`:

```kotlin
@RestController
class BookController(
    private val bookService: BookService
)
```

Spring берёт `BookService` из контейнера и передаёт его в controller.

---

## 2.6. Bean vs Component

```text
Bean = объект внутри Spring Container.
@Component = аннотация, которая говорит Spring создать bean.
```

Пример:

```kotlin
@Component
class EmailSender
```

`EmailSender` — класс.
`@Component` — инструкция для Spring.
Созданный объект `EmailSender` внутри контейнера — bean.

---

## 2.7. `@Component` vs `@Bean`

| Вещь         | Где ставится | Зачем                                                      |
| ------------ | ------------ | ---------------------------------------------------------- |
| `@Component` | на класс     | Spring сам создаёт объект                                  |
| `@Bean`      | на метод     | мы вручную создаём объект, а Spring кладёт его в контейнер |
| bean         | объект       | объект под управлением Spring                              |

`@Component`:

```kotlin
@Component
class EmailSender
```

`@Bean`:

```kotlin
@Configuration
class AppConfig {

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }
}
```

`@Bean` нужен, когда объект из внешней библиотеки и мы не можем повесить на его класс `@Component`.

---

## 2.8. Иерархия аннотаций Spring

```text
@Component
├── @Service
├── @Repository
└── @Controller
    └── @RestController
```

Все они создают bean, но имеют разный смысл.

---

## 2.9. Основные аннотации Spring

| Аннотация         | Зачем                        |
| ----------------- | ---------------------------- |
| `@Component`      | обычный Spring-компонент     |
| `@Service`        | бизнес-логика                |
| `@Repository`     | работа с БД                  |
| `@Controller`     | HTTP-контроллер для HTML     |
| `@RestController` | HTTP-контроллер для JSON API |
| `@RequestMapping` | общий путь controller’а      |
| `@GetMapping`     | обработка GET-запроса        |
| `@PostMapping`    | обработка POST-запроса       |
| `@PutMapping`     | обработка PUT-запроса        |
| `@PatchMapping`   | обработка PATCH-запроса      |
| `@DeleteMapping`  | обработка DELETE-запроса     |
| `@PathVariable`   | взять значение из URL        |
| `@RequestParam`   | взять query-параметр         |
| `@RequestBody`    | взять JSON из body           |
| `@Configuration`  | класс конфигурации           |
| `@Bean`           | создать bean вручную         |
| `@Transactional`  | выполнить метод в транзакции |

---

## 2.10. Пример Spring pipeline

```kotlin
@RestController
@RequestMapping("/api/books")
class BookController(
    private val bookService: BookService
) {

    @PostMapping
    fun create(@RequestBody request: CreateBookRequest): BookDto {
        return bookService.create(request)
    }
}
```

```kotlin
@Service
class BookService(
    private val bookRepository: BookRepository
) {

    fun create(request: CreateBookRequest): BookDto {
        val book = Book(title = request.title)
        val saved = bookRepository.save(book)
        return saved.toDto()
    }
}
```

```kotlin
@Repository
interface BookRepository : JpaRepository<Book, Long>
```

Что делает Spring:

```text
1. Видит POST /api/books
2. Находит метод с @PostMapping
3. Читает JSON из body
4. Создаёт CreateBookRequest
5. Вызывает controller
6. Controller вызывает service
7. Service вызывает repository
8. Repository идёт в БД
9. Ответ превращается в JSON
```

---

## 2.11. `application.yaml`

Файл с настройками приложения.

```yaml
server:
  port: 8080

spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/library
    username: postgres
    password: postgres

logging:
  level:
    root: INFO
    com.example.library: DEBUG
```

Читать значения можно через `@Value`:

```kotlin
@Service
class JwtService(
    @Value("\${jwt.secret}")
    private val secret: String
)
```

Или через `@ConfigurationProperties`:

```yaml
jwt:
  secret: my-secret
  expiration-minutes: 60
```

```kotlin
@ConfigurationProperties(prefix = "jwt")
data class JwtProperties(
    val secret: String,
    val expirationMinutes: Long
)
```

---

# 3. HTTP, REST и CRUD

## 3.1. HTTP

HTTP — протокол общения клиента и сервера.

HTTP-запрос состоит из:

```text
method
URL
headers
query params
body
```

Пример:

```http
POST /api/books?notify=true
Content-Type: application/json
Authorization: Bearer token

{
  "title": "Clean Code",
  "authorId": 1
}
```

HTTP-ответ состоит из:

```text
status code
headers
body
```

Пример:

```http
HTTP/1.1 201 Created
Content-Type: application/json

{
  "id": 5,
  "title": "Clean Code"
}
```

---

## 3.2. REST

HTTP сам по себе не говорит, как красиво строить API.

Можно сделать плохо:

```http
POST /do
```

```json
{
  "action": "deleteBook",
  "id": 5
}
```

REST говорит: строй API вокруг ресурсов.

Ресурсы:

```text
books
authors
users
orders
payments
```

REST API:

```http
GET    /api/books
GET    /api/books/5
POST   /api/books
PUT    /api/books/5
PATCH  /api/books/5
DELETE /api/books/5
```

```text
HTTP = протокол.
REST = стиль построения API поверх HTTP.
```

---

## 3.3. CRUD

CRUD — базовые действия с данными:

```text
Create — создать
Read — прочитать
Update — обновить
Delete — удалить
```

Соответствие REST/HTTP:

| CRUD   | HTTP     | URL            | Смысл             |
| ------ | -------- | -------------- | ----------------- |
| Create | `POST`   | `/api/books`   | создать книгу     |
| Read   | `GET`    | `/api/books/5` | получить книгу    |
| Update | `PUT`    | `/api/books/5` | заменить книгу    |
| Update | `PATCH`  | `/api/books/5` | частично обновить |
| Delete | `DELETE` | `/api/books/5` | удалить книгу     |

---

## 3.4. `PUT` vs `PATCH`

`PUT` — заменить объект целиком:

```http
PUT /api/books/5

{
  "title": "New title",
  "authorId": 1,
  "year": 2024
}
```

`PATCH` — обновить часть объекта:

```http
PATCH /api/books/5

{
  "title": "New title"
}
```

---

## 3.5. HTTP-коды

| Код                         | Значение         | Когда                          |
| --------------------------- | ---------------- | ------------------------------ |
| `200 OK`                    | успешно          | обычный успешный ответ         |
| `201 Created`               | создано          | после `POST`                   |
| `204 No Content`            | успешно без тела | после удаления                 |
| `400 Bad Request`           | плохой запрос    | неверные данные                |
| `401 Unauthorized`          | не авторизован   | нет/невалидный токен           |
| `403 Forbidden`             | запрещено        | пользователь есть, но прав нет |
| `404 Not Found`             | не найдено       | объекта нет                    |
| `409 Conflict`              | конфликт         | например email уже занят       |
| `500 Internal Server Error` | ошибка сервера   | баг на backend                 |

---

# 4. OpenAPI и Swagger

## 4.1. Зачем нужны

В команде есть:

```text
backend
frontend
mobile
тестировщики
другие сервисы
```

Им всем надо знать:

```text
какие endpoints есть
какой JSON отправлять
какой JSON приходит
какие параметры обязательные
какие коды ошибок бывают
```

---

## 4.2. OpenAPI

**OpenAPI** — формальное описание API.

Описывает:

```text
paths
HTTP methods
request body
response body
parameters
status codes
auth
```

Пример идеи:

```yaml
/api/books/{id}:
  get:
    summary: Get book by id
    parameters:
      - name: id
        in: path
        required: true
    responses:
      '200':
        description: Book found
      '404':
        description: Book not found
```

---

## 4.3. Swagger

**Swagger UI** — веб-страница, которая показывает OpenAPI красиво.

В Swagger можно:

```text
посмотреть endpoints
посмотреть схемы request/response
нажать Try it out
отправить тестовый запрос
увидеть ответ
```

```text
OpenAPI = описание API.
Swagger UI = интерфейс для просмотра и тестирования этого описания.
```

---

## 4.4. Code First и Contract First

| Подход         | Смысл                                           | Когда удобно                                                              |
| -------------- | ----------------------------------------------- | ------------------------------------------------------------------------- |
| Code First     | сначала код, потом OpenAPI генерируется из кода | маленький проект, быстрые изменения                                       |
| Contract First | сначала OpenAPI-контракт, потом код             | большие команды, frontend/backend работают параллельно, API нельзя ломать |

Code First:

```kotlin
@GetMapping("/{id}")
fun getById(@PathVariable id: Long): BookDto
```

Contract First:

```yaml
GET /api/books/{id}
```

---

# 5. REST, gRPC, SOAP

## 5.1. REST

JSON + HTTP.

```http
GET /api/books/5
```

```json
{
  "id": 5,
  "title": "Clean Code"
}
```

Хорош для:

```text
web
mobile
CRUD API
публичных API
```

---

## 5.2. gRPC

gRPC — вызов методов по строгому контракту `.proto`.

```proto
service BookService {
  rpc GetBook (GetBookRequest) returns (BookResponse);
}

message GetBookRequest {
  int64 id = 1;
}

message BookResponse {
  int64 id = 1;
  string title = 2;
}
```

Выглядит как вызов:

```text
BookService.GetBook(id = 5)
```

Хорош для:

```text
микросервисов
быстрого общения сервис-сервис
строгих контрактов
```

---

## 5.3. SOAP

SOAP — старый XML-based протокол.

```xml
<soap:Envelope>
  <soap:Body>
    <GetBookRequest>
      <id>5</id>
    </GetBookRequest>
  </soap:Body>
</soap:Envelope>
```

Часто встречается в:

```text
банках
гос. системах
старых enterprise-интеграциях
```

---

## 5.4. Сравнение

| Критерий           | REST           | gRPC         | SOAP              |
| ------------------ | -------------- | ------------ | ----------------- |
| Формат             | JSON           | Protobuf     | XML               |
| Стиль              | ресурсы        | методы       | операции          |
| Простота           | простой        | средний      | сложный           |
| Где используют     | web/mobile API | микросервисы | legacy/enterprise |
| Контракт           | OpenAPI        | `.proto`     | WSDL              |
| Тестировать руками | легко          | сложнее      | неудобно          |

---

# 6. SQL — краткая шпора

## SELECT

```sql
SELECT id, title, author_id
FROM books;
```

---

## WHERE

```sql
SELECT *
FROM books
WHERE year > 2000;
```

```sql
SELECT *
FROM books
WHERE title LIKE '%Kotlin%';
```

```sql
SELECT *
FROM books
WHERE author_id IN (1, 2, 3);
```

---

## ORDER BY

```sql
SELECT *
FROM books
ORDER BY year DESC;
```

---

## LIMIT / OFFSET

```sql
SELECT *
FROM books
ORDER BY id
LIMIT 10 OFFSET 20;
```

---

## INSERT

```sql
INSERT INTO books(title, year, author_id)
VALUES ('Clean Code', 2008, 1);
```

---

## UPDATE

```sql
UPDATE books
SET title = 'New Title'
WHERE id = 5;
```

---

## DELETE

```sql
DELETE FROM books
WHERE id = 5;
```

---

## JOIN

```sql
SELECT b.id, b.title, a.name
FROM books b
JOIN authors a ON b.author_id = a.id;
```

---

## INNER JOIN

Возвращает только строки, где есть совпадение в обеих таблицах.

```sql
SELECT *
FROM authors a
INNER JOIN books b ON b.author_id = a.id;
```

---

## LEFT JOIN

Возвращает все строки из левой таблицы и совпадения справа.

```sql
SELECT *
FROM authors a
LEFT JOIN books b ON b.author_id = a.id;
```

Если у автора нет книг, автор всё равно попадёт в результат, а поля книги будут `NULL`.

---

## GROUP BY

```sql
SELECT author_id, COUNT(*) AS books_count
FROM books
GROUP BY author_id;
```

---

## HAVING

`WHERE` фильтрует строки до группировки.
`HAVING` фильтрует группы после группировки.

```sql
SELECT author_id, COUNT(*) AS books_count
FROM books
GROUP BY author_id
HAVING COUNT(*) > 3;
```

---

## Индекс

```sql
CREATE INDEX idx_books_title ON books(title);
```

Индекс ускоряет поиск:

```sql
SELECT *
FROM books
WHERE title = 'Clean Code';
```

Минусы индексов:

```text
занимают место
замедляют INSERT
замедляют UPDATE
замедляют DELETE
```

---

## Нормализация

Нормализация — разбиение данных на связанные таблицы.

Плохо:

```text
books
id | title | author_name | author_birth_date
```

Хорошо:

```text
authors
id | name | birth_date

books
id | title | author_id
```

Плюсы:

```text
меньше дублирования
проще обновлять данные
меньше ошибок
понятные связи
```

---

# 7. ORM, JPA, Hibernate

## 7.1. Кто есть кто

```text
ORM = идея связывать объекты в коде с таблицами БД.
JPA = стандарт/спецификация ORM.
Hibernate = реализация JPA.
Spring Data JPA = удобная надстройка для repository.
```

Пример:

```text
Book class ↔ books table
Author class ↔ authors table
```

---

## 7.2. Entity

Entity — класс, который соответствует таблице.

```kotlin
import jakarta.persistence.*

@Entity
@Table(name = "books")
class Book(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false)
    var title: String,

    @Column(name = "publication_year")
    var year: Int
)
```

SQL-аналог:

```sql
CREATE TABLE books (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR NOT NULL,
    publication_year INT
);
```

---

## 7.3. Основные JPA-аннотации

| Аннотация              | Зачем                       |
| ---------------------- | --------------------------- |
| `@Entity`              | класс является сущностью БД |
| `@Table(name = "...")` | имя таблицы                 |
| `@Id`                  | primary key                 |
| `@GeneratedValue`      | генерация id                |
| `@Column`              | настройка колонки           |
| `@ManyToOne`           | многие к одному             |
| `@OneToMany`           | один ко многим              |
| `@OneToOne`            | один к одному               |
| `@ManyToMany`          | многие ко многим            |
| `@JoinColumn`          | foreign key                 |
| `@JoinTable`           | промежуточная таблица       |
| `@Enumerated`          | хранение enum               |

---

## 7.4. Repository

```kotlin
interface BookRepository : JpaRepository<Book, Long>
```

После этого доступны методы:

```kotlin
bookRepository.findAll()
bookRepository.findById(1)
bookRepository.save(book)
bookRepository.delete(book)
bookRepository.existsById(1)
```

---

## 7.5. Методы по названию

```kotlin
interface BookRepository : JpaRepository<Book, Long> {

    fun findByTitle(title: String): List<Book>

    fun findByYearGreaterThan(year: Int): List<Book>

    fun findByTitleContainingIgnoreCase(part: String): List<Book>

    fun existsByTitle(title: String): Boolean
}
```

Spring Data JPA сам строит запрос по названию метода.

---

# 8. Связи в JPA/Hibernate

## 8.1. `ManyToOne`

Много книг принадлежат одному автору.

```kotlin
@Entity
@Table(name = "authors")
class Author(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    var name: String
)
```

```kotlin
@Entity
@Table(name = "books")
class Book(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    var title: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    var author: Author
)
```

В БД:

```text
books.author_id → authors.id
```

---

## 8.2. `OneToMany`

Один автор имеет много книг.

```kotlin
@Entity
@Table(name = "authors")
class Author(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    var name: String,

    @OneToMany(mappedBy = "author", fetch = FetchType.LAZY)
    val books: List<Book> = emptyList()
)
```

`mappedBy = "author"` значит:

```text
Связь хранится на стороне Book.author.
```

Физически foreign key лежит в таблице `books`.

---

## 8.3. `ManyToMany`

Книга может иметь много жанров, жанр может быть у многих книг.

Нужна промежуточная таблица:

```text
books
genres
book_genres
```

```kotlin
@Entity
class Book(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    var title: String,

    @ManyToMany
    @JoinTable(
        name = "book_genres",
        joinColumns = [JoinColumn(name = "book_id")],
        inverseJoinColumns = [JoinColumn(name = "genre_id")]
    )
    val genres: MutableSet<Genre> = mutableSetOf()
)
```

```kotlin
@Entity
class Genre(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    var name: String
)
```

---

# 9. `@Query`

## 9.1. JPQL

JPQL похож на SQL, но работает с Entity и их полями.

```kotlin
@Query("""
    SELECT b
    FROM Book b
    WHERE b.year > :year
""")
fun findBooksAfterYear(@Param("year") year: Int): List<Book>
```

`Book` — класс, `year` — поле класса.

---

## 9.2. Native SQL

Можно писать обычный SQL.

```kotlin
@Query(
    value = """
        SELECT *
        FROM books
        WHERE publication_year > :year
    """,
    nativeQuery = true
)
fun findBooksAfterYearNative(@Param("year") year: Int): List<Book>
```

---

# 10. Lazy, Eager, EntityGraph, DTO

## 10.1. `LAZY`

```kotlin
@OneToMany(fetch = FetchType.LAZY)
val books: List<Book>
```

Значит:

```text
Не загружай books сразу.
Загрузи только когда реально обратятся к author.books.
```

---

## 10.2. `EAGER`

```kotlin
@OneToMany(fetch = FetchType.EAGER)
val books: List<Book>
```

Значит:

```text
Всегда загружай books вместе с author.
```

Минус: часто грузит лишние данные.

---

## 10.3. `@EntityGraph`

```kotlin
@EntityGraph(attributePaths = ["books"])
fun findAllBy(): List<Author>
```

Значит:

```text
Обычно books грузить не надо.
Но конкретно в этом запросе — загрузи.
```

Отличие:

| Вариант        | Что делает                               |
| -------------- | ---------------------------------------- |
| `EAGER`        | всегда грузит связь                      |
| `LAZY`         | не грузит связь сразу                    |
| `@EntityGraph` | грузит связь только в конкретном запросе |

---

## 10.4. DTO

Entity — объект БД.

DTO — объект для передачи данных наружу.

```kotlin
data class AuthorDto(
    val id: Long,
    val name: String,
    val booksCount: Long
)
```

DTO нужен, чтобы не отдавать наружу Entity и возвращать только нужные поля.

---

## 10.5. DTO-запрос

Если нужен ответ:

```json
{
  "id": 1,
  "name": "Author",
  "booksCount": 10
}
```

Не обязательно грузить автора и все книги. Можно сразу получить готовый DTO:

```kotlin
@Query("""
    SELECT new com.example.AuthorDto(
        a.id,
        a.name,
        COUNT(b.id)
    )
    FROM Author a
    LEFT JOIN a.books b
    GROUP BY a.id, a.name
""")
fun findAuthorDtos(): List<AuthorDto>
```

Смысл:

```text
Не загружать Author + все Book.
Сразу получить id, name, count.
```

---

# 11. Проблема N+1

## 11.1. Что такое N+1

Пример:

```kotlin
val authors = authorRepository.findAll()

authors.forEach { author ->
    println(author.books.size)
}
```

Hibernate может сделать:

```sql
SELECT * FROM authors;
```

Потом для каждого автора:

```sql
SELECT * FROM books WHERE author_id = 1;
SELECT * FROM books WHERE author_id = 2;
SELECT * FROM books WHERE author_id = 3;
```

Если авторов 100:

```text
1 запрос за авторами
100 запросов за книгами
итого 101 запрос
```

Это N+1.

---

## 11.2. Как обнаружить

Включить SQL-логи:

```yaml
logging:
  level:
    org.hibernate.SQL: DEBUG
```

Если видишь много одинаковых запросов в цикле — возможно, это N+1.

---

## 11.3. Как исправить

### `JOIN FETCH`

```kotlin
@Query("""
    SELECT DISTINCT a
    FROM Author a
    LEFT JOIN FETCH a.books
""")
fun findAllWithBooks(): List<Author>
```

Загружает авторов и книги одним запросом.

---

### `@EntityGraph`

```kotlin
@EntityGraph(attributePaths = ["books"])
fun findAllBy(): List<Author>
```

Загружает связь в конкретном запросе.

---

### DTO-запрос

```kotlin
@Query("""
    SELECT new com.example.AuthorDto(
        a.id,
        a.name,
        COUNT(b.id)
    )
    FROM Author a
    LEFT JOIN a.books b
    GROUP BY a.id, a.name
""")
fun findAuthorDtos(): List<AuthorDto>
```

Не грузит лишние Entity, сразу возвращает нужную форму.

---

# 12. `@Transactional`

Транзакция — группа операций, которая должна выполниться целиком.

```kotlin
@Transactional
fun borrowBook(readerId: Long, bookId: Long) {
    val reader = readerRepository.findById(readerId).orElseThrow()
    val book = bookRepository.findById(bookId).orElseThrow()

    if (book.borrowed) {
        throw IllegalStateException("Book already borrowed")
    }

    book.borrowed = true
    book.reader = reader
}
```

Spring делает:

```text
1. Открывает транзакцию
2. Выполняет метод
3. Если всё хорошо — commit
4. Если ошибка — rollback
```

Обычно `@Transactional` ставят на service layer.

---

# 13. Тесты

## 13.1. Виды тестов

| Вид         | Что проверяет                       | Пример                          |
| ----------- | ----------------------------------- | ------------------------------- |
| Unit        | маленький кусок логики изолированно | `BookService` с mock repository |
| Integration | взаимодействие частей               | service + repository + test DB  |
| E2E         | всю систему целиком                 | HTTP-запрос → БД → ответ        |

Пирамида:

```text
        E2E
   Integration
      Unit
```

Unit-тестов обычно больше всего.

---

## 13.2. JUnit 5

JUnit — библиотека для тестов.

```kotlin
class CalculatorTest {

    @Test
    fun `should add two numbers`() {
        val result = 2 + 2

        assertEquals(4, result)
    }
}
```

Основные assertions:

```kotlin
assertEquals(expected, actual)
assertTrue(condition)
assertFalse(condition)
assertNotNull(value)
assertThrows<IllegalArgumentException> {
    service.doSomethingWrong()
}
```

Обычный `assert` использовать хуже, потому что JUnit assertions дают понятные ошибки.

---

## 13.3. MockK

Mock — поддельный объект для теста.

Пример:

```kotlin
class BookServiceTest {

    private val bookRepository = mockk<BookRepository>()
    private val bookService = BookService(bookRepository)

    @Test
    fun `should return book by id`() {
        val book = Book(id = 1, title = "Clean Code")

        every { bookRepository.findById(1) } returns Optional.of(book)

        val result = bookService.getById(1)

        assertEquals("Clean Code", result.title)

        verify(exactly = 1) {
            bookRepository.findById(1)
        }
    }
}
```

Смысл:

```text
every { ... } returns ... — задаём поведение mock.
verify { ... } — проверяем, что метод был вызван.
```

---

# 14. Docker

## 14.1. Зачем нужен Docker

Docker упаковывает приложение и окружение в контейнер.

Решает проблему:

```text
"У меня работает, а у тебя нет"
```

Контейнер можно одинаково запускать:

```text
локально
на сервере
в CI/CD
у другого разработчика
```

---

## 14.2. Image и Container

```text
Image = шаблон/слепок приложения.
Container = запущенный экземпляр image.
```

Пример:

```bash
docker build -t my-app .
docker run -p 8080:8080 my-app
```

---

## 14.3. Dockerfile

Dockerfile — инструкция сборки image.

```dockerfile
FROM eclipse-temurin:17-jre

WORKDIR /app

COPY build/libs/app.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
```

---

## 14.4. Инструкции Dockerfile

| Инструкция   | Что значит                        | Пример                            |
| ------------ | --------------------------------- | --------------------------------- |
| `FROM`       | базовый образ                     | `FROM eclipse-temurin:17-jre`     |
| `WORKDIR`    | рабочая папка внутри контейнера   | `WORKDIR /app`                    |
| `COPY`       | скопировать файл/папку в image    | `COPY build/libs/app.jar app.jar` |
| `ADD`        | как `COPY`, но умеет URL/архивы   | `ADD app.tar.gz /app`             |
| `RUN`        | команда во время сборки image     | `RUN apt-get update`              |
| `ENV`        | переменная окружения              | `ENV APP_PORT=8080`               |
| `ARG`        | переменная только на этапе сборки | `ARG JAR_FILE=app.jar`            |
| `EXPOSE`     | документирует порт приложения     | `EXPOSE 8080`                     |
| `CMD`        | аргументы/команда по умолчанию    | `CMD ["app.jar"]`                 |
| `ENTRYPOINT` | основная команда запуска          | `ENTRYPOINT ["java", "-jar"]`     |
| `VOLUME`     | папка для внешних данных          | `VOLUME /data`                    |
| `USER`       | пользователь внутри контейнера    | `USER appuser`                    |

---

## 14.5. `ENTRYPOINT` и `CMD`

```text
ENTRYPOINT — неизменная основная команда.
CMD — аргументы по умолчанию.
```

Пример:

```dockerfile
ENTRYPOINT ["java", "-jar"]
CMD ["app.jar"]
```

Итог:

```bash
java -jar app.jar
```

---

# 15. Docker Compose

Docker Compose нужен, чтобы запускать несколько контейнеров вместе.

Например:

```text
backend + PostgreSQL
```

```yaml
services:
  app:
    build: .
    ports:
      - "8080:8080"
    environment:
      SPRING_DATASOURCE_URL: jdbc:postgresql://db:5432/library
      SPRING_DATASOURCE_USERNAME: postgres
      SPRING_DATASOURCE_PASSWORD: postgres
    depends_on:
      - db

  db:
    image: postgres:16
    ports:
      - "5432:5432"
    environment:
      POSTGRES_DB: library
      POSTGRES_USER: postgres
      POSTGRES_PASSWORD: postgres
```

Внутри compose backend обращается к БД по имени сервиса:

```text
jdbc:postgresql://db:5432/library
```

Не `localhost`, потому что `localhost` внутри контейнера — сам контейнер.

Команды:

```bash
docker compose up
docker compose up -d
docker compose down
docker compose up --build
```

---

# 16. Логирование и метрики

## 16.1. Логирование

В Spring Boot обычно уже есть:

```text
SLF4J — фасад логирования
Logback — реализация логирования
```

Мы сами решаем, где и что логировать.

```kotlin
private val log = LoggerFactory.getLogger(BookService::class.java)

fun getBook(id: Long): BookDto {
    log.info("Trying to get book by id={}", id)

    val book = bookRepository.findById(id).orElseThrow {
        log.warn("Book not found, id={}", id)
        NotFoundException("Book not found")
    }

    return book.toDto()
}
```

Уровни:

| Уровень | Когда                                 |
| ------- | ------------------------------------- |
| `TRACE` | максимально подробно                  |
| `DEBUG` | отладочная информация                 |
| `INFO`  | обычные важные события                |
| `WARN`  | подозрительно, но приложение работает |
| `ERROR` | ошибка                                |

Настройка:

```yaml
logging:
  level:
    root: INFO
    com.example.library: DEBUG
    org.hibernate.SQL: DEBUG
```

---

## 16.2. Метрики

Метрики — числовые показатели работы приложения:

```text
количество запросов
время ответа
количество ошибок
использование памяти
количество соединений с БД
```

Разница:

| Логи                              | Метрики                      |
| --------------------------------- | ---------------------------- |
| текстовые события                 | числа                        |
| помогают понять конкретную ошибку | показывают состояние системы |
| пример: stacktrace                | пример: 500 ошибок за минуту |

---

# 17. Аутентификация, авторизация, JWT

## 17.1. Аутентификация vs авторизация

```text
Аутентификация = кто ты?
Авторизация = что тебе можно?
```

Пример:

```text
Пользователь залогинился → аутентификация.
Пользователь может удалить книгу только если он ADMIN → авторизация.
```

---

## 17.2. Зачем JWT

После логина backend должен понимать, кто делает следующие запросы.

JWT — подписанный токен, который клиент отправляет в каждом запросе.

Схема:

```text
1. Пользователь отправляет login/password
2. Backend проверяет пользователя
3. Backend выдаёт JWT
4. Клиент хранит JWT
5. Клиент отправляет JWT в Authorization header
6. Backend проверяет токен
```

---

## 17.3. JWT flow

Логин:

```http
POST /api/auth/login
Content-Type: application/json

{
  "email": "user@mail.com",
  "password": "123456"
}
```

Ответ:

```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9..."
}
```

Следующий запрос:

```http
GET /api/books
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
```

---

## 17.4. Что внутри JWT

JWT:

```text
header.payload.signature
```

Payload может содержать:

```json
{
  "sub": "user@mail.com",
  "userId": 15,
  "roles": ["USER"],
  "exp": 1730000000
}
```

Важно:

```text
JWT можно прочитать, но нельзя безопасно изменить без secret key.
В JWT нельзя хранить пароль и секретные данные.
```

---

## 17.5. JWT в Spring

Обычно есть классы:

```text
AuthController
AuthService
JwtService
JwtAuthenticationFilter
SecurityConfig
UserDetailsService
```

`AuthController`:

```kotlin
@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val authService: AuthService
) {

    @PostMapping("/login")
    fun login(@RequestBody request: LoginRequest): TokenResponse {
        return authService.login(request)
    }
}
```

`AuthService`:

```kotlin
@Service
class AuthService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtService: JwtService
) {

    fun login(request: LoginRequest): TokenResponse {
        val user = userRepository.findByEmail(request.email)
            ?: throw RuntimeException("Invalid credentials")

        if (!passwordEncoder.matches(request.password, user.passwordHash)) {
            throw RuntimeException("Invalid credentials")
        }

        val token = jwtService.generateToken(user)

        return TokenResponse(token)
    }
}
```

`JwtService`:

```kotlin
@Service
class JwtService {

    fun generateToken(user: User): String {
        // создать токен
    }

    fun extractUsername(token: String): String {
        // достать email/username
    }

    fun isTokenValid(token: String): Boolean {
        // проверить подпись и срок жизни
    }
}
```

`JwtAuthenticationFilter` делает на каждый запрос:

```text
1. Достаёт Authorization header
2. Проверяет Bearer token
3. Валидирует JWT
4. Кладёт пользователя в SecurityContext
```

`SecurityConfig`:

```kotlin
@Configuration
class SecurityConfig {

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        return http
            .csrf { it.disable() }
            .authorizeHttpRequests {
                it.requestMatchers("/api/auth/**").permitAll()
                it.anyRequest().authenticated()
            }
            .build()
    }
}
```

---

# 18. Монолит и микросервисы

## 18.1. Монолит

Один backend-проект:

```text
library-app
├── auth
├── books
├── readers
└── payments
```

Всё запускается одним приложением.

Плюсы:

```text
проще писать
проще запускать
проще тестировать
проще дебажить
проще транзакции
меньше инфраструктуры
быстрее разработка
```

Минусы:

```text
со временем может раздуться
сложнее разделять команды
деплой всего приложения целиком
масштабируется всё сразу
падение монолита может уронить всё
```

---

## 18.2. Микросервисы

Несколько отдельных сервисов:

```text
auth-service
book-service
payment-service
notification-service
```

Плюсы:

```text
независимый деплой
можно масштабировать отдельные части
разные команды работают независимо
падение одного сервиса не всегда валит всё
можно использовать разные технологии
```

Минусы:

```text
сложнее инфраструктура
нужны Docker/Kubernetes/CI/CD/monitoring
сложнее дебаг
сетевые ошибки
сложнее транзакции
сложнее тестирование всей системы
нужно следить за API-контрактами
```

Главное:

```text
Маленький/средний проект → чаще монолит.
Большая система, много команд, разная нагрузка → микросервисы.
```

---

# 19. CI/CD

## 19.1. CI

**CI** — Continuous Integration.

Идея:

```text
Каждый push / pull request автоматически проверяется.
```

CI обычно делает:

```text
скачивает код
ставит Java/Kotlin/Gradle
запускает сборку
запускает тесты
проверяет стиль
собирает артефакт
```

Пример GitHub Actions:

```yaml
name: Build and Test

on:
  push:
  pull_request:

jobs:
  test:
    runs-on: ubuntu-latest

    steps:
      - name: Checkout code
        uses: actions/checkout@v4

      - name: Setup Java
        uses: actions/setup-java@v4
        with:
          distribution: temurin
          java-version: 17

      - name: Make gradlew executable
        run: chmod +x ./gradlew

      - name: Run tests
        run: ./gradlew test
```

---

## 19.2. CD

CD бывает двух смыслов.

### Continuous Delivery

Код автоматически собирается и готовится к выкладке, но финальный деплой подтверждает человек.

### Continuous Deployment

Код автоматически выкладывается на сервер без ручного подтверждения.

---

## 19.3. Инструменты CI/CD

```text
GitHub Actions
GitLab CI
Jenkins
TeamCity
CircleCI
Travis CI
Docker
Kubernetes
Helm
```

---

# 20. Финальная карта тем

```text
Spring
├── IoC / DI
├── Bean / Component
├── Controller / Service / Repository
├── application.yaml
└── @Transactional

HTTP / REST
├── request / response
├── methods
├── status codes
├── CRUD
├── OpenAPI
└── Swagger

API styles
├── REST
├── gRPC
└── SOAP

Database
├── SQL
├── JOIN
├── GROUP BY
├── indexes
└── normalization

JPA / Hibernate
├── Entity
├── Repository
├── relations
├── Lazy / Eager
├── EntityGraph
├── DTO-query
└── N+1

Testing
├── Unit
├── Integration
├── E2E
├── JUnit 5
└── MockK

Infrastructure
├── Dockerfile
├── Docker Compose
├── CI
└── CD

Production
├── Logs
├── Metrics
├── Auth
├── JWT
├── Monolith
└── Microservices
```

---

# 21. Самая главная схема

```text
HTTP request
↓
@RestController
↓
@Service
↓
@Repository
↓
Hibernate / JPA
↓
SQL
↓
Database
↓
HTTP response
```

```text
Spring связывает части приложения.
REST делает API понятным.
OpenAPI описывает API.
Hibernate связывает объекты с БД.
Docker упаковывает приложение.
CI/CD автоматизирует проверки и деплой.
JWT отвечает за stateless-авторизацию.
Логи и метрики помогают понимать состояние приложения.
```
