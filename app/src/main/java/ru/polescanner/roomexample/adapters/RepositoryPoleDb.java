package ru.polescanner.roomexample.adapters;

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
    public void add(Pole p) {
        PoleDb pdb = this.mapper.mapTo(p);
        dbDao.add(pdb);
    }

    @Override
    public List<Pole> getAll() {
        ListMapper<Pole, PoleDb> listMapper = new ListMapperImpl<>(mapper);
        return listMapper.mapFrom(dbDao.getAll());
    }
}
