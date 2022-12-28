package dut.finalproject.s106180042

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import dut.finalproject.s106180042.databinding.ActivityAddSentencesBinding


@AndroidEntryPoint
class AddSentencesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddSentencesBinding

    private val viewModel by viewModels<CheckingViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddSentencesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initListener()
        initObservers()
    }

    private fun initListener() {
        binding.tvCheck.movementMethod = ScrollingMovementMethod()
        binding.edtRaw.setText(intent.getStringExtra("text_key") ?: "")
        binding.btnChecking.setOnClickListener {
            if (!binding.edtRaw.text.isNullOrEmpty()) {
                viewModel.checkText(binding.edtRaw.text.toString().trim())
            }
        }
    }

    private fun initObservers() {
        viewModel.textResult.observe(this) {
            binding.tvCheck.text = it
        }
    }
}