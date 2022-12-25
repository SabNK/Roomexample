package ru.polescanner.roomexample.domain;

import java.util.UUID;

import ru.polescanner.roomexample.service.UnitOfWork;

public abstract class Aggregate extends Entity{

    public Aggregate(UUID entityId, int entityVersion) {
        super(entityId, entityVersion);
        markNew();
    }

    @Override
    protected void incrementVersion() {
        super.incrementVersion();
        markDirty();
    }

    protected void markNew(){
        UnitOfWork.getCurrent().registerNew(this);
    }
    protected void markDirty(){
        UnitOfWork.getCurrent().registerDirty(this);
    }
    protected void markClean(){
        UnitOfWork.getCurrent().registerClean(this);
    }
    void markDelete(){
        UnitOfWork.getCurrent().registerRemoved(this);
    }
}
