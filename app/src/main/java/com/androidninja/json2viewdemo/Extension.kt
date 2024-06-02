package com.androidninja.json2viewdemo

import org.json.JSONObject

fun JSONObject.toMap(): Map<String, String> {
    val map = mutableMapOf<String, String>()
    val keys = this.keys()
    while (keys.hasNext()) {
        val key = keys.next()
        map[key] = this.getString(key)
    }
    return map
}