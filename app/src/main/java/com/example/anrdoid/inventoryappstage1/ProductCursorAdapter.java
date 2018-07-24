package com.example.anrdoid.inventoryappstage1;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
    public void bindView(View view, Context context, Cursor cursor) {
        TextView nameTextView = view.findViewById(R.id.name);
        TextView priceTextView = view.findViewById(R.id.price);

        int nameColumnIndex = cursor.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_PRODUCT_NAME);
        int priceColumnIndex = cursor.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_PRODUCT_PRICE);

        String name = cursor.getString(nameColumnIndex);
        String price = cursor.getString(priceColumnIndex);

        nameTextView.setText(name);
        priceTextView.setText(price);
    }
}
