package com.sait.tawajudpremiumplusnewfeatured.ui.changelanguage

import android.annotation.TargetApi
import android.content.Context
import android.content.res.Configuration
import android.os.Build
import com.sait.tawajudpremiumplusnewfeatured.util.preferences.PrefUtils
import com.sait.tawajudpremiumplusnewfeatured.util.Const
import java.util.*

object LocaleHelper {
    fun onAttach(context: Context): Context {
        val lang = getPersistedData(context, Locale.getDefault().language)
        return setLocale(context, lang)
    }

    fun onAttach(context: Context, defaultLanguage: String): Context {
        val lang = getPersistedData(context, defaultLanguage)
        return setLocale(context, lang)
    }

    fun getLanguage(context: Context): String {
        return getPersistedData(context, Locale.getDefault().language)
    }


    fun convertToArabicNumerals(input: String): String {
        val arabicNumerals = listOf('٠', '١', '٢', '٣', '٤', '٥', '٦', '٧', '٨', '٩')
        return input.map {
            if (it.isDigit()) arabicNumerals[it.toString().toInt()] else it
        }.joinToString("")
    }
    private fun getPersistedData(context: Context, defaultLanguage: String): String {
        var lang = PrefUtils.getStringWithContext(context, Const.SharedPrefs.SELECTED_LANGUAGE)
        if (lang == null || lang == "")
            lang = Const.Other.ENGLISH_LANG_CODE
        return lang
    }

    private fun persist(context: Context, language: String?) {
        PrefUtils.setStringWithContext(context, Const.SharedPrefs.SELECTED_LANGUAGE, language)
    }
    fun setLocale(context: Context, language: String): Context {
        persist(context, language)
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            updateResources(context, language)
        } else updateResourcesLegacy(
            context,
            language
        )
    }
    @TargetApi(Build.VERSION_CODES.N)
    private fun updateResources(context: Context, language: String): Context {
        return Configuration(context.resources.configuration).run {
            Locale.setDefault(Locale(language).also { locale ->
                setLocale(locale)
            }).let {
                context.createConfigurationContext(this)
            }
        }
    }

    private fun updateResourcesLegacy(context: Context, language: String): Context {
        val locale = Locale(language)
        Locale.setDefault(locale)
        val resources = context.resources
        val configuration = resources.configuration
        configuration.locale = locale
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            configuration.setLayoutDirection(locale)
        }
        resources.updateConfiguration(configuration, resources.displayMetrics)
        return context
    }
    fun arabicToEnglish(arabicNumber: String): String {
        val arabicDigits = arrayOf("٠", "١", "٢", "٣", "٤", "٥", "٦", "٧", "٨", "٩")
        val englishDigits = arrayOf("0", "1", "2", "3", "4", "5", "6", "7", "8", "9")

        var englishNumber = arabicNumber
        for (i in arabicDigits.indices) {
            englishNumber = englishNumber.replace(arabicDigits[i], englishDigits[i])
        }

        return englishNumber.replace("\u066B",".")
    }
    fun convertArabicMonthToEnglish(arabicDate: String): String {
        val arabicMonthNames = arrayOf("يناير", "فبراير", "مارس", "أبريل", "مايو", "يونيو", "يوليو", "أغسطس", "سبتمبر", "أكتوبر", "نوفمبر", "ديسمبر")
        val englishMonthNames = arrayOf("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December")

        var englishDate = arabicDate
        for (i in arabicMonthNames.indices) {
            englishDate = englishDate.replace(arabicMonthNames[i], englishMonthNames[i], ignoreCase = true)
        }
        return englishDate
    }
}