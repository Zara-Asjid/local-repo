package com.sait.tawajudpremiumplusnewfeatured.util

object Const {
    /**
     * all keys for shared preferences must be put here
     */
    interface SharedPrefs {
        companion object {
            const val AUTHORIZATION = "authorization"
            const val USER_DATA = "user_data"
            const val SELECTED_LANGUAGE = "selected_language"
        }
    }

    /**
     * all keys for bundle/intent extras must be put here
     */
    interface BundleExtras {
        companion object {
            const val EMP_DETAILS = "emp_details"



        }
    }

    /**
     * all keys for category adapter must be put here
     */
    interface Other {
        companion object {
            const val GOLD_24 = "24k"
            const val GOLD_22 = "22k"
            const val GOLD_21 = "21k"
            const val GOLD_18 = "18k"
            const val OUNCE = "Ounce"
            const val CURRENCY = "KWD"
            const val LANGUAGE = "en"
            const val PAGE_SIZE = "15"
            const val PAGE_SIZE_FAQ = "20"
            const val POSTED_ON = "Posted on "
            const val ENGLISH_LANG_CODE = "en"
            const val ARABIC_LANG_CODE = "ar"
        }
    }

    /**
     * all keys for category adapter must be put here
     */
    interface ApiTimeout {
        companion object {
            const val CONNECTION_TIMEOUT = 30000
            const val READ_TIMEOUT = 30000
            const val WRITE_TIMEOUT = 30000
        }
    }

    /**
     * all keys for time out must be put here
     */
    interface TimeOut {
        companion object {
            const val ZOOM_OUT: Long = 1000
            const val SPLASH_DELAY: Long = 2000
        }
    }

    /**
     * all keys for Params must be put here
     */
    interface Params {
        companion object {
            const val USERNAME = "username"
        }
    }
}