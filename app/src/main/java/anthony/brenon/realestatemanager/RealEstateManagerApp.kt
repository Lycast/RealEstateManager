package anthony.brenon.realestatemanager

import android.annotation.SuppressLint
import android.app.Application
import android.database.CursorWindow
import anthony.brenon.realestatemanager.database.REMRoomDatabase
import anthony.brenon.realestatemanager.repository.AgentRepository
import anthony.brenon.realestatemanager.repository.EstateRepository
import java.lang.reflect.Field

class RealEstateManagerApp : Application() {

    // Using by lazy so the database and the repository are only created when they're needed
    // rather than when the application starts
    private val database by lazy { REMRoomDatabase.getDatabase(this) }
    val estateRepository by lazy { EstateRepository(database.estateDao()) }
    val agentRepository by lazy { AgentRepository(database.agentDao()) }

    override fun onCreate() {
        super.onCreate()
        setCursorSize()
    }

    @SuppressLint("DiscouragedPrivateApi")
    private fun setCursorSize() {
        try {
            val field: Field = CursorWindow::class.java.getDeclaredField("sCursorWindowSize")
            field.isAccessible = true
            field.set(null, 100 * 1024 * 1024) //the 100MB is the new size
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}