package com.example.MedTest;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class MyActivity extends Activity {
    public ListView listView;
    ArrayList<String> list, result;
    EditText search;
    Button button;
    InputMethodManager imm;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        initApp();
    }

    /**
     * Initialising lists, listView, button and TextField (EditText) and application logic
     */
    private void initApp() {
        listView = (ListView) findViewById(R.id.list);
        list = new ArrayList<String>();
        result = new ArrayList<String>();
        search = (EditText) findViewById(R.id.editText);

        //when Enter button typed, searchClick() method also called
        search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_NULL && event.getAction() == KeyEvent.ACTION_DOWN) {
                    searchClick();
                }
                return true;
            }
        });

        imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);

        button = (Button) findViewById(R.id.button);
        //Setting listener to our button
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchClick();
            }
        });

        initList();

        //Attaching our search-results list to adapter
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.list_black_text,
                R.id.list_content,
                result);

        //Setting adapter
        listView.setAdapter(adapter);
    }

    /**
     * This method is used to open text file, iterate through it
     * and fill arrayList with question-answer strings
     */
    public void initList() {
        try {

            BufferedReader reader = new BufferedReader(new InputStreamReader(getAssets().open("list.txt"), "UTF-8"));
            StringBuilder stringBuilder = new StringBuilder();
            String temp;
            while ((temp = reader.readLine()) != null) {
                if (temp.isEmpty()) {
                    list.add(stringBuilder.toString());
                    stringBuilder = new StringBuilder();
                } else {
                    stringBuilder.append(temp + "\n");
                }
            }

            list.add(stringBuilder.toString());

            reader.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to handle search through list.
     * It iterates through list, find matches with keyword(search.getText().toString())
     * and fill result arrayList with it
     */
    public void searchClick() {
        String target = search.getText().toString();
        search.setText("");
        result.clear();

        //hides keyboard when button or enter typed
        imm.hideSoftInputFromWindow(search.getWindowToken(), 0);

        for (String temp : list) {
            if(temp.contains(target)){
                result.add(temp);
            }
        }
    }
}
