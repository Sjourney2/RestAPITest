package com.example.restapitestapplication.model

import android.util.Log
import com.google.gson.*
import okhttp3.*
import org.json.JSONObject
import java.io.IOException
import java.util.concurrent.TimeUnit


class RestAPIModel {

    // 通信のタイムアウト時間を設定(10sec)
    private val CONNECTION_TIMEOUT_MILLISECONDS = 10L
    private val READ_TIMEOUT_MILLISECONDS = 10L

    // 通信用クライアント生成
    private val url = "https://api.github.com/search/repositories"

    /**
     * 検索用文字列からクエリパラメータを生成する.
     */
    fun makeQueryParameter(searchStrings: String): String? {
        Log.d("makeQueryParameter", "Start")
        val queryString = "?q="
        val queryParameter = StringBuffer()
        // 検索文字列が入力されていない場合は処理を行わない
        if (searchStrings.isBlank()) { return null }
        val searchStringList = searchStrings.split("[\\s]*,[\\s]*")
        searchStringList.forEach { queryParameter.append(queryString + it) }
        return queryParameter.toString()
    }

    /**
     * APIと通信を行う.
     */
    fun startTransaction(queryParameter: String): List<String> {
        Log.d("startTransaction", "Start")
        var result = mutableListOf<String>()
        val request = Request.Builder()
            .url(url + queryParameter)
            .build()
        val client = OkHttpClient.Builder()
            .connectTimeout(CONNECTION_TIMEOUT_MILLISECONDS, TimeUnit.SECONDS)
            .readTimeout(READ_TIMEOUT_MILLISECONDS, TimeUnit.SECONDS)
            .build()
        try {
            val responseJson = JSONObject(client.newCall(request).execute().body!!.string()).getJSONArray("items")
            for (i in 1..responseJson.length()) {
                result.add(responseJson.getJSONObject(i).getString("name"))
            }
        } catch (ioe: IOException) {
            Log.e("IO Exception", ioe.toString())
        } catch (jpe: JsonParseException) {
            Log.e("Json Parse Exception", jpe.toString())
        } catch (e: Exception) {
            Log.e("Unexpected Exception", e.toString())
        }
        return result
    }

}

