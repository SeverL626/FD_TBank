fun solve(banned: List<String>, posts: List<String>): List<String> {

    val result = MutableList(posts.size){"ALLOWED"}
    val banl = banned.map { it.lowercase() }.toSet()

    for ((i, p) in posts.withIndex()) {
        val w = p.split(Regex(" ")).map{it.lowercase()}
        if (w.any{it in banl}) {
            result[i] = "BLOCKED"
        }
    }
    return result
}

fun main() {
    val n = readln().trim().toInt()
    val banned = List(n) { readln() }

    val m = readln().trim().toInt()
    val posts = List(m) { readln() }

    val results = solve(banned, posts)
    results.forEach { println(it) }
}