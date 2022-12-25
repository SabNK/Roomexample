package ru.polescanner.roomexample.domain;

import static org.apache.commons.lang3.Validate.notNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Pole extends Aggregate{
    private Polename name;
    protected Map<PolePosition, Conductor> layout;
    private Description avatar;

    public Pole(UUID poleId,
                int poleVersion,
                Polename name,
                Map<PolePosition, Conductor> poleLayout,
                Description poleAvatar
                ) {
        super(poleId, poleVersion);
        this.name = name;
        this.layout = poleLayout;
        this.avatar = poleAvatar;
    }

    public static Pole register(String poleName,
                                String polePicture){
        notNull(polePicture, "Single picture of pole should be provided");
        Pole p = new Pole(UUID.randomUUID(),
                          0,
                          new Polename(poleName),
                          new HashMap<PolePosition, Conductor>(),
                          new Image(polePicture)
                          );
        p.markNew();
        return p;
    }



    public String getName() {
        return name.toString();
    }

    public Description getAvatar() {
        return avatar;
    }

    public Map<PolePosition, Conductor> getLayout() {
        return layout;
    }

    public void attachConductor(PolePosition polePosition, Conductor conductor){
        layout.put(polePosition, conductor);
        incrementVersion();
        markDirty();
    }

    //INNER CLASSES
    public static class Polename extends Name{
        private static final int POLENAME_MIN_LENGTH = 2;
        private static final int POLENAME_MAX_LENGTH = 20;
        private static final String POLENAME_VALID_CHARS = "[A-Za-zА-Яа-я0-9_-]+";

        public Polename(final String polename) {
            super(polename, POLENAME_MIN_LENGTH, POLENAME_MAX_LENGTH, POLENAME_VALID_CHARS);
        }
    }
}
