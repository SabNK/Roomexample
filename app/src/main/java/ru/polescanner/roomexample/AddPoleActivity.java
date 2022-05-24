package ru.polescanner.roomexample;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import ru.polescanner.roomexample.adapters.RepositoryPoleDb;
import ru.polescanner.roomexample.adapters.db.AppDatabase;
import ru.polescanner.roomexample.adapters.db.PoleDb;
import ru.polescanner.roomexample.domain.Pole;
import ru.polescanner.roomexample.domain.Repository;

public class AddPoleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pole);
        EditText nameInput = findViewById(R.id.poleNameInput);
        EditText avatarInput = findViewById(R.id.poleAvatarInput);
        Button saveButton = findViewById(R.id.savePoleButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveNewPole(nameInput.getText().toString(),
                            avatarInput.getText().toString());
            }
        });
    }

    private void saveNewPole(String name, String avatar) {
        AppDatabase db = AppDatabase.getDbInstance((this.getApplicationContext()));
        Pole p = Pole.register(name, avatar);
        Repository<Pole> repo = new RepositoryPoleDb(db.poleDbDao(), new PoleDb.Mapper());
        repo.add(p);
    }
}