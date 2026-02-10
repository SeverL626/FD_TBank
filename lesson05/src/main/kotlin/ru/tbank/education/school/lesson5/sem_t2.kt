package ru.tbank.education.school.lesson5

import ru.tbank.education.school.lesson5.Person

data class Book(
    val title: String,
    val author: String,
    val year: Int,
    val genre: String
)

data class Person(
    val name: String,
    val age: Int
)

class Library {
    private val books = mutableListOf<ru.tbank.education.school.lesson5.Book>()
    private val people = mutableListOf<ru.tbank.education.school.lesson5.Person>()
    private val takenBooks = mutableMapOf<ru.tbank.education.school.lesson5.Book, ru.tbank.education.school.lesson5.Person>() // Кто какую книгу взял

    // Добавляет книгу в библиотеку
    fun addBook(book: ru.tbank.education.school.lesson5.Book) {
        // todo: добавить книгу в список books
        books.add(book)
    }

    // Добавляет человека в список посетителей
    fun addPerson(person: ru.tbank.education.school.lesson5.Person) {
        // todo: добавить человека в список people
        people.add(person)
    }

    // Возвращает список всех доступных книг (не взятых)
    fun getAvailableBooks(): List<ru.tbank.education.school.lesson5.Book> {
        // todo: вернуть только те книги, которые НЕ находятся в takenBooks
        return books.filter{!takenBooks.containsKey(it)}

    }

    // Возвращает список книг определённого автора
    fun getBooksByAuthor(author: String): List<ru.tbank.education.school.lesson5.Book> {
        // todo: вернуть книги, у которых автор совпадает с переданным (игнорируя регистр)
        return books.filter{it.author.lowercase() == author.lowercase()}
    }

    // Возвращает список книг определённого жанра
    fun getBooksByGenre(genre: String): List<ru.tbank.education.school.lesson5.Book> {
        // todo: вернуть книги, у которых жанр совпадает с переданным (игнорируя регистр)
        return books.filter{it.genre.lowercase() == genre.lowercase()}
    }

    // Человек берёт книгу по названию
    fun takeBook(personName: String, bookTitle: String): Boolean {
        // todo:
        // 1. Найти человека по имени
        // 2. Найти книгу по названию
        // 3. Проверить, что книга существует и доступна (её нет в takenBooks)
        // 4. Если всё в порядке — добавить запись в takenBooks и вернуть true
        // 5. Иначе — вернуть false

        val book = books.find{it.title.equals(bookTitle, ignoreCase = true)}
        val person = people.find{it.name.equals(personName, ignoreCase = true)}

        if ((!takenBooks.containsKey(book)) && person != null && book != null) {
            takenBooks[book] = person
            return true
        } else {
            return false
        }
    }

    // Возвращает список всех посетителей
    fun getAllPeople(): List<ru.tbank.education.school.lesson5.Person> {
        // todo: вернуть копию списка people
        return people.toList()
    }

    // Возвращает книгу, которую взял человек (по имени)
    fun getBooksTakenByPerson(personName: String): List<ru.tbank.education.school.lesson5.Book> {
        // todo: вернуть список книг, которые взял человек с указанным именем
        return takenBooks.filterValues{it.name.equals(personName, ignoreCase = true)}.keys.toList()
    }

    // Возвращает информацию о том, кто взял конкретную книгу
    fun getPersonWhoTookBook(bookTitle: String): ru.tbank.education.school.lesson5.Person? {
        // todo: найти книгу по названию и вернуть человека, который её взял (или null)
        try {
            val book = books.find{it.title.equals(bookTitle, ignoreCase = true)}
            return takenBooks[book]
        } catch (exception: Exception) {
            return null
        }
    }
}

//Пример использования:
fun main() {
    val library = _root_ide_package_.ru.tbank.education.school.lesson5.Library()

    // Добавляем книги
    library.addBook(
        _root_ide_package_.ru.tbank.education.school.lesson5.Book(
            "Война и мир",
            "Лев Толстой",
            1869,
            "Роман"
        )
    )
    library.addBook(
        _root_ide_package_.ru.tbank.education.school.lesson5.Book(
            "Преступление и наказание",
            "Фёдор Достоевский",
            1866,
            "Роман"
        )
    )
    library.addBook(
        _root_ide_package_.ru.tbank.education.school.lesson5.Book(
            "Мастер и Маргарита",
            "Михаил Булгаков",
            1967,
            "Фантастика"
        )
    )

    // Добавляем людей
    library.addPerson(_root_ide_package_.ru.tbank.education.school.lesson5.Person("Анна", 25))
    library.addPerson(_root_ide_package_.ru.tbank.education.school.lesson5.Person("Иван", 30))

    // Проверяем доступные книги
    println("Доступные книги: ${library.getAvailableBooks().map { it.title }}")

    // Берём книгу
    val success = library.takeBook("Анна", "Мастер и Маргарита")
    println("Книга взята: $success")

    // Проверяем, кто взял книгу
    val person = library.getPersonWhoTookBook("Мастер и Маргарита")
    println("Книгу 'Мастер и Маргарита' взял: ${person?.name}")

    // Проверяем, какие книги взял человек
    println("Анна взяла: ${library.getBooksTakenByPerson("Анна").map { it.title }}")

    // Проверяем доступные книги после взятия
    println("Доступные книги: ${library.getAvailableBooks().map { it.title }}")

    // Книги по жанру
    println("Книги в жанре 'Роман': ${library.getBooksByGenre("Роман").map { it.title }}")
}