package anthony.brenon.realestatemanager.models.dao

import androidx.room.*
import anthony.brenon.realestatemanager.models.Agent
import kotlinx.coroutines.flow.Flow

@Dao
interface AgentDAO {
    @Query("SELECT * FROM agent_table ORDER BY word ASC")
    fun getAllAgents(): Flow<List<Agent>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(agent: Agent)

    @Delete
    suspend fun delete(agent: Agent) : Int
}