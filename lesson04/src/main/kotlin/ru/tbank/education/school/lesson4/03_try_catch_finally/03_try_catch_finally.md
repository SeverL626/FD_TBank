# Домашнее задание 3: Try-Catch-Finally

## Задача 1: Правильное использование finally

Напишите функцию для работы с файлом:

```kotlin
fun processFile(filename: String): List<String> {
    // Ваш код:
    // 1. Открыть файл
    // 2. Прочитать строки
    // 3. Отфильтровать пустые строки
    // 4. Обязательно закрыть файл в finally
    // 5. Обработать ошибки чтения
}
```

**Требования**:

- Файл должен закрыться даже при ошибке
- Используйте явный finally блок
- Обработайте FileNotFoundException и IOException отдельно

## Задача 2: Порядок выполнения

Проанализируйте код:

```kotlin
fun mysteryFunction(): Int {
    try {
        println("Try")
        return 1
    } catch (e: Exception) {
        println("Catch")
        return 2
    } finally {
        println("Finally")
        return 3
    }
}
```

**Задание**:

- Что выведет функция?
- Какое значение вернется?
- Почему так происходит?
- Перепишите код так, чтобы он был корректным (finally не должен возвращать значение)

## Задача 3: use vs try-finally

Даны два варианта кода:

**Вариант A:**

```kotlin
fun readFirstLine(path: String): String {
    val reader = BufferedReader(FileReader(path))
    try {
        return reader.readLine()
    } finally {
        reader.close()
    }
}
```

**Вариант B:**

```kotlin
fun readFirstLine(path: String): String {
    return BufferedReader(FileReader(path)).use { reader ->
        reader.readLine()
    }
}
```

**Задание**:

- Объясните разницу между вариантами
- В чем преимущество use?
- Напишите свою функцию с использованием use для работы с базой данных (создайте заглушку Connection класса)
