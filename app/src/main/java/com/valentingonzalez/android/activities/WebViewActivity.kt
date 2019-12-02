package com.valentingonzalez.android.activities

import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.valentingonzalez.android.R


class WebViewActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)
        val url = "https://github.com/ErwinValentin/KotlinTodo"
        val wv = findViewById<WebView>(R.id.web_view_container)
        wv.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                view?.loadUrl(url)
                return true
            }
        }
        //wv.getSettings().setJavaScriptEnabled(true);
        wv.loadUrl(url);
    }

}