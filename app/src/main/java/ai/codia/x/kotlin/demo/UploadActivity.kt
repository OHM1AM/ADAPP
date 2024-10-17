package ai.codia.x.kotlin.demo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button

class UploadActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload)

        // ค้นหา button4 (reset button) และตั้งค่าให้ทำการ reload หน้านี้เมื่อกดปุ่ม
        val resetButton: Button = findViewById(R.id.button4)
        resetButton.setOnClickListener {
            // รีโหลดหน้านี้ใหม่
            recreate()
        }
    }
}
