package ru.tbank.education.school.lesson9

import java.io.File
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

private val DATA = File("F:/FD_TBank/lesson9/src/main/kotlin/ru/tbank/education/school/lesson9/data/logs")
private val SOL = File("F:/FD_TBank/lesson9/src/main/kotlin/ru/tbank/education/school/lesson9/solutions")


/* t1 — ошибки */
fun t1() {
    val output = File(SOL, "errors.txt")
    output.printWriter().use { out ->
        DATA.resolve("logs").walkTopDown()
            .filter { it.isFile }
            .forEach { file ->
                file.readLines().forEach { line ->
                    if ("ERROR" in line) out.println(line)
                }
            }
    }
}

/* t2 — архивируем old */
fun t2() {
    val archived = DATA.resolve("archived").apply { mkdirs() }
    DATA.resolve("logs/old").listFiles()?.forEach {
        it.renameTo(File(archived, it.name))
    }
}

/* t3 — размер logs */
fun t3() {
    val size = DATA.resolve("logs").walkTopDown().filter { it.isFile }.sumOf { it.length() }
    File(SOL, "logs_size.txt").writeText("$size bytes")
}

/* t4 — самый большой лог */
fun t4() {
    val biggest = DATA.resolve("logs").listFiles()?.filter { it.isFile }?.maxByOrNull { it.length() }
    File(SOL, "biglog.txt").writeText(biggest?.name ?: "")
}

/* t5 — количество логов */
fun t5() {
    val count = DATA.resolve("logs").walkTopDown().count { it.isFile && it.extension == "log" }
    File(SOL, "log_count.txt").writeText(count.toString())
}

/* t6 — строки с host в конфиге */
fun t6() {
    val out = File(SOL, "host_params.txt")
    out.printWriter().use { writer ->
        DATA.resolve("config").walkTopDown()
            .filter { it.isFile && it.extension == "conf" }
            .forEach { f -> f.readLines().forEach { l -> if ("host" in l) writer.println(l) } }
    }
}

/* t7 — бэкап конфигов */
fun t7() {
    zipFiles(File(SOL, "config_backup.zip"),
        DATA.resolve("config").walkTopDown().filter { it.isFile }.toList(),
        DATA)
}

/* t8 — общий бэкап проекта */
fun t8() {
    val files = mutableListOf<File>()
    files += DATA.resolve("config").walkTopDown().filter { it.isFile && it.extension == "conf" }
    files += DATA.resolve("logs").walkTopDown().filter { it.isFile && it.extension == "log" }
    val errors = File(SOL, "errors.txt")
    if (errors.exists()) files += errors
    zipFiles(File(SOL, "project_backup.zip"), files, DATA)
}

/* t9 — очистка пустых строк */
fun t9() {
    val input = DATA.resolve("logs/app.log")
    val output = File(SOL, "cleaned_app.log")
    input.readLines().filter { it.isNotBlank() }.let { output.writeText(it.joinToString("\n")) }
}

/* t10 — строки в каждом конфиге */
fun t10() {
    val out = File(SOL, "conf_stats.txt")
    out.printWriter().use { writer ->
        DATA.resolve("config").listFiles { f -> f.isFile && f.extension == "conf" }
            ?.forEach { writer.println("${it.name} ${it.readLines().size}") }
    }
}

/* вспомогательная zip-функция */
fun zipFiles(zipFile: File, files: List<File>, base: File) {
    ZipOutputStream(zipFile.outputStream()).use { zip ->
        files.forEach { f ->
            zip.putNextEntry(ZipEntry(f.relativeTo(base).path))
            f.inputStream().copyTo(zip)
            zip.closeEntry()
        }
    }
}


fun main() {

    if (!DATA.exists()) {
        println("Папка data не найдена: ${DATA.absolutePath}")
        return
    }

    t1()
    t2()
    t3()
    t4()
    t5()
    t6()
    t7()
    t8()
    t9()
    t10()

    // выводим количество ошибок как пример
    val errors = File(SOL, "errors.txt")
    println("Количество ошибок: ${if (errors.exists()) errors.readLines().size else 0}")
}
