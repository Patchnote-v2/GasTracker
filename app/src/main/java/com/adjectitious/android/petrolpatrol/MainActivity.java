package com.adjectitious.android.petrolpatrol;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.support.v7.widget.Toolbar;

// TODO: Graphs and other statistics
// TODO: Multiple cars
// TODO: Help menu (single or per-view?)
// TODO: Nicer looking buttons (bigger, evenly split across view
public class MainActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar mainToolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(mainToolbar);
    }

    public void addEntry (View view)
    {
        Intent intent = new Intent(this, AddEntry.class);
        startActivity(intent);
    }

    public void viewEntries(View view)
    {
        Intent intent = new Intent(this, ViewEntries.class);
        startActivity(intent);
    }
}