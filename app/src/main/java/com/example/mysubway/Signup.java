package com.example.mysubway;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

public class Signup extends AppCompatActivity {

    Button btn_signup;
    TextInputEditText nickname_signup;
    TextInputEditText password_signup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        nickname_signup = findViewById(R.id.textinputedittext_NickName_signup);
        password_signup = findViewById(R.id.textinputedittext_password_signup);

        btn_signup = findViewById(R.id.button_signup);
        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nick_signup = nickname_signup.getText().toString();
                String pass_signup = password_signup.getText().toString();
                if(!MainActivity.nickOk.contains(nick_signup))MainActivity.nickOk.add(nick_signup);
                else Toast.makeText(getApplicationContext(),"이미 존재하는 아이디입니다",Toast.LENGTH_SHORT).show();

                if(!MainActivity.passwordOK.contains(pass_signup))MainActivity.passwordOK.add(pass_signup);

                //값을 주고받음(여기서, 일로가)
                Intent intent = new Intent(Signup.this, MainActivity.class);
                startActivity(intent);
            }
        });


        nickname_signup.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    //Enter키눌렀을떄 처리
                    password_signup.requestFocus();
                    return true;
                }
                return false;
            }
        });

        password_signup.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(password_signup.getWindowToken(), 0);

                    return true;
                }
                return false;
            }
        });

    }

}