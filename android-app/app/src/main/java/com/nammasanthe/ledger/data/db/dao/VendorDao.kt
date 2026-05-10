package com.nammasanthe.ledger.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.nammasanthe.ledger.data.db.entities.VendorEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface VendorDao {

    @Query("SELECT * FROM vendors LIMIT 1")
    suspend fun getVendor(): VendorEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertVendor(vendor: VendorEntity)
}
