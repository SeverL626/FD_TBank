package ru.tbank.education.school.lesson9

import java.io.*
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

fun zip_maker(
    input_path: String,
    output_path: String,
    allowed_format_list: List<String> = listOf("txt", "log")
): File? {

    val inp = File(input_path)
    if (!inp.exists() || !inp.isDirectory) {
        println("E | Ошибка: путь $input_path не существует")
        return null
    }

    val out = File(output_path)
    var zip: ZipOutputStream? = null

    try {
        zip = ZipOutputStream(BufferedOutputStream(FileOutputStream(out)))

        val queue = ArrayDeque<Array<String>>() // [путь, путь внутри архива]
        queue.add(arrayOf(inp.absolutePath, ""))

        while (queue.isNotEmpty()) {
            val current_file_path = queue.removeFirst()
            val currentFile = File(current_file_path[0])
            val pathInZip = current_file_path[1]

            val files = currentFile.listFiles()

            if (files != null) {
                for (f in files) { // обычный цикл по массиву
                    val entryPath = if (pathInZip.isEmpty()) f.name else pathInZip + "/" + f.name

                    if (f.isDirectory) {
                        queue.add(arrayOf(f.absolutePath, entryPath))
                    } else if (allowed_format_list.contains(f.extension.lowercase())) {
                        try {
                            val fis = FileInputStream(f)
                            val entry = ZipEntry(entryPath)
                            zip.putNextEntry(entry)
                            fis.copyTo(zip, 1024)
                            zip.closeEntry()
                            fis.close()
                            println("S | Добавлен: $entryPath (${f.length()} bytes)")
                        } catch (e: IOException) {
                            println("E | Ошибка при добавлении файла $entryPath: ${e.message}")
                        }
                    }
                }
            }
        }

        println("S | Архивирование завершено: ${out.absolutePath} (${out.length()} bytes)")
        return out

    } catch (e: IOException) {
        println("E | Ошибка при создании архива: ${e.message}")
        return null
    } finally {
        try {
            zip?.close()
        } catch (e: IOException) {
            println("E | Ошибка при закрытии архива: ${e.message}")
        }
    }
}





// Демонстрация работы

fun main() {

    // путь к файлам для архивации
    val sourceDir = "F:/FD_TBank/lesson9/src/main/kotlin/ru/tbank/education/school/lesson9/test_for_zip"

    // путь к будущему архиву
    val outputZip = "F:/FD_TBank/lesson9/src/main/kotlin/ru/tbank/education/school/lesson9/solutions/test_data_archive.zip"

    zip_maker(sourceDir, outputZip)
}
