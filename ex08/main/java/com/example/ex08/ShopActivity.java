package com.example.ex08;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class ShopActivity extends AppCompatActivity { // 로그인 성공했을 때 여기로 옴
    FirebaseUser user;
    List<ShopVO> array = new ArrayList<>();
    FirebaseFirestore db;
    ShopAdapter adapter = new ShopAdapter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);

        user = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();

        getSupportActionBar().setTitle("List | " + user.getEmail());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //상품추가버튼클릭
        findViewById(R.id.btnInsert).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShopActivity.this, InsertActivity.class);
                startActivity(intent);
            }
        });

        getList();

        RecyclerView list = findViewById(R.id.list);
        list.setAdapter(adapter);
        list.setLayoutManager(new LinearLayoutManager(this));

    }//onCreate

    class ShopAdapter extends RecyclerView.Adapter<ShopAdapter.ViewHolder> {

        @NonNull
        @Override
        public ShopAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.item_shop, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ShopAdapter.ViewHolder holder, int position) {
            ShopVO vo = array.get(position);
            holder.title.setText(vo.getTitle());
            holder.email.setText(vo.getEmail());
            DecimalFormat df = new DecimalFormat("#,###원");
            holder.price.setText(df.format(vo.getPrice()));
            holder.date.setText(vo.getDate());
            Picasso.with(ShopActivity.this).load(vo.getImage()).into(holder.image);
            holder.item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ShopActivity.this, ReadActivity.class);
                    intent.putExtra("id", vo.getId());
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return array.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView title, price, date, email;
            ImageView image;
            RelativeLayout item;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                title = itemView.findViewById(R.id.title);
                price = itemView.findViewById(R.id.price);
                email = itemView.findViewById(R.id.email);
                date = itemView.findViewById(R.id.date);
                image = itemView.findViewById(R.id.image);
                item = itemView.findViewById(R.id.item);
            }
        }
    }

    public void getList() { //리스트 가져오는 함수
        db.collection("shop")
                .orderBy("date", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        array.clear();
                        for (QueryDocumentSnapshot doc : task.getResult()) {
                            ShopVO vo = new ShopVO();
                            vo.setId(doc.getId());
                            vo.setTitle(doc.getData().get("title").toString());
                            vo.setPrice(Integer.parseInt(doc.getData().get("price").toString()));
                            vo.setDate(doc.getData().get("date").toString());
                            vo.setEmail(doc.getData().get("email").toString());
                            vo.setImage(doc.getData().get("image").toString());
                            array.add(vo);
                        }
                        //System.out.println("..........." + array.size());
                        adapter.notifyDataSetChanged();
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

    @Override
    protected void onRestart() {
        super.onRestart();
        getList();
    }//onRestart

}//ShopActivity