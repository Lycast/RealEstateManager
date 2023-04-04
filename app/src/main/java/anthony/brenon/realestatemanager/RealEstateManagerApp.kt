package anthony.brenon.realestatemanager

import android.app.Application
import anthony.brenon.realestatemanager.database.REMRoomDatabase
import anthony.brenon.realestatemanager.repository.AgentRepository
import anthony.brenon.realestatemanager.repository.EstateRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class RealEstateManagerApp : Application() {
    // No need to cancel this scope as it'll be torn down with the process
    private val applicationScope = CoroutineScope(SupervisorJob())

    // Using by lazy so the database and the repository are only created when they're needed
    // rather than when the application starts
    private val database by lazy { REMRoomDatabase.getDatabase(this, applicationScope) }
    val estateRepository by lazy { EstateRepository(database.estateDao(), database.pictureDao()) }
    val agentRepository by lazy { AgentRepository(database.agentDao()) }
}