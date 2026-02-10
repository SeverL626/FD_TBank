package backend.fd.seminar4.homework.motivation.exercises.task2

/**
 * Задача 2: Цена ошибки
 *
 * Вы разрабатываете систему онлайн-банкинга.
 * Напишите код для перевода денег между счетами и определите критичные места.
 */

data class BankAccount(
    val id: String,
    var balance: Double
)

class BankingSystem {
    private val accounts = mutableMapOf(
        "ACC001" to BankAccount("ACC001", 1000.0),
        "ACC002" to BankAccount("ACC002", 500.0)
    )

    fun getAccount(accountId: String): BankAccount? {
        return accounts[accountId]
    }

    fun updateBalance(accountId: String, newBalance: Double) {
        accounts[accountId]?.balance = newBalance
    }

    fun logTransaction(from: String, to: String, amount: Double) {
        println("Transaction: $from -> $to, amount: $amount")
    }
}

/**
 * TODO: Реализуйте функцию transferMoney
 *
 * Требования:
 * 1. Снять деньги со счета отправителя
 * 2. Зачислить деньги на счет получателя
 * 3. Залогировать операцию
 *
 * Затем:
 * 4. Определите 3 самых критичных места, где может произойти ошибка
 * 5. Опишите последствия каждой ошибки
 * 6. Добавьте обработку ошибок для критичных мест
 */

fun transferMoney(
    system: BankingSystem,
    fromAccountId: String,
    toAccountId: String,
    amount: Double
) {
    // TODO: Реализуйте здесь
    // Подсказка: что будет если:
    // - fromAccount не существует?
    // - недостаточно средств?
    // - toAccount не существует?
    // - операция упадет между снятием и зачислением?
}

/**
 * Критичные места:
 * 1. Место: ...
 *    Последствия: ...
 *
 * 2. Место: ...
 *    Последствия: ...
 *
 * 3. Место: ...
 *    Последствия: ...
 */

fun transferMoneyWithErrorHandling(
    system: BankingSystem,
    fromAccountId: String,
    toAccountId: String,
    amount: Double
): Result<Unit> {
    // TODO: Реализуйте безопасную версию с обработкой ошибок
    return Result.success(Unit)
}

fun main() {
    val system = BankingSystem()

    println("=== Начальное состояние ===")
    println("ACC001: ${system.getAccount("ACC001")?.balance}")
    println("ACC002: ${system.getAccount("ACC002")?.balance}")

    println("\n=== Тест перевода ===")
    // transferMoney(system, "ACC001", "ACC002", 200.0)

    println("\n=== Безопасный перевод ===")
    val result = transferMoneyWithErrorHandling(system, "ACC001", "ACC002", 200.0)
    println("Результат: ${if (result.isSuccess) "Успех" else "Ошибка: ${result.exceptionOrNull()?.message}"}")
}
