package ru.netology.androidhw3

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import ru.netology.androidhw3.adapter.OnInteractionListener
import ru.netology.androidhw3.adapter.PostsAdapter
import ru.netology.androidhw3.databinding.ActivityMainBinding
import ru.netology.androidhw3.dto.Post

class MainActivity : AppCompatActivity() {

    private val viewModel: PostViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding
    val editPostLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                result.data?.let { data ->
                    val postId = data.getLongExtra("POST_ID", -1)
                    val updatedContent = data.getStringExtra("POST_CONTENT") ?: return@let
                    if (postId != -1L) {
                        viewModel.changeContentandSave(updatedContent)
                    }
                }
            } else if (result.resultCode == RESULT_CANCELED) {
                viewModel.resetEditPost()
            }
        }

    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val adapter = PostsAdapter(object : OnInteractionListener {
            override fun onLike(post: Post) {
                viewModel.likePost(post.id)
            }

            override fun onEdit(post: Post) {
                viewModel.editPost(post)
                val intent = Intent(this@MainActivity, EditPostActivity::class.java).apply {
                    putExtra("POST_ID", post.id)
                    putExtra("POST_CONTENT", post.content)
                }
                editPostLauncher.launch(intent)
            }

            override fun onRemove(post: Post) {
                viewModel.removeById(post.id)
            }

            override fun repostPost(post: Post) {
                val intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, post.content)
                    type = "text/plain"
                }
                val sharedIntent =
                    Intent.createChooser(intent, getString(R.string.chooser_share_post))
                startActivity(sharedIntent)
            }

        })

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter
        viewModel.data.observe(this) { posts ->

            adapter.submitList(posts)
        }
        val newPostLauncher = registerForActivityResult(NewPostResultContract()) { content ->
            content ?: return@registerForActivityResult
            viewModel.changeContentandSave(content)
            viewModel.save()

        }



        binding.fab.setOnClickListener {
            newPostLauncher.launch(Intent(this, NewPostActivity::class.java))
        }
    }
}
