# Домашнее задание 5: Checked vs Unchecked исключения

## Задача 1: Анализ Java кода

Дан Java код:

```java
public String readFile(String path) throws IOException {
    FileReader reader = new FileReader(path);
    BufferedReader br = new BufferedReader(reader);
    return br.readLine();
}
```

**Задание**:

- Перепишите код на Kotlin
- Объясните, почему в Kotlin не нужно указывать throws
- Нужно ли обрабатывать IOException при вызове этой функции в Kotlin?
- Какие плюсы и минусы у такого подхода?

## Задача 2: Проектирование исключений

Вы разрабатываете библиотеку для работы с API. Определите для каждой ситуации, должно ли исключение быть checked или
unchecked (в концептуальном смысле):

1. Сетевой таймаут при запросе к серверу
2. Некорректный API ключ
3. Программист передал null вместо обязательного параметра
4. Сервер вернул 500 ошибку
5. JSON ответ имеет неожиданную структуру

**Задание**:

- Для каждой ситуации решите тип исключения
- Обоснуйте свой выбор
- Создайте иерархию исключений для этой библиотеки

## Задача 3: Миграция с Java

У вас есть Java интерфейс:

```java
public interface DataRepository {
    Data getData(String id) throws DataNotFoundException, DatabaseException;

    void saveData(Data data) throws ValidationException, DatabaseException;
}
```

**Задание**:

- Перепишите интерфейс на Kotlin
- Реализуйте класс, который имплементирует этот интерфейс
- Напишите функцию, которая использует этот репозиторий и обрабатывает все возможные ошибки
- Сравните количество кода в Java и Kotlin версиях
