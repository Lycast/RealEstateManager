package anthony.brenon.realestatemanager.models

import android.graphics.Bitmap
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.PrimaryKey

@Entity (tableName = "picture_table",
    foreignKeys = [
        ForeignKey(entity = Estate::class,
            parentColumns = ["id"],
            childColumns = ["estateId"],
            onDelete = CASCADE)])
class Picture (
    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    var picture: Bitmap,
    @ColumnInfo(name = "estateId")
    var estateId: Long
    )
{
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}