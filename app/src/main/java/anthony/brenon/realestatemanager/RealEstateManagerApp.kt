package anthony.brenon.realestatemanager

import android.app.Application
import anthony.brenon.realestatemanager.database.REMRoomDatabase
import anthony.brenon.realestatemanager.repository.AgentRepository
import anthony.brenon.realestatemanager.repository.EstateRepository

class RealEstateManagerApp : Application() {

    // Using by lazy so the database and the repository are only created when they're needed
    // rather than when the application starts
    private val database by lazy { REMRoomDatabase.getDatabase(this) }
    val estateRepository by lazy { EstateRepository(database.estateDao(), database.pictureDao()) }
    val agentRepository by lazy { AgentRepository(database.agentDao()) }
}