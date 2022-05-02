package com.example.qrjavasc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.zxing.Result;

import org.w3c.dom.Text;

import java.security.PrivateKey;

public class MainActivity extends AppCompatActivity{
    private CodeScanner mCodeScanner;
    private TextView textView;


    public static final String TEXT ="text";
    public static final String SHARED_PREFS = "sharedPrefs";

    private String text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);


        Button buttonSave = findViewById(R.id.SaveButton);
        Button buttonKabul = findViewById(R.id.kabul_button);
        Button buttonRed = findViewById(R.id.red_button);
        TextView metin = findViewById(R.id.code_text);
        CodeScannerView scannerView = findViewById(R.id.Scanner);
        textView = (TextView) findViewById(R.id.textview);


        buttonRed.setVisibility(View.GONE);
        buttonKabul.setVisibility(View.GONE);
        buttonSave.setVisibility(View.GONE);
        textView.setVisibility(View.INVISIBLE);


        metin.setText("KOD BEKLENİYOR");
        mCodeScanner = new CodeScanner(this, scannerView);
        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull Result result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, result.getText(), Toast.LENGTH_SHORT).show();
                        if (textView.getText().toString().equals("")){
                            metin.setText("Şifre olarak belirlemek istediğinize emin misiniz?");
                            buttonSave.setVisibility(View.VISIBLE);
                            buttonRed.setVisibility(View.VISIBLE);
                            buttonSave.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    saveData();
                                    loadData();
                                    updateViews();

                                }
                                public void saveData() {
                                    SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();

                                    editor.putString(TEXT, result.toString());

                                    editor.apply();

                                    Toast.makeText(MainActivity.this, "data saved", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(MainActivity.this, MainActivity.class));

                                }

                            });

                        }else {

                            if (result.toString().equals(textView.getText().toString())) {
                                buttonKabul.setVisibility(View.VISIBLE);
                                buttonRed.setVisibility(View.VISIBLE);
                                metin.setText("HOŞGELDİNİZ");
                                buttonKabul.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        startActivity(new Intent(MainActivity.this, MainActivity3.class));
                                    }

                                });
                                buttonRed.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        buttonRed.setVisibility(View.GONE);
                                        buttonKabul.setVisibility(View.GONE);
                                        metin.setText("KOD BEKLENİYOR");
                                        mCodeScanner.startPreview();
                                    }
                                });

                            }else{

                                buttonRed.setVisibility(View.VISIBLE);
                                metin.setText("HATALI KOD");


                                buttonRed.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        buttonRed.setVisibility(View.GONE);
                                        buttonKabul.setVisibility(View.GONE);
                                        metin.setText("KOD BEKLENİYOR");
                                        mCodeScanner.startPreview();


                                    }
                                });

                            }
                        }

                    }
                });
            }

        });

        loadData();
        updateViews();

    }
    @Override
    protected void  onResume(){
        super.onResume();
        mCodeScanner.startPreview();
    }

    @Override
    protected void  onPause() {
        mCodeScanner.releaseResources();
        super.onPause();
    }
    @Override
    public void onBackPressed() {
        startActivity(new Intent(MainActivity.this, MainActivity2.class));
    }

    public void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        text = sharedPreferences.getString(TEXT, "");
    }
    public void updateViews() {
        textView.setText(text);
    }





}