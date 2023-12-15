package com.example.ex05;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BlogFragment extends Fragment {
    String query = "민지";
    int page = 1;
    List<HashMap<String, Object>> array = new ArrayList<>();
    ListView list;
    BlogAdapter adapter;
    Boolean is_end = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_blog, container, false);
        new BlogThread().execute();
        list = view.findViewById(R.id.list);
        adapter = new BlogAdapter();
        list.setAdapter(adapter);
        EditText edtQuery = view.findViewById(R.id.query);

        view.findViewById(R.id.search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                query = edtQuery.getText().toString();
                array = new ArrayList<>();
                page = 1;
                new BlogThread().execute();
            }
        });

        view.findViewById(R.id.more).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (is_end) {
                    Toast.makeText(getActivity(), "last Page", Toast.LENGTH_SHORT).show();
                } else {
                    page += 1;
                    new BlogThread().execute();
                }
            }
        });

        return view;
    }//onCreateView

    class BlogAdapter extends BaseAdapter { //Blog 어답터 생성

        @Override
        public int getCount() {
            return array.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override //데이터 가져오는 곳
        public View getView(int position, View view, ViewGroup parent) {
            view = getLayoutInflater().inflate(R.layout.item_blog, parent, false);
            HashMap<String, Object> map = array.get(position);
            TextView title = view.findViewById(R.id.title);
            String strTitle = map.get("title").toString();
            title.setText(Html.fromHtml(strTitle));
            TextView contents = view.findViewById(R.id.contents);
            String strContents = map.get("contents").toString();
            contents.setText(Html.fromHtml(strContents));

            String url = map.get("url").toString();
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), WebActivity.class);
                    intent.putExtra("url", url);
                    startActivity(intent);
                }
            });

            return view;
        }

    }//BlogAdapter

    class BlogThread extends AsyncTask<String, String, String> {//Blog 쓰레드 생성

        @Override
        protected String doInBackground(String... strings) {
            String url = "https://dapi.kakao.com/v2/search/web?query=" + query + "&page=" + page;
            String result = KakaoAPI.connect(url);
            //System.out.println("..........-> " + result);
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            blogParser(s);
            //System.out.println("......size: " + array.size());
            adapter.notifyDataSetChanged();
        }

    }//BlogThread

    public void blogParser(String result) {
        try {
            JSONObject meta = new JSONObject(result).getJSONObject("meta");
            is_end = meta.getBoolean("is_end");

            JSONArray jArray = new JSONObject(result).getJSONArray("documents");
            for (int i = 0; i < jArray.length(); i++) {
                JSONObject obj = jArray.getJSONObject(i);
                HashMap<String, Object> map = new HashMap<>();
                map.put("title", obj.getString("title"));
                map.put("url", obj.getString("url"));
                map.put("contents", obj.getString("contents"));
                array.add(map);
            }
        } catch (Exception e) {
            System.out.println(".......parserErr: " + e.toString());
        }
    }//blogParser


}//BlogFragment