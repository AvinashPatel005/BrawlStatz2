package com.kal.brawlstatz2.downloader

import android.app.DownloadManager
import android.content.Context
import android.net.Uri

class UpdateDownloader{
    fun download(url: String, context: Context): Long {
        val files = context.getExternalFilesDir("update")
        for (file in files?.listFiles()!!) {
            file.delete()
        }
        val downloadManager = context.getSystemService(DownloadManager::class.java)
        val request = DownloadManager.Request(Uri.parse(url))
            .setDestinationInExternalFilesDir(context, "update", "update.apk")
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setTitle("Downloading Update!")

        return downloadManager.enqueue(request)
    }
    fun cancelDownload(id:Long,context:Context){
        if(id!=-1L){
            val downloadManager = context.getSystemService(DownloadManager::class.java)
            downloadManager.remove(id)
        }
    }

}