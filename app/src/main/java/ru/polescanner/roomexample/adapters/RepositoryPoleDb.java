package ru.polescanner.roomexample.adapters;

import java.util.Arrays;
import java.util.List;

import ru.polescanner.roomexample.adapters.db.EntityDb;
import ru.polescanner.roomexample.adapters.db.PoleDb;
import ru.polescanner.roomexample.domain.Pole;

public class RepositoryPoleDb extends RepositoryDb<Pole, PoleDb> {
    public RepositoryPoleDb(EntityDb.Dao<PoleDb> dbDao,
                            Mapper<Pole, PoleDb> mapper) {
        super(dbDao, mapper);
    }

    @Override
    public void add(Pole... p) {
        List<PoleDb> pdbs = this.listMapper.mapTo(Arrays.asList(p));
        dbDao.add(pdbs.toArray(new PoleDb[0]));
    }

    @Override
    public List<Pole> getAll() {
        return listMapper.mapFrom(dbDao.getAll());
    }

    @Override
    public Pole getById(String id) {
        return mapper.mapFrom(dbDao.getById(id));
    }
}
