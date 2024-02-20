package be.vives.vivesplus.screens.login

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.*
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import be.vives.vivesplus.MainActivity
import be.vives.vivesplus.R
import be.vives.vivesplus.databinding.FragmentLoginBinding
import be.vives.vivesplus.util.CheckerConnection
import be.vives.vivesplus.util.LocalFileManager
import be.vives.vivesplus.util.PreferencesManager

@Suppress("DEPRECATION")
class LoginFragment : Fragment() {

    private lateinit var endpointUrl: String
    lateinit var binding: FragmentLoginBinding

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)

        if(PreferencesManager().getStringFromPreferences(requireContext(), requireContext().getString(R.string.pref_jwt)).isNullOrBlank()){
            endpointUrl = "/mobile/login"
        } else {
            LocalFileManager(requireContext(), "").clearMyFiles()
            endpointUrl = "/signout?mobile=android"
        }

        if(CheckerConnection(requireContext()).hasInternetConnection()){
            binding.loginWebView.settings.javaScriptEnabled = true
            binding.loginWebView.addJavascriptInterface(MyJavaScriptInterface(requireContext()), "HtmlViewer")
            binding.loginWebView.webViewClient = object : WebViewClient() {

                @Deprecated("Deprecated in Java")
                override fun onReceivedError(view: WebView, errorCode: Int, description: String, failingUrl: String) {
                    println(description)
                }

                @TargetApi(Build.VERSION_CODES.M)
                override fun onReceivedError(view: WebView, req: WebResourceRequest, err: WebResourceError) {
                    onReceivedError(view, err.errorCode, err.description.toString(), req.url.toString())
                }

                override fun onPageFinished(view: WebView, url: String) {
                    if(url.contains("/mobile/jwt")) {
                        view.isVisible = false
                        view.loadUrl("javascript:HtmlViewer.showHTML" +
                                "('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>');")
                        navigateToMainActivity()
                    }
                    super.onPageFinished(binding.loginWebView, url)
                }
            }

            binding.loginWebView.loadUrl(getString(R.string.vivesplus_base_link) + endpointUrl)
        }
        else {
            this.findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToNoInternetFragment())
        }

        return binding.root
    }

    fun navigateToMainActivity() {
        val intent = Intent(requireContext(), MainActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }

    internal class MyJavaScriptInterface(c: Context) {
        private var ctx: Context = c

        @JavascriptInterface
        fun showHTML(html: String?) { // process the html as needed by the app
            println("$html")
            println( html?.substring(html.indexOf("id_token")))
            var token = html?.substring(html.indexOf("id_token")+11).toString()
            token = token.substring(0, token.indexOf("}")-1)
            println(token)
            PreferencesManager().writeStringToPreferences(ctx, ctx.getString(R.string.pref_jwt), "Bearer $token")
            PreferencesManager().writeStringToPreferences(ctx, ctx.getString(R.string.pref_jwt_without_beaerer), "$token")
        }
    }
}
