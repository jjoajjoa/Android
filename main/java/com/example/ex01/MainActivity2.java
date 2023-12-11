package com.example.ex01;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity2 extends AppCompatActivity {
    int count = 0;
    TextView txtCount, txtFruit;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.jjajang) {
            Toast.makeText(MainActivity2.this, "짜장짜장", Toast.LENGTH_SHORT).show();
        } else if (item.getItemId() == R.id.jjambbong) {
            Toast.makeText(MainActivity2.this, "뽕짬뽕", Toast.LENGTH_SHORT).show();
        } else if (item.getItemId() == R.id.udong) {
            Toast.makeText(MainActivity2.this, "우동동동", Toast.LENGTH_SHORT).show();
        } else if (item.getItemId() == R.id.mandoo) {
            Toast.makeText(MainActivity2.this, "만뚜룹뚜룹뚜", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        getSupportActionBar().setTitle("클릭 연습!");

        txtCount = findViewById(R.id.count);
        txtFruit = findViewById(R.id.fruit);

        findViewById(R.id.btnin).setOnClickListener(onClick);
        findViewById(R.id.btnde).setOnClickListener(onClick);

        findViewById(R.id.btnin).setOnLongClickListener(onLongClick);
        findViewById(R.id.btnde).setOnLongClickListener(onLongClick);

        findViewById(R.id.btnApple).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtFruit.setText("사과~!");
            }
        });
        findViewById(R.id.btnOrange).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtFruit.setText("오랜지~!!!");
            }
        });
    }

    View.OnClickListener onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.btnin) {
                count++;
            } else if (v.getId() == R.id.btnde) {
                count--;
            }
            txtCount.setText(String.valueOf(count));
        }
    };

    View.OnLongClickListener onLongClick = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            if (v.getId() == R.id.btnin) {
                count += 100;
            } else {
                count = 0;
            }
            txtCount.setText(String.valueOf(count));
            return true;
        }
    };
}