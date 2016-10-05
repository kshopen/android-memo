package com.example.kimsunghee.memoapplication;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Context context;
    private ViewPager pager;
    private ViewPagerAdapter viewPagerAdapter;
    private ListView listView;
    private SQLiteDatabase database;
    private DatabaseHelper databaseHelper;

    int p = 0;
    String databaseName = "memo1.db";
    String tableName = "todoTable";

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;
        getSupportActionBar().hide();

        // 1. DB를 연다.   >> onCreate() 호출 (첫 실행시) >> createTable() 호출 >> 기본인 todoTable 생성
        openDB();
        // 2. 뷰페이저를 세팅한다.
        setViewPager();
        // 3. 뷰페이저의 페이저가 넘어갈 때 테이블이 있는지 체크해서 없으면 createTable()을 호출해 각 페이지에 맞는 테이블 생성


    }

    // ================================== [ 메소드 ]==============================================
    // 1. DB 생성 / 열기
    public void openDB() {
        try {
            Toast.makeText(context, "openDB(): " + databaseName, Toast.LENGTH_LONG).show();
            //  database = openOrCreateDatabase(databaseName, Context.MODE_PRIVATE, null);

            databaseHelper = new DatabaseHelper(getApplicationContext(), databaseName, null, 1);
            database = databaseHelper.getWritableDatabase();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 2. 뷰페이저 설정
    private void setViewPager() {
        Toast.makeText(context, "setViewPager()", Toast.LENGTH_LONG).show();
        pager = (ViewPager) findViewById(R.id.pager);
        viewPagerAdapter = new ViewPagerAdapter(context);
        pager.setAdapter(viewPagerAdapter);

        findViewById(R.id.todobtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                p = 0;
                pager.setCurrentItem(0, true);
            }
        });

        findViewById(R.id.infobtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                p = 1;
                pager.setCurrentItem(1, true);
            }
        });

        findViewById(R.id.debtbtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                p = 2;
                pager.setCurrentItem(2, true);
            }
        });

        findViewById(R.id.etcbtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                p = 3;
                pager.setCurrentItem(3, true);
            }
        });

    }

    // 2. 테이블 만들기
    public void createTable(SQLiteDatabase db) {
        Toast.makeText(context, "createTable()", Toast.LENGTH_LONG).show();
        try {
            if (db != null) {
                db.execSQL("CREATE TABLE if not exists " + "todoTable" + "("
                        + "_id integer PRIMARY KEY autoincrement, "
                        + "memo text"
                        + ")");
                db.execSQL("CREATE TABLE if not exists " + "infoTable" + "("
                        + "_id integer PRIMARY KEY autoincrement, "
                        + "memo text"
                        + ")");
                db.execSQL("CREATE TABLE if not exists " + "debtTable" + "("
                        + "_id integer PRIMARY KEY autoincrement, "
                        + "memo text"
                        + ")");
                db.execSQL("CREATE TABLE if not exists " + "etcTable" + "("
                        + "_id integer PRIMARY KEY autoincrement, "
                        + "memo text"
                        + ")");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 3. 데이터 보여주기
    public void showData(SQLiteDatabase db) {
        Toast.makeText(context, "showData 호출", Toast.LENGTH_LONG).show();
        try {

            if (db != null) {
                Cursor cursor = db.rawQuery("SELECT _id, memo FROM " + tableName, null);

                startManagingCursor(cursor);

                String[] columns = new String[]{"memo"};    // DB에 들어가 있는 칼럼 이름
                int[] to = new int[]{R.id.memocontent};

                SimpleCursorAdapter adapter = new SimpleCursorAdapter(context, R.layout.content, cursor, columns, to);

                listView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    // ================================== [ 클래스 ]==============================================
    // 1. 데이터베이스 헬퍼클래스
    class DatabaseHelper extends SQLiteOpenHelper {

        public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            Toast.makeText(context, "Helper클래스의 onCreate() 호출", Toast.LENGTH_LONG).show();
            createTable(db);
        }

        public void onOpen(SQLiteDatabase db) {
            Toast.makeText(context, "Helper클래스의 onOpen() 호출", Toast.LENGTH_LONG).show();
            super.onOpen(db);

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int i, int i1) {
            Toast.makeText(context, "Helper클래스의 onUpgrade() 호출", Toast.LENGTH_LONG).show();
            //changeTable(db);
        }
    }

    // 2. 뷰페이저 어댑터
    class ViewPagerAdapter extends PagerAdapter {

        Context context;

        public ViewPagerAdapter(Context context) {
            this.context = context;
        }

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view.equals(object);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public Object instantiateItem(ViewGroup pager, int position) {
            Toast.makeText(context, "instantiateItem()", Toast.LENGTH_LONG).show();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.listview, null);
            listView = (ListView) view.findViewById(R.id.listView);

            switch (position) {
                case 0:
                    Toast.makeText(context, "todo화면", Toast.LENGTH_LONG).show();
                    tableName = "todoTable";
                    showData(database);
                    break;

                case 1:
                    Toast.makeText(context, "info화면", Toast.LENGTH_LONG).show();
                    tableName = "infoTable";
                    showData(database);
                    break;

                case 2:
                    Toast.makeText(context, "debt화면", Toast.LENGTH_LONG).show();
                    tableName = "debtTable";
                    showData(database);
                    break;

                case 3:
                    Toast.makeText(context, "etc화면", Toast.LENGTH_LONG).show();
                    tableName = "etctable";
                    showData(database);
                    break;
            }
            pager.addView(view);
            return view;
        }
    }
}
