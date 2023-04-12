package anthony.brenon.realestatemanager.repository

import android.content.Context
import androidx.annotation.WorkerThread
import anthony.brenon.realestatemanager.database.REMRoomDatabase
import anthony.brenon.realestatemanager.database.dao.EstateDAO
import anthony.brenon.realestatemanager.database.dao.PictureDAO
import anthony.brenon.realestatemanager.models.Estate
import anthony.brenon.realestatemanager.models.Picture
import kotlinx.coroutines.flow.Flow

class EstateRepository(private val estateDAO: EstateDAO, private val pictureDAO: PictureDAO) {

    // ESTATES
    val allEstates: Flow<List<Estate>> = estateDAO.getAllEstates()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insertEstate(context: Context, estate: Estate) : Long {
        val db = REMRoomDatabase.getDatabase(context)
        val dao = db.estateDao()
        return dao.insert(estate)
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

    fun getPicturesByEstate(estateId: Long) : Flow<List<Picture>> {
        return pictureDAO.getPicturesByEstate(estateId)
    }
}