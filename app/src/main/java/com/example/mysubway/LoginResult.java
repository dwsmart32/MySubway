package com.example.mysubway;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.io.InputStream;
import java.util.LinkedList;
import java.util.Scanner;

public class LoginResult extends AppCompatActivity  {

    TextView get;
    ListView listview_dep;
    ListView listview_des;
    EditText edt_dep;
    EditText edt_des;
    boolean dep_focus = true;
    boolean des_focus = true;
    Button cal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        //get과 Textview_get과 연동
        get = findViewById(R.id.Textview_get);

        //받는거
        Intent intent = getIntent();

        //Extra다 가져와서
        Bundle bundle = intent.getExtras();

        //그중에 nick과
        String nickname = bundle.getString("nick");

        //그중에 password를 받아온다.
        String password = bundle.getString("password");
        //바꿔치기
        get.setText(nickname);

        //검색
        listview_dep = (ListView) findViewById(R.id.Listview_dep);
        listview_des = (ListView) findViewById(R.id.Listview_des);
        edt_dep = (EditText) findViewById(R.id.editText_dep);
        edt_des = (EditText) findViewById(R.id.editText_des);


        LinkedList<String> data = new LinkedList<>();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, data);

        InputStream in = getResources().openRawResource(R.raw.datastation);
        Scanner sc = new Scanner(in, "UTF-8");

        while (sc.hasNextLine())
            data.add(sc.nextLine());

        listview_dep.setAdapter(adapter);
        listview_des.setAdapter(adapter);
        listview_dep.setTextFilterEnabled(true);
        listview_des.setTextFilterEnabled(true);
        listview_dep.setVisibility(View.INVISIBLE);
        listview_des.setVisibility(View.INVISIBLE);
        final TextWatcher textWatcher_dep = new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(dep_focus)listview_dep.setFilterText(edt_dep.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                //if (edt_dep.getText().length() == 0)  listview_dep.clearTextFilter();
                if (edt_dep.length() > 0 && edt_des.length()>0) {
                    cal.setClickable(true);
                    cal.setBackgroundColor(Color.BLUE);
                } else {
                    cal.setClickable(false);
                    cal.setBackgroundColor(Color.GRAY);
                }
            }

        };

        final TextWatcher textWatcher_des = new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(des_focus)listview_des.setFilterText(edt_des.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                //if (edt_des.getText().length() == 0)  listview_des.clearTextFilter();
                if (edt_dep.length() > 0 && edt_des.length()>0) {
                    cal.setClickable(true);
                    cal.setBackgroundColor(Color.BLUE);
                } else {
                    cal.setClickable(false);
                    cal.setBackgroundColor(Color.GRAY);
                }

            }

        };

        edt_dep.addTextChangedListener(textWatcher_dep);
        edt_des.addTextChangedListener(textWatcher_des);

        //list_dep 클릭이벤트
        listview_dep.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String data = (String) parent.getItemAtPosition(position);
                //그 글자로 채워주고
                edt_dep.setText(data);
                //listview 안보이게
                listview_dep.setVisibility(View.INVISIBLE);
            }
        });
        //list_des 클릭이벤트
        listview_des.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String data = (String) parent.getItemAtPosition(position);
                edt_des.setText(data);
                listview_des.setVisibility(View.INVISIBLE);
            }
        });


        //edt_dep 키 이벤트 1. 엔터시 des으로 간다.
        edt_dep.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    //Enter키눌렀을떄 처리
                    edt_des.requestFocus();
                    return true;
                }
                return false;
            }
        });

        edt_des.setOnKeyListener(new View.OnKeyListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(edt_des.getWindowToken(), 0);
                    return true;
                }
                return false;
            }
        });
        //dep포커스
        edt_dep.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    dep_focus = true;
                    listview_dep.setVisibility(View.VISIBLE);
                    listview_des.setVisibility(View.INVISIBLE);
                } else dep_focus = false;
                if (dep_focus && edt_dep.length() > 0)
                    listview_dep.setFilterText(edt_dep.getText().toString());
                if (des_focus && edt_des.length() > 0)
                    listview_des.setFilterText(edt_des.getText().toString());

                InputMethodManager immhide = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                immhide.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
            }
        });
        //des포커스
        edt_des.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    des_focus = true;
                    listview_des.setVisibility(View.VISIBLE);
                    listview_dep.setVisibility(View.INVISIBLE);
                } else des_focus = false;
                if (dep_focus && edt_dep.length() > 0)
                    listview_dep.setFilterText(edt_dep.getText().toString());
                if (des_focus && edt_des.length() > 0)
                    listview_des.setFilterText(edt_des.getText().toString());

                InputMethodManager immhide = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                immhide.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
            }
        });
        //dep클릭 이벤트
        edt_dep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listview_dep.setVisibility(View.VISIBLE);
                listview_des.setVisibility(View.INVISIBLE);
            }
        });
        //des클릭 이벤트
        edt_des.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listview_des.setVisibility(View.VISIBLE);
                listview_dep.setVisibility(View.INVISIBLE);
            }
        });


        cal = findViewById(R.id.Button_cal);
        cal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Dep = edt_dep.getText().toString();
                String Des = edt_des.getText().toString();
                Intent intent2 = new Intent(LoginResult.this, calculation.class);
                intent2.putExtra("Des", Des);
                intent2.putExtra("Dep", Dep);
                startActivity(intent2);
            }
        });
    }

}
