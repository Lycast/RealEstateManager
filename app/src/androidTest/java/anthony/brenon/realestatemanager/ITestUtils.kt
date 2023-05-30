package anthony.brenon.realestatemanager

import android.content.Context
import android.net.wifi.WifiManager
import androidx.test.platform.app.InstrumentationRegistry
import anthony.brenon.realestatemanager.utils.Utils
import junit.framework.TestCase.assertEquals
import org.junit.Test


class ITestUtils {

    @Test
    fun assertIsInternetAvailableWorks() {
        // Context of the app under test.
        val appContext: Context = InstrumentationRegistry.getInstrumentation().targetContext
        // get wifi enable information
        val wifiManager = appContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        val isWifiEnabled = wifiManager.isWifiEnabled
        assertEquals(isWifiEnabled, Utils.isInternetAvailable(appContext))
    }
}