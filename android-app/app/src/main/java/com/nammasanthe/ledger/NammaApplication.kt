package com.nammasanthe.ledger

import android.app.Application
import androidx.room.Room
import com.nammasanthe.ledger.data.db.NammaDatabase
import com.nammasanthe.ledger.data.repository.LedgerRepository

class NammaApplication : Application() {

    val database: NammaDatabase by lazy {
        Room.databaseBuilder(this, NammaDatabase::class.java, "namma_santhe.db")
            .fallbackToDestructiveMigration()
            .build()
    }

    val repository: LedgerRepository by lazy {
        LedgerRepository(database)
    }
}
