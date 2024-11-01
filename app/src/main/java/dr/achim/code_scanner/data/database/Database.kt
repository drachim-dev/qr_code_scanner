package dr.achim.code_scanner.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import dr.achim.code_scanner.data.dao.CodeDao
import dr.achim.code_scanner.data.entity.CodeEntity

@Database(entities = [CodeEntity::class], version = 1)
@TypeConverters(Converters::class)
abstract class CodeDatabase : RoomDatabase() {
    abstract val dao: CodeDao
}