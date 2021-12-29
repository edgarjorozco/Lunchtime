package com.edgarjorozco.lunchtime.util

import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.widget.AutoCompleteTextView
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.databinding.adapters.ListenerUtil
import androidx.databinding.adapters.TextViewBindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.edgarjorozco.lunchtime.R
import com.edgarjorozco.lunchtime.util.ui.HorizontalSpaceDecoration
import com.google.android.material.button.MaterialButton

object BindingAdapters {
    @BindingAdapter("icon")
    @JvmStatic
    fun setIcon(button: MaterialButton, icon: Drawable) {
        button.icon = icon
    }

    @BindingAdapter("googlePlacesPhoto")
    @JvmStatic
    fun loadGooglePlacesPhoto(view: ImageView, referenceId: String?) {
        if (referenceId == null) return
        var width = view.width
        if (width == 0) {
            val displayMetrics = Resources.getSystem().displayMetrics
            width = minOf(displayMetrics.widthPixels, displayMetrics.heightPixels)
        }
        val url = view.context.generateGooglePlacesPhotoUrl(width, referenceId)
        Glide
            .with(view)
            .load(url)
            .centerCrop()
            .placeholder(R.color.at_grey)
            .into(view)
    }

    /**
     * @see androidx.databinding.adapters.TextViewBindingAdapter
     * Slight change to ignore text changes from selecting an item in AutoCompleteTextView
     */
    @BindingAdapter("afterTextChanged")
    @JvmStatic
    fun setTextWatcher(view: AutoCompleteTextView, after: TextViewBindingAdapter.AfterTextChanged?) {
        val newValue: TextWatcher? = if (after == null) {
            null
        } else {
            object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) { }

                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}

                override fun afterTextChanged(s: Editable) {
                    if (!view.isPerformingCompletion) after.afterTextChanged(s)
                }
            }
        }
        val oldValue = ListenerUtil.trackListener(view, newValue, R.id.textWatcher)
        if (oldValue != null) view.removeTextChangedListener(oldValue)
        if (newValue != null) view.addTextChangedListener(newValue)

    }

    @BindingAdapter(value = [ "recyclerView_horizontalMargin", "recyclerView_startingHorizontalMargin", "recyclerView_endingHorizontalMargin" ], requireAll = false )
    @JvmStatic
    fun setHorizontalMargin(recycler: RecyclerView, defaultMargin: Float?, startingMargin: Float?, endingMargin: Float?) {

        val startSpace = (startingMargin?:defaultMargin?:0).toInt()
        val endingSpace = (endingMargin?:defaultMargin?:0).toInt()
        val defaultSpace = (defaultMargin?:0).toInt()

        val spaceDecorator = HorizontalSpaceDecoration(startSpace, endingSpace, defaultSpace)

        recycler.apply {
            addItemDecoration(spaceDecorator)
        }
    }
}