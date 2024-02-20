package be.vives.vivesplus.screens.traffic.bus

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import be.vives.vivesplus.R
import be.vives.vivesplus.util.PreferencesManager

class BusWebViewFragment : Fragment() {

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View =  inflater.inflate(R.layout.fragment_bus_web_view, container, false)
        val webview = view.findViewById(R.id.webView) as WebView
        val url = PreferencesManager().getStringFromPreferences(requireContext(), requireContext().getString(R.string.selected_bus_id))
        webview.loadUrl(url!!)

        val webSettings = webview.settings
        webSettings.javaScriptEnabled = true
        webview.webViewClient = WebViewClient()

        return view
    }
}