fun solve(post: String): Boolean {
    return post.contains("!!!+".toRegex())
}

fun main() {
    val post = readln()
    val suspicious = solve(post)
    println(if (suspicious) "Suspicious" else "Normal")
}