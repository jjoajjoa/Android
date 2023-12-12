package com.example.ex02;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.renderscript.Sampler;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity2 extends AppCompatActivity {
    TextView result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        getSupportActionBar().setTitle("대화상자");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        result = findViewById(R.id.result);

        // 대화상자 호출버튼 클릭
        Button btn = findViewById(R.id.btn1);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(MainActivity2.this)
                        .setTitle("Alert")
                        .setMessage("대화상자 입니당")
                        .setPositiveButton("확인", null)
                        .show();
            }
        });

        Button btn2 = findViewById(R.id.btn2);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(MainActivity2.this)
                        .setTitle("Confirm")
                        .setMessage("저장하시겠슴가?")
                        .setPositiveButton("저장", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(MainActivity2.this, "저장완료!", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("취소", null)
                        .show();
            }
        });

        Button btn3 = findViewById(R.id.btn3);
        btn3.setOnClickListener(new View.OnClickListener() {
            int selectIndex = 0;

            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(MainActivity2.this)
                        .setTitle("Value")
                        .setSingleChoiceItems(R.array.foods, 0, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                selectIndex = which;
                            }
                        })
                        .setPositiveButton("선택", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String[] foods = getResources().getStringArray(R.array.foods);
                                Toast.makeText(MainActivity2.this, "선택한거: " + foods[selectIndex], Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("취소", null)
                        .show();
            }
        });

        Button btn4 = findViewById(R.id.btn4);
        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout layout = (LinearLayout) View.inflate(MainActivity2.this, R.layout.custom, null);
                new AlertDialog.Builder(MainActivity2.this)
                        .setTitle("Custom")
                        .setView(layout)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                EditText name = layout.findViewById(R.id.name);
                                EditText price = layout.findViewById(R.id.price);
                                CheckBox chk = layout.findViewById(R.id.chk);
                                String strName = name.getText().toString();
                                String strPrice = price.getText().toString();
                                String strChk = chk.isChecked() ? "Y" : "N" ;
                                String str = "상품명: " + strName + "\n";
                                str += "상품가격: " + strPrice + "\n";
                                str += "착불유무: " + strChk;
                                //Toast.makeText(MainActivity2.this, str, Toast.LENGTH_SHORT).show();
                                result.setText(str);
                            }
                        })
                        .setNegativeButton("NO", null)
                        .show();
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

}