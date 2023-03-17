package anthony.brenon.realestatemanager.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "agent_table")
class Agent (
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "agent_id")
    val id : Int,
    @ColumnInfo(name = "agent_first_name")
    val firstName: String,
    @ColumnInfo(name = "agent_last_name")
    val lastName: String?,
    @ColumnInfo(name = "agent_password")
    val password: String
)