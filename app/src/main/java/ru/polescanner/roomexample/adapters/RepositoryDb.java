package ru.polescanner.roomexample.adapters;

import java.util.Arrays;
import java.util.List;

import ru.polescanner.roomexample.adapters.db.EntityDb;
import ru.polescanner.roomexample.domain.Aggregate;
import ru.polescanner.roomexample.domain.Repository;

public class RepositoryDb<I, O>
        implements Repository<I> {

    protected EntityDb.Dao<O> dbDao;
    protected Mapper<I,O> mapper;
    protected ListMapper<I,O> listMapper;

    public RepositoryDb(EntityDb.Dao<O> dbDao, Mapper<I, O> mapper) {
        this.dbDao = dbDao;
        this.mapper = mapper;
        this.listMapper = new ListMapperImpl<I,O>(mapper);
    }

    @Override
    public void add(I... i) {
        List<I> iList = Arrays.asList(i);
        List<O> oList = listMapper.mapTo(iList);
        dbDao.add((O) oList.toArray(new Object[0]));
    }

    @Override
    public List<I> getAll() {
        return listMapper.mapFrom(dbDao.getAll());
    }

    @Override
    public I getById(String id) {
        return mapper.mapFrom(dbDao.getById(id));
    }
}
