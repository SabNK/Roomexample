package ru.polescanner.roomexample.domain;

import static org.apache.commons.lang3.Validate.inclusiveBetween;
import static org.apache.commons.lang3.Validate.notNull;

import java.util.Objects;
import java.util.UUID;

public abstract class Entity{
    private final UUID id;
    private int version;
    private static int instanceCount;

    public Entity(UUID entityId, int entityVersion){
        notNull(entityId);
        this.id = entityId;
        inclusiveBetween(0, 10000, entityVersion,
                         "version not negative and less than 10000");
        this.version = entityVersion;
        instanceCount++;
    }
    protected void incrementVersion(){
        this.version++;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Entity entity = (Entity) o;
        return id.equals(entity.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public UUID getId(){
        /*A UUID unique identifier for the entity*/
        return this.id;
    }

    public int getVersion(){
        /*An integer version for the entity*/
        return this.version;
    }

    public static int getNumOfInstances(){
        return instanceCount;
    }
}
