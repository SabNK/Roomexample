package ru.polescanner.roomexample.adapters.db;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.List;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EntityDb entityDb = (EntityDb) o;
        return id.equals(entityDb.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    public static interface Dao<E /*extends EntityDb*/>{

        List<E> getAll();

        void add(E... e);
    }
}
