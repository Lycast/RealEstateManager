package anthony.brenon.realestatemanager.models.dao

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
}