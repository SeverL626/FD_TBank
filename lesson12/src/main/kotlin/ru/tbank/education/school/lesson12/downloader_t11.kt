package ru.tbank.education.school.lesson12

import kotlinx.coroutines.*
import java.io.File
import java.net.URL
import java.util.concurrent.atomic.AtomicInteger
import kotlin.system.measureTimeMillis

data class DownloadStats(
    val total: Int,
    val success: Int,
    val failed: Int,
    val time: Long
)

object ImageDownloader {

    fun run(url: String, n: Int, outputDir: String, stats: Boolean): DownloadStats = runBlocking {

        val downloads = AtomicInteger(0)
        val failed = AtomicInteger(0)

        File(outputDir)

        val time = measureTimeMillis {

            val jobs = (1..n).map { i ->
                async(Dispatchers.IO) {
                    try {

                        val file = File(outputDir, "image_$i.jpg")
                        file.writeBytes(URL(url).readBytes())

                        downloads.incrementAndGet()
                        println("image_$i.jpg downloaded")

                    } catch (e: Exception) {

                        failed.incrementAndGet()
                        println("Error on image_$i.jpg: ${e.message}")

                    }
                }
            }
            jobs.awaitAll()
        }

        val ds = DownloadStats(
            total = n,
            success = downloads.get(),
            failed = failed.get(),
            time = time
        )

        if (stats) {
            println("\nThe stat:")
            println("Total: ${ds.total}")
            println("Success: ${ds.success}")
            println("Failed: ${ds.failed}")
            println("Time: ${ds.time}ms")
        }

        ds
    }
}

fun main() {
    ImageDownloader.run(
        url = "https://picsum.photos/200/300",
        n = 10,
        outputDir = "tablets/images",
        stats = true
    )
}