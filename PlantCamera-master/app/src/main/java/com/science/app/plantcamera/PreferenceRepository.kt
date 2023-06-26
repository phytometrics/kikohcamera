package com.science.app.plantcamera

import android.content.Context
import android.content.SharedPreferences

class PreferenceRepository(val context: Context) {
    // アプリ設定識別子
    private val APP_CONFIG_NAME = "AppConfig"
    // KEY:ファイルPrefix
    private val KEY_FILE_PREFIX = "FilePrefix"
    // KEY:撮影回数
    private val KEY_PHOTO_COUNT = "PhotoCount"

    // ファイルPrefix
    private var filePrefix = ""
    // 撮影回数
    private var photoCount = 1

    // ファイルPrefix取得/設定
    fun getFilePrefix() : String {
        return filePrefix
    }
    fun setFilePrefix(prefix:String) {
        filePrefix = prefix
    }

    // 撮影回数取得/設定/加算
    fun getPhotoCount() : Int {
        return photoCount
    }
    fun setPhotoCount(count: Int) {
        photoCount = count
    }
    fun incrementPhotoCount() {
        photoCount++
    }

    // アプリ設定の読み込み
    fun load() {
        val preferences = context.getSharedPreferences(APP_CONFIG_NAME, Context.MODE_PRIVATE)
        // ファイルPrefix
        val prefix = preferences.getString(KEY_FILE_PREFIX, filePrefix)
        prefix?.let { filePrefix = it }
        // 撮影回数
        photoCount = preferences.getInt(KEY_PHOTO_COUNT, photoCount)
    }

    // アプリ設定の書き込み
    fun save() {
        val preferences = context.getSharedPreferences(APP_CONFIG_NAME, Context.MODE_PRIVATE)
        val editor = preferences.edit()
        // ファイルPrefix
        editor.putString(KEY_FILE_PREFIX, filePrefix)
        // 撮影回数
        editor.putInt(KEY_PHOTO_COUNT, photoCount)

        // 書き込み
        editor.apply()
    }


}