package com.uncmorfi.reservations

import android.app.Dialog
import android.os.Bundle
import android.webkit.JavascriptInterface
import android.webkit.WebView
import androidx.fragment.app.Fragment
import com.uncmorfi.balance.dialogs.BaseDialogHelper
import com.uncmorfi.models.User

class CaptchaDialog: BaseDialogHelper() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.init()
        val webView = WebView(requireContext())
        webView.settings.javaScriptEnabled = true
        webView.settings.domStorageEnabled = true
        webView.settings.builtInZoomControls = false
        webView.addJavascriptInterface(JsObject(), "injectedObject")
        webView.loadDataWithBaseURL("http://comedor.unc.edu.ar",
                """
                <script src="https://www.google.com/recaptcha/api.js"></script>
                <div align="center">
                <div class="g-recaptcha"
                    data-sitekey="6Lek3bYUAAAAAFs57EBhdBdogWqs0nG1nbFVsR48"
                    data-callback="captchaResponse"
                ></div>
                </div>
                <script type="text/javascript">
                function captchaResponse(token){
                    injectedObject.callback(token);
                }
                </script>
                """.trimIndent(),
                "text/html",
                "UTF-8",
                null)

        builder.setView(webView)
        return builder.create()
    }

    internal inner class JsObject {
        @JavascriptInterface
        fun callback(g_response: String) {
            viewModel.reserve(user, g_response)
            dismiss()
        }
    }

    companion object {
        fun newInstance(fragment: Fragment, code: Int, user: User): CaptchaDialog {
            return newInstance(::CaptchaDialog, fragment, code, user)
        }
    }

}