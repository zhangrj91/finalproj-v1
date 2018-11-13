package com.example.jason.finalproj.jump;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class JumpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new JumpView(JumpActivity.this));
    }
}
