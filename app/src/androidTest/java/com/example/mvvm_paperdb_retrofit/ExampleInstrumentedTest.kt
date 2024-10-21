package com.example.mvvm_paperdb_retrofit

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.mvvm_paperdb_retrofit.retrofit.RetrofitService
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 *
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val app = appContext.applicationContext as RetrofitService

        val result = app.checkInternetConnection()
        assertEquals(true, result)
    }
}