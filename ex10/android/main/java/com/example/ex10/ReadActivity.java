package com.example.ex10;

import static com.example.ex10.RemoteService.BASE_URL;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Multipart;

public class ReadActivity extends AppCompatActivity {
    EditText title, price;
    ImageView image;
    ShopVO vo = new ShopVO();
    String strFile = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read);

        Intent intent = getIntent();
        int pid = intent.getIntExtra("pid", 0);

        getSupportActionBar().setTitle("정보수정 | " + pid);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        title = findViewById(R.id.title);
        price = findViewById(R.id.price);
        image = findViewById(R.id.image);

        RemoteService service = ShopAPI.call();
        Call<ShopVO> call = service.read(pid);
        call.enqueue(new Callback<ShopVO>() {
            @Override
            public void onResponse(Call<ShopVO> call, Response<ShopVO> response) {
                vo = (ShopVO) response.body();
                System.out.println("res................." + vo.toString());
                title.setText(vo.getTitle());
                price.setText(String.valueOf(vo.getLprice()));
                if (vo.getImage().equals("")) {
                    image.setImageResource(R.drawable.ic_android_black_24dp);
                } else {
                    String url = BASE_URL + "/display?file=" + vo.getImage();
                    Picasso.with(ReadActivity.this).load(url).into(image);
                }
            }

            @Override
            public void onFailure(Call<ShopVO> call, Throwable t) {

            }
        });

        findViewById(R.id.btnInfo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder box = new AlertDialog.Builder(ReadActivity.this);
                box.setTitle("confirm");
                box.setMessage("really?");
                box.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        vo.setTitle(title.getText().toString());
                        vo.setLprice(Integer.parseInt(price.getText().toString()));
                        updateInfo(vo);
                    }
                });
                box.setNegativeButton("no", null);
                box.show();
            }
        });

        findViewById(R.id.image).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 0);
            }
        });

        findViewById(R.id.btnImage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder box = new AlertDialog.Builder(ReadActivity.this);
                if (strFile.equals("")) {
                    box.setTitle("Alert");
                    box.setMessage("image check");
                    box.setPositiveButton("ok", null);
                    box.show();
                } else {
                    box.setTitle("confirm");
                    box.setMessage("really?");
                    box.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            RequestBody reqId = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(pid));
                            File file = new File(strFile);
                            RequestBody reqFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                            MultipartBody.Part partFile = MultipartBody.Part.createFormData("file", file.getName(), reqFile);
                            uploadImage(reqId, partFile);
                        }
                    });
                    box.setNegativeButton("No", null);
                    box.show();
                }
            }
        });

    }//onCreate

    public void uploadImage(RequestBody pid, MultipartBody.Part file) {
        RemoteService service = ShopAPI.call();
        Call<Void> call = service.upload(pid, file);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Toast.makeText(ReadActivity.this, "success", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(ReadActivity.this, "failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void updateInfo(ShopVO vo) {
        RemoteService service = ShopAPI.call();
        Call<Void> call = service.update(vo);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Toast.makeText(ReadActivity.this, "success", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(ReadActivity.this, "failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @SuppressLint("Range")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            String[] projection = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(data.getData(), projection, null, null, null);
            cursor.moveToFirst();
            strFile = cursor.getString(cursor.getColumnIndex(projection[0]));
            image.setImageBitmap(BitmapFactory.decodeFile(strFile));
            cursor.close();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}