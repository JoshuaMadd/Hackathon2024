# Login
Het **login** gedeelte bestaat regelt de volledige login sequence met Ku leuven.

## LoginFragment
###### be.vives.vivesplus.screens.login.LoginFragment

Fragment dat de webview host die de ku leuven login weergeeft.

##### Super klassen en interfaces
| Klasse / interface  | Omschrijving |
|:------------|:-------------|
| Fragment | Zie android docs |

##### Variabelen
| Variabele   | type   | Omschrijving |
|:------------|:-------|:-------------|
| binding | FragmentLoginBinding | Binding verantwoordelijk voor het manipuleren van de view |
| binding.loginWebView | WebView | WebView waar ku leuven login in weergegeven wordt |

##### onCreateView
In de **onCreateView** wordt de **loginWebView** ingesteld, als eerste staan we javascript toe en
voegen we een custom javascript interface toe.

```kotlin
binding.loginWebView.settings.javaScriptEnabled = true
binding.loginWebView.addJavascriptInterface(MyJavaScriptInterface(context!!), "HtmlViewer")
```

Als volgende wordt de WebViewClient aangemaakt voor de **loginWebView**. De belangrijkste methode hier is de  
**onPageFinished**. Deze zal iederkeer als de huidige webpagina in de WebView veranderd getriggerd worde. Via de  
url kunnen we dan controleren op welke pagina we ons bevinden, zodra we zien da we op een url de begint met
*"${context.getString(R.string.vivesplus_base_link)}/login/oauth2/code/kul/"* zijn beland is de login sequence doorlopen en triggeren we de
javascript interface om het jwt token uit deze pagina te halen en op te slaan.

Hierna navigeren we naar het **FieldOFStudyFragment** zodat de gebruiker zijn studiegebied kan selecteren.

```kotlin
        binding.loginWebView.webViewClient = object : WebViewClient() {
            override fun onReceivedError(view: WebView, errorCode: Int, description: String, failingUrl: String) {
                //Toast.makeText(activity, description, Toast.LENGTH_SHORT).show()
                println(description)
            }

            @TargetApi(Build.VERSION_CODES.M)
            override fun onReceivedError(view: WebView, req: WebResourceRequest, err: WebResourceError) { // Redirect to deprecated method, so you can use it in all SDK versions
                onReceivedError(view, err.errorCode, err.description.toString(), req.url.toString())
            }

            override fun onPageFinished(view: WebView, url: String) {
                if(url.startsWith("${context.getString(R.string.vivesplus_base_link)}/login/oauth2/code/kul/")) {
                    view.loadUrl("javascript:HtmlViewer.showHTML" +
                            "('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>');")
                    binding.root.findNavController().navigate(R.id.action_loginFragment_to_fieldOfStudyFragment)
                }
                super.onPageFinished(binding.loginWebView, url)
                
            }
        }
```

Als laatste wordt de start url op de WebView ingesteld.

##### class MyJavaScriptInterface

Deze innerklasse is de javascript interface die het jwt token uit de pagina zal gaan halen, de pagina geeft ons html
terug met daarin het token, vandaar dat (op een beetje een slordige manier) we de html string die we terug krijgen gaan substringen naar
enkel het jwt token en dit dan gaan opslaan in de local storage.


