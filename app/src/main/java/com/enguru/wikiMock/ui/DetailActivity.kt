package com.enguru.wikiMock.ui

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import android.view.View
import com.enguru.wikiMock.R
import kotlinx.android.synthetic.main.activity_detail.progress_bar
import kotlinx.android.synthetic.main.activity_detail.web_view

/**
 * This Activity loads the URL of the selected article in a webView
 */
class DetailActivity : AppCompatActivity() {

    private var url: String? = null

    companion object {
        const val KEY_URL = "URL"
        fun newInstance(context: Context?, url: String): Intent {
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra(KEY_URL, url)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        url = intent.getStringExtra(KEY_URL)
        setContentView(R.layout.activity_detail)
        setWebView()
    }

    /**
     * this method sets the web view url and implements the [WebViewClient] for handling
     * 1. URL loading within the application
     * 2. restricting redirects
     * 3. showing progress bar when the content is loading
     */
    private fun setWebView() {
        progress_bar.visibility = View.VISIBLE
        web_view.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                if (url == this@DetailActivity.url) {
                    view.loadUrl(url)
                } else {
                    Toast.makeText(
                        this@DetailActivity,
                        getString(R.string.redirect_restriction_msg),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                return true
            }

            override fun onPageFinished(view: WebView, url: String) {
                progress_bar.visibility = View.GONE
            }
        }
        url?.run {
            web_view.loadUrl(this)
        }
    }
}
