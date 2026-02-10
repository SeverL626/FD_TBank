package ru.tbank.education.school.lesson12.hw_tasks

import kotlinx.coroutines.*

fun main() = runBlocking {

    println("\nBankAccount")
    val accountA = BankAccount("A", 1000)
    val accountB = BankAccount("B", 1000)

    val t1 = Thread { accountA.transfer(accountB, 700) }
    val t2 = Thread { accountB.transfer(accountA, 500) }

    t1.start()
    t2.start()

    t1.join()
    t2.join()

    println("A balance: ${accountA.balance}")
    println("B balance: ${accountB.balance}")
    println("upd: возникает racing error, но в рамках задачи я её не исправлял")



    println("\nVisibilityProblem")
    val visibility = VisibilityProblem()
    val writer = visibility.startWriter()
    val reader = visibility.startReader()

    reader.start()
    writer.start()

    writer.join()
    reader.join()



    println("\nCounter")
    val counter = counter()
    val finalValue = counter.runConcurrentIncrements(coroutineCount = 10, incrementsPerCoroutine = 1000)
    println("ans: $finalValue")



    println("\nParallelTransform")
    val numbers = listOf(1, 2, 3, 4, 5)
    val squares = parallelTransform(numbers) { n ->
        delay(100)
        n * n
    }
    println("ans: $squares")

}