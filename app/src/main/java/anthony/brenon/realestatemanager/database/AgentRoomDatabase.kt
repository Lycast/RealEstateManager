package anthony.brenon.realestatemanager.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import anthony.brenon.realestatemanager.models.Agent
import anthony.brenon.realestatemanager.models.dao.AgentDAO
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = [Agent::class], version = 1, exportSchema = false)
abstract class AgentRoomDatabase : RoomDatabase() {

    abstract fun agentDao() : AgentDAO

    private class AgentDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let {
                scope.launch {
                    var agentDAO = it.agentDao()

                    // Delete all content here.
                    agentDAO.deleteAll()

                    // Add admin account agent.
                    var agent = Agent(0,"admin","account","administrator")
                    agentDAO.insert(agent)
                }
            }
        }
    }

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: AgentRoomDatabase? = null

        fun getAgentDatabase(
            context: Context,
            scope: CoroutineScope
        ) : AgentRoomDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AgentRoomDatabase::class.java,
                    "agent_database")
                    .addCallback(AgentDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}