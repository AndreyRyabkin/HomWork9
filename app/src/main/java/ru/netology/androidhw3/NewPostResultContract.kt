package ru.netology.androidhw3

import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract




class NewPostResultContract : ActivityResultContract<Intent, String?>()  {
    override fun createIntent(context: Context, input: Intent): Intent {
return Intent(context, NewPostActivity::class.java)
    }

    override fun parseResult(resultCode: Int, intent: Intent?): String? {
return intent?.getStringExtra(Intent.EXTRA_TEXT)
    }


}

