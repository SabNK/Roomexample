package ru.polescanner.roomexample;

import android.content.Context;

import androidx.room.Room;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;
import static org.assertj.core.api.Assertions.*;

import java.util.List;

import ru.polescanner.roomexample.adapters.RepositoryPoleDb;
import ru.polescanner.roomexample.adapters.db.AppDatabase;
import ru.polescanner.roomexample.adapters.db.PoleDb;
import ru.polescanner.roomexample.domain.Conductor;
import ru.polescanner.roomexample.domain.Entity;
import ru.polescanner.roomexample.domain.Pole;
import ru.polescanner.roomexample.domain.PoleCollection;
import ru.polescanner.roomexample.domain.PolePosition;
import ru.polescanner.roomexample.service.UnitOfWork;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    private AppDatabase mDb;
    private UnitOfWork sut;

    @Before
    public void createDb(){
        Context context =  InstrumentationRegistry.getInstrumentation().getTargetContext();
        mDb = Room.inMemoryDatabaseBuilder(context, AppDatabase.class)
                // Allowing main thread queries, just for testing.
                .allowMainThreadQueries()
                .build();
        RepositoryPoleDb repo = new RepositoryPoleDb(mDb.poleDbDao(), new PoleDb.Mapper());
        UnitOfWork.setUnitOfWork(repo);
        sut = UnitOfWork.getCurrent();

    }
    @After
    public void closeDb() {
        mDb.close();
    }

    @Test
    public void roomTest(){
        Pole p1 = Pole.register("pole1", "pole1");
        Pole p2 = Pole.register("pole2", "pole2");
        PoleCollection poleCollection = new PoleCollection(p1, p2);
        String p1Id = p1.getId().toString();
        String p2Id = p2.getId().toString();
        Conductor c1 = Conductor.register("leftA35", Conductor.Voltage.V6K_10K);
        Conductor c2 = Conductor.register("middleA35", Conductor.Voltage.V6K_10K);
        Conductor c3 = Conductor.register("rightA35", Conductor.Voltage.V6K_10K);
        p1.attachConductor(PolePosition.A, c1);
        p1.attachConductor(PolePosition.B, c2);
        p1.attachConductor(PolePosition.C, c3);
        p2.attachConductor(PolePosition.A, c1);
        p2.attachConductor(PolePosition.B, c2);
        p2.attachConductor(PolePosition.C, c3);
        sut.commit();


        assertThat(Entity.getNumOfInstances()).isEqualTo(5);
        //ToDo https://enfuse.io/testing-java-collections-with-assertj/
        List<Pole> all = sut.aRepository().getAll();
        assertThat(Entity.getNumOfInstances()).isEqualTo(13);
        assertThat(all).hasSize(2);
        assertThat(all.get(0)).isEqualTo(p1);
        assertThat(all.get(1).getLayout().get(PolePosition.A)).isEqualTo(c1);
        assertThat(all.get(0).getLayout().values()).containsExactlyInAnyOrder(c1, c2, c3);
        assertThat(all.get(1).getLayout().keySet()).contains(PolePosition.B);

        Pole one = (Pole) sut.aRepository().getById(p1Id);
        assertThat(one).isEqualTo(p1);
        assertThat(one.getName()).isEqualTo("pole1");
        assertThat(one.getLayout().values()).containsExactlyInAnyOrder(c1, c2, c3);
        assertThat(one.getLayout().keySet()).contains(PolePosition.B);
    }


    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("ru.polescanner.roomexample", appContext.getPackageName());
    }
}