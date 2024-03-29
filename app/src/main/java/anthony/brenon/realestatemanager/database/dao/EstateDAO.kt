package anthony.brenon.realestatemanager.database.dao

import android.database.Cursor
import androidx.room.*
import anthony.brenon.realestatemanager.models.Estate
import kotlinx.coroutines.flow.Flow

@Dao
interface EstateDAO {

    @Query("SELECT * FROM estate_table")
    fun getAllEstates(): Flow<List<Estate>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(estate: Estate)

    @Query("SELECT * FROM estate_table")
    fun getEstatesWithCursor() : Cursor

}