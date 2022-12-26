package ru.polescanner.roomexample;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class AddPoleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pole);
        EditText nameInput = findViewById(R.id.poleNameInput);
        EditText avatarInput = findViewById(R.id.poleAvatarInput);
        Button saveButton = findViewById(R.id.savePoleButton);
        /*
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveNewPole(nameInput.getText().toString(),
                            avatarInput.getText().toString());
            }
        });*/
    }
/*
    private void saveNewPole(String name, String avatar) {
        AppDatabase db = AppDatabase.getDbInstance((this.getApplicationContext()));
        Pole p = Pole.register(name, avatar);
        Repository<Pole> repo = new RepositoryPoleDb(db.poleDbDao(), new PoleDb.Mapper());
        repo.add(p);
    }
*/
}
