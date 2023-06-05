package anthony.brenon.realestatemanager

import anthony.brenon.realestatemanager.utils.Utils
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Test
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Date

class UtilsUnitTest {

    @Before
    fun setUp() {}

    // Test for convert right dollar to euro
    @Test
    fun convertDollarToEuro() {
        val value = Utils.convertDollarToEuro(1000)
        val expectedValue = 930
        assertEquals(expectedValue, value)
    }

    // Test for date of today in good format
    @Test
    fun assertGetTodayDateWorks() {
        val today = Date()
        val dateFormat: DateFormat = SimpleDateFormat("dd/MM/yyyy")
        assertEquals(dateFormat.format(today), Utils.todayDate)
    }
}