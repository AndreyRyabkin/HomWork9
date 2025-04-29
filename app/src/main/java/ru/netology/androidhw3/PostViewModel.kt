package ru.netology.androidhw3

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.netology.androidhw3.dto.Post
import ru.netology.androidhw3.repository.PostRepositoryFilesImpl

private val empty = Post(
    0,
    "",
    "",
    "",
    false,
    false,
    0,
    0,
    0,
    null

)

class PostViewModel(application: Application) : AndroidViewModel(application) {
    private val repository  = PostRepositoryFilesImpl(application)

    val data = repository.getAll()
    val edited = MutableLiveData(empty)
    fun likePost(id: Long) = repository.like(id)
    fun repostPost(id: Long) = repository.repost(id)
    fun removeById(id: Long) = repository.removeById(id)

    fun changeContentandSave(text: String) {
        edited.value?.let {
            if (it.content != text) {
                repository.save(it.copy(content = text))
            }
        }
        edited.value = empty
    }

    fun editPost(post: Post) {
        edited.value = post
    }

    fun resetEditPost() {
        edited.value = empty
    }

    fun save() {
    }
}





