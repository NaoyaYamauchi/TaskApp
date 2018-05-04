package com.example.na_sun.taskapp;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import io.realm.Realm;
import io.realm.RealmResults;

public class CategoryActivity extends AppCompatActivity {

    Category mCategory = new Category();
    boolean overlap = false;
    private Realm mRealm;
    private EditText mEditText;
    private View.OnClickListener mOnDoneClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            RealmResults<Category> results = mRealm.where(Category.class).findAll();

            //既存カテゴリのダブり判定
            for (Category cat : results) {
                if (mEditText.getText().toString().equals(cat.getCategory().toString())) {
                    overlap = true;
                    Log.d("categoryList", "root");
                }
                Log.d("categoryList", mEditText.getText().toString() + ":" + cat.getCategory());
            }
            //ダブってたら登録できない
            if (!overlap) {
                //空欄判定
                if (mEditText.getText().toString().isEmpty()) {
                    Snackbar.make(v, "せめて何か入れてください", Snackbar.LENGTH_LONG).show();
                } else {
                    addCategory();
                    finish();
                }
            } else {
                Snackbar.make(v, "同じカテゴリが登録されています", Snackbar.LENGTH_LONG).show();
            }
        }
    };
    private View.OnClickListener mOnCancelClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        findViewById(R.id.done_button).setOnClickListener(mOnDoneClickListener);
        findViewById(R.id.cancel_button).setOnClickListener(mOnCancelClickListener);
        mEditText = (EditText) findViewById(R.id.title_edit_text);

        mRealm = Realm.getDefaultInstance();

    }

    private void addCategory() {
        RealmResults<Category> results = mRealm.where(Category.class).findAll();
        Log.d("android", String.valueOf(results.size()));

        mRealm.beginTransaction();


        String s_category = mEditText.getText().toString();

        mCategory.setCategory(s_category);
        mCategory.setId(results.size());
        mRealm.copyToRealmOrUpdate(mCategory);
        mRealm.commitTransaction();
        mRealm = Realm.getDefaultInstance();

        mRealm.close();
    }
}
