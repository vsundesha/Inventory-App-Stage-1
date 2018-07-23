package com.example.anrdoid.inventoryappstage1;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.TextView;

import com.example.anrdoid.inventoryappstage1.data.InventoryContract.InventoryEntry;
import com.example.anrdoid.inventoryappstage1.data.InventoryDbHelper;

public class InventoryActivity extends AppCompatActivity {

    private InventoryDbHelper mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);

        FloatingActionButton plus = findViewById(R.id.plus);
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(InventoryActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        mDbHelper = new InventoryDbHelper(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        displayDatabaseInfo();
    }

    private void displayDatabaseInfo() {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String[] projection = {
                InventoryEntry._ID,
                InventoryEntry.COLUMN_PRODUCT_NAME,
                InventoryEntry.COLUMN_PRODUCT_PRICE,
                InventoryEntry.COLUMN_PRODUCT_QUANTITY,
                InventoryEntry.COLUMN_PRODUCT_SUPPLIER_NAME,
                InventoryEntry.COLUMN_PRODUCT_SUPPLIER_PHONE_NUMBER
        };
        Cursor cursor = db.query(
                InventoryEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null);

        TextView displayView = (TextView) findViewById(R.id.text_view_inventory);

        try {
            displayView.setText(
                    InventoryEntry._ID + " " +
                            InventoryEntry.COLUMN_PRODUCT_NAME + " " +
                            InventoryEntry.COLUMN_PRODUCT_PRICE + " " +
                            InventoryEntry.COLUMN_PRODUCT_QUANTITY + " " +
                            InventoryEntry.COLUMN_PRODUCT_SUPPLIER_NAME + " " +
                            InventoryEntry.COLUMN_PRODUCT_SUPPLIER_PHONE_NUMBER + "\n");

            int idColumnIndex = cursor.getColumnIndex(InventoryEntry._ID);
            int nameColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_PRODUCT_NAME);
            int priceColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_PRODUCT_PRICE);
            int quantityColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_PRODUCT_QUANTITY);
            int supplierNameColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_PRODUCT_SUPPLIER_NAME);
            int supplierPhoneColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_PRODUCT_SUPPLIER_PHONE_NUMBER);
            while (cursor.moveToNext()) {
                displayView.append(("\n" + cursor.getInt(idColumnIndex) + " " +
                        cursor.getString(nameColumnIndex) + " " +
                        cursor.getInt(priceColumnIndex) + " " +
                        cursor.getInt(quantityColumnIndex) + " " +
                        cursor.getInt(supplierNameColumnIndex) + " " +
                        cursor.getInt(supplierPhoneColumnIndex) ));
            }

        } finally {
            cursor.close();
        }
    }
}
