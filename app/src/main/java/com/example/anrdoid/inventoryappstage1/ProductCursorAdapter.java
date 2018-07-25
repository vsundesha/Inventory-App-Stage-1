package com.example.anrdoid.inventoryappstage1;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.anrdoid.inventoryappstage1.data.InventoryContract;

public class ProductCursorAdapter extends CursorAdapter{

    public ProductCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, viewGroup, false);
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        TextView nameTextView = view.findViewById(R.id.name);
        TextView priceTextView = view.findViewById(R.id.price);
        TextView quantityTextView = view.findViewById(R.id.quantity);

        int nameColumnIndex = cursor.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_PRODUCT_NAME);
        int priceColumnIndex = cursor.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_PRODUCT_PRICE);
        int quantityColumnIndex = cursor.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_PRODUCT_QUANTITY);
        int productIdColumIndex = cursor.getColumnIndex(InventoryContract.InventoryEntry._ID);

        String name = cursor.getString(nameColumnIndex);
        String  price = cursor.getString(priceColumnIndex);
        final int quantity = cursor.getInt(quantityColumnIndex);

        final int id = Integer.parseInt(cursor.getString(productIdColumIndex));

        nameTextView.setText(name);
        priceTextView.setText(price);
        quantityTextView.setText(String.valueOf(quantity));

        Button saleButton = view.findViewById(R.id.sale);
        saleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri currentUri = ContentUris.withAppendedId(InventoryContract.InventoryEntry.CONTENT_URI, id);

                String updatedQuantity = String.valueOf(quantity - 1);

                if(Integer.parseInt(updatedQuantity)>=0){
                    ContentValues values = new ContentValues();
                    values.put(InventoryContract.InventoryEntry.COLUMN_PRODUCT_QUANTITY,updatedQuantity);
                    context.getContentResolver().update(currentUri,values,null,null);
                }
            }
        });
    }
}
