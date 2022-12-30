package ru.polescanner.roomexample.service;

import androidx.annotation.NonNull;

import org.apache.commons.lang3.NotImplementedException;

import java.io.InvalidObjectException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import ru.polescanner.roomexample.domain.Aggregate;
import ru.polescanner.roomexample.domain.Repository;

public class UnitOfWork<A extends Aggregate> implements IUnitOfWork<A> {
    private static UnitOfWork uow;
    private Repository<A> repository;
    private Class<A> classAggregate;

    private List<A> newAggregates = new ArrayList<>();
    private List<A> dirtyAggregates = new ArrayList<>();
    private List<A> removeAggregates = new ArrayList<>();

    private UnitOfWork(Repository<A> repository, Class<A> classAggregate) {
        this.repository = repository;
        this.classAggregate = classAggregate;
    }

    public static void setUnitOfWork(Repository<? extends Aggregate> repository,
                                     Class<? extends Aggregate> classAggregate) {
        uow = new UnitOfWork(repository, classAggregate);
    }

    public static UnitOfWork getCurrent() {
        return uow;
    }

    public void registerNew(@NonNull A aggregate) {
        try {
            assertNotDirty(aggregate);
            assertNotRemoved(aggregate);
            assertNotNew(aggregate);
            newAggregates.add(aggregate);
        } catch (InvalidObjectException e) {
        }
    }

    public void registerDirty(@NonNull A aggregate) {
        try {
            assertNotRemoved(aggregate);
            assertNotDirty(aggregate);
            assertNotNew(aggregate);
            dirtyAggregates.add(aggregate);
        } catch (InvalidObjectException e) {
        }
    }

    public void registerRemoved(@NonNull A aggregate) {
        try {
            assertNotRemoved(aggregate);
            assertNotDirty(aggregate);
            assertNotNew(aggregate);
            removeAggregates.add(aggregate);
        } catch (InvalidObjectException e) {
        }
    }

    public void registerClean(@NonNull A aggregate) {
    }

    private void assertNotDirty(A a) throws InvalidObjectException {
        if (dirtyAggregates.contains(a))
            throw new InvalidObjectException("Pole is to Update");
    }

    private void assertNotRemoved(A a) throws InvalidObjectException {
        if (removeAggregates.contains(a))
            throw new InvalidObjectException("Pole is to Delete");
    }

    private void assertNotNew(A a) throws InvalidObjectException {
        if (newAggregates.contains(a))
            throw new InvalidObjectException("Pole is to Register new");
    }

    @Override
    public Repository<A> aRepository() {
        return repository;
    }

    @Override
    public void commit() {
        insertNew();
        updateDirty();
        //deleteRemoved();
    }

    private void deleteRemoved() {
        throw new NotImplementedException("Not delete");
    }

    private void updateDirty() {
        repository.add(toArray(dirtyAggregates));
    }

    private void insertNew() {
        repository.add(toArray(newAggregates));
    }

    private A[] toArray(List<A> list) {
        @SuppressWarnings("unchecked")
        final A[] array = (A[]) Array.newInstance(classAggregate, list.size());
        return list.toArray(array);
    }

    @Override
    public void rollback() {
        newAggregates = new ArrayList<>();
        dirtyAggregates = new ArrayList<>();
        removeAggregates = new ArrayList<>();
    }
}
