package ru.polescanner.roomexample.adapters.db;


import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {  UserDb.class, PoleDb.class, ConductorDb.class,
                        PositionDb.class},
          version=1, exportSchema = false)
public abstract class AppDatabase<E /*extends EntityDb*/> extends RoomDatabase {
    private static AppDatabase INSTANCE;

    public abstract UserDb.Dao userDbDao();
    public abstract PoleDb.Dao poleDbDao();

    public static AppDatabase getDbInstance(Context context) {
        if (INSTANCE == null) {
            //ToDo DBname to Settings
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                            AppDatabase.class, "DB_NAME")
                        .allowMainThreadQueries()
                        .build();}
        return INSTANCE;
    }
}
