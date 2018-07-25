package com.example.anrdoid.inventoryappstage1;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentValues;

import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;

import android.text.TextUtils;

import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
    private int currentSupplierPhone;
    private Button mIncrementButton;
    private Button mDecrementButton;
    private int mSupplieName = InventoryEntry.SUPPLIER_ONE;

    private boolean productHasChanged = false;

    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            productHasChanged = true;
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        mCurrentProductUri = intent.getData();

        if (mCurrentProductUri == null){
            setTitle(getString(R.string.add_product));
            invalidateOptionsMenu();
        } else {
            setTitle(getString(R.string.edit_product));
            getLoaderManager().initLoader(EXISTING_PRODUCT_LOADER, null, this);
        }

        mProductName = findViewById(R.id.product_name);
        mProductPrice = findViewById(R.id.product_price);
        mProductQuantity = findViewById(R.id.product_quantity);
        mProductSupplieName = findViewById(R.id.product_supplier_name);
        mProductSupplierPhoneNumber = findViewById(R.id.product_supplier_phone_number);
        mIncrementButton = findViewById(R.id.increment);
        mDecrementButton = findViewById(R.id.decrement);
        mDecrementButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String currentQuantity = mProductQuantity.getText().toString();
                int qty;
                if (currentQuantity.length() == 0) {
                    mProductQuantity.setText(String.valueOf(0));
                } else {
                    qty = Integer.parseInt(currentQuantity) - 1;
                    if(qty >=0) {
                        mProductQuantity.setText(String.valueOf(qty));
                    }
                }
            }
        });
        mIncrementButton .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String currentQuantity = mProductQuantity.getText().toString();
                int qty;
                if(currentQuantity.length() == 0){
                    qty = 1;
                    mProductQuantity.setText(String.valueOf(qty));
                }else{
                    qty = Integer.parseInt(currentQuantity) + 1;
                    mProductQuantity.setText(String.valueOf(qty));
                }
            }
        });

        mProductName.setOnTouchListener(mTouchListener);
        mProductPrice.setOnTouchListener(mTouchListener);
        mProductQuantity.setOnTouchListener(mTouchListener);
        mProductSupplierPhoneNumber.setOnTouchListener(mTouchListener);
        mProductSupplieName.setOnTouchListener(mTouchListener);
        mIncrementButton.setOnTouchListener(mTouchListener);
        mDecrementButton.setOnTouchListener(mTouchListener);

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
            finish();
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

            }
            finish();
        }


    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if (mCurrentProductUri == null) {
            MenuItem menuItem;
            menuItem = menu.findItem(R.id.delete_product);
            menuItem.setVisible(false);
            menuItem = menu.findItem(R.id.call);
            menuItem.setVisible(false);
        }
        return true;
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
                return true;
            case R.id.delete_product:
                deleteDialog();
                break;
            case R.id.call:
                call();
                break;
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
            final int currentQuantity = cursor.getInt(quantityColumnIndex);
            int currentSupplierName = cursor.getInt(supplierNameColumnIndex);
            currentSupplierPhone = cursor.getInt(supplierPhoneColumnIndex);

            mProductName.setText(currentName);
            mProductPrice.setText(Integer.toString(currentPrice));
            mProductQuantity.setText(Integer.toString(currentQuantity));
            mProductSupplierPhoneNumber.setText(Integer.toString(currentSupplierPhone));

            switch (currentSupplierName) {
                case InventoryEntry.SUPPLIER_ONE:
                    mProductSupplieName.setSelection(0);
                    break;
                case InventoryEntry.SUPPLIER_TWO:
                    mProductSupplieName.setSelection(1);
                    break;
                case InventoryEntry.SUPPLIER_THREE:
                    mProductSupplieName.setSelection(2);
                    break;
                default:
                    mProductSupplieName.setSelection(0);
                    break;
            }

        }
    }
    private void deleteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_msg);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
              deleteProduct();

            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void deleteProduct() {
        if (mCurrentProductUri != null) {
            int rowsDeleted = 0;
            rowsDeleted = getContentResolver().delete(
                    mCurrentProductUri,
                    null,
                    null
            );
            if (rowsDeleted == 0) {
                Toast.makeText(this, getString(R.string.delete_msg_err),
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.delete_msg_pass),
                        Toast.LENGTH_SHORT).show();
            }
            finish();
        }
    }

    private void call(){
        Intent supplierNumberIntent = new Intent(Intent.ACTION_DIAL);
        supplierNumberIntent.setData(Uri.parse("tel:" + currentSupplierPhone));
        startActivity(supplierNumberIntent);
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
