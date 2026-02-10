fun solve(shots: List<Int>): String {
    if (shots.isEmpty()) {
        return "No data"
    }

    val s = shots.size
    val m1 = shots.maxOrNull()
    val m2 = shots.filter{it != m1}.maxOrNull()

    val result = StringBuilder()
    result.append("Total shots: $s \n")
    result.append("Best shot: $m1 \n")
    result.append("Second best shot: ${m2 ?: ""} \n")

    return result.toString()
}


fun main() {
    val shots = mutableListOf<Int>()

    while (true) {
        val line = readlnOrNull() ?: break
        val parts = line.split(" ")
        for (part in parts) {
            if (part.isNotBlank()) {
                shots.add(part.toInt())
            }
        }
    }

    val result = solve(shots)
    println(result)
}