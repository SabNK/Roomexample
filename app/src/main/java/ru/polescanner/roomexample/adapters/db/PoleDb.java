package ru.polescanner.roomexample.adapters.db;

import androidx.annotation.NonNull;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Insert;
import androidx.room.Junction;
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
    public Map<PolePositionDb,ConductorDb> layout = new HashMap<>();


    public PoleDb(){};

    @Ignore
    public PoleDb(@NonNull String id, int version, String name,
                  String avatar, Map<PolePositionDb, ConductorDb> layout) {
        super(id, version);
        this.name = name;
        this.avatar = avatar;
        this.layout = layout;
    }

    public Description description(String s){ return new Image(s);}

    @androidx.room.Dao
    public interface Dao extends EntityDb.Dao<PoleDb> {

        @Transaction
        default void add(PoleDb... polesDbs) {
            addWithoutLayout(polesDbs);
            List<Layout> layouts = new ArrayList<>();
            for (PoleDb poleDb : polesDbs)
                layouts.add(new Layout(poleDb, poleDb.layout));
            addLayout(layouts.toArray(new Layout[0]));
        }

        @Insert
        void addWithoutLayout(PoleDb... poleDbs);

        default void addLayout(Layout... poleLayouts){
            List<PositionDb> positionDbs = new ArrayList<>();
            for (Layout layout : poleLayouts)
                for (Map.Entry<PolePositionDb, ConductorDb> entry : layout.positions.entrySet())
                    positionDbs.add(new PositionDb(layout.poleDb.id,
                                                   entry.getKey(),
                                                   entry.getValue().id));
            addPosition(positionDbs.toArray(new PositionDb[0]));
        }

        @Insert
        void addPosition(PositionDb... positionDbs);

        @Override
        @Transaction
        default List<PoleDb> getAll(){
            List<PoleDb> poleDbs = getAllWithoutLayout();
            List<Layout> layouts = getLayouts();
            for (PoleDb poleDb : poleDbs)
                for (Layout layout : layouts)
                    if (layout.poleDb == poleDb)
                        poleDb.layout = layout.positions;
            return poleDbs;
        }

        @Query("SELECT * FROM poles")
        List<PoleDb> getAllWithoutLayout();


        @Query( "SELECT * FROM pole_positions " +
                "JOIN conductors ON pole_positions.conductorId = conductors.id " +
                "JOIN poles ON pole_positions.poleId = poles.id "
                )
        List<Layout> getLayouts();

        //ToDo for the future methods of Repository interface
        @Query( "SELECT * FROM pole_positions " +
                "JOIN conductors ON pole_positions.conductorId = conductors.id " +
                "JOIN poles ON pole_positions.poleId = poles.id " +
                "WHERE poleId = :poleId ")
        Layout getLayout(String poleId);


        //ToDo Refer to todo in the head of PoleSnapshot Class below
        @Query("SELECT name, avatar FROM poles")
        List<PoleSnapshot> getSnapshots();
    }

    public static class Mapper implements ru.polescanner.roomexample.adapters.Mapper<Pole, PoleDb> {
        ru.polescanner.roomexample.adapters.Mapper<Conductor, ConductorDb>
                conductorDbMapper = new ConductorDb.Mapper();
        ru.polescanner.roomexample.adapters.Mapper<PolePosition, PolePositionDb>
                polePositionDbMapper = new PolePositionDb.Mapper();

        @Override
        public PoleDb mapTo(Pole pole) {
            Map<PolePositionDb, ConductorDb> layout = new HashMap<>();
            for (Map.Entry<PolePosition, Conductor>  entry : pole.getLayout().entrySet()){
                layout.put(polePositionDbMapper.mapTo(entry.getKey()),
                           conductorDbMapper.mapTo(entry.getValue()));
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
            for (Map.Entry<PolePositionDb, ConductorDb> entry : poleDb.layout.entrySet()){
                layout.put(polePositionDbMapper.mapFrom(entry.getKey()),
                           conductorDbMapper.mapFrom(entry.getValue()));
            }
            return new Pole(UUID.fromString(poleDb.id),
                            poleDb.version,
                            new Pole.Polename(poleDb.name),
                            layout,
                            poleDb.description(poleDb.avatar));
        }
    }


}
//ToDo Idea from SecureByDesign and MurphyRoom page 32
class PoleSnapshot {
    public final String name;
    public final String avatar;

    public PoleSnapshot(String name, String avatar) {
        this.name = name;
        this.avatar = avatar;
    }
}

@Entity(tableName = "pole_positions", primaryKeys = {"poleId", "position"})
class PositionDb {
    @NonNull
    String poleId;
    @NonNull
    PolePositionDb position;
    //ToDo The column conductorId in the junction entity ru.polescanner.roomexample.adapters.db.PositionDb
    // is being used to resolve a relationship but it is not covered by any index.
    // This might cause a full table scan when resolving the relationship, it is highly advised
    // to create an index that covers this column. String conductorId;
    String conductorId;

    public PositionDb() {}

    @Ignore
    public PositionDb(@NonNull String poleId, @NonNull PolePositionDb position, String conductorId) {
        this.poleId = poleId;
        this.position = position;
        this.conductorId = conductorId;
    }
}


class Layout{
    @Embedded
    PoleDb poleDb;
    @Relation(  parentColumn = "id",
                entity = ConductorDb.class,
                entityColumn = "id",
                associateBy = @Junction(value = PositionDb.class,
                                        parentColumn = "poleId",
                                        entityColumn = "conductorId"))
    Map<PolePositionDb, ConductorDb> positions;

    public Layout() {}

    @Ignore
    public Layout(PoleDb poleDb, Map<PolePositionDb, ConductorDb> positions) {
        this.poleDb = poleDb;
        this.positions = positions;
    }
}
