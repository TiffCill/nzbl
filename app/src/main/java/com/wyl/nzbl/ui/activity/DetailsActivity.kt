package com.wyl.nzbl.ui.activity

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import com.wyl.nzbl.R
import com.wyl.nzbl.view.MyLoadingView

class DetailsActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        val webView = findViewById<WebView>(R.id.web_view)
        webView.settings.javaScriptEnabled = true
        webView.settings.setSupportZoom(true)
        webView.settings.builtInZoomControls = true
        val loadingView = findViewById<MyLoadingView>(R.id.loading_view)
        val url = intent.getStringExtra("url")
        if (null != url && !"".contains(url)){
            loadingView.visibility = View.GONE
        }
        url?.let { webView.loadUrl(it) }
    }
}