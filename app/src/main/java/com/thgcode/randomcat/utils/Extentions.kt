package com.thgcode.randomcat.utils

import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.widget.ImageView
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import kotlinx.android.synthetic.main.activity_main.*
import java.io.ByteArrayOutputStream
import kotlin.reflect.KProperty

fun <T : ViewDataBinding> contentView(@LayoutRes layoutRes: Int) = SetContentView<T>(layoutRes)

class SetContentView<out T : ViewDataBinding>(@LayoutRes private val layoutRes: Int) {
    private var value: T? = null

    operator fun getValue(thisRef: Activity, property: KProperty<*>): T {
        value = value ?: DataBindingUtil.setContentView(thisRef, layoutRes)
        return value!!
    }
}

fun ImageView.sharedImage(contentResolver: ContentResolver) {
    this.isDrawingCacheEnabled = true
    val bitmap: Bitmap = this.drawingCache
    val share = Intent(Intent.ACTION_SEND)
    share.type = "image/jpeg"
    val bytes = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, 50, bytes)
    val path = MediaStore.Images.Media.insertImage(contentResolver, bitmap, "Titulo da Imagem", null)
    val imageUri = Uri.parse(path)
    share.putExtra(Intent.EXTRA_STREAM, imageUri)
    this.context.startActivity(Intent.createChooser(share, "Selecione:"))
}