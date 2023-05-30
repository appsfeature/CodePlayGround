package com.posadvertise.util

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import com.helper.util.BasePrefUtil
import com.helper.util.FileUtils
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.InputStream

object POSFileManager {

    fun getUriFromFile(context: Context, file: File): Uri {
        val fileProvider = context.packageName + context.getString(com.posadvertise.R.string.file_provider)
        return FileProvider.getUriForFile(context, fileProvider, file)
    }

    fun getPOSDownloadsFolder(context: Context): File {
        return getFileStoreDirectory(context)
    }

    private fun getFileStoreDirectory(context: Context): File {
        return FileUtils.getFileStoreDirectory(context)
    }

    fun getPOSAdvertiseFolder(context: Context): File {
        return File(getFileStoreDirectory(context) , POSAdvertiseConstants.AdvertiseFolder)
    }
    fun getPOSBannerFolder(context: Context): File {
        return File(getFileStoreDirectory(context) , "/Banner/")
    }
    fun getPOSScreenSaverFolder(context: Context): File {
        return File(getFileStoreDirectory(context) , "/ScreenSaver/")
    }
    fun getPOSTutorialFolder(context: Context): File {
        return File(getFileStoreDirectory(context) , "/Tutorial/")
    }

    fun readFile(file: File): String {
        try {
            val fileReader = FileReader(file)
            val bufferedReader = BufferedReader(fileReader)
            val stringBuilder = StringBuilder()
            var line = bufferedReader.readLine()
            while (line != null) {
                stringBuilder.append("${line}\n")
                line = bufferedReader.readLine()
            }
            bufferedReader.close()
            return stringBuilder.toString()
        }catch (e : java.lang.Exception){
            return ""
        }
    }

    fun getAssetsFile(context: Context, fileName: String): InputStream? {
        try {
            return context.assets.open(fileName)
        } catch (e: Exception) {
            return null
        }
    }
}