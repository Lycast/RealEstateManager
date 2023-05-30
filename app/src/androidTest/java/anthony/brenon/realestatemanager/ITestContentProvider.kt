package anthony.brenon.realestatemanager

import android.content.ContentResolver
import android.graphics.Bitmap
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.platform.app.InstrumentationRegistry
import anthony.brenon.realestatemanager.database.REMRoomDatabase
import anthony.brenon.realestatemanager.models.Estate
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue
import org.junit.After
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4


@Suppress("DEPRECATION")
@RunWith(JUnit4::class)
class ITestContentProvider {

    // For data
    private var mContentResolver: ContentResolver? = null
    private lateinit var database: REMRoomDatabase
    private lateinit var bitmap: Bitmap

    @Before
    fun setUp() {
        bitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888)

        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            REMRoomDatabase::class.java
        ).allowMainThreadQueries().build()

        mContentResolver = InstrumentationRegistry.getInstrumentation().context.contentResolver
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun getEstatesWithCursor() = runBlocking {
        // Insert a test estate
        val estateDAO = database.estateDao()
        val estate = generateEstate()
        estateDAO.insert(estate)

        // Get estates with cursor
        val cursor = estateDAO.getEstatesWithCursor()

        // Verify that the cursor is not null
        assertThat(cursor, notNullValue())

        // Check if the cursor has rows
        if (cursor.moveToFirst()) {
            // Retrieve the estate data from the cursor's columns
            val id = cursor.getLong(cursor.getColumnIndexOrThrow("id"))
            val type = cursor.getString(cursor.getColumnIndexOrThrow("type"))
            val price = cursor.getString(cursor.getColumnIndexOrThrow("price"))
            val surface = cursor.getString(cursor.getColumnIndexOrThrow("surface"))
            val roomsNumber = cursor.getString(cursor.getColumnIndexOrThrow("roomsNumber"))
            val description = cursor.getString(cursor.getColumnIndexOrThrow("description"))
            val addressStreet = cursor.getString(cursor.getColumnIndexOrThrow("addressStreet"))
            val addressCity = cursor.getString(cursor.getColumnIndexOrThrow("addressCity"))
            val addressCode = cursor.getString(cursor.getColumnIndexOrThrow("addressCode"))
            val addressCountry = cursor.getString(cursor.getColumnIndexOrThrow("addressCountry"))
            val lng = cursor.getDouble(cursor.getColumnIndexOrThrow("lng"))
            val lat = cursor.getDouble(cursor.getColumnIndexOrThrow("lat"))
            val interestingPoint = cursor.getString(cursor.getColumnIndexOrThrow("interestingPoint"))
            val saleDate = cursor.getString(cursor.getColumnIndexOrThrow("saleDate"))
            val soldDate = cursor.getString(cursor.getColumnIndexOrThrow("soldDate"))
            val agentInChargeName = cursor.getString(cursor.getColumnIndexOrThrow("agentInChargeName"))
            val pictures = cursor.getString(cursor.getColumnIndexOrThrow("pictures")).split(",")
            val numberOfPicture = cursor.getInt(cursor.getColumnIndexOrThrow("numberOfPicture"))
            // Add picture
            val picture = bitmap

            // Create the estate object using the retrieved data
            val retrievedEstate = Estate(
                id, type, price, surface, roomsNumber, description, addressStreet, addressCity,
                addressCode, addressCountry, lng, lat, interestingPoint, saleDate, soldDate,
                agentInChargeName, pictures, picture, numberOfPicture
            )

            // Compare the pictures using sameAs() method
            val picturesMatch = estate.picture.sameAs(retrievedEstate.picture)

            // Verify that the retrieved estate matches the inserted estate and the pictures match
            assertThat(retrievedEstate, `is`(estate))
            assertThat(picturesMatch, `is`(true))
        }

        // Close the cursor
        cursor.close()
    }

    private fun generateEstate(): Estate {
        return Estate(
            id = 1,
            type = "house",
            price = "500000",
            surface = "200",
            roomsNumber = "4",
            description = "This is a beautiful house",
            addressStreet = "123 Main St",
            addressCity = "San Francisco",
            addressCode = "94101",
            addressCountry = "USA",
            lng = 37.7749,
            lat = -122.4194,
            interestingPoint = "Golden Gate Bridge",
            saleDate = "2022-01-01",
            soldDate = "",
            agentInChargeName = "John Doe",
            pictures = listOf("https://example.com/image1.jpg", "https://example.com/image2.jpg"),
            picture = bitmap,
            numberOfPicture = 2
        )
    }
}