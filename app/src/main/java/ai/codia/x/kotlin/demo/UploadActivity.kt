package ai.codia.x.kotlin.demo

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.ImageView
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import org.json.JSONObject
import java.io.File
import java.io.IOException
import java.io.InputStream

class UploadActivity : AppCompatActivity() {

    private lateinit var imageView: ImageView;
    private val PICK_IMAGE_REQUEST = 1;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload)

        imageView = findViewById(R.id.uploadImage)

        // ค้นหา button4 (reset button) และตั้งค่าให้ทำการ reload หน้านี้เมื่อกดปุ่ม
        val resetButton: Button = findViewById(R.id.button4)
        resetButton.setOnClickListener {
            // รีโหลดหน้านี้ใหม่
//            recreate()
            openImagePicker()
        }
    }

    private fun openImagePicker() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/* "
        startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE_REQUEST)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            val imageUri: Uri? = data?.data
            if (imageUri != null) {
                // Display the selected image in the ImageView
                println("imageURi :$imageUri")
                println("data?.data: " + data.data)

                sendRequestToPredict(imageUri) { result ->
                    println("At onActivityResult:" + result)

                    // predict result is "result" variable

                    TODO("Show on app")
                }
            }
        }
    }

    @SuppressLint("Recycle")
    fun sendRequestToPredict(uri: Uri, callback: (String?) -> Unit) {

        val contentResolver: ContentResolver = contentResolver;
        val inputStream: InputStream? = contentResolver.openInputStream(uri)
        val imageFile = inputStream?.readBytes()
        if(imageFile == null) {
            return
        }
        predict(imageFile, callback)
    }

    interface PredictCallback {
        fun onSuccess(result: String)
        fun onError(error: Exception)
    }

    fun predict(file: ByteArray, callback: (String?) -> Unit) {

        val body = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart(
                "file",
                "image.jpeg",
                RequestBody.create("image/jpg".toMediaTypeOrNull(), file)
            )
            .build()

        val request = Request.Builder()
            .url("http://10.0.2.2:8000/predict/")
            .post(body)
            .build()

        val client = OkHttpClient()
        client.newCall(request).enqueue(object: Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
                callback(null)
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!response.isSuccessful) throw IOException("Unexpected code $response")
                    val _json = JSONObject(response.body?.string()?: "Error")
                    val result = _json.getJSONObject("data").getString("result")

//                    println("Body: "+ _json.getString("data"))
                    println("result: "+ result)

                    callback(result)

                }
            }
        })
    }
}
