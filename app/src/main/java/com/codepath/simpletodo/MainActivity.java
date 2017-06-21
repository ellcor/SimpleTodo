package com.codepath.simpletodo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    // field declarations: associated with any instance of Main
    ArrayList<String> items;
    ArrayAdapter<String> itemsAdapter;
    ListView lvItems;

    // called by Android when the activity is created
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // the superclass' logic will be executed first
        super.onCreate(savedInstanceState);

        // inflating the layout file from res/layout/activity_main.xml
        setContentView(R.layout.activity_main);

        // call method to initialize field variables
        readItems();

        // 1st argument: "this" = reference to main activity
        // 2nd: type of item that the adapter will wrap
        itemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);

        // need reference to list view so you can wire the adapter to it
        // framework already created listview --> don't need to create a new instance
        // must resolve instance that already exists by using id
        lvItems = (ListView) findViewById(R.id.lvItems);

        // wire adapter to listview
        lvItems.setAdapter(itemsAdapter);

        // create mock data
        // items.add("First item");
        // items.add("Second item");

        setupListViewListener();

    }

    public void onAddItem(View v) {

        EditText etNewItem = (EditText) findViewById(R.id.etNewItem);
        String itemText = etNewItem.getText().toString();

        // add text to list
        itemsAdapter.add(itemText);

        // clear text box so user can enter new item
        etNewItem.setText("");

        writeItems();

        Toast.makeText(getApplicationContext(), "Item added to list", Toast.LENGTH_SHORT).show();
    }

    private void setupListViewListener() {

        // logging: filtered by severity level
        // message called when app is created
        Log.i("MainActivity", "Setting up listener on list view");

        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                // more logging: only called when long click is intercepted on an item in the list
                Log.i("MainActivity", "Item removed from list: " + position);

                items.remove(position);
                itemsAdapter.notifyDataSetChanged();
                writeItems();
                return true;
            }
        });
    }

    private File getDataFile() {

        return new File(getFilesDir(), "todo.txt");
    }

    // read from the file
    private void readItems() {

        // initializd items array using contents of the file
        try {
            items = new ArrayList<>(FileUtils.readLines(getDataFile(), Charset.defaultCharset()));
        } catch (IOException e) {
            Log.e("MainActivity", "Error reading file", e);
            items = new ArrayList<>();
        }

    }

    // write to the file (every time the model is changed so that what is persisted to the file
    // system is always reflected on the screen)
    private void writeItems() {

        try {
            FileUtils.writeLines(getDataFile(), items);
        } catch (IOException e) {
            Log.e("MainActivity", "Error writing file", e);
        }
    }

}



















