package ru.tbank.education.school.lesson12

import kotlinx.coroutines.*
import kotlin.random.*
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicInteger
import java.math.BigInteger
import java.io.File

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


// Task6
fun factorial(n: Int): BigInteger {
    var r = BigInteger.ONE
    for (i in 1..n) {
        r *= i.toBigInteger()
    }
    return r
}
object FutureFactorial {
    fun run(): Map<Int, BigInteger> {
        val thread = Executors.newFixedThreadPool(4)
        val res = mutableMapOf<Int, BigInteger>()

        val tasks = (1..10).map { n ->
            n to thread.submit<BigInteger> {
                factorial(n)
            }
        }

        for ((n, future) in tasks) {
            res[n] = future.get()
        }

        thread.shutdown()
        return res
    }
}


// Task7
object CoroutineLaunch {
    fun run() = runBlocking {
        val names = listOf("A", "B", "C")
        val jobs = names.map { n ->
            launch {
                repeat(5) {
                    delay(500)
                    println(n)
                }
            }
        }
        jobs.joinAll()
    }
}


// Task8
object AsyncAwait {
    fun run(): Long = runBlocking {
        val n1 = 250_000L
        val n2 = 2 * n1
        val n3 = 3 * n1
        val n4 = 4 * n1

        val job1 = async { (1L..n1).sum() }
        val job2 = async { (n1 + 1..n2).sum() }
        val job3 = async { (n2 + 1..n3).sum() }
        val job4 = async { (n3 + 1..n4).sum() }

        job1.await() + job2.await() + job3.await() + job4.await()
    }
}


// Task9
object StructuredConcurrency {
    fun run(failingCoroutineIndex: Int) = runBlocking {
        try {
            coroutineScope {
                (1..5).forEach { i ->
                    launch {
                        val iDelay = Random.nextLong(100, 1000)
                        delay(iDelay)

                        if (i == failingCoroutineIndex) {
                            error("Task $i went wrong (predict time - ${iDelay}ms)")
                        } else {
                            println("Task $i completed in ${iDelay}ms")
                        }
                    }
                }
            }
        } catch (e: Exception) {
            println("${e.message}\nCancelling all tasks...")
        }
    }
}


// Task10
object WithContextIO {
    fun run(fileNames: List<String>): String = runBlocking {
        val res = mutableMapOf<String, String>()
        val path = "F:\\FD_TBank\\lesson12\\src\\main\\kotlin\\ru\\tbank\\education\\school\\lesson12\\tablets"

        val jobDeferred = fileNames.map { name ->
            async(Dispatchers.IO) {
                try {
                    println("Start $name")
                    val content = File("$path\\$name").readText()
                    println("End $name")
                    content
                } catch (e: Exception) {
                    println("ERROR $name: ${e.message}")
                    ""
                }
            }
        }
        "\nTOTAL:\n"+jobDeferred.awaitAll().joinToString("\n")
    }
}




fun main() {
    // CreateThreads.run()
    // for (x in 0 until 100) { println(RaceCondition.run()) } // иногда проскакивает варианты < 10000 -> Race Condition
    // println(SynchronizedCounter.run())
    // println(RaceCondition_v2.run())
    // Deadlock.runDeadlock()
    // Deadlock.runFixed()
    // ExecutorServiceExample.run()
    // println(FutureFactorial.run())
    // CoroutineLaunch.run()
    // println(AsyncAwait.run())
    // StructuredConcurrency.run(Random.nextInt(1, 6))
    // println(WithContextIO.run(listOf("file1.txt", "file2.txt", "file3.txt"))
}