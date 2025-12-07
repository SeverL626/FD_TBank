data class Transaction(
    val fromId: String,
    val toId: String,
    val amount: Double,
    val timestamp: String
)

fun solve(transactions: List<Transaction>, k: Int, t: Int): List<List<String>> {
    // Ваше решение
    return emptyList()
}

fun main() {
    val (n, k, t) = readln().split(" ").map { it.toInt() }

    val transactions = List(n) {
        val parts = readln().split(" ")
        Transaction(
            parts[0],
            parts[1],
            parts[2].toDouble(),
            parts[3]
        )
    }

    val results = solve(transactions, k, t)

    if (results.isEmpty()) {
        println("OK")
    } else {
        results.forEach { println(it.joinToString(" ")) }
    }
}