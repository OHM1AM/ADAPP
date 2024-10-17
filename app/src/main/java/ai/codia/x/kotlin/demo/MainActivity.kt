package ai.codia.x.kotlin.demo

import android.content.Intent
import android.os.Bundle
import android.widget.Button

class MainActivity : BaseMainActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_codia_root) // ต้องแน่ใจว่าเชื่อมโยง Layout ที่ถูกต้อง

        // ค้นหา Button และจัดการเมื่อถูกกด
        val buttonGoToFruitDetection = findViewById<Button>(R.id.button2)
        buttonGoToFruitDetection.setOnClickListener {
            // เปิดหน้าต่อไป (UploadActivity)
            val intent = Intent(this, UploadActivity::class.java)
            startActivity(intent)
        }
    }
    override fun onViewCreated() {
        // You can define your custom logic here, or leave it empty if not needed
    }
}
