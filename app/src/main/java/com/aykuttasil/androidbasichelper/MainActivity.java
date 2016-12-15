package com.aykuttasil.androidbasichelper;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.aykuttasil.androidbasichelperlib.PrefsHelper;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PrefsHelper.init(this).writeString("Key", "Value"); // Default Shared Preference
        PrefsHelper.writePrefString(this, "Ket", "Value"); // same above

        PrefsHelper.init(this, "PrefName").writeString("Key", "Value"); // context Shared Preference and PrefName = PrefName

    }
}
