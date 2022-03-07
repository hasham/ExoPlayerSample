package com.hasht.exoplayerexample

import android.content.Context
import android.util.Log
import androidx.work.Data
import androidx.work.ListenableWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import java.io.BufferedInputStream
import java.io.File
import java.io.FileOutputStream
import java.net.URL

class DownLoadFileWorkManager(val context: Context, workerParams: WorkerParameters) :
    Worker(context, workerParams) {

    /**
     * Workmanager worker thread which do processing
     * in background, so it will not impact to main thread or UI
     *
     */


    companion object {

        val FILE_URL = "FILE_URL"
        val KEY_MESSAGE = "DownloadResult"

    }

    override fun doWork(): ListenableWorker.Result {
        try {

            val fileToDownload = inputData.getString(FILE_URL)

            if (fileToDownload != null) {


                val url = URL(fileToDownload)
                val connection = url.openConnection()
                connection.connect()
                // getting file length

                // input stream to read file - with 8k buffer
                val input = BufferedInputStream(url.openStream(), 8192)

                val fileName: String = fileToDownload.substring(fileToDownload.lastIndexOf('/') + 1)

                val filePath = context.cacheDir.absolutePath + "/${fileName}"
                // Output stream to write file
                val myFile = File(filePath)
                val output = FileOutputStream(myFile)


                val data = ByteArray(1024)

                var count: Int? = 0

                while ({ count = input.read(data);count }() != -1) {
                    output.write(data, 0, count!!)
                }

                // flushing output
                output.flush()

                // closing streams
                output.close()
                input.close()

                val outputData: Data = Data.Builder()
                    .putString(KEY_MESSAGE, myFile.absolutePath)
                    .build()

                Log.e("workManager", "file Path"+myFile.absolutePath)
                Log.e("workManager", "file Size"+myFile.length())

                return Result.success(outputData)
            }

        } catch (e: Exception) {
            print(e)
            return Result.retry()
        }

        return Result.failure()
    }
}