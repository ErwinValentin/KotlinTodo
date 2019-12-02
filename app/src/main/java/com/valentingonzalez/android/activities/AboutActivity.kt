package com.valentingonzalez.android.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.valentingonzalez.android.R

class AboutActivity : AppCompatActivity() {
    /*TODO standard about activity, might link to repo
        must add credits to any external library used
    * */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)
    }

    fun openRepo(view: View) {
        val intent = Intent(this,WebViewActivity::class.java)
        startActivity(intent)
    }
}