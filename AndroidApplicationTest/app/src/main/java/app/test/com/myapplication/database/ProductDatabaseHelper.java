package app.test.com.myapplication.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import app.test.com.myapplication.model.Product;

/**
 * Created by anupama.shelke on 9/28/2016.
 */
public class ProductDatabaseHelper extends SQLiteOpenHelper {
    // database version
    private static final int database_VERSION = 1;
    // database name
    private static final String databaseName = "ProductDB";
    private static final String productName = "productName";
    private static final String productPrice = "productPrice";
    private static final String vendorName = "vendorName";
    private static final String vendorAddress = "vendorAddress";
    private static final String phoneNo = "phoneNo";
    private static final String productImage = "productImage";
    private static final String REC_ID = "id";
    private static final String productMasterTbl = "productMasterTbl";
    private final Context context;
    private long mid;

    @Override
    public void onCreate(SQLiteDatabase database) {
        System.out.println("==========on create==");
        String productTable = "CREATE TABLE productMasterTbl( " + "id INTEGER PRIMARY KEY AUTOINCREMENT, " + "productName TEXT, " + "vendorName TEXT, " + "vendorAddress TEXT, " + "phoneNo TEXT, " + "productImage TEXT, " + "productPrice TEXT)";
        database.execSQL(productTable);
    }

    public ProductDatabaseHelper(Context context) {
        super(context,databaseName, null, database_VERSION);
        this.context = context;
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        // drop table if already exists
        db.execSQL("DROP TABLE IF EXISTS productMasterTbl");
        this.onCreate(db);
    }

    public void insertProductRecordIntoDatabase(Product product) {

        SQLiteDatabase db = this.getWritableDatabase();
System.out.println("==========insert=="+ product.getProductName());
        // make values to be inserted
        ContentValues values = new ContentValues();
        values.put(productName, product.getProductName());
        values.put(productPrice, product.getproductPrice());
        values.put(vendorName, product.getVendorName());
        values.put(vendorAddress, product.getVendorAddress());
        values.put(productImage, product.getThumbnailUrl());
        values.put(phoneNo, product.getPhoneNumber());


        // insert product
        try {
            db.insert(productMasterTbl, null, values);
            System.out.println("inserted records into db=====tey================" + mid);
        }catch (SQLException e) {

            Log.e("Exception", "SQLException" + String.valueOf(e.getMessage()));
            e.printStackTrace();
        }
        // close database transaction
        db.close();
    }
    public List<Product> getAllProducts() {



        List<Product> ProductList = new ArrayList<>();

        // select product query
        String query = "SELECT  * FROM " + productMasterTbl +" ORDER BY " + REC_ID + " DESC";

        // get reference of the database
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        // parse all results
        Product product_rec = null;
        if (cursor.moveToFirst()) {
            do {
                product_rec = new Product();
                product_rec.setProductId(Integer.parseInt(cursor.getString(0)));
                product_rec.setProductName(cursor.getString(1));
                product_rec.setVendorName(cursor.getString(2));
                product_rec.setVendorAddress(cursor.getString(3));
                product_rec.setPhoneNumber(cursor.getString(4));
                product_rec.setThumbnailUrl(cursor.getString(5));
                product_rec.setproductPrice(Integer.parseInt(cursor.getString(6)));


                ProductList.add(product_rec);

            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        System.out.println("selected records from db===================" + ProductList.size());
        return ProductList;
    }
    public int getAllProductsPrice() {



      int totalprice=0;

        // select product query
        String query = "SELECT  * FROM " + productMasterTbl +" ORDER BY " + REC_ID + " DESC";

        // get reference of the database
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        // parse all results

        Product product_rec = null;
        if (cursor.moveToFirst()) {
            do {

               // product_rec.setproductPrice(Integer.parseInt(cursor.getString(6)));

                totalprice=totalprice+Integer.parseInt(cursor.getString(6));


            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        System.out.println("selected records from db===totalprice================" + totalprice);
        return totalprice;
    }
    // Deleting single product
    public void deletePerticularProduct(String position) {


        SQLiteDatabase db = this.getWritableDatabase();
        System.out.println("delete row ===========" + position);

        int state = db.delete(productMasterTbl, REC_ID + " = ?", new String[]{position});
        //	int state=db.delete(search_RECORDS, null, null);
        System.out.println("===================delete==" + state);
        db.close();
    }
}
