package com.aykuttasil.androidbasichelperlib

import android.bluetooth.BluetoothAdapter
import android.content.Context.TELEPHONY_SERVICE
import android.telephony.TelephonyManager
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.Config
import kotlin.random.Random


@RunWith(AndroidJUnit4::class)
@Config(sdk = [28])
class SuperHelperTest {

    @Test
    @Throws(Exception::class)
    fun checkCorrectPhoneNumber() {
        val telNo = "5358151244"
        val correctTelNo = SuperHelper.getCorrectPhoneNumber(telNo)
        assertNotNull(correctTelNo)
        assertEquals("05358151244", correctTelNo)
    }

    @Test
    @Throws(Exception::class)
    fun checkCorrectPhoneNumber1() {
        val telNo = "0905358151244"
        val correctTelNo = SuperHelper.getCorrectPhoneNumber(telNo)
        assertNotNull(correctTelNo)
        assertEquals("0905358151244", correctTelNo)
    }

    @Test
    @Throws(Exception::class)
    fun checkCorrectPhoneNumber2() {
        val telNo = "905358151244"
        val correctTelNo = SuperHelper.getCorrectPhoneNumber(telNo)
        assertNotNull(correctTelNo)
        assertEquals("05358151244", correctTelNo)
    }

    @Test
    @Throws(Exception::class)
    fun checkCorrectPhoneNumber3() {
        val telNo = "90535815 1244"
        val correctTelNo = SuperHelper.getCorrectPhoneNumber(telNo)
        assertNotNull(correctTelNo)
        assertEquals("05358151244", correctTelNo)
    }

    @Test
    @Throws(Exception::class)
    fun checkCorrectPhoneNumber4() {
        val telNo = "318 2242771"
        val correctTelNo = SuperHelper.getCorrectPhoneNumber(telNo)
        assertNotNull(correctTelNo)
        assertEquals("03182242771", correctTelNo)
    }

    @Test
    @Throws(Exception::class)
    fun checkCorrectPhoneNumber5() {
        val telNo = "090318 22427 71"
        val correctTelNo = SuperHelper.getCorrectPhoneNumber(telNo)
        assertNotNull(correctTelNo)
        assertEquals("0903182242771", correctTelNo)
    }

    @Test
    @Throws(Exception::class)
    fun checkCorrectPhoneNumber6() {
        val telNo = "90535 815 12 44"
        val correctTelNo = SuperHelper.getCorrectPhoneNumber(telNo)
        assertNotNull(correctTelNo)
        assertEquals("05358151244", correctTelNo)
    }

    @Test
    @Throws(Exception::class)
    fun checkGetStandardText() {
        val stdText = SuperHelper.getStandartText("ğüşiç")
        assertNotNull(stdText)
        assertEquals("gusic", stdText)
    }

    @Test
    @Throws(Exception::class)
    fun checkGetStandardText1() {
        val stdText = SuperHelper.getStandartText("NAMIK")
        assertNotNull(stdText)
        assertEquals("namik", stdText)
    }

    @Test
    @Throws(Exception::class)
    fun checkGetStandardText2() {
        val stdText = SuperHelper.getStandartText("HÜSEYİN")
        assertNotNull(stdText)
        assertEquals("huseyin", stdText)
    }

    @Test
    fun check_sim_state() {
        val telephonyManager = InstrumentationRegistry.getInstrumentation().targetContext.applicationContext.getSystemService(TELEPHONY_SERVICE) as TelephonyManager
        val shadowTelephonyManager = shadowOf(telephonyManager)
        shadowTelephonyManager.setSimState(0)
        var result = SuperHelper.getSimState(InstrumentationRegistry.getInstrumentation().targetContext.applicationContext)
        assertEquals("Bilinmiyor", result)

        shadowTelephonyManager.setSimState(1)
        result = SuperHelper.getSimState(InstrumentationRegistry.getInstrumentation().targetContext.applicationContext)
        assertEquals("Sim Kart Yok", result)

        shadowTelephonyManager.setSimState(2)
        result = SuperHelper.getSimState(InstrumentationRegistry.getInstrumentation().targetContext.applicationContext)
        assertEquals("Pin Girilmesi Bekleniyor", result)

        shadowTelephonyManager.setSimState(3)
        result = SuperHelper.getSimState(InstrumentationRegistry.getInstrumentation().targetContext.applicationContext)
        assertEquals("Puk girilmesi bekleniyor", result)

        shadowTelephonyManager.setSimState(4)
        result = SuperHelper.getSimState(InstrumentationRegistry.getInstrumentation().targetContext.applicationContext)
        assertEquals("Pin(Network) girilmesi bekleniyor", result)

        shadowTelephonyManager.setSimState(5)
        result = SuperHelper.getSimState(InstrumentationRegistry.getInstrumentation().targetContext.applicationContext)
        assertEquals("Hazır", result)

        shadowTelephonyManager.setSimState(Random.nextInt(6, 99))
        result = SuperHelper.getSimState(InstrumentationRegistry.getInstrumentation().targetContext.applicationContext)
        assertEquals("Bilinmiyor", result)
    }

    @Test
    fun check_bluetooth_state() {
        val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        val shadowTelephonyManager = shadowOf(bluetoothAdapter)
        shadowTelephonyManager.setEnabled(true)
        var result = SuperHelper.getBluetoothState(InstrumentationRegistry.getInstrumentation().targetContext.applicationContext)
        assertEquals("VAR - ENABLE", result)

        shadowTelephonyManager.setEnabled(false)
        result = SuperHelper.getBluetoothState(InstrumentationRegistry.getInstrumentation().targetContext.applicationContext)
        assertEquals("VAR - DISABLE", result)
    }


    @Test
    fun testX() {
        val a =  SuperHelper.getInstalledApps(InstrumentationRegistry.getInstrumentation().context).toString()
        print(a)
    }

}
