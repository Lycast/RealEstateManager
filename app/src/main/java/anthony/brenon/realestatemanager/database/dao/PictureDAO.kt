package anthony.brenon.realestatemanager.database.dao

import androidx.room.*
import anthony.brenon.realestatemanager.models.Picture
import kotlinx.coroutines.flow.Flow

@Dao
interface PictureDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(picture: Picture)

    @Delete
    suspend fun delete(picture: Picture) : Int

    @Query("SELECT * FROM picture_table WHERE estateId = :estateId")
    fun getPicturesByEstate(estateId: Int): Flow<List<Picture>>

    @Query("DELETE FROM picture_table")
    suspend fun deleteAll()
}