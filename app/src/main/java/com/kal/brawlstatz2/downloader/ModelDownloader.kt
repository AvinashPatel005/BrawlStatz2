package com.kal.brawlstatz2.downloader

import android.app.DownloadManager
import android.content.Context
import android.net.Uri

class ModelDownloader {
    fun download(url: String,name:String,version:String, context: Context):Long{
        val files = context.getExternalFilesDir("model/${name}/")
        for (file in files?.listFiles()!!) {
            file.delete()
        }
        val downloadManager = context.getSystemService(DownloadManager::class.java)
        val request = DownloadManager.Request(Uri.parse(url))
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN).setVisibleInDownloadsUi(false)
            .setDestinationInExternalFilesDir(context,"model/${name}/","${name}${version}.glb")
        return downloadManager.enqueue(request)
    }
}