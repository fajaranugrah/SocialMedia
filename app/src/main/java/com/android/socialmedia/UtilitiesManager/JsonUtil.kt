package com.android.socialmedia.UtilitiesManager

import java.io.IOException

object JsonUtil {

    /*need library from implementation 'com.squareup.retrofit:converter-jackson:1.9.0'*/

    private val TAG = JsonUtil::class.java.name
    /**
     * This Method Convert Object in JaksonJSON String
     * @param object
     * @return Json String or null if some error
     */
    fun toJson(`object`: Any): String? {
        /*val mapper = ObjectMapper()
        try {
            return mapper.writeValueAsString(`object`)
        } catch (e: Exception) {
            Log.e(TAG, "Error in Converting Model to Json", e)
        }*/

        return null
    }

    /**
     * This method convert json to model
     * @param json
     * @param object
     * @return Model or null if some error
     */
    fun <T> toModel(json: String, `object`: Class<T>): Any? {
        /*val mapper = ObjectMapper()
        try {
            mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true)
            return mapper.readValue(json, `object`)
        } catch (e: Exception) {
            Log.e(TAG, "Error in Converting Json to Model", e)
        }*/

        return null
    }

    fun isJSONValid(jsonInString: String): Boolean {
        try {
            /*val mapper = ObjectMapper()
            mapper.readTree(jsonInString)*/
            return true
        } catch (e: IOException) {
            return false
        }

    }
}