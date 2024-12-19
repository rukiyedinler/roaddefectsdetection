package com.rukiyedinler.roaddefectsdetection.view
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.rukiyedinler.roaddefectsdetection.data.User
import com.rukiyedinler.roaddefectsdetection.databinding.ActivityUserHomeBinding

class UserHomeActivity : AppCompatActivity(){

        private lateinit var binding: ActivityUserHomeBinding
        private lateinit var user: User // User nesnesi için değişken

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            binding = ActivityUserHomeBinding.inflate(layoutInflater)
            setContentView(binding.root)

            user = intent.getParcelableExtra("user") ?: return // 'user' nesnesini alıyoruz

            setupListeners()
        }

        private fun setupListeners() {
            // feedbackButton'a tıklanıldığında LoginActivity'e yönlendirme
            binding.feedbackButton.setOnClickListener {
                val user = intent.getParcelableExtra<User>("user")
                // DefectReportActivity'ye geçiş
                val intent = Intent(this, DefectReportActivity::class.java)
                intent.putExtra("user", user) // User nesnesini ekliyoruz
                startActivity(intent) // DefectReportActivity'e geçiş başlatılıyor
            }
    }

}