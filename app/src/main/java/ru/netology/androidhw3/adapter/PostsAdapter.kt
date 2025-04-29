package ru.netology.androidhw3.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.androidhw3.R
import ru.netology.androidhw3.databinding.CardPostBinding
import ru.netology.androidhw3.dto.Post
import ru.netology.androidhw3.format

interface OnInteractionListener {
    fun onLike(post: Post) {}
    fun onEdit(post: Post) {}
    fun onRemove(post: Post) {}
    fun repostPost(post: Post) {}
}

class PostsAdapter(
    private val onInteractionListener: OnInteractionListener
) : ListAdapter<Post, PostsViewHolder>(PostDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostsViewHolder {
        val binding = CardPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostsViewHolder(binding, onInteractionListener)
    }

    override fun onBindViewHolder(holder: PostsViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class PostsViewHolder(
    private val binding: CardPostBinding,
    private val onInteractionListener: OnInteractionListener
) : RecyclerView.ViewHolder(binding.root) {

    private fun openVideo(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        if (intent.resolveActivity(binding.root.context.packageManager) != null) {
            binding.root.context.startActivity(intent)
        } else {
            Toast.makeText(binding.root.context, "Нет доступных приложений для открытия видео", Toast.LENGTH_SHORT).show()
        }
    }

    fun bind(post: Post) {
        with(binding) {
            author.text = post.author
            published.text = post.published
            content.text = post.content


            if (!post.video.isNullOrEmpty()) {
                binding.videoContainer.visibility = View.VISIBLE
                binding.videoPicture.setImageResource(R.drawable.audacity_icon)
                binding.videoContainer.setOnClickListener {
                    openVideo(post.video)
                }
            } else {
                binding.videoContainer.visibility = View.GONE
            }

            numberOfVies.text = format(post.numberView)

            imageView.apply {
                isChecked = post.share
                text = format(post.repost)
            }

            likesView.apply {
                isChecked = post.likeByMe
                text = format(post.counter)
            }

            likesView.setOnClickListener { onInteractionListener.onLike(post) }
            imageView.setOnClickListener { onInteractionListener.repostPost(post) }

            menu.setOnClickListener {
                PopupMenu(it.context, it).apply {
                    inflate(R.menu.post_actions)
                    setOnMenuItemClickListener { item ->
                        when (item.itemId) {
                            R.id.remove -> {
                                onInteractionListener.onRemove(post)
                                true
                            }
                            R.id.edit -> {
                                onInteractionListener.onEdit(post)
                                true
                            }
                            else -> false
                        }
                    }
                }.show()
            }
        }
    }
}

class PostDiffCallback : DiffUtil.ItemCallback<Post>() {
    override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
        return oldItem == newItem
    }
}

