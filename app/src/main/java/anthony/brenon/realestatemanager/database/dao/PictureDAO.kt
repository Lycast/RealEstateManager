package anthony.brenon.realestatemanager.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import anthony.brenon.realestatemanager.models.Picture
import kotlinx.coroutines.flow.Flow

@Dao
interface PictureDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(picture: Picture)

    @Query("SELECT * FROM picture_table WHERE estateId = :estateId")
    fun getPicturesByEstate(estateId: Int): Flow<List<Picture>>

}