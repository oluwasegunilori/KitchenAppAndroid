package com.example.changeme.kitchenapp.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import com.example.changeme.kitchenapp.Model.Favourites;
import com.example.changeme.kitchenapp.Model.Order;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SHEGZ on 1/1/2018.
 */
public class Database extends SQLiteAssetHelper {
    private static final String DB_NAME = "mypeople.sqlite";
    private static final int DB_VERSION = 1;


    public Database(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }


            public List<Order> getCart(String userphone){

                SQLiteDatabase db = getReadableDatabase();
                SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
                String[] SqlSelect = { "userphone","productid", "productname", "quantity", "price", "discount"};
                String SqlTable = "peopl_life";

                qb.setTables(SqlTable);

                Cursor c = qb.query(db, SqlSelect, "userphone = ?", new String[] {userphone}, null, null, null);

                final List<Order> result = new ArrayList<>();

                if(c.moveToFirst())

                {
                    do{
                        result.add(new Order(
                                c.getString(c.getColumnIndex("userphone")),
                                c.getString(c.getColumnIndex("productid")),c.getString(c.getColumnIndex("productname")),
                                 c.getString(c.getColumnIndex("quantity")),c.getString(c.getColumnIndex("price")),
                        c.getString(c.getColumnIndex("discount"))));


                    }while(c.moveToNext());


                }
                return  result;
            }


    public void  addToCart(Order order){
        SQLiteDatabase db = getReadableDatabase();
        if(!CheckIsDataAlreadyInDBorNot(order.getUserpphone(), order.getProductid())){
        String query = String.format("INSERT INTO peopl_life (userphone,productid, productname,quantity, price, discount) VALUES ('%s', '%s', '%s', '%s', '%s','%s');   ",
                order.getUserpphone(),
                order.getProductid(),
                order.getProductname(),
                order.getQuantity(),
                order.getPrice(),
                order.getDiscount());

        db.execSQL(query);}
        else{
            updateCarte(order);
        }
    }

    public void  cleanCart(String userPhone){
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("DELETE FROM peopl_life WHERE userphone = '%s' ", userPhone);
        db.execSQL(query);

    }
public void addToFavourites(Favourites food){

    SQLiteDatabase db = getReadableDatabase();
    String query = String.format("INSERT INTO Favourites(" + "foodid, foodname,foodprice, foodmenuid, foodimage, fooddiscount, fooddescription, userphone) " +
            "VALUES ('%s','%s','%s','%s','%s','%s','%s','%s');",
            food.getFoodid()
            ,food.getFoodname()
    ,food.getFoodprice()
    ,food.getFoodmenuid()
    ,food.getFoodimage()
    ,food.getFooddiscount()
    ,food.getFooddescription()
    ,food.getUserphone());
    db.execSQL(query);
    }
    public void removeFavourites(String foodid, String userphone){

        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("DELETE FROM Favourites where foodid = '%s' AND userphone = '%s'", foodid, userphone);
        db.execSQL(query);

    }
    public  boolean CheckIsDataAlreadyInDBorNot(String userphone,
                                                      String productid) {
        SQLiteDatabase sqldb = getReadableDatabase();
        String Query = String.format("SELECT * FROM peopl_life where productid = '%s' AND userphone = '%s'", productid,userphone);
        Cursor cursor = sqldb.rawQuery(Query, null);
        if(cursor.getCount() <= 0){
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }




    public  boolean isFavourites(String foodid){

        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("SELECT * FROM Favourites WHERE foodid ='%s';", foodid);
        Cursor cursor =db.rawQuery(query,null);
        if(cursor.getCount()<= 0){
            cursor.close();
            return false;
        }
        cursor.close();
        return  true;

    }

    public int getCartCount(String userphone) {
        int count = 0;
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("SELECT COUNT (*) FROM peopl_life WHERE userphone = '%s'", userphone );
        Cursor cursor =db.rawQuery(query,null);
        if(cursor.moveToFirst()){

            do{
                count =cursor.getInt(0);
            }while(cursor.moveToNext());{

            }

        }
        return count;

    }

    public void updateCart(Order order) {
                SQLiteDatabase db  = getReadableDatabase();
                String query = String.format("Update peopl_life set quantity = '%s' WHERE userphone = '%s' AND productid = '%s'", order.getQuantity(), order.getUserpphone(),order.getProductid());
                    db.execSQL(query);

    }
    public void updateCarte(Order order) {
        int r = 0;
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("SELECT * FROM  peopl_life where productname = '%s' AND userphone = '%s'", order.getProductname(), order.getUserpphone());
        Cursor cursor = db.rawQuery(query,null);
        while (cursor.moveToNext()){
             r = cursor.getPosition();
        }

        cursor.moveToPosition(r);

        int y = Integer.parseInt(cursor.getString(3)) + 1;



        String query2 = String.format("Update peopl_life set quantity = '%s' WHERE productid = '%s' AND userphone = '%s'",  String.valueOf(y),order.getProductid(),order.getUserpphone());
        db.execSQL(query2);

    }

    public void removeFromCart(String productid, String userphone) {

        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("DELETE FROM peopl_life where productid = '%s' AND userphone = '%s'", productid,userphone);
        db.execSQL(query);

    }


    public List<Favourites> getAllFavourites(String userphone){

        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        String[] SqlSelect = { "userphone","foodid", "foodname","foodprice", "foodmenuid", "foodimage", "fooddiscount",
                "fooddescription"};
        String SqlTable = "Favourites";

        qb.setTables(SqlTable);

        Cursor c = qb.query(db, SqlSelect, "userphone = ?", new String[] {userphone}, null, null, null);

        final List<Favourites> result = new ArrayList<>();

        if(c.moveToFirst())

        {
            do{
                result.add(new Favourites(
                        c.getString(c.getColumnIndex("foodid")),
                        c.getString(c.getColumnIndex("foodname")),
                        c.getString(c.getColumnIndex("foodprice")),
                        c.getString(c.getColumnIndex("foodmenuid")),
                        c.getString(c.getColumnIndex("foodimage")),
                        c.getString(c.getColumnIndex("fooddiscount")),
                        c.getString(c.getColumnIndex("fooddescription")),
                        c.getString(c.getColumnIndex("userphone"))));


            }while(c.moveToNext());


        }
        return  result;
    }


}
