package com.hasht.exoplayerexample

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.PackageManagerCompat.LOG_TAG
import androidx.lifecycle.Observer
import androidx.work.*


class MainActivity : AppCompatActivity() {

    private lateinit var workManager: WorkManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        workManager = WorkManager.getInstance(applicationContext)

        startOneTimeWorkManager()
    }

    private fun startOneTimeWorkManager() {

//        URLS can be passed as bundle in activity
        val files = arrayOf(
            "https://file-examples-com.github.io/uploads/2017/04/file_example_MP4_640_3MG.mp4",
            "https://www.learningcontainer.com/wp-content/uploads/2020/05/sample-mp4-file.mp4"
        )


        val constraints =
            androidx.work.Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

        val tasks = ArrayList<WorkRequest>()

        files.forEach {
            val data = Data.Builder()
            data.putString(DownLoadFileWorkManager.FILE_URL, it)
            val task = OneTimeWorkRequest.Builder(DownLoadFileWorkManager::class.java)
                .setConstraints(constraints).setInputData(data.build()).build()

            workManager.getWorkInfoByIdLiveData(task.id)
                .observe(this@MainActivity, Observer {
                    it?.let {

                        if (it.state == WorkInfo.State.RUNNING) {
                            //task running, you can update UI
                            Log.e("workManager", "task running, you can update UI")
                        } else if (it.state.isFinished) {
                            // task finished you can notify to Views
                            Log.e("workManager", "task finished you can notify to Views")

                            Log.e("workManager", "WorkInfo received: state: " + it.state)
                            val message: String? =
                                it.outputData.getString(DownLoadFileWorkManager.KEY_MESSAGE)
                            // get the file path of downloaded video
                            Log.e("workManager", "message: $message")

                            // You can put a LocalBroadCast here to notify you of the downloaded video
                        }
                    }
                })
            tasks.add(task)
        }

        workManager.enqueue(tasks)


    }
}