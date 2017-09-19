package com.bwie.app.activity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.bwie.app.R;
import com.bwie.app.util.RecordSQLiteOpenHelper;
import com.bwie.app.view.MyListView;

public class FindActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView back;
    private EditText et_search;
    private TextView tv_tip;
    private MyListView listView;
    private TextView tv_clear;
    private RecordSQLiteOpenHelper helper = new RecordSQLiteOpenHelper(this);;
    private SQLiteDatabase db;
    private BaseAdapter adapter;
    private Button findbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find);
        //找控件
        findid();
    }

    private void findid() {
        back= (ImageView) findViewById(R.id.back);
        et_search = (EditText) findViewById(R.id.et_search);
        tv_tip = (TextView) findViewById(R.id.tv_tip);
        listView = (MyListView) findViewById(R.id.listView);
        tv_clear = (TextView) findViewById(R.id.tv_clear);
        findbtn= (Button) findViewById(R.id.findbtn);

        back.setOnClickListener(this);
        tv_clear.setOnClickListener(this);
        findbtn.setOnClickListener(this);
        querylast();
        //edittext的监听
        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().trim().length() == 0) {
                    tv_tip.setText("搜索历史");
                } else {
                    tv_tip.setText("搜索结果");
                }
                String tempName = et_search.getText().toString();
                // 根据tempName去模糊查询数据库中有没有数据
                queryData(tempName);
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView textView = (TextView) view.findViewById(android.R.id.text1);
                String name = textView.getText().toString();
                et_search.setText(name);
                if(name.equals("劳力士")){
                    Intent intent=new Intent(FindActivity.this,FindOutActivity.class);
                    startActivity(intent);
                }

            }
        });
        // 第一次进入查询所有的历史记录
        queryData("");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                String name = querylast();
                Intent it=new Intent();
                //传值
                it.putExtra("name", name);
                //resultCode 结果码???
                setResult(500, it);
                finish();
                break;
            case R.id.tv_clear:
                deleteData();
                queryData("");
                break;
            case R.id.findbtn:
                boolean hasData = hasData(et_search.getText().toString().trim());
                if (!hasData) {
                    String edit=et_search.getText().toString().trim();
                    insertData(edit);
                    queryData("");
                }
                if("劳力士".equals(et_search.getText().toString().trim())){
                    Intent intent=new Intent(FindActivity.this,FindOutActivity.class);
                    startActivity(intent);
                }
                break;
        }
    }
    private void insertData(String tempName) {
        db = helper.getWritableDatabase();
        db.execSQL("insert into records(name) values('" + tempName + "')");
        db.close();
    }
    private boolean hasData(String tempName) {
        Cursor cursor = helper.getReadableDatabase().rawQuery(
                "select id as _id,name from records where name =?", new String[]{tempName});
        //判断是否有下一个
        return cursor.moveToNext();
    }
    private void queryData(String tempName) {
        Cursor cursor = helper.getReadableDatabase().rawQuery(
                "select id as _id,name from records where name like '%" + tempName + "%' order by id desc ", null);
        // 创建adapter适配器对象
        adapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_1, cursor, new String[] { "name" },
                new int[] { android.R.id.text1 }, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        // 设置适配器
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }

    private void deleteData() {
        db = helper.getWritableDatabase();
        db.execSQL("delete from records");
        db.close();
    }
    //查询数据库最后一次存入的数据
    public String querylast(){
        String name=null;
        db=helper.getReadableDatabase();
        String sql="select name from records";
        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()){
            if(cursor.isLast()){
                name = cursor.getString(cursor.getColumnIndex("name"));
                et_search.setText(name);
            }
        }
        return name;
    }
}
