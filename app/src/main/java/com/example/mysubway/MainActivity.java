package com.example.mysubway;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.CharacterPickerDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.LinkedList;

public class MainActivity extends AppCompatActivity {


    TextInputEditText Password;
    TextInputEditText NickName;
    LinearLayout Login;
    TextView fg;
    TextView signup;


    public static LinkedList<String> nickOk = new LinkedList<>();
    public static LinkedList<String> passwordOK = new LinkedList<>();



    String inputnick="";
    String inputpass="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        //중복제거
        if(!nickOk.contains("ldw"))
        nickOk.add("ldw");
        if(!passwordOK.contains("971123"))
        passwordOK.add("971123");



        Password    = findViewById(R.id.textinputedittext_password);
        NickName    = findViewById(R.id.textinputedittext_NickName);
        Login       = findViewById(R.id.Linearlayout_Login);

        Login.setEnabled(false);

        fg=findViewById(R.id.Textview_forgot);
        signup=findViewById(R.id.Textview_SignUp);

        fg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"이런! 개발자에게 물어보세요",Toast.LENGTH_SHORT).show();
            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(),"회원가입을 원하나요? 개발자에게 가세요",Toast.LENGTH_SHORT).show();
                Intent intent2 = new Intent(MainActivity.this, Signup.class);
                startActivity(intent2);
            }
        });
        //1. 값을 가져온다.(완) - 검사(미) (ldw 971123)
        //2. 클릭을 감지한다.(완)
        //3. 1번의 값을 다음화면으로 넘긴다.

//        NickName.setClickable(false);
        NickName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //Log.d("input_nick", s+"/");
                //Log.d("0th",nickOk.get(0));
                //Log.d("0thpass",passwordOK.get(0));
                //if(nickOk.size()==2) {Log.d("1th",nickOk.get(1)); Log.d("1thpa",passwordOK.get(1));}
                //Log.d("size:",Integer.toString(nickOk.size()));
                if (s != null) inputnick = s.toString();

                for (int i = 0; i < nickOk.size(); i++) {
                    if (validataion(i)) {
                        Login.setEnabled(true);
                        break;
                    } else Login.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });


        Password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //Log.d("input_password", s+"/");
                if(s!=null) inputpass=s.toString();

                for(int i =0; i<nickOk.size();i++) {
                    if(validataion(i)) {
                        Login.setEnabled(true); Log.d("validation", validataion(i)+"/");break;
                    }
                    else Login.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });



        Login.setOnClickListener(new View.OnClickListener() { // 리스너를 달건데 그게 이거다
            @Override
            public void onClick(View v) {

                //값을 가져옴
                String nick = NickName.getText().toString();
                String password = Password.getText().toString();
                //값을 주고받음(여기서, 일로가)
                Intent intent = new Intent(MainActivity.this, LoginResult.class);
                intent.putExtra("nick",nick);
                intent.putExtra("password",password);
                startActivity(intent);
            }
        });

        NickName.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    //Enter키눌렀을떄 처리
                    Password.requestFocus();
                    return true;
                }
                return false;
            }
        });

        Password.setOnKeyListener(new View.OnKeyListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(Password.getWindowToken(), 0);

                    return true;
                }
                return false;
            }
        });



    }

    public boolean validataion(int i){
        //Log.d("dw", inputnick.equals(nickOk[i]) +"/" + inputnick.equals(nickOk[i]));
        return inputnick.equals(nickOk.get(i)) && inputpass.equals(passwordOK.get(i));

    }

}