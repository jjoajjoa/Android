package com.example.ex08;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class InsertActivity extends AppCompatActivity {
    FirebaseUser user;
    EditText title, price;
    FirebaseFirestore db;
    String strFile = "";
    ImageView image;
    FirebaseStorage storage;
    ProgressBar bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert);

        user = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();

        getSupportActionBar().setTitle("insert | " + user.getEmail());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        title = findViewById(R.id.title);
        price = findViewById(R.id.price);
        SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd HH:mm");
        image = findViewById(R.id.image);
        bar = findViewById(R.id.bar);

        //저장버튼클릭
        findViewById(R.id.btnSave).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strTitle = title.getText().toString();
                String strPrice = price.getText().toString();
                if (strTitle.equals("") || strPrice.equals("") || strFile.equals("")) {
                    Toast.makeText(InsertActivity.this, "plz enter the details", Toast.LENGTH_SHORT).show();
                } else {
                    AlertDialog.Builder box = new AlertDialog.Builder(InsertActivity.this);
                    box.setTitle("Confilm");
                    box.setMessage("really?");
                    box.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            bar.setVisibility(View.VISIBLE);
                            ShopVO vo = new ShopVO();
                            vo.setTitle(strTitle);
                            vo.setDate(sdf.format(new Date()));
                            vo.setEmail(user.getEmail());
                            vo.setPrice(Integer.parseInt(strPrice));
                            //insertShop(vo);
                            String fileName = System.currentTimeMillis() + ".jpg"; //새로운파일이름
                            Uri file = Uri.fromFile(new File(strFile)); //업로드할 파일
                            StorageReference ref = storage.getReference("/images/" + fileName);
                            ref.putFile(file)
                                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            Task<Uri> result = taskSnapshot.getStorage().getDownloadUrl();
                                            result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                @Override
                                                public void onSuccess(Uri uri) {
                                                    vo.setImage(uri.toString());
                                                    insertShop(vo);
                                                    bar.setVisibility(View.GONE);
                                                }
                                            });
                                        }
                                    });
                        }
                    });
                    box.setNegativeButton("no", null);
                    box.show();
                }
            }
        });

        //기본이미지 눌리면 갤러리로 이동
        findViewById(R.id.image).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 0);
            }
        });

    }//onCreate

    @SuppressLint("Range")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //갤러리에서 이미지 선택하면 미리보기
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            Cursor cursor = getContentResolver().query(data.getData(), null, null, null, null);
            cursor.moveToFirst();
            strFile = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            //System.out.println("..............." + strFile);
            cursor.close();
            image.setImageBitmap(BitmapFactory.decodeFile(strFile));
        }
    }

    public void insertShop(ShopVO vo) { //상품등록함수
        db.collection("shop")
                .add(vo)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(InsertActivity.this, "insert success!", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(InsertActivity.this, "insert failed..", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }//onOptionsItemSelected

}//InsertActivity