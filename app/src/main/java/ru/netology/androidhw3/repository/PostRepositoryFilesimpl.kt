package ru.netology.androidhw3.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.netology.androidhw3.dto.Post


class PostRepositoryFilesImpl(private val context: Context) : PostRepository {


    private var nextId: Long = 1
    private var posts = emptyList<Post>()
        set(value) {
            field = value
            data.value = posts
            sync()
        }
    private val data = MutableLiveData(posts)

    init {
        val file = context.filesDir.resolve(FILENAME)
        if (file.exists()) {
            context.openFileInput(FILENAME).bufferedReader().use {

                posts = gson.fromJson(it, type)
                nextId = (posts.maxOfOrNull { it.id } ?: 0) + 1

            }
        }
    }

    override fun getAll(): LiveData<List<Post>> = data

    override fun get(id: Long): LiveData<Post> {
        val postLiveData = MutableLiveData<Post>()
        postLiveData.value = posts.find { it.id == id }
        return postLiveData
    }


    override fun like(id: Long) {
        posts = posts.map {
            if (it.id != id) it else it.copy(
                likeByMe = !it.likeByMe,
                counter = if (it.likeByMe) it.counter - 1 else it.counter + 1
            )
        }

    }

    override fun repost(id: Long) {
        posts = posts.map { post ->
            if (post.id == id) {
                post.copy(repost = post.repost + 1)
            } else {
                post
            }
        }


    }

    override fun removeById(id: Long) {
        posts = posts.filter { it.id != id }


    }

    override fun save(post: Post) {

        if (post.id == 0L) {
            posts = listOf(post.copy(id = nextId++, author = "Me")) + posts
        } else {
            posts = posts.map {
                if (it.id != post.id) it else it.copy(content = post.content)
            }
        }


    }

    private fun sync() {
        context.openFileOutput(FILENAME, Context.MODE_PRIVATE).bufferedWriter().use {
            it.write(gson.toJson(posts))
        }
    }

    companion object {
        private const val FILENAME = "post.json"
        private val gson = Gson()
        private val type = TypeToken.getParameterized(List::class.java, Post::class.java).type
    }


}