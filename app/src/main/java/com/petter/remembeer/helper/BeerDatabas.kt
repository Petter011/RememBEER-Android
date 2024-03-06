package com.petter.remembeer.helper

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface BeerDao {
    /*@Query("SELECT * FROM beertype ORDER BY name ASC")
    fun getAllTypes(): Flow<List<BeerType>>*/

    @Query("SELECT * FROM beer")
    fun getAll(): Flow<List<Beer>>
    @Query("SELECT * FROM beer ORDER BY type ASC")
    fun getAllflow(): Flow<List<Beer>>
    @Query("SELECT * FROM beer WHERE type = :beerType LIMIT 1")
    suspend fun getBeerByType(beerType: String): Beer?

    @Insert
    fun insertAll(vararg beer: Beer)

    @Update
    suspend fun updateBeers(beers: List<Beer>)

    @Delete
    suspend fun delete(beer: Beer)

    /*@Transaction
    @Query("SELECT * FROM beer")
    fun getAllBeerTypesWithBeers(): Flow<List<BeerTypeWithBeers>>


    @Transaction
    @Query("SELECT * FROM beerType")
    fun getBeerTypeByName(name: String): BeerType?


    @Transaction
    @Query("SELECT * FROM BeerType")
    fun getBeerTypeWithBeers(): Flow<List<BeerTypeWithBeers>> */


}

@Database(entities = [Beer::class/*, BeerType::class*/], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun beerDao(): BeerDao

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context?): AppDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context!!,
                    AppDatabase::class.java,
                    "database-name"
                ).build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}
