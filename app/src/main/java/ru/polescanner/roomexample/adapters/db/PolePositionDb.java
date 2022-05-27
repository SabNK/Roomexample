package ru.polescanner.roomexample.adapters.db;

import androidx.room.Entity;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import ru.polescanner.roomexample.domain.Conductor;
import ru.polescanner.roomexample.domain.Pole;
import ru.polescanner.roomexample.domain.PolePosition;
import ru.polescanner.roomexample.domain.PoleType;

@Entity
public class PolePositionDb implements PoleType {
    String p;



    public static class Mapper implements ru.polescanner.roomexample.adapters.Mapper<PolePosition, PolePositionDb> {

        @Override
        public PolePositionDb mapTo(PolePosition polePosition) {
            switch (polePosition) {
                case A:
                    return PolePositionDb.A;
                case B:
                    return PolePositionDb.B;
                case C:
                    return PolePositionDb.C;
                case D:
                    return PolePositionDb.D;
                case E:
                    return PolePositionDb.E;
                case F:
                    return PolePositionDb.F;
            }
            return null;
        }

        @Override
        public PolePosition mapFrom(PolePositionDb polePositionDb) {
            switch (polePositionDb) {
                case A:
                    return PolePosition.A;
                case B:
                    return PolePosition.B;
                case C:
                    return PolePosition.C;
                case D:
                    return PolePosition.D;
                case E:
                    return PolePosition.E;
                case F:
                    return PolePosition.F;
            }
            return null;
        }
    }
}