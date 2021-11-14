package com.edgarjorozco.lunchtime.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.util.TypedValue
import com.edgarjorozco.lunchtime.BuildConfig
import com.edgarjorozco.lunchtime.R
import com.google.android.gms.maps.GoogleMap
import com.google.maps.android.SphericalUtil


fun Drawable.toBitmap(): Bitmap {
    val canvas = Canvas()
    val bitmap = Bitmap.createBitmap(this.intrinsicWidth, this.intrinsicHeight, Bitmap.Config.ARGB_8888)
    canvas.setBitmap(bitmap)
    this.setBounds(0, 0, this.intrinsicWidth, this.intrinsicHeight)
    this.draw(canvas)
    return bitmap
}

fun Float.dpToPx(context: Context): Int =
    TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this, context.resources.displayMetrics).toInt()

fun Context.generateGooglePlacesPhotoUrl(width: Int, referenceId: String): String {
    return this.getString(R.string.google_places_photo_url, width, referenceId, BuildConfig.GOOGLE_PLACES_KEY)
}

fun GoogleMap.metersAcrossMinimumDimension(): Double {
    val visibleRegion = this.projection.visibleRegion
    val width = SphericalUtil.computeDistanceBetween(visibleRegion.farLeft, visibleRegion.farRight)
    val height = SphericalUtil.computeDistanceBetween(visibleRegion.farLeft, visibleRegion.nearLeft)
    return minOf(width,height)
}