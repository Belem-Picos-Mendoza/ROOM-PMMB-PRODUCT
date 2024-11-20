package com.example.room_pmmb_product.Datos
import androidx.room.Database
import androidx.room.RoomDatabase
import android.content.Context
import androidx.room.Room

@Database(entities = [Product::class], version = 1, exportSchema = false)
abstract class Product_DB : RoomDatabase() {

    abstract fun ProductDao(): ProductDao
    // permite el acceso a los métodos para crear u obtener la base de datos y usa el nombre de clase como calificador
    companion object {
        @Volatile
        private var Instance: Product_DB? = null

        fun getDatabase(context: Context): Product_DB {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, Product_DB::class.java, "product_database")
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }
    }
}