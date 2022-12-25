package ru.polescanner.roomexample.service;

import ru.polescanner.roomexample.domain.Aggregate;
import ru.polescanner.roomexample.domain.Pole;
import ru.polescanner.roomexample.domain.Repository;

public interface IUnitOfWork<A> {
    Repository<A> aRepository();
    void commit();
    void rollback();

}
