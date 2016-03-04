package com.adjectitious.android.gastracker;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.database.sqlite.*;
import android.widget.TextView;

import com.adjectitious.android.gastracker.sql.*;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddEntry extends AppCompatActivity
{

    private static final String TAG = "AddEntry";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_entry);

        // Autofills date by default
        View dateCheckboxView = findViewById(R.id.text_date_edit_checkbox);
        onCheckBoxToggle(dateCheckboxView);

        // Populates spinner with options from string array names_array
        Spinner nameSpinner = (Spinner) findViewById(R.id.spinner_name);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.names_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        nameSpinner.setAdapter(adapter);
    }

    public boolean addEntry(EditText date, EditText name, EditText price, EditText gallons, EditText mileage)
    {
        // Create new helper
        DatabaseHelper dbHelper = new DatabaseHelper(getApplicationContext());

        // Get the database. If it does not exist, this is where it will also be created.
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Create insert entries
        ContentValues values = new ContentValues();
        values.put(DatabaseContract.gasTable.COLUMN_NAME_DATE, date.getText().toString());
        values.put(DatabaseContract.gasTable.COLUMN_NAME_NAME, name.getText().toString());
        values.put(DatabaseContract.gasTable.COLUMN_NAME_PRICE, Float.parseFloat(price.getText().toString()));
        values.put(DatabaseContract.gasTable.COLUMN_NAME_GALLONS, Float.parseFloat(gallons.getText().toString()));
        values.put(DatabaseContract.gasTable.COLUMN_NAME_MILEAGE, Integer.parseInt(mileage.getText().toString()));

        // Insert the new row, returning the primary key value of the new row
        long newRowId;
        newRowId = db.insert(
                DatabaseContract.gasTable.TABLE_NAME,
                null,
                values);
        return true;
    }

    public boolean addEntry(EditText date, Spinner name, EditText price, EditText gallons, EditText mileage)
    {
        String dateString = date.getText().toString();
        String nameString = name.getSelectedItem().toString();
        String priceString = price.getText().toString();
        String gallonsString = gallons.getText().toString();
        String mileageString = mileage.getText().toString();

        TextView errorText = (TextView) findViewById(R.id.text_fields_errors);
        errorText.setVisibility(View.VISIBLE);
        errorText.setText("");

        boolean emptyField = false;

        if (dateString == null || dateString.isEmpty())
        {
            emptyField = true;
            errorText.append(getString(R.string.text_date_error));
            errorText.append(getString(R.string.text_newline));
        }
        if (nameString == null || nameString.isEmpty())
        {
            emptyField = true;
            errorText.append(getString(R.string.text_name_error));
            errorText.append(getString(R.string.text_newline));
        }
        if (priceString == null || priceString.isEmpty())
        {
            emptyField = true;
            errorText.append(getString(R.string.text_price_error));
            errorText.append(getString(R.string.text_newline));
        }
        if (gallonsString == null || gallonsString.isEmpty())
        {
            emptyField = true;
            errorText.append(getString(R.string.text_gallons_error));
            errorText.append(getString(R.string.text_newline));
        }
        if (mileageString == null || mileageString.isEmpty())
        {
            emptyField = true;
            errorText.append(getString(R.string.text_mileage_error));
            errorText.append(getString(R.string.text_newline));
        }

        if (emptyField == true)
        {
            errorText.setVisibility(View.VISIBLE);
            return false;
        }

        // Create new helper
        DatabaseHelper dbHelper = new DatabaseHelper(getApplicationContext());

        // Get the database. If it does not exist, this is where it will also be created.
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Create insert entries
        ContentValues values = new ContentValues();
        values.put(DatabaseContract.gasTable.COLUMN_NAME_DATE, dateString);
        values.put(DatabaseContract.gasTable.COLUMN_NAME_NAME, nameString);
        values.put(DatabaseContract.gasTable.COLUMN_NAME_PRICE, priceString);
        values.put(DatabaseContract.gasTable.COLUMN_NAME_GALLONS, gallonsString);
        values.put(DatabaseContract.gasTable.COLUMN_NAME_MILEAGE, mileageString);

        // Insert the new row, returning the primary key value of the new row
        long newRowId;
        newRowId = db.insert(
                DatabaseContract.gasTable.TABLE_NAME,
                null,
                values);

        return true;
    }

    public void submitEntry(View view)
    {
        EditText date = (EditText) findViewById(R.id.text_date_edit);

        EditText nameEditText = (EditText) findViewById(R.id.text_name_edit);
        Spinner nameSpinner = (Spinner) findViewById(R.id.spinner_name);

        EditText price = (EditText) findViewById(R.id.text_price_edit);
        EditText gallons = (EditText) findViewById(R.id.text_gallons_edit);
        EditText mileage = (EditText) findViewById(R.id.text_mileage_edit);

        if (nameEditText.isShown())
        {
            addEntry(date, nameEditText, price, gallons, mileage);
        }
        else if (nameSpinner.isShown())
        {
            addEntry(date, nameSpinner, price, gallons, mileage);
        }
        else
        {
            Log.wtf(TAG, "Neither name EditText or Spinner is visible.\nNow just how the heck did you manage to do that?");
        }
    }

    /*
        General CheckBox toggle by passing view
     */
    public void onCheckBoxToggle(View view)
    {
        CheckBox checkbox = (CheckBox) view;

        switch(checkbox.getId())
        {
            // Date toggle
            case R.id.text_date_edit_checkbox:
                EditText dateEditText = (EditText)findViewById(R.id.text_date_edit);
                if (checkbox.isChecked())
                {
                    dateEditText.setFocusableInTouchMode(false);
                    dateEditText.setFocusable(false);
                    dateEditText.setText(getDate());
                }
                else
                {
                    dateEditText.setFocusableInTouchMode(true);
                    dateEditText.setFocusable(true);
                    dateEditText.setText("");
                }
                break;

            // Gas station name toggle
            case R.id.text_name_edit_checkbox:
                Spinner nameSpinner = (Spinner)findViewById(R.id.spinner_name);
                EditText nameEditText = (EditText) findViewById(R.id.text_name_edit);
                if (checkbox.isChecked())
                {
                    nameEditText.setVisibility(View.VISIBLE);
                    nameSpinner.setVisibility(View.GONE);
                }
                else
                {
                    nameEditText.setVisibility(View.GONE);
                    nameSpinner.setVisibility(View.VISIBLE);
                }
        }

    }

    /*
        Passed EditText and fills with current date
     */
    public String getDate()
    {
        SimpleDateFormat simpleDate = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());
        String date = simpleDate.format(Calendar.getInstance().getTime());
        return date;
    }
}
