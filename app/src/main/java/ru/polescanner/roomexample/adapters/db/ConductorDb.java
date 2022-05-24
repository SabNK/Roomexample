package ru.polescanner.roomexample.adapters.db;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;

import java.util.UUID;

import ru.polescanner.roomexample.adapters.Mapper;
import ru.polescanner.roomexample.domain.Conductor;

@Entity(tableName = "conductors")
public class ConductorDb extends EntityDb {
    public String alias;
    public Conductor.Voltage voltage;

    public ConductorDb() {};

    @Ignore
    public ConductorDb(@NonNull String id, int version, String alias, Conductor.Voltage voltage) {
        super(id, version);
        this.alias = alias;
        this.voltage = voltage;
    }

    public static class Mapper implements ru.polescanner.roomexample.adapters.Mapper<Conductor, ConductorDb> {
        @Override
        public ConductorDb mapTo(Conductor conductor) {
            return new ConductorDb(conductor.getId().toString(),
                                   conductor.getVersion(),
                                   conductor.getAlias(),
                                   conductor.getVoltage()
                                   );
        }

        @Override
        public Conductor mapFrom(ConductorDb conductorDb) {
            return new Conductor(UUID.fromString(conductorDb.id),
                                 conductorDb.version,
                                 new Conductor.ConductorName(conductorDb.alias),
                                 conductorDb.voltage);
        }
    }
}