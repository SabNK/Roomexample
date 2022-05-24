package ru.polescanner.roomexample.domain;

import java.util.UUID;

public class Conductor extends Entity {
    private ConductorName alias; //Рабочее (временное) наименование
    private Voltage voltage;

    public Conductor(UUID entityId, int entityVersion, ConductorName alias, Voltage voltage) {
        super(entityId, entityVersion);
        this.alias = alias;
        this.voltage = voltage;
    }

    public String getAlias() {
        return alias.toString();
    }

    public Voltage getVoltage() {
        return voltage;
    }

    //INNER CLASSES
    //ToDo complete class
    public static class ConductorName extends Name {
        private static final int CONDUCTORNAME_MIN_LENGTH = 2;
        private static final int CONDUCTORNAME_MAX_LENGTH = 20;
        private static final String CONDUCTORNAME_VALID_CHARS = "[A-Za-zА-Яа-я0-9_-]+";
        public ConductorName(String name) {
            super(name, CONDUCTORNAME_MIN_LENGTH,
                  CONDUCTORNAME_MAX_LENGTH, CONDUCTORNAME_VALID_CHARS);
        }
    }
    public enum Voltage { V220, V04K, V6K_10K, NA
    }


}
