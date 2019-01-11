package com.aykuttasil.androidbasichelperlib

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [28])
class PrefsHelperTest {

    private lateinit var prefsHelper: PrefsHelper
    @Before
    fun setUp() {
        prefsHelper = PrefsHelper.init(InstrumentationRegistry.getInstrumentation().context)
    }

    @After
    fun tearDown() {

    }

    @Test
    fun getPreference() {
        assertNotNull(prefsHelper)
    }

    @Test
    fun getPrefEditor() {
        assertNotNull(prefsHelper.prefEditor)
    }

    @Test
    fun writeString() {
        prefsHelper.writeString("testKey", "testValue")
        val result = prefsHelper.readString("testKey")
        assertEquals("testValue", result)
    }

    @Test
    fun readString() {
        prefsHelper.writeString("testKey", "testValue")
        val result = prefsHelper.readString("testKey")
        assertEquals("testValue", result)
    }

    @Test
    fun writeInt() {
        prefsHelper.writeInt("testKey", 1)
        val result = prefsHelper.readInt("testKey")
        assertEquals(1, result)
    }

    @Test
    fun readInt() {
        prefsHelper.writeInt("testKey", 1)
        val result = prefsHelper.readInt("testKey")
        assertEquals(1, result)
    }

    @Test
    fun writeBool() {
        prefsHelper.writeBool("testKey", true)
        val result = prefsHelper.readBool("testKey")
        assertEquals(true, result)
    }

    @Test
    fun readBool() {
        prefsHelper.writeBool("testKey", true)
        val result = prefsHelper.readBool("testKey")
        assertEquals(true, result)
    }

    @Test
    fun clearAllPreference() {
        prefsHelper.writeBool("testKeyBool", true)
        prefsHelper.writeInt("testKeyInt", 1)
        prefsHelper.writeString("testKeyString", "test")

        prefsHelper.clearAllPreference()

        val resultString = prefsHelper.readString("testKeyString")
        assertNull(resultString)

        val resultInt = prefsHelper.readInt("testKeyInt")
        assertEquals(-100, resultInt)

        val resultBoolean = prefsHelper.readBool("testKeyBool")
        assertEquals(false, resultBoolean)
    }
}