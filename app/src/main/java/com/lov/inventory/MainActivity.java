package com.lov.inventory;

import android.content.Intent;
import android.database.Cursor;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.lov.inventory.database.DatabaseHelper;
import com.lov.inventory.database.DatabaseManager;

public class MainActivity extends AppCompatActivity {
    private DatabaseManager dbManager;
    private ListView listView;
    private SimpleCursorAdapter adapter;
    private DatabaseHelper dbHelper;
    final String[] from = new String[]{dbHelper._ID, dbHelper.TITLE, dbHelper.DESC};
    final int[] to = new int[] {R.id.id, R.id.listTitle, R.id.listDesc};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        dbManager = new DatabaseManager( this);
        dbManager.open();
        Cursor cursor = dbManager.fetch();

        listView = (ListView) findViewById(R.id.myListView);

        adapter = new SimpleCursorAdapter(  this, R.layout.adapter, cursor, from, to,0);
        listView.setAdapter(adapter);

        try {
            Intent intent = getIntent();
            Boolean ItemDeleted = intent.getExtras ().getBoolean( "ItemDeleted");
            Modify modify = new Modify();
            if (ItemDeleted) {
                Snackbar.make(listView, "ItemDeleted!", Snackbar.LENGTH_LONG).show();
                modify.setItemDeleted(false);
            }
        }catch (Exception e) {
            if (adapter.isEmpty()) {
                Snackbar.make(listView,"Click on fab to add list", Snackbar. LENGTH_LONG).show();
            } else {
                Snackbar.make(listView, "Hold on item to modify", Snackbar. LENGTH_LONG).show();
            }
        }
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View view, int pos, long id) {
                TextView itemID = (TextView) view.findViewById(R.id.id);
                TextView itemTitle = (TextView) view.findViewById(R.id.listTitle);
                TextView itemDesc = (TextView) view.findViewById(R.id.listDesc);
                String myId = itemID.getText().toString();
                String myTitle = itemTitle.getText().toString();
                String myDesc = itemDesc.getText().toString();
                Intent intent = new Intent(getApplicationContext(),Modify.class);
                intent.putExtra("Id", myId);
                intent.putExtra("Title", myTitle);
                intent.putExtra("Desc", myDesc);
                startActivity(intent);
                return true;
            }
        });
    }

    public void onClickAddItem(View view) {
        Intent intent = new Intent (getApplicationContext(), AddItem.class);
        startActivity (intent);
    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu) {
        getMenuInflater().inflate (R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_clear_all) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
