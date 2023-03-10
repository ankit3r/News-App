package com.gyanhub.newsapp.activity

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.net.http.SslError
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.webkit.*
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
        binding.webPage.settings.apply {
            mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
            domStorageEnabled = true
            setSupportZoom(true)
            javaScriptEnabled = true
            cacheMode = WebSettings.LOAD_DEFAULT
        }
        binding.webPage.webViewClient = object : WebViewClient() {
            @SuppressLint("WebViewClientOnReceivedSslError")
            override fun onReceivedSslError(view: WebView?, handler: SslErrorHandler?, error: SslError?) {
                handler?.proceed() // Ignore SSL certificate errors
                Log.d("ANKIT","SSL ERROR: $error")
            }

            override fun onReceivedError(view: WebView?, request: WebResourceRequest?, error: WebResourceError?) {
                // Handle other network errors
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                Log.d("ANKIT","uel: $url \nwebView: $view \nfavIcon: $favicon")
                super.onPageStarted(view, url, favicon)
                Log.d("ANKIT","loading")
                binding.progressBar2.visibility = View.VISIBLE
            }


            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                Log.d("ANKIT","loaded")
                binding.progressBar2.visibility = View.GONE
            }
        }
        binding.webPage.loadUrl(urls)
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





}