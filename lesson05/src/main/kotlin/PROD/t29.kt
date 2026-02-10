data class Category(val id: Int, var parentId: Int)

data class Command(val type: String, val child: Int, val parent: Int = -1)

fun solve(categories: List<Category>, commands: List<Command>): List<Category> {

    val cats = categories.associateBy{it.id}.toMutableMap()

    for (i in commands) {
        when (i.type) {
            "ATTACH" -> {
                cats[i.child]?.parentId = i.parent
            }
            "DETACH" -> {
                cats[i.child]?.parentId = -1
            }
            "REMOVE" -> {
                val delID = i.child
                cats.values.forEach{
                    if (it.parentId == delID) {
                        it.parentId = -1
                    }
                }
                cats.remove(delID)
            }
        }
    }

    return cats.values.toList()
}

fun main() {
    val n = readln().toInt()

    val categories = mutableListOf<Category>()
    repeat(n) {
        val (id, parentId) = readln().split(" ").map { it.toInt() }
        categories.add(Category(id, parentId))
    }

    val m = readln().toInt()

    val commands = mutableListOf<Command>()
    repeat(m) {
        val parts = readln().split(" ")
        val type = parts[0]
        when (type) {
            "ATTACH" -> {
                val child = parts[1].toInt()
                val parent = parts[2].toInt()
                commands.add(Command(type, child, parent))
            }
            "DETACH" -> {
                val child = parts[1].toInt()
                commands.add(Command(type, child))
            }
            "REMOVE" -> {
                val category = parts[1].toInt()
                commands.add(Command(type, category))
            }
        }
    }

    val results = solve(categories, commands).sortedBy { it.id }

    println(results.size)
    results.forEach { println("${it.id} ${it.parentId}") }
}
