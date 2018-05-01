package com.example.na_sun.taskapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Date;
import java.util.GregorianCalendar;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

public class CategoryActivity extends AppCompatActivity {

    private Realm mRealm= Realm.getDefaultInstance();
    private Category mCategory;
    private EditText mEditText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        findViewById(R.id.done_button).setOnClickListener(mOnDoneClickListener);
        findViewById(R.id.cancel_button).setOnClickListener(mOnCancelClickListener);
        mEditText = (EditText)findViewById(R.id.title_edit_text);

    }
    private View.OnClickListener mOnDoneClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            addCategory();
            finish();
        }
    };
    private  View.OnClickListener mOnCancelClickListener= new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            finish();
        }
    };

    private void addCategory(){
        RealmResults<Category> results = mRealm.where(Category.class).findAll();
        Log.d("android", String.valueOf(results.size()));

        mRealm.beginTransaction();

        Category category = new Category();
        String s_category = mEditText.getText().toString();

        category.setCategory(s_category);
        category.setId(results.size());
        mRealm.copyToRealmOrUpdate(category);
        mRealm.commitTransaction();
        mRealm = Realm.getDefaultInstance();

        mRealm.close();
    }

}
