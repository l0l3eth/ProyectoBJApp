<<<<<<<< HEAD:app/src/androidTest/java/mx/tec/proyectobj/ExampleInstrumentedTest.kt
package mx.tec.proyectobj
========
package mx.tec.proyectoBJ
>>>>>>>> Registro:app/src/androidTest/java/mx/tec/proyectoBJ/ExampleInstrumentedTest.kt

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("mx.tec.ptoyectobj", appContext.packageName)
    }
}