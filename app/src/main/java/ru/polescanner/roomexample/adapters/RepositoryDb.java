package ru.polescanner.roomexample.adapters;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

import ru.polescanner.roomexample.adapters.db.EntityDb;
import ru.polescanner.roomexample.adapters.db.PoleDb;
import ru.polescanner.roomexample.domain.Aggregate;
import ru.polescanner.roomexample.domain.Pole;
import ru.polescanner.roomexample.domain.Repository;

public class RepositoryDb<I extends Aggregate, O extends EntityDb>
        implements Repository<I> {

    protected EntityDb.Dao<O> dbDao;
    protected Mapper<I,O> mapper;
    protected ListMapper<I,O> listMapper;
    private Class<I> classAggregate;
    private Class<O> classEntityDb;

    public RepositoryDb(EntityDb.Dao<O> dbDao,
                        Mapper<I, O> mapper,
                        Class<I> classAggregate,
                        Class<O> classEntityDb) {
        this.dbDao = dbDao;
        this.mapper = mapper;
        this.listMapper = new ListMapperImpl<>(mapper);
        this.classAggregate = classAggregate;
        this.classEntityDb = classEntityDb;
    }

    public static RepositoryDb setRepository(EntityDb.Dao<? extends EntityDb> dbDao,
                                             Mapper<? extends Aggregate, ? extends EntityDb> mapper,
                                             Class<? extends Aggregate> classAggregate,
                                             Class<? extends EntityDb> classEntityDb) {
        return new RepositoryDb(dbDao, mapper, classAggregate, classEntityDb);
    }

    @Override
    public void add(I... i) {
        List<I> iList = Arrays.asList(i);
        List<O> oList = listMapper.mapTo(iList);
        dbDao.add(toArrayEntityDb(oList));
    }

    @Override
    public List<I> getAll() {
        return listMapper.mapFrom(dbDao.getAll());
    }

    @Override
    public I getById(String id) {
        return mapper.mapFrom(dbDao.getById(id));
    }

    private I[] toArrayAggregate(List<I> list) {
        @SuppressWarnings("unchecked")
        final I[] array = (I[]) Array.newInstance(classAggregate, list.size());
        return list.toArray(array);
    }

    private O[] toArrayEntityDb(List<O> list) {
        @SuppressWarnings("unchecked")
        final O[] array = (O[]) Array.newInstance(classEntityDb, list.size());
        return list.toArray(array);
    }
}
