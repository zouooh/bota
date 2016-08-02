package me.zouooh.utils;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.util.Base64;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import org.nutz.lang.Strings;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by zouooh on 2016/7/26.
 */
public class BitmapUtils {

    public final static float[] BT_SELECTED = new float[] { 1, 0, 0, 0, -50, 0, 1,
            0, 0, -50, 0, 0, 1, 0, -50, 0, 0, 0, 1, 0 };
    public final static float[] BT_NOT_SELECTED = new float[] { 1, 0, 0, 0, 0, 0,
            1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0 };

    public final static ColorMatrixColorFilter COLOR_FILTER = new ColorMatrixColorFilter(BT_SELECTED);
    public final static ColorMatrixColorFilter COLOR_FILTER_UN = new ColorMatrixColorFilter(BT_NOT_SELECTED);

    public static void makeImageClickEffect(ImageView imageView){
        if (imageView==null) {
            return;
        }
        imageView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ImageView imageView =(ImageView) v;
                if (imageView.getDrawable()==null) {
                    return false;
                }
                if (event.getAction() == MotionEvent.ACTION_DOWN||event.getAction() == MotionEvent.ACTION_MOVE) {

                    imageView.setColorFilter(COLOR_FILTER) ;
                }else {
                    imageView.setColorFilter(COLOR_FILTER_UN) ;
                }
                return false;
            }
        });
    }

    public static Bitmap getResizedBitmap(Context context, Uri uri, int widthLimit, int heightLimit) throws
            IOException {
        String path;
        Bitmap result;

        if (uri.getScheme().equals("file")) {
            path = uri.getPath();
        } else if (uri.getScheme().equals("content")) {
            Cursor cursor = context.getContentResolver().query(uri, new String[]{"_data"}, null, null, null);
            cursor.moveToFirst();
            path = cursor.getString(0);
            cursor.close();
        } else {
            return null;
        }

        ExifInterface exifInterface = new ExifInterface(path);

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface
                .ORIENTATION_UNDEFINED);

        if ((orientation == ExifInterface.ORIENTATION_ROTATE_90) || (orientation == ExifInterface
                .ORIENTATION_ROTATE_270) || (orientation == ExifInterface.ORIENTATION_TRANSPOSE) || (orientation ==
                ExifInterface.ORIENTATION_TRANSVERSE)) {
            int tmp = widthLimit;
            widthLimit = heightLimit;
            heightLimit = tmp;
        }

        int width = options.outWidth;
        int height = options.outHeight;
        int sampleW = 1;
        int sampleH = 1;
        while (width / 2 > widthLimit) {
            width /= 2;
            sampleW <<= 1;
        }

        while (height / 2 > heightLimit) {
            height /= 2;
            sampleH <<= 1;
        }

        int sampleSize;
        options = new BitmapFactory.Options();
        if ((widthLimit == Integer.MAX_VALUE) || (heightLimit == Integer.MAX_VALUE))
            sampleSize = Math.max(sampleW, sampleH);
        else {
            sampleSize = Math.max(sampleW, sampleH);
        }
        options.inSampleSize = sampleSize;
        Bitmap bitmap;
        try {
            bitmap = BitmapFactory.decodeFile(path, options);
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            options.inSampleSize <<= 1;
            bitmap = BitmapFactory.decodeFile(path, options);
        }

        Matrix matrix = new Matrix();
        if (bitmap == null) {
            return null;
        }
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        if ((orientation == 6) || (orientation == 8) || (orientation == 5) || (orientation == 7)) {
            int tmp = w;
            w = h;
            h = tmp;
        }
        switch (orientation) {
            case 6:
                matrix.setRotate(90.0F, w / 2.0F, h / 2.0F);
                break;
            case 3:
                matrix.setRotate(180.0F, w / 2.0F, h / 2.0F);
                break;
            case 8:
                matrix.setRotate(270.0F, w / 2.0F, h / 2.0F);
                break;
            case 2:
                matrix.preScale(-1.0F, 1.0F);
                break;
            case 4:
                matrix.preScale(1.0F, -1.0F);
                break;
            case 5:
                matrix.setRotate(90.0F, w / 2.0F, h / 2.0F);
                matrix.preScale(1.0F, -1.0F);
                break;
            case 7:
                matrix.setRotate(270.0F, w / 2.0F, h / 2.0F);
                matrix.preScale(1.0F, -1.0F);
        }

        float xS = widthLimit / bitmap.getWidth();
        float yS = heightLimit / bitmap.getHeight();

        matrix.postScale(Math.min(xS, yS), Math.min(xS, yS));
        try {
            result = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            return null;
        }
        return result;
    }

    public static Bitmap getRotateBitmap(float degrees, Bitmap bm) {
        int bmpW = bm.getWidth();
        int bmpH = bm.getHeight();

        Matrix mt = new Matrix();

        mt.setRotate(degrees);
        return Bitmap.createBitmap(bm, 0, 0, bmpW, bmpH, mt, true);
    }


    public static String getBase64FromBitmap(Bitmap bitmap) {
        String base64Str = null;
        ByteArrayOutputStream baos = null;
        try {
            if (bitmap != null) {
                baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 60, baos);
                byte[] bitmapBytes = baos.toByteArray();
                base64Str = Base64.encodeToString(bitmapBytes, 2);
                baos.flush();
                baos.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null) {
                    baos.flush();
                    baos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return base64Str;
    }

    public static Bitmap getBitmapFromBase64(String base64Str) {
        if (Strings.isEmpty(base64Str)) {
            return null;
        }

        byte[] bytes = Base64.decode(base64Str, 2);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    private static BitmapFactory.Options decodeBitmapOptionsInfo(Context context, Uri uri) {
        InputStream input = null;
        BitmapFactory.Options opt = new BitmapFactory.Options();
        try {
            if (uri.getScheme().equals("content"))
                input = context.getContentResolver().openInputStream(uri);
            else if (uri.getScheme().equals("file")) {
                input = new FileInputStream(uri.getPath());
            }
            if (input == null) {
                return null;
            }
            opt.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(input, null, opt);
            return opt;
        } catch (FileNotFoundException e) {
            return null;
        } finally {
            if (null != input)
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }

    public static Bitmap rotateBitmap(String srcFilePath, Bitmap bitmap) {
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(srcFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        }

        float degree = 0.0F;

        if (exif != null) {
            switch (exif.getAttributeInt("Orientation", 0)) {
                case 6:
                    degree = 90.0F;
                    break;
                case 3:
                    degree = 180.0F;
                    break;
                case 8:
                    degree = 270.0F;
                    break;
            }

        }

        if (degree != 0.0F) {
            Matrix matrix = new Matrix();
            matrix.setRotate(degree, bitmap.getWidth(), bitmap.getHeight());
            Bitmap b2 = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

            if ((b2 != null) && (bitmap != b2)) {
                bitmap.recycle();
                bitmap = b2;
            }
        }

        return bitmap;
    }

    public static InputStream getFileInputStream(String path) {
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(new File(path));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return fileInputStream;
    }

    //lis
    public static Bitmap doBlur(Bitmap sentBitmap,  boolean canReuseInBitmap) {
        // Stack Blur Algorithm by Mario Klingemann <mario@quasimondo.com>

        Bitmap bitmap;
        int radius = 5;
        if (canReuseInBitmap) {
            bitmap = sentBitmap;
        } else {
            bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);
        }

        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        int[] pix = new int[w * h];
        bitmap.getPixels(pix, 0, w, 0, 0, w, h);

        int wm = w - 1;
        int hm = h - 1;
        int wh = w * h;
        int div = radius + radius + 1;

        int r[] = new int[wh];
        int g[] = new int[wh];
        int b[] = new int[wh];
        int rsum, gsum, bsum, x, y, i, p, yp, yi, yw;
        int vmin[] = new int[Math.max(w, h)];

        int divsum = (div + 1) >> 1;
        divsum *= divsum;
        int dv[] = new int[256 * divsum];
        for (i = 0; i < 256 * divsum; i++) {
            dv[i] = (i / divsum);
        }

        yw = yi = 0;

        int[][] stack = new int[div][3];
        int stackpointer;
        int stackstart;
        int[] sir;
        int rbs;
        int r1 = radius + 1;
        int routsum, goutsum, boutsum;
        int rinsum, ginsum, binsum;

        for (y = 0; y < h; y++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            for (i = -radius; i <= radius; i++) {
                p = pix[yi + Math.min(wm, Math.max(i, 0))];
                sir = stack[i + radius];
                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);
                rbs = r1 - Math.abs(i);
                rsum += sir[0] * rbs;
                gsum += sir[1] * rbs;
                bsum += sir[2] * rbs;
                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }
            }
            stackpointer = radius;

            for (x = 0; x < w; x++) {

                r[yi] = dv[rsum];
                g[yi] = dv[gsum];
                b[yi] = dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (y == 0) {
                    vmin[x] = Math.min(x + radius + 1, wm);
                }
                p = pix[yw + vmin[x]];

                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[(stackpointer) % div];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi++;
            }
            yw += w;
        }
        for (x = 0; x < w; x++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            yp = -radius * w;
            for (i = -radius; i <= radius; i++) {
                yi = Math.max(0, yp) + x;

                sir = stack[i + radius];

                sir[0] = r[yi];
                sir[1] = g[yi];
                sir[2] = b[yi];

                rbs = r1 - Math.abs(i);

                rsum += r[yi] * rbs;
                gsum += g[yi] * rbs;
                bsum += b[yi] * rbs;

                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }

                if (i < hm) {
                    yp += w;
                }
            }
            yi = x;
            stackpointer = radius;
            for (y = 0; y < h; y++) {
                // Preserve alpha channel: ( 0xff000000 & pix[yi] )
                pix[yi] = (0xff000000 & pix[yi]) | (dv[rsum] << 16) | (dv[gsum] << 8) | dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (x == 0) {
                    vmin[y] = Math.min(y + r1, hm) * w;
                }
                p = x + vmin[y];

                sir[0] = r[p];
                sir[1] = g[p];
                sir[2] = b[p];

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[stackpointer];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi += w;
            }
        }

        bitmap.setPixels(pix, 0, w, 0, 0, w, h);

        return (bitmap);
    }

    public static void saveImage(ImageView imageView, File to) {
        if (imageView == null) {
            return;
        }
        BitmapDrawable bitmapDrawable = null;
        if (imageView.getDrawable() instanceof BitmapDrawable) {
            bitmapDrawable = (BitmapDrawable) imageView.getDrawable();
        }
        if (bitmapDrawable == null) {
            return;
        }
        Bitmap bitmap = bitmapDrawable.getBitmap();
        try {
            if (Environment.getExternalStorageState().equals(
                    Environment.MEDIA_MOUNTED)) {
                FileOutputStream fos = new FileOutputStream(to);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                fos.close();
            }
        } catch (Exception e) {
        }
    }
}
