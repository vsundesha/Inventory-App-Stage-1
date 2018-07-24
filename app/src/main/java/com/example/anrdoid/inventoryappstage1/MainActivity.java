package com.example.anrdoid.inventoryappstage1;

import android.app.LoaderManager;
import android.content.ContentValues;

import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.anrdoid.inventoryappstage1.data.InventoryContract.InventoryEntry;

public class MainActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor>{

    private static final int EXISTING_PRODUCT_LOADER = 0;


    private Uri mCurrentProductUri;
    private EditText mProductName;
    private EditText mProductPrice;
    private EditText mProductQuantity;
    private Spinner mProductSupplieName;
    private EditText mProductSupplierPhoneNumber;

    private int mSupplieName = InventoryEntry.SUPPLIER_THREE;

    private boolean productHasChanged = false;

    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            productHasChanged = true;
            Log.d("message", "onTouch");

            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d("message", "onCreate");

        Intent intent = getIntent();
        mCurrentProductUri = intent.getData();

        if (mCurrentProductUri == null){
            setTitle(getString(R.string.add_product));
        } else {
            setTitle(getString(R.string.edit_product));
            getLoaderManager().initLoader(EXISTING_PRODUCT_LOADER, null, this);
        }

        mProductName = findViewById(R.id.product_name);
        mProductPrice = findViewById(R.id.product_price);
        mProductQuantity = findViewById(R.id.product_quantity);
        mProductSupplieName = findViewById(R.id.product_supplier_name);
        mProductSupplierPhoneNumber = findViewById(R.id.product_supplier_phone_number);

        mProductName.setOnTouchListener(mTouchListener);
        mProductPrice.setOnTouchListener(mTouchListener);
        mProductQuantity.setOnTouchListener(mTouchListener);
        mProductSupplieName.setOnTouchListener(mTouchListener);
        mProductSupplierPhoneNumber.setOnTouchListener(mTouchListener);
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
        if (mCurrentProductUri == null) {
            if (TextUtils.isEmpty(productName)) {
                Toast.makeText(this, getString(R.string.product_name_required), Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(productPrice)) {
                Toast.makeText(this, getString(R.string.price_required), Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(productQuantity)) {
                Toast.makeText(this, getString(R.string.quantity_required), Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(productSupplierPhoneNumber)) {
                Toast.makeText(this, getString(R.string.supplier_phone_required), Toast.LENGTH_SHORT).show();
                return;
            }

            ContentValues values = new ContentValues();
            values.put(InventoryEntry.COLUMN_PRODUCT_NAME, productName);
            values.put(InventoryEntry.COLUMN_PRODUCT_PRICE, Integer.parseInt(productPrice));
            values.put(InventoryEntry.COLUMN_PRODUCT_QUANTITY, Integer.parseInt(productQuantity));
            values.put(InventoryEntry.COLUMN_PRODUCT_SUPPLIER_NAME, mSupplieName);
            values.put(InventoryEntry.COLUMN_PRODUCT_SUPPLIER_PHONE_NUMBER, productSupplierPhoneNumber);
            Uri newUri = getContentResolver().insert(InventoryEntry.CONTENT_URI,values);

            if (newUri == null) {
                // If the new content URI is null, then there was an error with insertion.
                Toast.makeText(this, getString(R.string.editor_insert_product_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the insertion was successful and we can display a toast.
                Toast.makeText(this, getString(R.string.editor_insert_product_successful),
                        Toast.LENGTH_SHORT).show();
            }
        } else {
            if (TextUtils.isEmpty(productName)) {
                Toast.makeText(this, getString(R.string.product_name_required), Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(productPrice)) {
                Toast.makeText(this, getString(R.string.price_required), Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(productQuantity)) {
                Toast.makeText(this, getString(R.string.quantity_required), Toast.LENGTH_SHORT).show();
                return;
            }

            if (TextUtils.isEmpty(productSupplierPhoneNumber)) {
                Toast.makeText(this, getString(R.string.supplier_phone_required), Toast.LENGTH_SHORT).show();
                return;
            }

            ContentValues values = new ContentValues();

            values.put(InventoryEntry.COLUMN_PRODUCT_NAME, productName);
            values.put(InventoryEntry.COLUMN_PRODUCT_PRICE, productPrice);
            values.put(InventoryEntry.COLUMN_PRODUCT_QUANTITY, productQuantity);
            values.put(InventoryEntry.COLUMN_PRODUCT_SUPPLIER_NAME, mSupplieName);
            values.put(InventoryEntry.COLUMN_PRODUCT_SUPPLIER_PHONE_NUMBER, productSupplierPhoneNumber);


            int rowsAffected = getContentResolver().update(mCurrentProductUri, values, null, null);
            if (rowsAffected == 0) {
                Toast.makeText(this, getString(R.string.update_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.update_successful),
                        Toast.LENGTH_SHORT).show();
                finish();
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add, menu);
        Log.d("message", "main activity");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                insertProduct();
                finish();
            case android.R.id.home:
                if (!productHasChanged) {
                    NavUtils.navigateUpFromSameTask(MainActivity.this);
                    return true;
                }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String[] projection = {
                InventoryEntry._ID,
                InventoryEntry.COLUMN_PRODUCT_NAME,
                InventoryEntry.COLUMN_PRODUCT_PRICE,
                InventoryEntry.COLUMN_PRODUCT_QUANTITY,
                InventoryEntry.COLUMN_PRODUCT_SUPPLIER_NAME,
                InventoryEntry.COLUMN_PRODUCT_SUPPLIER_PHONE_NUMBER
        };
        return new CursorLoader(this,
                mCurrentProductUri,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor == null || cursor.getCount() < 1) {
            return;
        }
        if (cursor.moveToFirst()) {
            int nameColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_PRODUCT_NAME);
            int priceColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_PRODUCT_PRICE);
            int quantityColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_PRODUCT_QUANTITY);
            int supplierNameColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_PRODUCT_SUPPLIER_NAME);
            int supplierPhoneColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_PRODUCT_SUPPLIER_PHONE_NUMBER);

            String currentName = cursor.getString(nameColumnIndex);
            int currentPrice = cursor.getInt(priceColumnIndex);
            int currentQuantity = cursor.getInt(quantityColumnIndex);
            int currentSupplierName = cursor.getInt(supplierNameColumnIndex);
            int currentSupplierPhone = cursor.getInt(supplierPhoneColumnIndex);

            mProductName.setText(currentName);
            mProductPrice.setText(Integer.toString(currentPrice));
            mProductQuantity.setText(Integer.toString(currentQuantity));
            mProductSupplierPhoneNumber.setText(Integer.toString(currentSupplierPhone));

            switch (currentSupplierName) {
                case InventoryEntry.SUPPLIER_ONE:
                    mProductSupplieName.setSelection(0);
                    break;
                case InventoryEntry.SUPPLIER_TWO:
                    mProductSupplieName.setSelection(2);
                    break;
                case InventoryEntry.SUPPLIER_THREE:
                    mProductSupplieName.setSelection(3);
                    break;
                default:
                    mProductSupplieName.setSelection(0);
                    break;
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mProductName.setText("");
        mProductPrice.setText("");
        mProductQuantity.setText("");
        mProductSupplierPhoneNumber.setText("");
        mProductSupplieName.setSelection(0);
    }
}
