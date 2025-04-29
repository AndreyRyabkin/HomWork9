package ru.netology.androidhw3

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

import ru.netology.androidhw3.databinding.ActivityEditPostBinding

class EditPostActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditPostBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditPostBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val postId = intent.getLongExtra("POST_ID", -1)
        val postContent = intent.getStringExtra("POST_CONTENT") ?: ""
        binding.editText.setText(postContent)
        binding.saveButton.setOnClickListener {
            val updatedContent = binding.editText.text.toString()
            val resultIntent = Intent().apply {
                putExtra("POST_ID", postId)
                putExtra("POST_CONTENT", updatedContent)
            }
            setResult(RESULT_OK, resultIntent)
            finish()
        }
        binding.cancelButton.setOnClickListener {
            setResult(RESULT_CANCELED)
            finish()
        }
    }
}
