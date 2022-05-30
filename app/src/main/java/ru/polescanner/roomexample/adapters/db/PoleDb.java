package ru.polescanner.roomexample.adapters.db;

import androidx.annotation.NonNull;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.Insert;
import androidx.room.Junction;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Relation;
import androidx.room.Transaction;

import java.util.ArrayList;
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

    @Ignore
    public Map<PolePosition, ConductorDb> layout = new HashMap<>();

    public PoleDb() {
        layout = new HashMap<>();
    }

    @Ignore
    public PoleDb(@NonNull String id, int version, String name,
                  String avatar, Map<PolePosition, ConductorDb> layout) {
        super(id, version);
        this.name = name;
        this.avatar = avatar;
        this.layout = layout;
    }

    public Description description(String s) {
        return new Image(s);
    }

    @androidx.room.Dao
    public interface Dao extends EntityDb.Dao<PoleDb> {

        @Override
        @Transaction
        default void add(PoleDb... polesDbs) {
            List<ConductorDb> conductorsToAdd = new ArrayList<>();
            List<ConductorAttachDb> attaches = new ArrayList<>();
            for (PoleDb p : polesDbs)
                if (p.layout != null) {
                    conductorsToAdd.addAll(p.layout.values());
                    for (Map.Entry<PolePosition, ConductorDb> entry : p.layout.entrySet()) {
                        PolePosition pp = entry.getKey();
                        ConductorDb c = entry.getValue();
                        attaches.add(new ConductorAttachDb(p.id, pp, c.id));
                    }
                }
            addSimple(polesDbs);
            addSimple(conductorsToAdd.toArray(new ConductorDb[0]));
            addSimple(attaches.toArray(new ConductorAttachDb[0]));
        }

        @Insert(onConflict = OnConflictStrategy.REPLACE)
        void addSimple(PoleDb... poleDbs);

        @Insert(onConflict = OnConflictStrategy.REPLACE)
        void addSimple(ConductorDb... conductorDbs);

        @Insert(onConflict = OnConflictStrategy.REPLACE)
        void addSimple(ConductorAttachDb... attaches);


        @Override
        @Transaction
        default List<PoleDb> getAll() {
            List<PoleDb> poleDbs = getAllSimple();
            List<ConductorAttachApp> attaches = getAllAttaches();
            for (ConductorAttachApp a : attaches) {
                int j = poleDbs.indexOf(a.poleDb);
                poleDbs.get(j).layout.put(a.position, a.conductorDb);
            }
            return poleDbs;
        }

        @Query("SELECT * FROM poles")
        List<PoleDb> getAllSimple();

        @Query( "SELECT * FROM pole_attaches " +
                "JOIN conductors ON pole_attaches.conductorId = conductors.id " +
                "JOIN poles ON pole_attaches.poleId = poles.id ")
        List<ConductorAttachApp> getAllAttaches();

        @Override
        @Transaction
        default PoleDb getById(String poleDbId) {
            PoleDb poleDb = getByIdSimple(poleDbId);
            List<ConductorAttachApp> attaches = getByIdAttaches(poleDbId);
            for (ConductorAttachApp a : attaches)
                poleDb.layout.put(a.position, a.conductorDb);
            return poleDb;
        }

        @Query("SELECT * FROM poles WHERE id = :poleDbId")
        PoleDb getByIdSimple(String poleDbId);


        @Query( "SELECT * FROM pole_attaches " +
                "JOIN conductors ON pole_attaches.conductorId = conductors.id " +
                "JOIN poles ON pole_attaches.poleId = poles.id "+
                "WHERE poleId = :poleDbId")
        List<ConductorAttachApp> getByIdAttaches(String poleDbId);


        //ToDo Refer to todo in the head of PoleSnapshot Class below
        @Query("SELECT id, name, avatar FROM poles")
        List<PoleSnapshot> getSnapshots();
    }

    public static class Mapper implements ru.polescanner.roomexample.adapters.Mapper<Pole, PoleDb> {
        ru.polescanner.roomexample.adapters.Mapper<Conductor, ConductorDb>
                conductorDbMapper = new ConductorDb.Mapper();

        @Override
        public PoleDb mapTo(Pole pole) {
            Map<PolePosition, ConductorDb> layout = new HashMap<>();
            for (Map.Entry<PolePosition, Conductor> entry : pole.getLayout().entrySet())
                layout.put(entry.getKey(),
                           conductorDbMapper.mapTo(entry.getValue()));
            return new PoleDb(pole.getId().toString(),
                              pole.getVersion(),
                              pole.getName(),
                              pole.getAvatar().toString64(),
                              layout);
        }

        @Override
        public Pole mapFrom(PoleDb poleDb) {
            Map<PolePosition, Conductor> layout = new HashMap<>();
            for (Map.Entry<PolePosition, ConductorDb> entry : poleDb.layout.entrySet())
                layout.put( entry.getKey(),
                            conductorDbMapper.mapFrom(entry.getValue()));
            return new Pole(UUID.fromString(poleDb.id),
                            poleDb.version,
                            new Pole.Polename(poleDb.name),
                            layout,
                            poleDb.description(poleDb.avatar));
        }
    }

    @Entity(tableName = "pole_attaches",
            primaryKeys = {"poleId", "position"},
            indices = {@Index("poleId"), @Index("conductorId")},
            foreignKeys = { @ForeignKey(entity = PoleDb.class,
                                        parentColumns = "id",
                                        childColumns = "poleId",
                                        onDelete = ForeignKey.CASCADE),
                            @ForeignKey(entity = ConductorDb.class,
                                        parentColumns = "id",
                                        childColumns = "conductorId",
                                        onDelete = ForeignKey.CASCADE)})
    static class ConductorAttachDb {
        @NonNull
        String poleId;
        @NonNull
        PolePosition position;
        String conductorId;

        public ConductorAttachDb() {}

        @Ignore
        public ConductorAttachDb(@NonNull String poleId,
                                 @NonNull PolePosition position,
                                 String conductorId) {
            this.poleId = poleId;
            this.position = position;
            this.conductorId = conductorId;
        }
    }

    static class ConductorAttachApp {
        @Junction(value = ConductorAttachDb.class, parentColumn = "poleId", entityColumn = "id")
        PoleDb poleDb;
        PolePosition position;
        @Relation(parentColumn = "conductorId", entityColumn = "id")
        ConductorDb conductorDb;
    }
}

//ToDo Idea from SecureByDesign and MurphyRoom page 32
class PoleSnapshot {
    public final String id;
    public final String name;
    public final String avatar;

    public PoleSnapshot(String id, String name, String avatar) {
        this.id = id;
        this.name = name;
        this.avatar = avatar;
    }
}




