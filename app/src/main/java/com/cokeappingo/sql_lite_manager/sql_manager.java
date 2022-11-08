package com.cokeappingo.sql_lite_manager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.cokeappingo.class_utile.account;
import com.cokeappingo.class_utile.new_recipe;
import com.cokeappingo.class_utile.save_recipe;

import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.HashMap;

public class sql_manager extends SQLiteOpenHelper {


    Context context;
    public static final String DATABASE_NAME ="cokeapp.db";

    public static final String TABLE_NAME ="accuil_information",
                               TABLE_SETTING ="setting",
                               TABLE_SETTING_CHOIX ="choix",
                               TABLE_IMAGE ="IMAGE",
                               TABLE_ACCOUNT ="ACCOUNT",
                               TABLE_RECIPE_ADD_BY_USER ="RECIPE",
                               TABLE_INGREDIENT ="INGREDIENT",
                               TABLE_PREPARE ="DESCRIPTION",
                               TABLE_RECIPE_SAVE ="TABLE_RECIPE_SAVE",
                               TABLE_IMAGE_LIST_SEARCH ="TABLE_IMAGE_LIST_SEARCH",
                               TABLE_ALL_TITLE ="TABLE_ALL_TITLE";

    public static final String ID ="ID",
                               ID_SETTING ="ID_SETTING";

    public static final String COL_1   ="Name",
                               COL_1_CHOIX   ="ID",
                               COL_1_IMAGE   ="IMAGE";

    public static final String COL_2   ="Photo",
                               COL_2_SETTING   ="exact_choise",
                               COL_2_CHOIX   ="NAME";

    public static final String TABLE_ACCOUNT_ID   ="id",
                               TABLE_ACCOUNT_NAME   ="name",
                               TABLE_ACCOUNT_BIO   ="bio",
                               TABLE_ACCOUNT_IMAGE   ="image",
                               TABLE_ACCOUNT_LIEN_IMAGE   ="image_lien",
                               TABLE_ACCOUNT_NUMBER_RECIPE   ="num_recipe";

    public static final String TABLE_RECIPE_ID   ="ID",
                               TABLE_RECIPE_LIEN   ="lien",
                               TABLE_RECIPE_TITLE   ="title",
                               TABLE_RECIPE_DATE   ="date",
                               TABLE_RECIPE_DESCRIPTION   ="description",
                               TABLE_RECIPE_COOKING_TIME   ="cooking_time",
                               TABLE_RECIPE_PERSON   ="person",
                               TABLE_RECIPE_AUTEUR  ="auteur",
                               TABLE_RECIPE_IMAGE  ="image",
                               TABLE_RECIPE_CATEGORIES  ="categories",
                               TABLE_RECIPE_CUISINE  ="cuisine",
                               TABLE_RECIPE_ETAT  ="etat",
                               TABLE_RECIPE_DATE_PUBLICATION  ="date_pub",
                               TABLE_RECIPE_REASON_REFUSE  ="reason_refuse",
                               TABLE_RECIPE_ID_PUSH  ="push";

    public static final String TABLE_INGREDIEN_ID_RECIPE   ="id",
                               TABLE_INGREDIEN_NAME   ="name";

    public static final String TABLE_PREPARE_ID_RECIPE   ="id",
                               TABLE_PREPARE_NAME   ="name";


    public static final String TABLE_IMAGE_LIST_SEARCH_ID  ="IMAGE_LIST_SEARCH_ID",
                               TABLE_IMAGE_LIST_SEARCH_IMAGE  ="IMAGE_LIST_SEARCH_IMAGE";

    public static final String TABLE_ALL_TITLE_ID ="TABLE_ALL_TITLE_ID",
                               TABLE_ALL_TITLE_ID_PUSH ="TABLE_ALL_TITLE_ID_PUSH ",
                               TABLE_ALL_TITLE_TITLE=   "TABLE_ALL_TITLE_TITLE";

    public static final String TABLE_SAVE_ID_PUSH   ="id_push",
                               TABLE_SAVE_TITLE   ="title",
                               TABLE_SAVE_IMAGE   ="image";

    public sql_manager(@Nullable Context context) {
        super(context,DATABASE_NAME, null, 1);
        this.context=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COL_1 + " TEXT," + COL_2 + " text)");
        db.execSQL("create table " + TABLE_SETTING + "(" + ID_SETTING + " INTEGER PRIMARY KEY AUTOINCREMENT," + COL_1 + " TEXT," + COL_2_SETTING + " text)");
        db.execSQL("create table " + TABLE_SETTING_CHOIX + "(" + COL_1_CHOIX + " TEXT," + COL_2_CHOIX + " text)");
        db.execSQL("create table " + TABLE_IMAGE + "(" + ID + " TEXT PRIMARY KEY," + COL_1_IMAGE + " blob)");
        db.execSQL("create table " + TABLE_ACCOUNT + "(" + TABLE_ACCOUNT_ID + " TEXT PRIMARY KEY," + TABLE_ACCOUNT_NAME + " TEXT,"+TABLE_ACCOUNT_BIO+" TEXT," + TABLE_ACCOUNT_IMAGE + " blob,"+ TABLE_ACCOUNT_NUMBER_RECIPE+" integer,"+TABLE_ACCOUNT_LIEN_IMAGE+" TEXT)");
        db.execSQL("create table " + TABLE_RECIPE_ADD_BY_USER + "(" + TABLE_RECIPE_ID + " TEXT PRIMARY KEY ," + TABLE_RECIPE_LIEN + " TEXT,"+TABLE_RECIPE_TITLE+" TEXT,"+TABLE_RECIPE_DATE+" TEXT,"+TABLE_RECIPE_DESCRIPTION+
                " TEXT,"+TABLE_RECIPE_COOKING_TIME+" TEXT,"+TABLE_RECIPE_PERSON+" TEXT,"+TABLE_RECIPE_AUTEUR+" TEXT," + TABLE_RECIPE_IMAGE + " blob,"+ TABLE_RECIPE_ETAT+" text,"+TABLE_RECIPE_CATEGORIES+" text,"+TABLE_RECIPE_CUISINE+" text,"
                +TABLE_RECIPE_DATE_PUBLICATION+" TEXT,"+TABLE_RECIPE_REASON_REFUSE+" TEXT,"+TABLE_RECIPE_ID_PUSH+" TEXT)");
        db.execSQL("create table " + TABLE_INGREDIENT + "(" + TABLE_INGREDIEN_ID_RECIPE+ " TEXT," + TABLE_INGREDIEN_NAME + " text)");
        db.execSQL("create table " + TABLE_PREPARE + "(" + TABLE_PREPARE_ID_RECIPE+ " TEXT," + TABLE_PREPARE_NAME + " text)");
        db.execSQL("create table " + TABLE_IMAGE_LIST_SEARCH + "(" + TABLE_IMAGE_LIST_SEARCH_ID +" TEXT," + TABLE_IMAGE_LIST_SEARCH_IMAGE + " blob)");
        db.execSQL("create table " + TABLE_ALL_TITLE + "(" +TABLE_ALL_TITLE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"  + TABLE_ALL_TITLE_ID_PUSH + " TEXT," + TABLE_ALL_TITLE_TITLE + " TEXT)");
        db.execSQL("create table " + TABLE_RECIPE_SAVE + "(" + TABLE_SAVE_ID_PUSH + " TEXT PRIMARY KEY," + TABLE_SAVE_TITLE + " TEXT," + TABLE_SAVE_IMAGE + " blob)");
        db.execSQL("create table search_pointeure (id INTEGER PRIMARY KEY,point INTEGER)");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);

    }

     public  boolean insert_data_into_pointer(){
         SQLiteDatabase db=this.getWritableDatabase();

         ContentValues contantValues=new ContentValues();
         contantValues.put("point",0);
         contantValues.put("id",0);
         long result = db.insert("search_pointeure",null,contantValues);
         if (result == -1)
             return false;
         else
             return true;
     }

    public boolean Update_data_pointer(int num){
        SQLiteDatabase db=this.getWritableDatabase();
        db.execSQL("Update search_pointeure set point="+num+" where id=0");
        return true;
    }

    public int get_point(){
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor res=db.rawQuery("select point FROM  search_pointeure",null);
        res.moveToFirst();
        Log.e("dd",res.getInt(0)+"");
        return res.getInt(0);
    }

    public  boolean insert_data_into_TABLE_ALL_TITLE(String id_push,String title){
        SQLiteDatabase db=this.getWritableDatabase();

        ContentValues contantValues=new ContentValues();
        contantValues.put(TABLE_ALL_TITLE_ID_PUSH,id_push);
        contantValues.put(TABLE_ALL_TITLE_TITLE,title);

        long result = db.insert(TABLE_ALL_TITLE,null,contantValues);
       // //db.close();
        if (result == -1)
            return false;
        else
            return true;
    }

    public  boolean insert_data_into_accuil_information(String name,String photo){
        SQLiteDatabase db=this.getWritableDatabase();

        ContentValues contantValues=new ContentValues();
        contantValues.put(COL_1,name);
        contantValues.put(COL_2,photo);

        long result = db.insert(TABLE_NAME,null,contantValues);
        ////db.close();
        if (result == -1)
            return false;
        else
        return true;
    }

    public  boolean insert_data_into_Setting(String name,String exact_choix){
        SQLiteDatabase db=this.getWritableDatabase();

        ContentValues contantValues=new ContentValues();
        contantValues.put(COL_1,name);
        contantValues.put(COL_2_SETTING,exact_choix);

        long result = db.insert(TABLE_SETTING,null,contantValues);
        //db.close();
        if (result == -1)
            return false;
        else
            return true;
    }

    public  boolean insert_data_into_setting_choix(String id,String value){
        SQLiteDatabase db=this.getWritableDatabase();

        ContentValues contantValues=new ContentValues();
        contantValues.put(COL_1_CHOIX,id);
        contantValues.put(COL_2_CHOIX,value);

        long result = db.insert(TABLE_SETTING_CHOIX,null,contantValues);
        //db.close();
        if (result == -1)
            return false;
        else
            return true;
    }

    public  boolean insert_data_into_SAVE(String id_push,String title,byte[] value){
        SQLiteDatabase db=this.getWritableDatabase();

        ContentValues contantValues=new ContentValues();
        contantValues.put(TABLE_SAVE_ID_PUSH,id_push);
        contantValues.put(TABLE_SAVE_TITLE,title);
        contantValues.put(TABLE_SAVE_IMAGE,value);

        long result = db.insert(TABLE_RECIPE_SAVE,null,contantValues);
        //db.close();
        if (result == -1)
            return false;
        else
            return true;
    }

    public ArrayList<save_recipe> getAllData__SAVE(){
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor res=db.rawQuery("select * FROM "+TABLE_RECIPE_SAVE,null);


        ArrayList<save_recipe> list_of_recipe_saved=new ArrayList<>();

        while (res.moveToNext()) {
            save_recipe save_recipe= new save_recipe();
            save_recipe.setId_push(res.getString(0));
            save_recipe.setTitle(res.getString(1));
            save_recipe.setImage(res.getBlob(2));
            list_of_recipe_saved.add(save_recipe);
        }

        //db.close();
        return list_of_recipe_saved;
    }

    public void delet_recipe_saved(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete  FROM "+TABLE_RECIPE_SAVE+" where "+TABLE_SAVE_ID_PUSH+" like '"+id+"'");
        //db.close();
    }

    public boolean exist_recipe_saved(String id) {
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor res=db.rawQuery("select * FROM "+TABLE_RECIPE_SAVE+" where "+TABLE_SAVE_ID_PUSH+" like '"+id+"'",null);

        //db.close();
        return res.moveToNext();
    }

    public Cursor getAllData_setting(){
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor res=db.rawQuery("select * FROM "+TABLE_SETTING,null);
        //db.close();
        return res;
    }

    public boolean Update_data_setting(String id,String exact_choise){
        SQLiteDatabase db=this.getWritableDatabase();

        ContentValues contantValues=new ContentValues();
        contantValues.put(ID_SETTING,id);
        contantValues.put(COL_2_SETTING,exact_choise);

        db.update(TABLE_SETTING,contantValues,"ID_SETTING = ?",new String[] { id });
        //db.close();
        return true;
    }

    public  boolean insert_data_into_account(String ID,String NAME,String BIO,byte[] photo,int number_recipe,String lien_PUC){
        SQLiteDatabase db=this.getWritableDatabase();

        ContentValues contantValues=new ContentValues();
        contantValues.put(TABLE_ACCOUNT_ID,ID);
        contantValues.put(TABLE_ACCOUNT_NAME,NAME);
        contantValues.put(TABLE_ACCOUNT_BIO,BIO);
        contantValues.put(TABLE_ACCOUNT_IMAGE,photo);
        contantValues.put(TABLE_ACCOUNT_NUMBER_RECIPE,number_recipe);
        contantValues.put(TABLE_ACCOUNT_LIEN_IMAGE,lien_PUC);

        long result = db.insert(TABLE_ACCOUNT,null,contantValues);
        if (result == -1)
            return false;
        else
            return true;
    }

    public boolean Update_data_account(String ID,String NAME,String BIO,byte[] photo,int number_recipe,String lien_PUC){
        SQLiteDatabase db=this.getWritableDatabase();

        if (photo!=null) {
            ContentValues contantValues = new ContentValues();

            contantValues.put(TABLE_ACCOUNT_ID, ID);
            contantValues.put(TABLE_ACCOUNT_NAME, NAME);
            contantValues.put(TABLE_ACCOUNT_BIO, BIO);
            contantValues.put(TABLE_ACCOUNT_IMAGE, photo);
            contantValues.put(TABLE_ACCOUNT_NUMBER_RECIPE,number_recipe);
            contantValues.put(TABLE_ACCOUNT_LIEN_IMAGE,lien_PUC);


            db.update(TABLE_ACCOUNT, contantValues, TABLE_ACCOUNT_ID + " = ?", new String[]{ID});

        }else
            {
            ContentValues contantValues = new ContentValues();
            contantValues.put(TABLE_ACCOUNT_ID, ID);
            contantValues.put(TABLE_ACCOUNT_NAME, NAME);
            contantValues.put(TABLE_ACCOUNT_BIO, BIO);
            contantValues.put(TABLE_ACCOUNT_NUMBER_RECIPE,number_recipe);

            db.update(TABLE_ACCOUNT, contantValues, TABLE_ACCOUNT_ID + " = ?", new String[]{ID});
        }
        //db.close();
        return true;
    }

    public void delet_all_data_account() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete  FROM "+TABLE_ACCOUNT);
        //db.close();
    }

    public account get__account() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * FROM " + TABLE_ACCOUNT, null);
        account account = new account();
        while (res.moveToNext()) {
            account.setAccount_ID(res.getString(0));
            account.setName(res.getString(1));
            account.setBio(res.getString(2));
            account.setPhoto_saved(res.getBlob(3));
            account.setNumber_recipe(res.getInt(4));
            account.setImage(res.getString(5));
        }
        //db.close();
        return account;
    }

    public  boolean insert_data_into_recipe(new_recipe re){
        SQLiteDatabase db=this.getWritableDatabase();

        ContentValues contantValues=new ContentValues();
        contantValues.put(TABLE_RECIPE_ID,re.getID());
        contantValues.put(TABLE_RECIPE_LIEN,re.getLien_puc());
        contantValues.put(TABLE_RECIPE_TITLE,re.getTitle());
        contantValues.put(TABLE_RECIPE_DATE,re.getDate());
        contantValues.put(TABLE_RECIPE_DESCRIPTION,re.getDescription());
        contantValues.put(TABLE_RECIPE_COOKING_TIME,re.getCooking_time());
        contantValues.put(TABLE_RECIPE_PERSON,re.getPerson());
        contantValues.put(TABLE_RECIPE_AUTEUR,re.getAuteur());
        contantValues.put(TABLE_RECIPE_IMAGE ,re.getImage());
        contantValues.put(TABLE_RECIPE_ETAT ,re.getStatus_recipe());
        contantValues.put(TABLE_RECIPE_CATEGORIES ,re.getCategories());
        contantValues.put(TABLE_RECIPE_CUISINE ,re.getCuisine());
        contantValues.put(TABLE_RECIPE_ID_PUSH ,re.getId_push());

        if(re.getStatus_recipe().equals("ok") || re.getStatus_recipe().equals("refused")){
            contantValues.put(TABLE_RECIPE_DATE_PUBLICATION ,re.getDate_publication());
            contantValues.put(TABLE_RECIPE_REASON_REFUSE ,re.getCause_refuse());
        }

        long result = db.insert(TABLE_RECIPE_ADD_BY_USER,null,contantValues);

        if (result != -1) {
            ArrayList<String> ingredien = re.getIngredient();

            try {
                for (String m : ingredien) {
                    insert_data_into_ingredient(re.getID(), m);
                }
            } catch (NullPointerException e) {
            }


            ArrayList<String> prepare = re.getHow_to_prepare();

            try {
                for (String m : prepare) {
                    insert_data_into_PREPARE(re.getID(), m);
                }
            } catch (NullPointerException e) {
            }
            //
        }
        if (result == -1)
            return false;
        else
            return true;
    }

    public void delet_data_recipe(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete FROM "+TABLE_RECIPE_ADD_BY_USER+" where "+TABLE_RECIPE_ID+" like "+id);
        //db.close();
    }

    public void delet_all_data_recipe() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete  FROM "+TABLE_RECIPE_ADD_BY_USER);
        db.execSQL("delete  FROM "+TABLE_INGREDIENT);
        db.execSQL("delete  FROM "+TABLE_PREPARE);
        //db.close();
    }

    public boolean Update_data_recipe(new_recipe re){
        SQLiteDatabase db=this.getWritableDatabase();

        ContentValues contantValues=new ContentValues();
        contantValues.put(TABLE_RECIPE_ID,re.getID());
        contantValues.put(TABLE_RECIPE_LIEN,re.getLien_puc());
        contantValues.put(TABLE_RECIPE_TITLE,re.getTitle());
        contantValues.put(TABLE_RECIPE_DATE,re.getDate());
        contantValues.put(TABLE_RECIPE_DESCRIPTION,re.getDescription());
        contantValues.put(TABLE_RECIPE_COOKING_TIME,re.getCooking_time());
        contantValues.put(TABLE_RECIPE_PERSON,re.getPerson());
        contantValues.put(TABLE_RECIPE_AUTEUR,re.getAuteur());
        contantValues.put(TABLE_RECIPE_IMAGE ,re.getImage());
        contantValues.put(TABLE_RECIPE_ETAT ,re.getStatus_recipe());
        contantValues.put(TABLE_RECIPE_CATEGORIES ,re.getCategories());
        contantValues.put(TABLE_RECIPE_CUISINE ,re.getCuisine());

        try {
            contantValues.put(TABLE_RECIPE_DATE_PUBLICATION ,re.getDate_publication());
            contantValues.put(TABLE_RECIPE_REASON_REFUSE ,re.getCause_refuse());
        }catch (NullPointerException e) {
            Log.e("date_publication_null","null");
        }

        ArrayList<String> ingredien=re.getIngredient();


        //db.rawQuery("delete FROM "+TABLE_INGREDIENT+" where "+TABLE_INGREDIEN_ID_RECIPE+" like "+re.getID(),null);
        db.execSQL("delete FROM "+TABLE_INGREDIENT+" where "+TABLE_INGREDIEN_ID_RECIPE+" like "+re.getID());
        db.execSQL("delete FROM "+TABLE_PREPARE+" where "+TABLE_PREPARE_ID_RECIPE+" like "+re.getID());
        for (String m:ingredien){
            insert_data_into_ingredient(re.getID(),m);
        }

        ArrayList<String>  prepare=re.getHow_to_prepare();

        for (String m:prepare){
            insert_data_into_PREPARE(re.getID(),m);
        }

        db.update(TABLE_RECIPE_ADD_BY_USER, contantValues, TABLE_RECIPE_ID + " = ?", new String[]{re.getID()});

        //db.close();
        return true;
    }

    public  boolean insert_data_into_ingredient(String id_recipe,String ingredient){
        SQLiteDatabase db=this.getWritableDatabase();

        ContentValues contantValues=new ContentValues();
        contantValues.put(TABLE_INGREDIEN_ID_RECIPE,id_recipe);
        contantValues.put(TABLE_INGREDIEN_NAME,ingredient);

        long result = db.insert(TABLE_INGREDIENT,null,contantValues);
        //db.close();
        if (result == -1)
            return false;
        else
            return true;
    }

    /*public boolean Update_data_ingredient(String id_recipe,String ingredient){
        SQLiteDatabase db=this.getWritableDatabase();

        ContentValues contantValues=new ContentValues();
        contantValues.put(TABLE_INGREDIEN_ID_RECIPE,id_recipe);
        contantValues.put(TABLE_INGREDIEN_NAME,ingredient);

            db.update(TABLE_INGREDIENT, contantValues, TABLE_INGREDIEN_ID_RECIPE + " = ?", new String[]{id_recipe});
            return true;
        }
     */

    public  boolean insert_data_into_PREPARE(String id_recipe,String PREPARE){
        SQLiteDatabase db=this.getWritableDatabase();

        ContentValues contantValues=new ContentValues();
        contantValues.put(TABLE_PREPARE_ID_RECIPE,id_recipe);
        contantValues.put(TABLE_PREPARE_NAME,PREPARE);

        long result = db.insert(TABLE_PREPARE,null,contantValues);
        //db.close();
        if (result == -1)
            return false;
        else
            return true;
    }

    /*public boolean Update_data_prepare(String id_recipe,String prepare){
        SQLiteDatabase db=this.getWritableDatabase();

        ContentValues contantValues=new ContentValues();
        contantValues.put(TABLE_PREPARE_ID_RECIPE,id_recipe);
        contantValues.put(TABLE_PREPARE_NAME,prepare);

        db.update(TABLE_PREPARE, contantValues, TABLE_PREPARE_ID_RECIPE + " = ?", new String[]{id_recipe});
        return true;
    }
     */

    public ArrayList<String> getAllData_Ingredient(String id){
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor res=db.rawQuery("select * FROM "+TABLE_INGREDIENT+" WHERE "+TABLE_INGREDIEN_ID_RECIPE+" like '"+id+"'",null);

        ArrayList<String> ingredients=new ArrayList<>();

        while (res.moveToNext()){
            ingredients.add(res.getString(1));
        }

        //db.close();
        return ingredients;
    }

    public ArrayList<String> getAllData_Prepare(String id){
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor res=db.rawQuery("select * FROM "+TABLE_PREPARE+" WHERE "+TABLE_PREPARE_ID_RECIPE+" like '"+id+"'",null);

        ArrayList<String> prepares=new ArrayList<>();

        while (res.moveToNext()){
            prepares.add(res.getString(1));
        }

        //db.close();
        return prepares;
    }

    public ArrayList<new_recipe> getAllData_recipe_user(){
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor res=db.rawQuery("select * FROM "+TABLE_RECIPE_ADD_BY_USER,null);

        ArrayList<new_recipe> recipes=new ArrayList<>();

        while (res.moveToNext()){
            new_recipe this_recipe =new new_recipe();

            this_recipe.setID(res.getString(0));
            this_recipe.setLien_puc(res.getString(1));
            this_recipe.setTitle(res.getString(2));
            this_recipe.setDescription(res.getString(4));
            this_recipe.setDate(res.getString(3));
            this_recipe.setCooking_time(res.getString(5));
            this_recipe.setPerson(res.getString(6));
            this_recipe.setAuteur(res.getString(7));
            this_recipe.setImage(res.getBlob(8));
            this_recipe.setStatus_recipe(res.getString(9));
            this_recipe.setCategories(res.getString(10));
            this_recipe.setCuisine(res.getString(11));
            this_recipe.setId_push(res.getString(14));

            if(res.getString(9).equals("ok") || res.getString(9).equals("refused")){
                this_recipe.setDate_publication(res.getString(12));
                this_recipe.setCause_refuse(res.getString(13));
            }

            this_recipe.setIngredient(getAllData_Ingredient(res.getString(0)));
            this_recipe.setHow_to_prepare(getAllData_Prepare(res.getString(0)));

            recipes.add(this_recipe);

        }

        //db.close();
        return recipes;
    }

    public new_recipe getData_recipe_user_by_id_push(String id){
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor res=db.rawQuery("select * FROM "+TABLE_RECIPE_ADD_BY_USER+" where "+TABLE_RECIPE_ID_PUSH+" like '"+id+"'",null);

        res.moveToNext();
            new_recipe this_recipe =new new_recipe();

            this_recipe.setID(res.getString(0));
            this_recipe.setLien_puc(res.getString(1));
            this_recipe.setTitle(res.getString(2));
            this_recipe.setDescription(res.getString(4));
            this_recipe.setDate(res.getString(3));
            this_recipe.setCooking_time(res.getString(5));
            this_recipe.setPerson(res.getString(6));
            this_recipe.setAuteur(res.getString(7));
            this_recipe.setImage(res.getBlob(8));
            this_recipe.setStatus_recipe(res.getString(9));
            this_recipe.setCategories(res.getString(10));
            this_recipe.setCuisine(res.getString(11));
            this_recipe.setId_push(res.getString(14));

            if(res.getString(9).equals("ok") || res.getString(9).equals("refused")){
                this_recipe.setDate_publication(res.getString(12));
                this_recipe.setCause_refuse(res.getString(13));
            }

            this_recipe.setIngredient(getAllData_Ingredient(res.getString(0)));
            this_recipe.setHow_to_prepare(getAllData_Prepare(res.getString(0)));


        //db.close();
        return this_recipe;
    }

    public new_recipe getData_recipe_user_by_id(String id){
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor res=db.rawQuery("select * FROM "+TABLE_RECIPE_ADD_BY_USER+" where "+TABLE_RECIPE_ID+" like '"+id+"'",null);

        res.moveToNext();
        new_recipe this_recipe =new new_recipe();

        this_recipe.setID(res.getString(0));
        this_recipe.setLien_puc(res.getString(1));
        this_recipe.setTitle(res.getString(2));
        this_recipe.setDescription(res.getString(4));
        this_recipe.setDate(res.getString(3));
        this_recipe.setCooking_time(res.getString(5));
        this_recipe.setPerson(res.getString(6));
        this_recipe.setAuteur(res.getString(7));
        this_recipe.setImage(res.getBlob(8));
        this_recipe.setStatus_recipe(res.getString(9));
        this_recipe.setCategories(res.getString(10));
        this_recipe.setCuisine(res.getString(11));
        this_recipe.setId_push(res.getString(14));

        if(res.getString(9).equals("ok") || res.getString(9).equals("refused")){
            this_recipe.setDate_publication(res.getString(12));
            this_recipe.setCause_refuse(res.getString(13));
        }

        this_recipe.setIngredient(getAllData_Ingredient(res.getString(0)));
        this_recipe.setHow_to_prepare(getAllData_Prepare(res.getString(0)));


        //db.close();
        return this_recipe;
    }



    public  boolean insert_data_into_image_list_search(String id_image,byte[] image){
        SQLiteDatabase db=this.getWritableDatabase();

        ContentValues contantValues=new ContentValues();
        contantValues.put(TABLE_IMAGE_LIST_SEARCH_ID,id_image);
        contantValues.put(TABLE_IMAGE_LIST_SEARCH_IMAGE,image);

        long result = db.insert(TABLE_IMAGE_LIST_SEARCH_IMAGE,null,contantValues);
        if (result == -1)
            return false;
        else
            return true;
    }

    public byte[] get_image_list_search(String id){
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor res=db.rawQuery("select * FROM "+TABLE_IMAGE_LIST_SEARCH_IMAGE+" WHERE "+TABLE_IMAGE_LIST_SEARCH_ID+" like '"+id+"'",null);

        byte[] g=null;

        while (res.moveToNext()){
            g=res.getBlob(1);
        }
        return g;
    }
/*
    public boolean delet_data(String id){
        SQLiteDatabase db=this.getWritableDatabase();
        db.delete(TABLE_NAME,"ID = ?",new String[] { id });
        return true;
    }

     */
}
