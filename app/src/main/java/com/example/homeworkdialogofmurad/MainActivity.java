package com.example.homeworkdialogofmurad;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText et_name;
    private EditText et_surname;
    private EditText et_city;
    private Button btn_birthday;
    private Button btn_sex;
    private Button btn_save;

    private Dialog selectSexDialog;
    private AlertDialog saveDialog;

    private RadioButton rbtn_man;
    private RadioButton rbtn_woman;

    final String KEY_NAME = "key_name";
    final String KEY_SURNAME = "key_surname";
    final String KEY_CITY = "key_city";
    final String KEY_BIRTHDAY = "key_birthday";
    final String KEY_SEX = "key_sex";

    String name;
    String surname;
    String city;
    String birthday;
    String sex;

    SharedPreferences sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        et_name = findViewById(R.id.et_name);
        et_surname = findViewById(R.id.et_surname);
        et_city = findViewById(R.id.et_city);
        btn_birthday = findViewById(R.id.btn_birthday);
        btn_sex = findViewById(R.id.btn_sex);
        btn_save = findViewById(R.id.btn_save);

        btn_birthday.setOnClickListener(this);
        btn_sex.setOnClickListener(this);
        btn_save.setOnClickListener(this);

        sp = getSharedPreferences("savedData", Context.MODE_PRIVATE);
        getSavedData();
    }

    //sets date
    public class SetDate implements DatePickerDialog.OnDateSetListener {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int day) {
            month = month + 1;
            btn_birthday.setText("You selected " + day + "/" + month + "/" + year);
        }
    }

    @Override
    public void onClick(View view) {
        //creates pop-uping calendar
        if ( view == btn_birthday ) {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datePickerDialog = new DatePickerDialog(this, new SetDate(), year, month, day);
            datePickerDialog.show();
        }
        if ( view == btn_sex ) {
            createSexDialog();
        }
        if ( view == btn_save ) {
            createSaveDialog();
        }
    }

    //creates dialog to choose user's sex
    public void createSexDialog() {
        selectSexDialog = new Dialog(this);
        selectSexDialog.setContentView(R.layout.select_sex_dialog_layout);
        selectSexDialog.setCancelable(true);

        rbtn_man = selectSexDialog.findViewById(R.id.rbtn_man);
        rbtn_woman = selectSexDialog.findViewById(R.id.rbtn_woman);

        rbtn_man.setOnClickListener(view -> {
            btn_sex.setText("Man");
            selectSexDialog.dismiss();
        });

        rbtn_woman.setOnClickListener(view -> {
            btn_sex.setText("Woman");
            selectSexDialog.dismiss();
        });

        selectSexDialog.show();
    }

    //creates alertDialog to choose if to save data or not
    public void createSaveDialog() {
        AlertDialog.Builder builder= new AlertDialog.Builder(this);
        builder.setTitle("User data save");
        builder.setMessage("Are you sure that you want to save the data");
        builder.setCancelable(true);

        builder.setPositiveButton("Yes", new positiveAlertDialogListener());

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                saveDialog.dismiss();
            }
        });
        saveDialog = builder.create();
        builder.show();
    }

    //method for activity that will happen after clicking on Yes in alertDialog
    public class positiveAlertDialogListener implements DialogInterface.OnClickListener{
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            SharedPreferences.Editor editor = sp.edit();

            editor.putString(KEY_NAME, et_name.getText().toString());
            editor.putString(KEY_SURNAME, et_surname.getText().toString());
            editor.putString(KEY_CITY, et_city.getText().toString());
            editor.putString(KEY_BIRTHDAY, btn_birthday.getText().toString());
            editor.putString(KEY_SEX, btn_sex.getText().toString());

            editor.commit();
            saveDialog.dismiss();
            Toast.makeText(MainActivity.this, "Data saved", Toast.LENGTH_LONG).show();
        }
    }

    //gets data saved in SharedPreferences
    public void getSavedData() {
        name = sp.getString(KEY_NAME, null);
        surname = sp.getString(KEY_SURNAME, null);
        city = sp.getString(KEY_CITY, null);
        birthday = sp.getString(KEY_BIRTHDAY, null);
        sex = sp.getString(KEY_SEX, null);

        if ( name != null ) {
            et_name.setText(name);
        }
        if ( surname != null ) {
            et_surname.setText(surname);
        }
        if ( city != null ) {
            et_city.setText(city);
        }
        if ( birthday != null ) {
            btn_birthday.setText(birthday);
        }
        if ( sex != null ) {
            btn_sex.setText(sex);
        }
    }

}