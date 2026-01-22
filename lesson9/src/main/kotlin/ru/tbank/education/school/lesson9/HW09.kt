package ru.tbank.education.school.lesson9

import java.io.*
import java.util.ArrayDeque
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

fun zip_maker(
    input_path: String,
    output_path: String,
    allowed_format_list: List<String> = listOf("txt", "log")
): File? {

    val inp = File(input_path)
    if (!inp.exists() || !inp.isDirectory) {
        println("E | Ошибка: путь $input_path не существует или не является каталогом")
        return null
    }

    val out = File(output_path)
    var zip: ZipOutputStream? = null

    try {
        zip = ZipOutputStream(BufferedOutputStream(FileOutputStream(out)))

        val queue = ArrayDeque<Array<String>>()
        queue.add(arrayOf(inp.absolutePath, ""))

        while (queue.isNotEmpty()) {
            val current = queue.removeFirst()
            val cf = File(current[0])
            val path_in_zip = current[1]

            val files = cf.listFiles()
            if (files != null) {
                for (f in files) {
                    val sub_path = if (path_in_zip.isEmpty()) f.name else path_in_zip + "/" + f.name

                    if (f.isDirectory) {
                        queue.add(arrayOf(f.absolutePath, sub_path))
                    } else if (allowed_format_list.contains(f.extension.lowercase())) {
                        try {
                            val fis = FileInputStream(f)
                            zip.putNextEntry(ZipEntry(sub_path))

                            val bf = ByteArray(1024)
                            var bf_reader = fis.read(bf)

                            while (bf_reader > 0) {
                                zip.write(bf, 0, bf_reader)
                                bf_reader = fis.read(bf)
                            }

                            zip.closeEntry()
                            fis.close()
                            println("S | Добавлен: $sub_path (${f.length()} bytes)")
                        } catch (e: IOException) {
                            println("E | Ошибка при добавлении файла $sub_path: ${e.message}")
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
