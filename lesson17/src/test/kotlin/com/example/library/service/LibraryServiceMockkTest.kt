package com.example.library.service

import com.example.library.entity.Author
import com.example.library.entity.Genre
import com.example.library.entity.Book
import io.mockk.impl.annotations.MockK
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Disabled

import com.example.library.repository.AuthorRepository
import com.example.library.repository.BookRepository
import com.example.library.repository.GenreRepository
import com.example.library.repository.ReaderRepository

import io.mockk.every
import kotlin.test.assertEquals
import io.mockk.verify
import io.mockk.mockk
import io.mockk.slot

import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort

import org.junit.jupiter.api.Assertions.assertThrows
import jakarta.persistence.EntityNotFoundException
import java.util.Optional

/**
 * ШАБЛОН ЗАНЯТИЯ (без готового кода тестов).
 *
 * Как работать:
 * 1. Сними [@Disabled] с класса ниже, когда начнёшь писать код.
 * 2. Для каждого теста следуй блоку «ИНСТРУКЦИЯ» — шаг за шагом.
 *
 * Подключи в начале файла импорты по мере необходимости, например:
 * - io.mockk: mockk, every, verify, slot, capture, any, eq, match
 * - org.junit.jupiter.api.Assertions.* (assertEquals, assertThrows — для проверки **результата** сервиса)
 * - Матчеры **eq** / **any** / **match** используются внутри `every { }` и `verify { }` для **аргументов** мока
 * - сущности и репозитории из com.example.library.*
 * - java.util.Optional — для findById у Spring Data
 * - org.springframework.data.domain.* — для постраничности
 */

@MockK
class LibraryServiceMockkTest {

    // ИНСТРУКЦИЯ (общая):
    // Объяви четыре репозитория как mockk<...>() — как в рабочем файле тестов.
    // Объяви lateinit var service: LibraryService
    // В @BeforeEach создай LibraryService, передав в конструктор все четыре мока.

    val authorRepository = mockk<AuthorRepository>()
    val bookRepository = mockk<BookRepository>()
    val genreRepository = mockk<GenreRepository>()
    val readerRepository = mockk<ReaderRepository>()

    lateinit var service: LibraryService

    @BeforeEach
    fun setUp() {
        service = LibraryService(authorRepository, bookRepository, genreRepository, readerRepository)
    }

    @Test
    fun createAuthor_test() {
        /*
         * ИНСТРУКЦИЯ — основы every и verify:
         * 1. Создай автора Author(name = "...") — как будто его только что создали в коде сервиса.
         * 2. Создай «сохранённого» автора с id (например 42L) и тем же именем.
         * 3. Напиши: every { authorRepository.save(eq(тот_что_без_id)) } returns сохранённый_с_id
         * 4. Вызови service.createAuthor("то же имя")
         * 5. assertEquals по id и имени результата (JUnit — проверка возвращаемого значения)
         * 6. verify(exactly = 1) { authorRepository.save(eq(тот_что_без_id)) } — матчер eq в MockK
         */

        val saveAuthor = Author(name = "Abraham Lincoln")
        val dataAuthor = Author(id = 1488, name = "Abraham Lincoln")

        every { authorRepository.save(eq(saveAuthor)) } returns dataAuthor

        val result = service.createAuthor("Abraham Lincoln")

        assertEquals(dataAuthor.id, result.id)
        assertEquals(dataAuthor.name, result.name)

        verify(exactly = 1) { authorRepository.save(eq(saveAuthor)) }
    }

    @Test
    fun getAllGenres_returns_genreRepository() {
        /*
         * ИНСТРУКЦИЯ — стаб (заглушка) возвращает данные:
         * 1. Собери список из двух Genre(id, name).
         * 2. every { genreRepository.findAll() } returns этот_список
         * 3. Вызови service.getAllGenres()
         * 4. Проверь размер списка и имя первого жанра (assertEquals).
         * 5. verify { genreRepository.findAll() } — убедись, что метод вызывался.
         */

        val gl = listOf(Genre(id = 0, name = "Horror"), Genre(id = 1, name = "Detective"))

        every { genreRepository.findAll() } returns gl

        val result = service.getAllGenres()

        assertEquals(gl.size, result.size)
        assertEquals(gl[0].name, result[0].name)
        assertEquals(gl[0].id, result[0].id)

        verify(exactly = 1) { genreRepository.findAll() }

    }

    @Test
    fun createBook_EntityNotFoundException_test() {
        /*
         * ИНСТРУКЦИЯ — исключения и verify(exactly = 0):
         * 1. Настрой every { authorRepository.findById(eq(99L)) } returns Optional.empty()
         *    (в Kotlin: import java.util.Optional).
         * 2. Используй assertThrows(EntityNotFoundException::class.java) { service.createBook(...) }
         * 3. Проверь message у исключения (должен совпадать с текстом в LibraryService).
         * 4. verify(exactly = 1) { authorRepository.findById(eq(99L)) }
         * 5. verify(exactly = 0) { bookRepository.save(any()) } — книга не сохранялась.
         *    Для any() нужен импорт io.mockk.any (или звёздочка io.mockk.*).
         */

        every { authorRepository.findById(eq(111)) } returns Optional.empty()

        val ex = assertThrows(EntityNotFoundException::class.java) {
            service.createBook("TestBook", "qwerty", 111, 0)
        }

        assertEquals("Author not found with id: 111", ex.message)

        verify(exactly = 1) { authorRepository.findById(eq(111)) }
        verify(exactly = 0) { bookRepository.save(any()) }

    }

    @Test
    fun createBook_work_test() {
        /*
         * ИНСТРУКЦИЯ — slot и capture (поймать аргумент save):
         * 1. Создай author с id и genre с id.
         * 2. every { authorRepository.findById(eq(...)) } returns Optional.of(author)
         *    every { genreRepository.findById(eq(...)) } returns Optional.of(genre)
         * 3. Объяви val bookSlot = slot<Book>()
         * 4. every { bookRepository.save(capture(bookSlot)) } answers { bookSlot.captured.copy(id = 100L) }
         *    (или свой id — главное, чтобы answers вернул Book с id после save)
         * 5. Вызови service.createBook("название", "isbn", authorId, genreId)
         * 6. Проверь bookSlot.captured.title, isbn, author, genre
         * 7. Дополнительно: verify(exactly = 1) { bookRepository.save(match { it.title == "..." && it.isbn == "..." }) }
         */

        val author = Author(id = 0, name = "Abraham Lincoln")
        val genre = Genre(id = 2, name = "Detective")

        every{authorRepository.findById(eq(0))} returns Optional.of(author)
        every{genreRepository.findById(eq(2))} returns Optional.of(genre)

        val bookSlot = slot<Book>()

        every{bookRepository.save(capture(bookSlot))} answers {bookSlot.captured.copy(id = 100)}

        val result = service.createBook("TestBook2", "qwerty", 0, 2)

        assertEquals(100, result.id)
        assertEquals("TestBook2", bookSlot.captured.title)
        assertEquals("qwerty", bookSlot.captured.isbn)
        assertEquals(author, bookSlot.captured.author)

        verify(exactly = 1){bookRepository.save(any())}

    }

    @Test
    fun getBooksPage_bookRepository() {
        /*
         * ИНСТРУКЦИЯ — мок Page и точное совпадение аргумента:
         * 1. Создай минимум одну Book в списке (нужны author и genre для конструктора Book).
         * 2. Собери PageImpl(список, PageRequest.of(0, 20, Sort.by("title")), totalElements)
         *    (импорты из org.springframework.data.domain).
         * 3. Сохрани val pageRequest = PageRequest.of(0, 20, Sort.by("title")); every { bookRepository.findAll(eq(pageRequest)) } returns page
         * 4. Вызови service.getBooksPage(page = 0, size = 20)
         * 5. Проверь content.size и title первой книги (JUnit).
         * 6. verify(exactly = 1) { bookRepository.findAll(eq(pageRequest)) }
         */

        val author = Author(id = 1, name = "Abraham Lincoln")
        val book = Book(
            id = 100500,
            title = "TestBook",
            isbn = "qwerty",
            author = author
        )

        val pageRequest = PageRequest.of(0, 33, Sort.by("title"))
        val pageData = PageImpl(listOf(book), pageRequest, 1)

        every{bookRepository.findAll(eq(pageRequest))} returns pageData

        val result = service.getBooksPage(0, 33)

        assertEquals(1, result.content.size)
        assertEquals("TestBook", result.content[0].title)
        assertEquals("qwerty", result.content[0].isbn)
        assertEquals(author, result.content[0].author)

        verify(exactly = 1){bookRepository.findAll(eq(pageRequest))}
    }
}
