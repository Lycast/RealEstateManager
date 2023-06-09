package anthony.brenon.realestatemanager

import android.content.Context
import android.net.ConnectivityManager
import android.net.wifi.WifiManager
import androidx.test.platform.app.InstrumentationRegistry
import anthony.brenon.realestatemanager.utils.Utils
import junit.framework.TestCase.assertEquals
import org.junit.Test

class NetworkUtilsInstrumentedTest {

    @Test
    fun isInternetAvailable_shouldReturnWiFiStatus() {
        // Context of the app under test.
        val appContext: Context = InstrumentationRegistry.getInstrumentation().targetContext
        // get wifi enable information
        val wifiManager = appContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        val isWifiEnabled = wifiManager.isWifiEnabled
        assertEquals(isWifiEnabled, Utils.isInternetAvailable(appContext))
    }

    @Suppress("DEPRECATION")
    // for api 21 compatibility i can only use deprecated method
    // activeNetworkInfo and isConnectedOrConnecting
    @Test
    fun isConnected_shouldReturnConnectivityStatus() {
        // Get the application context using InstrumentationRegistry
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext

        // Get the connectivity manager and active network info
        val connectivityManager = appContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo

        // Check if the active network is connected or connecting
        val isConnected = activeNetworkInfo?.isConnectedOrConnecting ?: false

        // Assert that the isConnected value matches the expected connectivity status
        assertEquals(isConnected, Utils.isConnected(appContext))
    }
}