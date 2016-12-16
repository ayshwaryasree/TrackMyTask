package com.microsoft.wise.myapplication;

import android.app.Activity;
import android.content.Intent;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {
    EditText Task_name;
    Button map, save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Task_name = (EditText)findViewById(R.id.task_name);
        map = (Button)findViewById(R.id.map);
        save = (Button)findViewById(R.id.save_place);

        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Maps_Place_Activity.class);
                Toast.makeText(MainActivity.this, "Maps", Toast.LENGTH_SHORT);
                startActivity(intent);
            }
        });
    }
}
