package com.example.ex01;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity4 extends AppCompatActivity {
    EditText name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);
        name = findViewById(R.id.name);

        //DATA 생성
        List<String> array = new ArrayList<>();
        array.add("김쪼랭");
        array.add("김부추");
        array.add("김몽청");

        //어댑터 생성
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_single_choice, array);

        //ListView에 어댑터 연결
        ListView list = findViewById(R.id.list);
        list.setAdapter(adapter);
        list.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        //등록버튼 클릭
        findViewById(R.id.insesrt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strName = name.getText().toString();
                if (strName.equals("")) {
                    Toast.makeText(MainActivity4.this, "이름을 입력하세용", Toast.LENGTH_SHORT).show();
                } else {
                    array.add(strName);
                    adapter.notifyDataSetChanged();
                    name.setText("");
                }
            }
        });

        //삭제버튼 클릭
        findViewById(R.id.delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = list.getCheckedItemPosition();
                if (position == -1) {
                    Toast.makeText(MainActivity4.this, "선택할 아이템을 선택하세용", Toast.LENGTH_SHORT).show();
                } else {
                    array.remove(position);
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }
}