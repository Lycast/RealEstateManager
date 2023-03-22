package anthony.brenon.realestatemanager.repository

import androidx.annotation.WorkerThread
import anthony.brenon.realestatemanager.models.Estate
import anthony.brenon.realestatemanager.models.dao.EstateDAO
import kotlinx.coroutines.flow.Flow

class EstateRepository(private val estateDAO: EstateDAO) {

    // Room executes all queries on a separate thread.
    // Observed Flow will notify the observer when the data has changed.
    val allEstates: Flow<List<Estate>> = estateDAO.getAllEstates()

    // By default Room runs suspend queries off the main thread, therefore, we don't need to
    // implement anything else to ensure we're not doing long running database work
    // off the main thread.
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(estate: Estate) {
        estateDAO.insert(estate)
    }
}