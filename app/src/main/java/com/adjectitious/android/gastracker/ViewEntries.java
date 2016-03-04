package com.adjectitious.android.gastracker;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.view.ViewGroup.LayoutParams;

import com.adjectitious.android.gastracker.sql.*;

import java.lang.reflect.Field;

/**
 * Created by Infernous on 2/27/2016.
 */
public class ViewEntries extends AppCompatActivity
{
    private static final String TAG = "ViewEntries";
    private Context context;

    protected static final int SUBITEM_ID = 1;

    protected DatabaseHelper dbHelper;
    protected SQLiteDatabase db;
    protected Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_entries);
        this.context = getApplicationContext();
        viewAll();
    }

    @Override
    protected void onStart()
    {
        super.onStart();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();

        this.dbHelper.close();
        this.db.close();
        this.cursor.close();
    }

    public void viewAll()
    {
        this.dbHelper = new DatabaseHelper(getApplicationContext());
        this.db = this.dbHelper.getWritableDatabase();
        this.cursor = this.db.query(
                DatabaseContract.gasTable.TABLE_NAME,               // The table to query
                null,                                               // The columns to return
                null,                                               // The columns for the WHERE clause
                null,                                               // The values for the WHERE clause
                null,                                               // don't group the rows
                null,                                               // don't filter by row groups
                null                                                // The sort order
        );

        int color;
        LinearLayout list = (LinearLayout) findViewById(R.id.list);
        list.removeAllViews();
        if (this.cursor.moveToFirst())
        {
            while (!this.cursor.isAfterLast())
            {
                LinearLayout layout = new LinearLayout(this.context);
                layout.setId(R.id.list_subitem);
                layout.setOrientation(LinearLayout.VERTICAL);
                LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
                layout.setLayoutParams(params);
                layout.setPadding(0, 50, 0, 0);
                layout.setLongClickable(true);
                layout.setOnLongClickListener(new DeleteOnLongClickListener(this.cursor.getInt(this.cursor.getColumnIndex(DatabaseContract.gasTable._ID))));

                list.addView(layout);

                if (Build.VERSION.SDK_INT >= 23)
                {
                    color = this.context.getColor(R.color.colorBlack);
                }
                else
                {
                    color = getResources().getColor(R.color.colorBlack);
                }

                // DATE
                TextView date = new TextView(this.context);
                date.setLayoutParams(params);
                date.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                date.setTextSize(context.getResources().getDimension(R.dimen.header_font_size));
                date.setTypeface(null, Typeface.BOLD);
                date.setTextColor(color);
                date.setText(this.cursor.getString(this.cursor.getColumnIndex(DatabaseContract.gasTable.COLUMN_NAME_DATE)));
                date.setVisibility(View.VISIBLE);
                layout.addView(date);

                // LOCATION
                TextView location = new TextView(this.context);
                location.setLayoutParams(params);
                location.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                location.setTextSize(context.getResources().getDimension(R.dimen.subitems_font_size));
                location.setTextColor(color);
                location.setText(this.cursor.getString(this.cursor.getColumnIndex(DatabaseContract.gasTable.COLUMN_NAME_NAME)));
                layout.addView(location);

                // PRICE
                TextView price = new TextView(this.context);
                price.setLayoutParams(params);
                price.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                price.setTextSize(context.getResources().getDimension(R.dimen.subitems_font_size));
                price.setTextColor(color);
                price.setText(this.cursor.getString(this.cursor.getColumnIndex(DatabaseContract.gasTable.COLUMN_NAME_PRICE)));
                layout.addView(price);

                // GALLONS
                TextView gallons = new TextView(this.context);
                gallons.setLayoutParams(params);
                gallons.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                gallons.setTextSize(context.getResources().getDimension(R.dimen.subitems_font_size));
                gallons.setTextColor(color);
                gallons.setText(this.cursor.getString(this.cursor.getColumnIndex(DatabaseContract.gasTable.COLUMN_NAME_GALLONS)));
                layout.addView(gallons);

                // MILEAGE
                TextView mileage = new TextView(this.context);
                mileage.setLayoutParams(params);
                mileage.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                mileage.setTextSize(context.getResources().getDimension(R.dimen.subitems_font_size));
                mileage.setTextColor(color);
                mileage.setText(this.cursor.getString(this.cursor.getColumnIndex(DatabaseContract.gasTable.COLUMN_NAME_MILEAGE)));
                layout.addView(mileage);

                this.cursor.moveToNext();
            }
        }

        return;
    }

    private class DeleteOnLongClickListener implements View.OnLongClickListener
    {
        public DatabaseHelper dbHelper;
        public SQLiteDatabase db;
        public int position;

        public DeleteOnLongClickListener(int position)
        {
            this.dbHelper = new DatabaseHelper(getApplicationContext());
            this.db = this.dbHelper.getWritableDatabase();
            this.position = position;
        }

        @Override
        public boolean onLongClick(View v)
        {
            AlertDialog dialog = new AlertDialog.Builder(ViewEntries.this).create();
            dialog.setTitle(R.string.delete_entry_title);
            dialog.setMessage(getString(R.string.delete_entry_prompt));
            dialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.confirm),
                    new DialogTest(this.position)
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            String where = DatabaseContract.gasTable._ID + "= ?";
                            String[] values = new String[]{String.valueOf(this.position)};
                            db.delete(DatabaseContract.gasTable.TABLE_NAME, where, values);
                            viewAll();
                            Log.wtf(ViewEntries.TAG, String.valueOf(this.position));
                        }
                    });

            dialog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.cancel),
                    new DialogTest(this.position)
                    {
                        public void onClick(DialogInterface dialog, int which)
                        {
                            dialog.dismiss();
                        }
                    });
            dialog.show();
            return true;
        }

        public class DialogTest implements DialogInterface.OnClickListener
        {
            public DatabaseHelper dbHelper;
            public SQLiteDatabase db;
            public int position;

            public DialogTest(int position)
            {
                this.dbHelper = new DatabaseHelper(getApplicationContext());
                this.db = this.dbHelper.getWritableDatabase();
                this.position = position;
            }

            @Override
            public void onClick(DialogInterface dialog, int which)
            {
            }
        }
    }
}