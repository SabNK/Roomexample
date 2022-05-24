package ru.polescanner.roomexample.adapters.db;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.List;

@Entity
public abstract class EntityDb {
    @NonNull
    @PrimaryKey
    public String id;
    public int version;

    public EntityDb(){};

    @Ignore
    public EntityDb(@NonNull String id, int version) {
        this.id = id;
        this.version = version;
    }

    public static interface Dao<E /*extends EntityDb*/>{

        List<E> getAll();

        void add(E... e);
    }
}
