package be.vives.vivesplus.screens.webview

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import be.vives.vivesplus.R
import be.vives.vivesplus.databinding.WebviewBinding

class WebViewFragment : Fragment() {

    private lateinit var binding: WebviewBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.webview, container, false)
        binding.lifecycleOwner = this

        val args = WebViewFragmentArgs.fromBundle(
            requireArguments()
        )
        val site = args.link

        webViewSetup(site)

        return binding.root
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun webViewSetup(site: String) {
        binding.webview.webViewClient = WebViewClient()
        binding.webview.apply {
            loadUrl(site)
            settings.javaScriptEnabled = true
            settings.safeBrowsingEnabled = true
        }
    }
}