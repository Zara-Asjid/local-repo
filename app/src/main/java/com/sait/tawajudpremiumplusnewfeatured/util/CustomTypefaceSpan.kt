package com.sait.tawajudpremiumplusnewfeatured.util

import android.graphics.Paint
import android.graphics.Typeface
import android.text.TextPaint
import android.text.style.TypefaceSpan

class CustomTypefaceSpan(private val customTypeface: Typeface) : TypefaceSpan("") {
    override fun updateDrawState(textPaint: TextPaint) {
        applyCustomTypeface(textPaint, customTypeface)
    }

    override fun updateMeasureState(textPaint: TextPaint) {
        applyCustomTypeface(textPaint, customTypeface)
    }

    private fun applyCustomTypeface(paint: Paint, tf: Typeface) {
        paint.typeface = tf
    }
}