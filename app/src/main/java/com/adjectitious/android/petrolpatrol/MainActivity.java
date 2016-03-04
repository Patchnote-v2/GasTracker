package com.adjectitious.android.petrolpatrol;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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