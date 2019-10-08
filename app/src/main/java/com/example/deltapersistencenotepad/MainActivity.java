package com.example.deltapersistencenotepad;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
//make some changes
public class MainActivity extends AppCompatActivity {
    @BindView(R.id.displayView)
    TextView readingView;
    @BindView(R.id.applicationRestarts)
    TextView appRestartsView;
//test
    private int howManyTimesBeenRun = 0;
    private static final String NUMBER_OF_TIMES_RUN_KEY = "NUMBER_OF_TIMES_RUN_KEY";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        SharedPreferences sharedPreferences = getPreferences(Context.MODE_PRIVATE);

        //read
        int defaultValue = 0;
        howManyTimesBeenRun = sharedPreferences.getInt(NUMBER_OF_TIMES_RUN_KEY, defaultValue);

        //first time message
        if (howManyTimesBeenRun == 0){
            Toast.makeText(this, "Welcome to your new magic notepad!", Toast.LENGTH_LONG).show();

        }
        howManyTimesBeenRun++;

        //write
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(NUMBER_OF_TIMES_RUN_KEY, howManyTimesBeenRun);
        editor.commit();

        appRestartsView.setText(String.valueOf(howManyTimesBeenRun));
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveTextFile(readingView.getText().toString());
    }

    @Override
    protected void onResume() {
        super.onResume();
        readingView.setText(getTextFile());
    }

    private static final String DATA_FILE = "my_file";

    //Reading and Writing

    public String getTextFile(){
        FileInputStream fileInputStream = null;
        String fileData = null;

        try {
            fileInputStream = openFileInput(DATA_FILE);
            int size = fileInputStream.available();
            byte[] buffer = new byte[size];
            fileInputStream.read(buffer);
            fileInputStream.close();
            fileData = new String(buffer, "UTF-8");

        }catch (FileNotFoundException e){
            Log.e("FILE", "Couldn't find that file");
            e.printStackTrace();
        }catch (Exception e){
            Log.e("FILE", "Error");
            e.printStackTrace();
        }finally {
            try {
                if (fileInputStream != null){
                    fileInputStream.close();
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }
        return fileData;
    }

    public void saveTextFile(String content){
        FileOutputStream fileOutputStream = null;

        try {
            fileOutputStream = openFileOutput(DATA_FILE,Context.MODE_PRIVATE);
            fileOutputStream.write(content.getBytes());

        }catch (FileNotFoundException e){
            e.printStackTrace();
        }catch (IOException e){
            Log.e("FILE", "IO Error");
            e.printStackTrace();
        }finally {
            try {
                if (fileOutputStream != null){
                    fileOutputStream.close();
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }

    }
}
