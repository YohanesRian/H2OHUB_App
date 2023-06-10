package com.tanpanama.h2ohub.Data;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class CupsData {
    private String name;
    private int empty_weight;
    private int full_weight;
    private Bitmap BitmapPicture;

    public CupsData(){}

    public void setName(String name) {
        this.name = name;
    }

    public void setEmpty_weight(int empty_weight) {
        this.empty_weight = empty_weight;
    }

    public void setFull_weight(int full_weight) {
        this.full_weight = full_weight;
    }

    public void setPicture(Bitmap picture){
        this.BitmapPicture = picture;
    }

    public void setPicture(byte[] picture){
        this.BitmapPicture = BitmapFactory.decodeByteArray(picture, 0, picture.length);
    }

    public void setPicture(Context ctx, Uri picture){
        try{
            this.BitmapPicture = getThumbnail(ctx, picture);
        }
        catch (FileNotFoundException E){ }
        catch (IOException I){}
    }

    private static int getPowerOfTwoForSampleRatio(double ratio){
        int k = Integer.highestOneBit((int)Math.floor(ratio));
        if(k==0) return 1;
        else return k;
    }

    public static Bitmap getThumbnail(Context ctx, Uri uri) throws FileNotFoundException, IOException{
        InputStream input = ctx.getContentResolver().openInputStream(uri);

        BitmapFactory.Options onlyBoundsOptions = new BitmapFactory.Options();
        onlyBoundsOptions.inJustDecodeBounds = true;
        onlyBoundsOptions.inDither=true;//optional
        onlyBoundsOptions.inPreferredConfig=Bitmap.Config.ARGB_8888;//optional
        BitmapFactory.decodeStream(input, null, onlyBoundsOptions);
        input.close();

        if ((onlyBoundsOptions.outWidth == -1) || (onlyBoundsOptions.outHeight == -1)) {
            return null;
        }

        double ratio = 1.0;

        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmapOptions.inSampleSize = getPowerOfTwoForSampleRatio(ratio);
        bitmapOptions.inDither = true; //optional
        bitmapOptions.inPreferredConfig=Bitmap.Config.ARGB_8888;//
        input = ctx.getContentResolver().openInputStream(uri);
        Bitmap bitmap = BitmapFactory.decodeStream(input, null, bitmapOptions);
        input.close();
        return bitmap;
    }

    public String getName() {
        return name;
    }

    public int getEmpty_weight() {
        return empty_weight;
    }

    public int getFull_weight() {
        return full_weight;
    }

    public Bitmap getBitmapPicture(){
        return BitmapPicture;
    }

    public byte[] getByteArrPicture(){
        ByteArrayOutputStream byteArr = new ByteArrayOutputStream();
        BitmapPicture.compress(Bitmap.CompressFormat.PNG, 100, byteArr);
        byte[] img = byteArr.toByteArray();
        return img;
    }
}
