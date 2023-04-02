package anthony.brenon.realestatemanager.database.dao

import androidx.room.*
import anthony.brenon.realestatemanager.models.Estate
import kotlinx.coroutines.flow.Flow

@Dao
interface EstateDAO {

    @Query("SELECT * FROM estate_table")
    fun getAllEstates(): Flow<List<Estate>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(estate: Estate)

    @Delete
    suspend fun delete(estate: Estate): Int

    @Query("DELETE FROM estate_table")
    suspend fun deleteAll()

    //todo implement get estate by id for put ids of images
//    @Query("SELECT * FROM estate_table WHERE estateId = :estateId")
//    fun getEstateById(estateId: Int): Flow<List<Picture>>
}