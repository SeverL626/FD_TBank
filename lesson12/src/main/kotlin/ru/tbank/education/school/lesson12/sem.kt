package ru.tbank.education.school.lesson12

import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicInteger

// Task1
object CreateThreads {
    fun run(): List<Thread> {
        val threadNames = listOf("Thread-A", "Thread-B", "Thread-C")
        val threads = threadNames.map{ name ->
            Thread({
                repeat(5) {
                    println(name)
                    Thread.sleep(500)
                }
            }, name)
        }

        threads.forEach{it.start()}
        return threads
    }
}


// Task2
object RaceCondition {
    fun run(): Int {
        var counter = 0
        val threads = (1..10).map{
            Thread({
                repeat(1000) {
                    counter++
                }
            })
        }
        threads.forEach{it.start()}
        threads.forEach{it.join()}
        return counter
    }
}


// Task3.1
object SynchronizedCounter {
    fun run(): Int {
        var counter = 0
        val lock = Any()
        val threads = (1..10).map{
            Thread({
                repeat(1000) {
                    synchronized(lock) {
                        counter++
                    }
                }
            })
        }
        threads.forEach{it.start()}
        threads.forEach{it.join()}
        return counter
    }
}
// Task3.2
object RaceCondition_v2 {
    fun run(): Int {
        val counter = AtomicInteger(0)
        val threads = (1..10).map{
            Thread({
                repeat(1000) {
                    counter.incrementAndGet()
                }
            })
        }
        threads.forEach{it.start()}
        threads.forEach{it.join()}
        return counter.get()
    }
}

// Task4
object Deadlock {
    private val resourceA = Any()
    private val resourceB = Any()
    fun runDeadlock() {
        val t1 = Thread {
            synchronized(resourceA) {
                println("Поток 1 взял A")
                synchronized(resourceB) {
                    println("Поток 1 взял B")
                }
            }
        }

        val t2 = Thread {
            synchronized(resourceB) {
                println("Поток 2 взял B")
                synchronized(resourceA) {
                    println("Поток 2 взял A")
                }
            }
        }

        t1.start()
        t2.start()
        t1.join()
        t2.join()
    }

    fun runFixed() {
        val t1 = Thread {
            synchronized(resourceA) {
                println("Поток 1 взял A")
            }
            synchronized(resourceB) {
                println("Поток 1 взял B")
            }
        }

        val t2 = Thread {
            synchronized(resourceB) {
                println("Поток 2 взял B")
            }
            synchronized(resourceA) {
                println("Поток 2 взял A")
            }
        }

        t1.start()
        t2.start()
        t1.join()
        t2.join()
    }
}


// Task5
object ExecutorServiceExample {
    fun run() {
        val thread = Executors.newFixedThreadPool(4)
        for (i in 1..20) {
            thread.submit {
                println("Task $i in thread ${Thread.currentThread().name}")
                Thread.sleep(200)
            }
        }
        thread.shutdown()
    }
}


/*
// Task6
object FutureFactorial {
    fun run(): Map<Int, BigInteger> {
        TODO()
    }
}


// Task7
object CoroutineLaunch {
    fun run(): List<String> = runBlocking {
        TODO()
    }
}


// Task8
object AsyncAwait {
    fun run(): Long = runBlocking {
        TODO()
    }
}


// Task9
object StructuredConcurrency {
    fun run(failingCoroutineIndex: Int): Int = runBlocking {
        TODO()
    }
}


// Task10
object WithContextIO {
    fun run(filePaths: List<String>): Map<String, String> = runBlocking {
        TODO()
    }
}
*/



fun main() {
    // CreateThreads.run()
    // for (x in 0 until 100) { println(RaceCondition.run()) } // иногда проскакивает варианты < 10000 -> Race Condition
    // println(SynchronizedCounter.run())
    // println(RaceCondition_v2.run())
    // Deadlock.runDeadlock()
    // Deadlock.runFixed()
    ExecutorServiceExample.run()
}