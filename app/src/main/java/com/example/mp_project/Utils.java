package com.example.mp_project;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;

/**
 * Utils class - 이미지 관련
 * @author 김희주
 */
public class Utils {
    Context context;

    Utils(Context context){
        this.context = context;
    }

    //Bitmap을 ByteArray로 변경(DB에 저장할 때 사용)
    public byte[] BitmapToByteArray(Bitmap bitmap){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100,stream);
        return stream.toByteArray();
    }

    //ByteArrray를 Bitmap으로 변경(이미지뷰에 사용)
    public Bitmap ByteArraytoBitmap(byte[] bytearray){
        return BitmapFactory.decodeByteArray(bytearray,0,bytearray.length);
    }

    //Bitmap 리사이징 메소드
    public Bitmap resize(Uri uri, int size){
        Bitmap resizeBitmap = null;
        BitmapFactory.Options options = new BitmapFactory.Options();

        try {
            options.inJustDecodeBounds = true;  //메모리할당 없이 width, height, mimetype 셋팅
            BitmapFactory.decodeStream(context.getContentResolver().openInputStream(uri),null,options);

            int width = options.outWidth;
            int height = options.outHeight;
            int samplesize = 1;

            while(true){
                if((width/2 < size) && (height/2 < size)){
                    break;
                }
                width /= 2;
                height /= 2;
                samplesize *= 2;
            }

            options.inSampleSize = samplesize;
            options.inJustDecodeBounds = false;
            resizeBitmap = BitmapFactory.decodeStream(context.getContentResolver().openInputStream(uri),null,options);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return resizeBitmap;
    }
}
