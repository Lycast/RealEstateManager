package anthony.brenon.realestatemanager.repository

import androidx.annotation.WorkerThread
import anthony.brenon.realestatemanager.database.dao.EstateDAO
import anthony.brenon.realestatemanager.models.Estate
import kotlinx.coroutines.flow.Flow

class EstateRepository(private val estateDAO: EstateDAO) {

    // ESTATES
    val allEstates: Flow<List<Estate>> = estateDAO.getAllEstates()

    @WorkerThread
    suspend fun insertEstate(estate: Estate) {
        estateDAO.insert(estate)
    }
}