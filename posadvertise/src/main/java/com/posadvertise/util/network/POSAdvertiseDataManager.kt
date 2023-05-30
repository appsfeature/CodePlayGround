package com.posadvertise.util

import android.content.Context
import com.helper.util.BasePrefUtil
import com.helper.util.FileUtils
import com.helper.util.UnzipUtils
import com.posadvertise.POSAdvertise
import com.posadvertise.POSAdvertiseCallback
import com.posadvertise.logAdv
import kotlinx.coroutines.*
import java.io.File
import java.io.IOException


object POSAdvertiseDataManager {
    fun downloadLocally(context: Context, callback: POSAdvertiseCallback.Callback<Boolean>?) {
        CoroutineScope(Dispatchers.IO).launch {
            val task = async { extractLocalFiles(context) }
            task.await().apply {
                POSAdvertise.resetPOSAdvertiseProperty(context)
                callback?.onSuccess(this)
            }
        }
    }

    private fun extractLocalFiles(context: Context): Boolean {
        val downloadZipFile = File(
            createDirectory(POSFileManager.getPOSDownloadsFolder(context)),
            POSAdvertiseConstants.AdvertiseFileName
        )
        val assetsFile = POSFileManager.getAssetsFile(context, POSAdvertiseConstants.AdvertiseFileName)
        FileUtils.saveAssetsToStorage(assetsFile, downloadZipFile)
        return extractFiles(context)
    }

    fun downloadUpdate(context: Context, downloadFileUrl : String, callback: POSAdvertiseCallback.Download<Boolean>?) {
        val fileName = POSAdvertiseConstants.AdvertiseFileName
        val downloadedFilePath = File(POSFileManager.getPOSDownloadsFolder(context), fileName)
        HTTPSDownloadManager(
            downloadFileUrl,
            downloadedFilePath,
            fileName,
            object : HTTPSDownloadManager.Callback {
                override fun onDownloadComplete(path: String, appName: String, fileUri: File?) {
                    CoroutineScope(Dispatchers.IO).launch {
                        val task = async { extractFiles(context) }
                        launch(Dispatchers.Main) {
                            task.await().apply {
                                POSAdvertise.resetPOSAdvertiseProperty(context)
                                POSAdvertisePreference.setUpdateDownloaded(context, this)
                                callback?.onSuccess(this)
                            }
                        }
                    }
                }

                override fun onProgressUpdate(progress: Int) {
                    callback?.onProgressUpdate(progress)
                }

                override fun onError(e: Exception) {
                    callback?.onFailure(e)
                }
            }).execute()
    }

    private fun extractFiles(context: Context): Boolean {
        try {
            //existing folder rename
            val downloadsFolder = createDirectory(POSFileManager.getPOSDownloadsFolder(context))
            val posAdvertiseFolder = POSFileManager.getPOSAdvertiseFolder(context)
            val mFolderRename = File(downloadsFolder, "${POSAdvertiseConstants.AdvertiseFolder}-Old")
            if(posAdvertiseFolder.exists()){
                posAdvertiseFolder.renameTo(mFolderRename)
            }
            val downloadZipFile = File(
                POSFileManager.getPOSDownloadsFolder(context),
                POSAdvertiseConstants.AdvertiseFileName
            )
            if (downloadZipFile.exists()) {
                UnzipUtils.unzip(downloadZipFile, downloadsFolder)
                downloadZipFile.delete()
            }
            //delete renamed folder
            if(mFolderRename.exists()){
                mFolderRename.deleteRecursively()
            }
            copyFiles(context)
            readFiles(context)
            return true
        } catch (e: Exception) {
            logAdv(e.toString())
            return false
        }
    }

    private fun copyFiles(context: Context) {
        val configBanner = createDirectory(POSFileManager.getPOSBannerFolder(context))
        val configScreenSaver = createDirectory(POSFileManager.getPOSScreenSaverFolder(context))
        val configTutorial = createDirectory(POSFileManager.getPOSTutorialFolder(context))
        val mainFolder = POSFileManager.getPOSAdvertiseFolder(context)
        if(mainFolder.exists()){
            val folders = mainFolder.listFiles()
            folders?.let {
                for (folder in it){
                    when(folder.name){
                        "Banner" -> {
                            copyFiles(configBanner, folder.listFiles())
                        }
                        "ScreenSaver" -> {
                            copyFiles(configScreenSaver, folder.listFiles())
                        }
                        "Tutorial" -> {
                            copyFiles(configTutorial, folder.listFiles())
                        }
                    }
                }
            }
            mainFolder.deleteRecursively()
        }
    }

    @Throws(IOException::class)
    private fun readFiles(context: Context) {
        val mainFolder = POSFileManager.getPOSDownloadsFolder(context)
        if(mainFolder.exists()){
            val configBanner = File(POSFileManager.getPOSBannerFolder(context), "config_banner.json")
            val configScreenSaver = File(POSFileManager.getPOSScreenSaverFolder(context), "config_screen_saver.json")
            val configTutorial = File(POSFileManager.getPOSTutorialFolder(context), "config_tutorial.json")

            if(configBanner.exists()){
                val configJson = POSFileManager.readFile(configBanner)
                POSAdvertisePreference.setConfigBannerJson(context, configJson)
            }
            if(configScreenSaver.exists()){
                val configJson = POSFileManager.readFile(configScreenSaver)
                POSAdvertisePreference.setConfigScreenSaverJson(context, configJson)
            }
            if(configTutorial.exists()){
                val configJson = POSFileManager.readFile(configTutorial)
                POSAdvertisePreference.setConfigTutorialJson(context, configJson)
            }
        }
    }

    private fun createDirectory(file: File): File {
        file.run {
            if (!exists()) {
                mkdirs()
            }
        }
        return file
    }


    private fun copyFiles(targetFilePath: File, files: Array<File>?) {
        files?.let {
            for (file in it){
                val targetFile = File(targetFilePath, file.name)
                if(targetFile.exists()){
                    targetFile.delete()
                }
                file.copyRecursively(targetFile, true)
            }
        }
    }


}