package dr.achim.code_scanner.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import dr.achim.code_scanner.data.entity.CodeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CodeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: CodeEntity)

    @Delete
    suspend fun delete(vararg codes: CodeEntity)

    @Query("DELETE FROM CodeEntity")
    suspend fun deleteAll()

    @Query("DELETE FROM CodeEntity WHERE id in (:ids)")
    suspend fun deleteById(vararg ids: String)

    @Update
    suspend fun update(entity: CodeEntity)

    @Query("SELECT * FROM CodeEntity ORDER BY created DESC")
    fun getAllCodes(): Flow<List<CodeEntity>>
}