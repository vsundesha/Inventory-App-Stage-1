package com.example.anrdoid.inventoryappstage1.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public class InventoryContract {

    public static final String CONTENT_AUTHORITY = "com.example.anrdoid.inventoryappstage1";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_PRODUCT = "product";

    public InventoryContract() {}

    public final static class InventoryEntry implements BaseColumns {

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_PRODUCT);

        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" +  PATH_PRODUCT;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PRODUCT;

        public final static String TABLE_NAME = "product";

        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_PRODUCT_NAME = "product_name";
        public final static String COLUMN_PRODUCT_PRICE = "price";
        public final static String COLUMN_PRODUCT_QUANTITY = "quantity";
        public final static String COLUMN_PRODUCT_SUPPLIER_NAME = "supplier_name";
        public final static String COLUMN_PRODUCT_SUPPLIER_PHONE_NUMBER = "supplier_phone_number";

        public final static int SUPPLIER_ONE = 0;
        public final static int SUPPLIER_TWO = 1;
        public final static int SUPPLIER_THREE = 2;

        public static boolean isValidSupplier(int supplier) {
            if (supplier == SUPPLIER_ONE || supplier == SUPPLIER_TWO || supplier == SUPPLIER_THREE) {
                return true;
            }
            return false;
        }
    }
}
