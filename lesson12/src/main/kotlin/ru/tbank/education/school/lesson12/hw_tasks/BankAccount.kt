package ru.tbank.education.school.lesson12.hw_tasks

/**
 *
 * В данном классе `BankAccount` реализован метод `transfer()` для перевода денег
 * с одного счета на другой. Однако в текущей реализации присутствует
 * **дедлок (deadlock)** - ситуация взаимной блокировки потоков.
 *
 * Проблема:
 * Дедлок возникает при одновременном выполнении двух переводов в противоположных направлениях:
 *
 * Задание:
 * 1. Проанализируйте код и воспроизведите дедлок с помощью тестов
 * 2. Исправьте метод `transfer()` так, чтобы дедлок был невозможен
 * 3. Убедитесь, что все тесты проходят
 */

class BankAccount(val id: String, var balance: Int) {

    private val transferLock = Any()

    fun transfer(to: BankAccount, amount: Int) {

        val firstLock: BankAccount
        val secondLock: BankAccount

        if (this.id < to.id) {
            firstLock = this
            secondLock = to
        } else {
            firstLock = to
            secondLock = this
        }

        synchronized(firstLock.transferLock) {
            synchronized(secondLock.transferLock) {

                if (balance >= amount) {
                    balance -= amount
                    to.balance += amount
                }
            }
        }
    }
}