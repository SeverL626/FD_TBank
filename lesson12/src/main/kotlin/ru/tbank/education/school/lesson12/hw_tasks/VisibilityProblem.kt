package ru.tbank.education.school.lesson12.hw_tasks
import java.util.concurrent.atomic.AtomicBoolean

/**
 *
 * Проблема:
 * При оптимизации компилятор и процессор могут переупорядочивать операции
 * или кешировать переменные в регистрах процессора. Это приводит к тому,
 * что изменения переменной в одном потоке могут быть не видны в другом потоке.
 *
 */

class VisibilityProblem {

    private val running = AtomicBoolean(true)

    /**
     * Создает и возвращает поток writer.
     * Поток выполняет некоторую работу, затем меняет флаг running на false.
     * Изменение может быть не видно потоку reader из-за проблем с видимостью.
     */
    fun startWriter(): Thread {
        return Thread {
            repeat(100) {
                Thread.sleep(10)
                Thread.yield()
            }

            running.set(false)
            println("Writer: установил running = false (изменение может быть не видно)")
        }
    }

    /**
     * Создает и возвращает поток reader.
     * Поток читает флаг running в цикле и может зависнуть навсегда,
     * если не увидит изменение running = false.
     */

    fun startReader(): Thread {
        return Thread {
            println("Reader: начал работу (ждет running = false)")

            while (running.get()) {
                Thread.sleep(0)
            }

            println("Reader: завершил работу (увидел running = false)")
        }
    }
}