package com.rukiyedinler.roaddefectsdetection.view
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.rukiyedinler.roaddefectsdetection.databinding.ActivityUserHomeBinding

class UserHomeActivity : AppCompatActivity(){

        private lateinit var binding: ActivityUserHomeBinding

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            binding = ActivityUserHomeBinding.inflate(layoutInflater)
            setContentView(binding.root)

            setupListeners()
        }

        private fun setupListeners() {
            // feedbackButton'a tıklanıldığında LoginActivity'e yönlendirme
            binding.feedbackButton.setOnClickListener {
                val intent = Intent(this, DefectReportActivity::class.java) // LoginActivity hedef sınıf
                startActivity(intent) // LoginActivity'e geçiş başlatılıyor
            }

        }

}