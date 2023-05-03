package anthony.brenon.realestatemanager.database

import android.content.Context
import androidx.room.*
import anthony.brenon.realestatemanager.models.Agent
import anthony.brenon.realestatemanager.models.Estate
import anthony.brenon.realestatemanager.database.dao.AgentDAO
import anthony.brenon.realestatemanager.database.dao.EstateDAO

@Database(entities = [Agent::class, Estate::class], version = 1, exportSchema = false)
abstract class REMRoomDatabase : RoomDatabase() {

    abstract fun agentDao() : AgentDAO
    abstract fun estateDao() : EstateDAO

    companion object {
        @Volatile
        private var INSTANCE: REMRoomDatabase? = null

        fun getDatabase(
            context: Context,
        ) : REMRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    REMRoomDatabase::class.java,
                    "database_rem"
                )
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}