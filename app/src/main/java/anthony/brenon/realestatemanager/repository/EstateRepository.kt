package anthony.brenon.realestatemanager.repository

import androidx.annotation.WorkerThread
import anthony.brenon.realestatemanager.models.Estate
import anthony.brenon.realestatemanager.models.Picture
import anthony.brenon.realestatemanager.database.dao.EstateDAO
import anthony.brenon.realestatemanager.database.dao.PictureDAO
import kotlinx.coroutines.flow.Flow

class EstateRepository(private val estateDAO: EstateDAO, private val pictureDAO: PictureDAO) {

    // Room executes all queries on a separate thread.
    // Observed Flow will notify the observer when the data has changed.
    val allEstates: Flow<List<Estate>> = estateDAO.getAllEstates()

    fun getPicturesByEstate(estateId: Int) : Flow<List<Picture>> {
        return pictureDAO.getPicturesByEstate(estateId)
    }


    // By default Room runs suspend queries off the main thread, therefore, we don't need to
    // implement anything else to ensure we're not doing long running database work
    // off the main thread.
    // ESTATES
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insertEstate(estate: Estate) {
        estateDAO.insert(estate)
    }
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun deleteEstate(estate: Estate) {
        estateDAO.delete(estate)
    }

    // PICTURES
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insertPicture(picture: Picture) {
        pictureDAO.insert(picture)
    }
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun deletePicture(picture: Picture) {
        pictureDAO.delete(picture)
    }
}