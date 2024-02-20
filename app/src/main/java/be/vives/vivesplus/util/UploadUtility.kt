package be.vives.vivesplus.util

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.net.Uri
import android.util.Log
import android.webkit.MimeTypeMap
import android.widget.Toast
import be.vives.vivesplus.R
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class UploadUtility(var activity: Activity, val context: Context) {

    var dialog: ProgressDialog? = null
    var serverURL: String = "${context.getString(R.string.vivesplus_api_link)}/members/me/photo"
    val client = OkHttpClient()

    fun uploadFile(sourceFilePath: String, uploadedFileName: String? = null) {
        uploadFile(File(sourceFilePath), uploadedFileName)
    }

    fun uploadFile(sourceFileUri: Uri, uploadedFileName: String? = null) {
        val pathFromUri = URIPathHelper().getPath(activity,sourceFileUri)
        uploadFile(File(pathFromUri), uploadedFileName)
    }

    fun uploadFile(sourceFile: File, uploadedFileName: String? = null) {
        Thread {
            var mimeType = getMimeType(sourceFile)
            if (mimeType == null) {
                   mimeType = "/jpeg"
            }

           val extension = "." + mimeType.substringAfter("/")

            val fileName: String = uploadedFileName ?: sourceFile.name
            toggleProgressDialog(true)
            try {
                val requestBody: RequestBody =
                    MultipartBody.Builder().setType(MultipartBody.FORM)
                        .addFormDataPart("file", fileName + extension,sourceFile.asRequestBody(mimeType.toMediaTypeOrNull()))
                        .build()
                val request: Request = Request.Builder().url(serverURL).post(requestBody).addHeader("Authorization", PreferencesManager().getStringFromPreferences(context, "jwt_token")!!).build()


                val response: Response = client.newCall(request).execute()
            } catch (ex: Exception) {
                ex.printStackTrace()
                Log.e("File upload", "s")
            }
            toggleProgressDialog(false)
        }.start()
    }

    fun getMimeType(file: File): String? {
        var type: String? = null
        val extension = MimeTypeMap.getFileExtensionFromUrl(file.path)
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
        }
        return type
    }

    fun toggleProgressDialog(show: Boolean) {
        activity.runOnUiThread {
            if (show) {
                dialog = ProgressDialog.show(activity, "", "Uploading file...", true);
            } else {
                dialog?.dismiss();
            }
        }
    }

}