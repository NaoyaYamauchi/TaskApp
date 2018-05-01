package com.example.na_sun.taskapp;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

public class InputActivity extends AppCompatActivity {
    private Realm mRealm;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private Button mDateButton, mTimeButton;
    private EditText mTitleEdit, mContentEdit;
    private Spinner mSpinner;
    private Task mTask;
    private Category mCategory;

    //日付
    private View.OnClickListener mOnDateClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            DatePickerDialog datePickerDialog = new DatePickerDialog(InputActivity.this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    mYear = year;
                    mMonth = month;
                    mDay = dayOfMonth;
                    String dateString = mYear + "/" + String.format("%02d", (mMonth + 1)) + "/" + String.format("%02d", mDay);
                    mDateButton.setText(dateString);
                }
            }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }
    };

    //時間
    private View.OnClickListener mOnTimeClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            TimePickerDialog timePickerDialog = new TimePickerDialog(InputActivity.this,
                    new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                            mHour = hourOfDay;
                            mMinute = minute;
                            String timeString = String.format("%02d", mHour) + ":" + String.format("%02d", mMinute);
                            mTimeButton.setText(timeString);
                        }
                    }, mHour, mMinute, false);
            timePickerDialog.show();
        }
    };

    private View.OnClickListener mOnDoneClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            addTask();
            finish();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);

        mRealm = Realm.getDefaultInstance();
        //ActionBarの設定
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        //UI部品の設定
        mDateButton = (Button) findViewById(R.id.date_button);
        mDateButton.setOnClickListener(mOnDateClickListener);
        mTimeButton = (Button) findViewById(R.id.times_button);
        mTimeButton.setOnClickListener(mOnTimeClickListener);
        findViewById(R.id.done_button).setOnClickListener(mOnDoneClickListener);
        mTitleEdit = (EditText) findViewById((R.id.title_edit_text));
        mContentEdit = (EditText) findViewById(R.id.content_edit_text);
        mSpinner = (Spinner) findViewById(R.id.category_spinner);

        //EXTRA_TASKからTaskのIdを取得。、idからTaskのインスタンス取得
        Intent intent = getIntent();
        int taskId = intent.getIntExtra(MainActivity.EXTRA_TASK, -1);
        Realm realm = Realm.getDefaultInstance();
        mTask = realm.where(Task.class).equalTo("id", taskId).findFirst();
        RealmResults<Category> results = realm.where(Category.class).findAll();
        List<Category> categoryList = mRealm.copyFromRealm(results);
        ArrayList<String> category = new ArrayList<String>();

        Log.d("ARRAYfirst", String.valueOf(results.size()));

        //初回起動時のみカテゴリを設定
        if (results.size() == 0) {
            Category category1 = new Category();
            Category category2 = new Category();

            mRealm.beginTransaction();
            category1.setCategory("未定義");
            category1.setId(0);
            mRealm.copyToRealmOrUpdate(category1);
            category2.setCategory("新規作成");
            category2.setId(1);
            mRealm.copyToRealmOrUpdate(category2);
            mRealm.commitTransaction();
            mRealm = Realm.getDefaultInstance();
        }
        realm.close();

        Log.d("ARRAYsecond", String.valueOf(results.size()));
        for (int i = 0; i < results.size(); i++) {
            category.add(categoryList.get(i).getCategory());
        }
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, category);
        mSpinner.setAdapter(arrayAdapter);

        if (mTask == null) {
            //新規作成の場合
            Calendar calendar = Calendar.getInstance();
            mYear = calendar.get(Calendar.YEAR);
            mMonth = calendar.get(Calendar.MONTH);
            mDay = calendar.get(Calendar.DAY_OF_MONTH);
            mHour = calendar.get(Calendar.HOUR_OF_DAY);
            mMinute = calendar.get(Calendar.MINUTE);
            //mSpinner.setSelection(Arrays.asList(categoryList).indexOf("未定義"));
            Log.d("indexof", String.valueOf(Arrays.asList(categoryList).indexOf("未定義")));
            Log.d("indexof", String.valueOf(categoryList.indexOf("未定義")));
            Log.d("indexof", String.valueOf(categoryList.size()));
        } else {
            //更新の場合
            mTitleEdit.setText(mTask.getTitle());
            mContentEdit.setText(mTask.getContents());

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(mTask.getDate());
            mYear = calendar.get(Calendar.YEAR);
            mMonth = calendar.get(Calendar.MONTH);
            mDay = calendar.get(Calendar.DAY_OF_MONTH);
            mHour = calendar.get(Calendar.HOUR_OF_DAY);
            mMinute = calendar.get(Calendar.MINUTE);

            String dateString = mYear + "/" + String.format("%02d", (mMonth + 1)) + "/"
                    + String.format("%02d", mDay);
            String timeString = String.format("%02d", mHour) + ":" + String.format("%02d", mMinute);
            mDateButton.setText(dateString);
            mTimeButton.setText(timeString);
            //カテゴリの取得
           // mSpinner.setSelection(Arrays.asList(mCategory).indexOf(mTask.getCategory()));
        }
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Spinner spinner = (Spinner) parent;
                //選択されたアイテムを取得
                String item = (String) spinner.getSelectedItem();

                Log.d("SPINERSEL", "root1");
                if (item.equals("新規作成")) {
                    Log.d("SPINERSEL", "root");
                    final EditText editText = new EditText(InputActivity.this);
                    new AlertDialog.Builder(InputActivity.this)
                            .setTitle("カテゴリ新規作成")
                            .setView(editText)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (editText.getText().toString() != null || !editText.getText().toString().isEmpty()) {
                                        //stringArray.add(editText.getText().toString());

                                    }
                                }
                            });
                    Intent intent = new Intent(InputActivity.this, CategoryActivity.class);
                    startActivity(intent);

                }
                Log.d("SPINERSEL", item);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void addTask() {
        Realm realm = Realm.getDefaultInstance();

        realm.beginTransaction();

        if (mTask == null) {
            //これも新規作成の場合
            mTask = new Task();

            RealmResults<Task> taskRealmResults = realm.where(Task.class).findAll();

            int identifier;
            if (taskRealmResults.max("id") != null) {
                identifier = taskRealmResults.max("id").intValue() + 1;
            } else {
                identifier = 0;
            }
            mTask.setId(identifier);
        }

        String title = mTitleEdit.getText().toString();
        String content = mContentEdit.getText().toString();
        String s_category = (String) mSpinner.getSelectedItem();

        Category category = realm.where(Category.class).equalTo("category",s_category).findFirst();

        mTask.setTitle(title);
        mTask.setContents(content);
        GregorianCalendar calendar = new GregorianCalendar(mYear, mMonth, mDay, mHour, mMinute);
        Date date = calendar.getTime();
        mTask.setDate(date);
        mTask.setCategory(category);

        realm.copyToRealmOrUpdate(mTask);
        realm.commitTransaction();

        realm.close();

        Intent resultIntent = new Intent(getApplicationContext(), TaskAlarmReceiver.class);
        resultIntent.putExtra(MainActivity.EXTRA_TASK, mTask.getId());
        PendingIntent resultPendingIntent = PendingIntent.getBroadcast(
                this, mTask.getId(), resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), resultPendingIntent);
    }
}
