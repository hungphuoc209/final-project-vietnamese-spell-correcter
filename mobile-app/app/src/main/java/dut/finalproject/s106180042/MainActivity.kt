package dut.finalproject.s106180042

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dut.finalproject.s106180042.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initListener()
    }

    private fun initListener() {
        binding.viewNewSentences.root.setOnClickListener {
            val intent = Intent(this, AddSentencesActivity::class.java)
            startActivity(intent)
        }
        binding.viewPhoto.root.setOnClickListener {
            val intent = Intent(this, PreviewActivity::class.java)
            intent.putExtra("type",0)
            startActivity(intent)
        }
        binding.viewGallery.root.setOnClickListener {
            val intent = Intent(this, PreviewActivity::class.java)
            intent.putExtra("type",1)
            startActivity(intent)
        }
    }
}