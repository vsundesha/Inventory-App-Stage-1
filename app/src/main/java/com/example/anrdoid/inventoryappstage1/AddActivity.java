package com.example.anrdoid.inventoryappstage1;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.anrdoid.inventoryappstage1.data.InventoryContract.InventoryEntry;
import com.example.anrdoid.inventoryappstage1.data.InventoryDbHelper;

public class AddActivity extends AppCompatActivity {

    private EditText mProductName;
    private EditText mProductPrice;
    private EditText mProductQuantity;
    private Spinner mProductSupplieName;
    private EditText mProductSupplierPhoneNumber;

    private int mSupplieName = InventoryEntry.SUPPLIER_THREE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        mProductName = findViewById(R.id.product_name);
        mProductPrice = findViewById(R.id.product_price);
        mProductQuantity = findViewById(R.id.product_quantity);
        mProductSupplieName = findViewById(R.id.product_supplier_name);
        mProductSupplierPhoneNumber = findViewById(R.id.product_supplier_phone_number);
        setupSpinner();
    }

    private void setupSpinner() {

        ArrayAdapter productSupplieNameSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_supplier_options, android.R.layout.simple_spinner_item);

        productSupplieNameSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        mProductSupplieName.setAdapter(productSupplieNameSpinnerAdapter);

        mProductSupplieName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals(getString(R.string.supplier_one))) {
                        mSupplieName = InventoryEntry.SUPPLIER_ONE;
                    } else if (selection.equals(getString(R.string.supplier_two))) {
                        mSupplieName = InventoryEntry.SUPPLIER_TWO;
                    } else {
                        mSupplieName = InventoryEntry.SUPPLIER_THREE;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mSupplieName = InventoryEntry.SUPPLIER_ONE;
            }
        });
    }

    private void insertProduct() {
        String productName = mProductName.getText().toString().trim();
        String productPrice = mProductPrice.getText().toString().trim();
        String productQuantity = mProductQuantity.getText().toString().trim();
        String productSupplierPhoneNumber = mProductSupplierPhoneNumber.getText().toString().trim();

        InventoryDbHelper mDbHelper = new InventoryDbHelper(this);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(InventoryEntry.COLUMN_PRODUCT_NAME, productName);
        values.put(InventoryEntry.COLUMN_PRODUCT_PRICE, Integer.parseInt(productPrice));
        values.put(InventoryEntry.COLUMN_PRODUCT_QUANTITY, Integer.parseInt(productQuantity));
        values.put(InventoryEntry.COLUMN_PRODUCT_SUPPLIER_NAME, mSupplieName);
        values.put(InventoryEntry.COLUMN_PRODUCT_SUPPLIER_PHONE_NUMBER, Integer.parseInt(productSupplierPhoneNumber));
        db.insert(InventoryEntry.TABLE_NAME, null, values);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                insertProduct();
                finish();
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
