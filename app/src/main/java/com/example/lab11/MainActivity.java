package com.example.lab11;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private EditText edBook,edPrice;
    private Button btnFind,btnNew,btnUpdate,btnDel;

    private ListView list;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> items = new ArrayList<>();

    private SQLiteDatabase dbrw;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        edBook = findViewById(R.id.bookname);
        edPrice = findViewById(R.id.price);
        btnNew = findViewById(R.id.btn_new);
        btnFind = findViewById(R.id.btn_find);
        btnUpdate = findViewById(R.id.btn_update);
        btnDel = findViewById(R.id.btn_del);
        list = findViewById(R.id.listview);
        adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1,items);
        list.setAdapter(adapter);
        dbrw = new MyDBHelper(this).getWritableDatabase();

        btnFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cursor c;
                if(edBook.length()<1)
                    c = dbrw.rawQuery("SELECT * FROM mytable",null);
                else
                    c = dbrw.rawQuery("SELECT * FROM myTable WHERE book LIKE '" +
                            edBook.getText().toString() + "'",null);
                c.moveToFirst();
                items.clear();
                Toast.makeText(MainActivity.this,"共有"+c.getCount()
                        +"筆資料",Toast.LENGTH_SHORT).show();
                for(int i =0;i<c.getCount();i++){
                    items.add("書名:" + c.getString(0)+"\t\t\t\t 價格:" + c.getString(1));
                    c.moveToNext();
                }
                adapter.notifyDataSetChanged();
                c.close();
            }
        });
        btnNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(edBook.length()<1 || edPrice.length()<1)
                    Toast.makeText(MainActivity.this,"欄位請勿為空",
                            Toast.LENGTH_SHORT).show();
                else{
                    try {
                        dbrw.execSQL("INSERT INTO myTable(book, price) VALUES(?,?)",
                                new Object[]{edBook.getText().toString(),
                                        edPrice.getText().toString()});
                        Toast.makeText(MainActivity.this, "新增書名" + edBook.getText().toString()
                                + "    價格" + edPrice.getText().toString(), Toast.LENGTH_SHORT).show();
                        edPrice.setText("");
                        edBook.setText("");
                    }catch (Exception e){
                        Toast.makeText(MainActivity.this,"新增失敗:"+
                                        e.toString(),Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(edBook.length()<1 || edPrice.length()<1)
                    Toast.makeText(MainActivity.this,"欄位請勿為空",
                            Toast.LENGTH_SHORT).show();
                else{
                    try {
                        dbrw.execSQL("UPDATE myTable SET price = "+
                                    edPrice.getText().toString() + " WHERE book LIKE '" +
                                    edBook.getText().toString() + "'");
                        Toast.makeText(MainActivity.this, "更新書名" + edBook.getText().toString()
                                + "    價格" + edPrice.getText().toString(), Toast.LENGTH_SHORT).show();
                        edPrice.setText("");
                        edBook.setText("");
                    }catch (Exception e){
                        Toast.makeText(MainActivity.this,"更新失敗:"+
                                e.toString(),Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
        btnDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(edBook.length()<1)
                    Toast.makeText(MainActivity.this,"書名請勿為空",
                            Toast.LENGTH_SHORT).show();
                else{
                    try {
                        dbrw.execSQL("DELETE FROM myTable WHERE book LIKE '" +
                                        edBook.getText().toString() + "'");
                        Toast.makeText(MainActivity.this, "刪除書名" + edBook.getText().toString()
                                ,Toast.LENGTH_SHORT).show();
                        edPrice.setText("");
                        edBook.setText("");
                    }catch (Exception e){
                        Toast.makeText(MainActivity.this,"刪除失敗:"+
                                e.toString(),Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }
/*    @Override
    public void onDestory(){
        super.onDestroy();
        dbrw.close();
    }*/

}