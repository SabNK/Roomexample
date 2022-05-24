package ru.polescanner.roomexample.adapters.db;

import androidx.annotation.NonNull;
import androidx.room.Dao;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import ru.polescanner.roomexample.domain.Conductor;
import ru.polescanner.roomexample.domain.Description;
import ru.polescanner.roomexample.domain.Image;
import ru.polescanner.roomexample.domain.Pole;
import ru.polescanner.roomexample.domain.PolePosition;


@Entity(tableName = "poles")
public class PoleDb extends EntityDb {

    public String name;
    public String avatar;
    @Embedded(prefix="pole_layout")
    public Map<PolePosition,ConductorDb> layout = new HashMap<>();

    public PoleDb(){};

    @Ignore
    public PoleDb(@NonNull String id, int version, String name,
                  String avatar, Map<PolePosition, ConductorDb> layout) {
        super(id, version);
        this.name = name;
        this.avatar = avatar;
        this.layout = layout;
    }

    public Description description(String s){ return new Image(s);}

    @androidx.room.Dao
    public interface Dao extends EntityDb.Dao<PoleDb> {

        @Query("SELECT * FROM poles")
        List<PoleDb> getAll();

        @Insert
        void add(PoleDb... poleDbs);
    }

    public static class Mapper implements ru.polescanner.roomexample.adapters.Mapper<Pole, PoleDb> {
        ru.polescanner.roomexample.adapters.Mapper<Conductor, ConductorDb> conductorDbMapper =
                new ConductorDb.Mapper();

        @Override
        public PoleDb mapTo(Pole pole) {
            Map<PolePosition, ConductorDb> layout = new HashMap<>();
            for (Map.Entry<PolePosition, Conductor>  entry : pole.getLayout().entrySet()){
                layout.put(entry.getKey(), conductorDbMapper.mapTo(entry.getValue()));
            }
            return new PoleDb(pole.getId().toString(),
                              pole.getVersion(),
                              pole.getName(),
                              pole.getAvatar().toString64(),
                              layout);
        }

        @Override
        public Pole mapFrom(PoleDb poleDb) {
            Map<PolePosition, Conductor> layout = new HashMap<>();
            for (Map.Entry<PolePosition, ConductorDb> entry : poleDb.layout.entrySet()){
                layout.put(entry.getKey(), conductorDbMapper.mapFrom(entry.getValue()));
            }
            return new Pole(UUID.fromString(poleDb.id),
                            poleDb.version,
                            new Pole.Polename(poleDb.name),
                            layout,
                            poleDb.description(poleDb.avatar));
        }
    }
}
