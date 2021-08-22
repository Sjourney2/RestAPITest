package com.example.restapitestapplication.activity

import android.R.attr.button
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.restapitestapplication.R
import com.example.restapitestapplication.fragment.ResultFragment
import com.example.restapitestapplication.model.RestAPIModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MainActivity: AppCompatActivity(){

    val scope = CoroutineScope(Dispatchers.Default)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        search_button.setOnSingleClickListener {
            val inputText = text_box?.text.toString()
            scope.launch {
                startBackGroundTask(inputText)
            }
        }
        cancel_button.setOnClickListener {
            text_box.editableText?.clear()
        }

    }

    private suspend fun startBackGroundTask(inputText: String) {
        var result = listOf<String>()
        val api = RestAPIModel()
        var queryParameter: String? = null

        try {
            // 検索用文字列の作成
            withContext(Dispatchers.Main) {
                queryParameter = api.makeQueryParameter(inputText)
                queryParameter?:{ throw Exception("Nothing is input in TextBox.") }
            }

            // 検索処理開始
            inputText.isNotBlank().let {
                result = api.startTransaction(queryParameter!!)
            }

            // onPostExecuteメソッドと同等の処理
            withContext(Dispatchers.Main) {
                val fragmentManager: FragmentManager = supportFragmentManager
                val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
                fragmentTransaction.addToBackStack("");
                fragmentTransaction.replace(R.id.container, ResultFragment().newInstance(result))
                fragmentTransaction.commit()
            }
        } catch (e: Exception) {
            Log.e("startBackGroundTask", e.toString())
        }
    }

    /**
     * 短時間での連打による複数回実行を防ぐ setOnClickListener 実装.
     *
     * @param listener setOnClickListener
     */
    fun View.setOnSingleClickListener(listener: () -> Unit) {
        val delayMillis = 1000 // 二度押しを防止する時間
        var pushedAt = 0L
        setOnClickListener {
            if (System.currentTimeMillis() - pushedAt < delayMillis) return@setOnClickListener
            pushedAt = System.currentTimeMillis()
            listener()
        }
    }

}