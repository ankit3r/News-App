package com.gyanhub.newsapp.activity

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.gyanhub.newsapp.databinding.ActivityNewsDetailBinding

class NewsDetail : AppCompatActivity() {
    private lateinit var binding: ActivityNewsDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewsDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar2)
        title = intent.getStringExtra("title").toString()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        // THIS METHODE CALL USE TO LOAD WEB PAGE
        webPage(intent.getStringExtra("url").toString())

    }

    // THIS METHODE USE TO LOAD WEB PAGE
    @SuppressLint("SetJavaScriptEnabled")
    private fun webPage(urls:String){
        binding.webPage.webViewClient = MyWebViewClient()
        binding.webPage.loadUrl(urls)
        binding.webPage.settings.setSupportZoom(true)
        binding.webPage.settings.javaScriptEnabled = true
    }

    // THIS OVERRIDE METHODE USE TO SHOW BACK BUTTON ON TOOLBAR
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    // THIS OVERRIDE METHODE USE TO HANDLE BACK PRESS
    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if (binding.webPage.canGoBack()) {
            binding.webPage.goBack()
        } else {
            super.onBackPressed()
        }
    }

    // THIS INNER CLASS USE TO MANAGE WEB VIEW CLIENT
    inner class MyWebViewClient : WebViewClient() {
        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)
           binding.progressBar2.visibility = View.VISIBLE
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            binding.progressBar2.visibility = View.GONE
    }
    }
}