package ru.tbank.education.school.lesson12.hw_tasks

import kotlinx.coroutines.*

/**
 * Задание: Параллельное преобразование элементов списка с использованием async.
 *
 * Преобразуйте каждый элемент списка в отдельной корутине с помощью async.
 *
 * @param items список элементов для преобразования
 * @param transform функция преобразования
 * @return список преобразованных элементов в исходном порядке
 */

suspend fun <T, R> parallelTransform(
    items: List<T>,
    transform: suspend (T) -> R
): List<R> = coroutineScope {
    val jobs = items.map { item ->
        async {
            transform(item)
        }
    }
    jobs.awaitAll()
}