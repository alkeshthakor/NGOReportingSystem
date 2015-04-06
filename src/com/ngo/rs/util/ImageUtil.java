package com.ngo.rs.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

public class ImageUtil {
	static File MY_IMG_DIR;
	
	public static File createMyImagesDir() {
	    try {
	        // Get SD Card path & your folder name
	        MY_IMG_DIR = new File(Environment.getExternalStorageDirectory(), "/NGOReportingSystem/");
	        // check if exist 
	        if (!MY_IMG_DIR.exists()) {
	            // Create New folder 
	            MY_IMG_DIR.mkdirs();
	            Log.i("path", ">>.." + MY_IMG_DIR);
	        }
	    } catch (Exception e) {
	        // TODO: handle exception
	        Log.e("Create_MY_IMAGES_DIR", "" + e);
	    }
	    return MY_IMG_DIR;
	}

	public static File createImageFile(){
		return new File(MY_IMG_DIR.getAbsolutePath().toString()+"/NGO_PROJECT_"+System.currentTimeMillis()+".jpg");
	}
	
	
	/**
	 * This method used to decode Image file as per Height and Width which is provided in argument
	 * @param file should be image file
	 * @param WIDTH should be int width of image. 
	 * @param HIGHT should be int Height of image.
	 * @return an object of Bitmap 
	 */
//	public static Bitmap decodeFile(File file, int WIDTH, int HIGHT) {
//		try {
//			// Decode image size
//			BitmapFactory.Options o = new BitmapFactory.Options();
//			o.inJustDecodeBounds = true;
//			BitmapFactory.decodeStream(new FileInputStream(file), null, o);
//
//			// The new size we want to scale to
//			final int REQUIRED_WIDTH = WIDTH;
//			final int REQUIRED_HIGHT = HIGHT;
//			// Find the correct scale value. It should be the power of 2.
//			int scale = 1;
//			while (o.outWidth / scale / 2 >= REQUIRED_WIDTH
//					&& o.outHeight / scale / 2 >= REQUIRED_HIGHT)
//				scale *= 2;
//
//			// Decode with inSampleSize
//			BitmapFactory.Options o2 = new BitmapFactory.Options();
//			o2.inSampleSize = scale;
//			return BitmapFactory.decodeStream(new FileInputStream(file), null, o2);
//
//		} catch (FileNotFoundException e) {
//		}
//		return null;
//	}
    
	public static void saveResizedImage(String imagePath){
		File resizeFile = new File(imagePath);
		 Bitmap bm=ImageUtil.decodeFile(new File(imagePath),600,1000);
			try {
				FileOutputStream out = new FileOutputStream(resizeFile);
                bm.compress(Bitmap.CompressFormat.JPEG, 90, out);
                out.flush();
                out.close();
                
			} catch (IOException e) {
				Log.e("BROKEN", "Could not write file " + e.getMessage());
	    }
	}
	
	public static String getValidName(String path){
		return path.replace(" ","_");
	}
	
	public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
	        int reqWidth, int reqHeight) {

	    // First decode with inJustDecodeBounds=true to check dimensions
	    final BitmapFactory.Options options = new BitmapFactory.Options();
	    options.inJustDecodeBounds = true;
	    BitmapFactory.decodeResource(res, resId, options);

	    // Calculate inSampleSize
	    options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

	    // Decode bitmap with inSampleSize set
	    options.inJustDecodeBounds = false;
	    return BitmapFactory.decodeResource(res, resId, options);
	}
	
	public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
    // Raw height and width of image
    final int height = options.outHeight;
    final int width = options.outWidth;
    int inSampleSize = 1;

    if (height > reqHeight || width > reqWidth) {

        final int halfHeight = height / 2;
        final int halfWidth = width / 2;

        // Calculate the largest inSampleSize value that is a power of 2 and keeps both
        // height and width larger than the requested height and width.
        while ((halfHeight / inSampleSize) > reqHeight
                && (halfWidth / inSampleSize) > reqWidth) {
            inSampleSize *= 2;
        }
    }

    return inSampleSize;
}
	
	
	public static Bitmap decodeFile(File file, int WIDTH, int HIGHT) {
		try {
			// Decode image size
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(new FileInputStream(file), null, options);

//			// The new size we want to scale to
//			final int REQUIRED_WIDTH = WIDTH;
//			final int REQUIRED_HIGHT = HIGHT;
//			// Find the correct scale value. It should be the power of 2.
//			int scale = 1;
//			while (o.outWidth / scale / 2 >= REQUIRED_WIDTH
//					&& o.outHeight / scale / 2 >= REQUIRED_HIGHT)
//				scale *= 2;
//
//			// Decode with inSampleSize
//			BitmapFactory.Options o2 = new BitmapFactory.Options();
//			o2.inSampleSize = scale;
//			return BitmapFactory.decodeStream(new FileInputStream(file), null, o2);

			// Calculate inSampleSize
		    options.inSampleSize = calculateInSampleSize(options, WIDTH, HIGHT);

		    // Decode bitmap with inSampleSize set
		    options.inJustDecodeBounds = false;
		    return BitmapFactory.decodeStream(new FileInputStream(file), null, options);
		    
		} catch (FileNotFoundException e) {
		}
		return null;
	}
}
