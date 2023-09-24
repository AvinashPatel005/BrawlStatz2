package com.kal.brawlstatz2.broadcast

import android.annotation.SuppressLint
import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.content.FileProvider

class UpdateBroadcastReceiver:BroadcastReceiver() {
    private lateinit var downloadManager: DownloadManager
    @SuppressLint("Range")
    override fun onReceive(context: Context?, intent: Intent?) {
        downloadManager = context?.getSystemService(DownloadManager::class.java)!!
        if(intent?.action==DownloadManager.ACTION_DOWNLOAD_COMPLETE){
            context.unregisterReceiver(this)
            val id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID,-1L)
            val query = DownloadManager.Query().setFilterById(id)
            val cursor = downloadManager.query(query)

            if (cursor.moveToFirst()){
                val status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))
                when (status) {
                    DownloadManager.STATUS_SUCCESSFUL ->{
                        if(id!=-1L) {
                            val file = context.getExternalFilesDir("update/update.apk")
                            val contentUri = file?.let {
                                FileProvider.getUriForFile(context,"com.kal.brawlstatz2.fp",it)
                            }
                            val updateInstall = Intent(Intent.ACTION_VIEW)
                                .setDataAndType(contentUri,"application/vnd.android.package-archive")
                            updateInstall.flags =Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_ACTIVITY_NEW_TASK
                            context.startActivity(updateInstall)
                        }
                    }
                    DownloadManager.STATUS_FAILED -> {
                        //Later
                    }
                }
            }
        }
    }
}