package com.jie.tungcheung.utils.enshrine

import com.tencent.mmkv.MMKV
import org.jetbrains.annotations.NotNull

//Created by Priscilla Cheung 2021年12月16日10:55:01.
object KeyValueStore {
    private val mmkv by lazy { MMKV.mmkvWithID("RDEN", MMKV.MULTI_PROCESS_MODE)!! }

    fun put(@NotNull key: String, @NotNull value: String) {
        mmkv.encode(key, value)
    }


    fun put(@NotNull key: String, @NotNull value: Float) {
        mmkv.encode(key, value)
    }

    fun put(@NotNull key: String, @NotNull value: Boolean) {
        mmkv.encode(key, value)
    }


    fun put(@NotNull key: String, @NotNull value: Int) {
        mmkv.encode(key, value)
    }


    fun put(@NotNull key: String, @NotNull value: Long) {
        mmkv.encode(key, value)
    }


    fun put(@NotNull key: String, @NotNull value: ByteArray) {
        mmkv.encode(key, value)
    }


    fun getString(@NotNull key: String, @NotNull default: String): String? =
        mmkv.decodeString(key, default)

    fun getFloat(@NotNull key: String, @NotNull default: Float) = mmkv.decodeFloat(key, default)

    fun getBoolean(@NotNull key: String, @NotNull default: Boolean) = mmkv.decodeBool(key, default)

    fun getInt(@NotNull key: String, @NotNull default: Int) = mmkv.decodeInt(key, default)

    fun getLong(@NotNull key: String, @NotNull default: Long) = mmkv.decodeLong(key, default)

    fun getByteArray(@NotNull key: String, @NotNull default: ByteArray) =
        mmkv.decodeBytes(key, default)

    fun remove(@NotNull value:String){
        mmkv.remove(value)
    }
    fun removeAll(){
        val keys = mmkv.allKeys()
        mmkv.removeValuesForKeys(keys)
    }
}