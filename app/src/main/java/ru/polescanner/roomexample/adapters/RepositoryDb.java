package ru.polescanner.roomexample.adapters;

import java.util.List;

import ru.polescanner.roomexample.adapters.db.EntityDb;
import ru.polescanner.roomexample.domain.Entity;
import ru.polescanner.roomexample.domain.Repository;

public abstract class RepositoryDb<I /*extends Entity*/, O /*extends EntityDb*/>
        implements Repository<I> {

    protected EntityDb.Dao<O> dbDao;
    protected Mapper<I,O> mapper;

    public RepositoryDb(EntityDb.Dao<O> dbDao, Mapper<I, O> mapper) {
        this.dbDao = dbDao;
        this.mapper = mapper;
    }

    @Override
    public void add(I i) {
        O o = mapper.mapTo(i);
        dbDao.add(o);
    }

    @Override
    public List<I> getAll() {
        ListMapper<I,O> listMapperFromDb = new ListMapperImpl<I,O>(mapper);
        return listMapperFromDb.mapFrom(dbDao.getAll());
    }
}
