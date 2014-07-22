package com.imcore.xbionic.imagework;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.imcore.xbionic.imagework.AsyncBitmapLoader.ImageCallBack;

public class ImageWork {
	private static ImageWork mImageWork = new ImageWork();
	private AsyncBitmapLoader asyncBitmapLoader;
	private ImageWork(){
		asyncBitmapLoader=new AsyncBitmapLoader();  
	}
	
	public static ImageWork getImageWork(){
		return mImageWork;
	}
	
	public Bitmap setImgBitmap (ImageView img, String url){
		final String u = url;
		return asyncBitmapLoader.loadBitmap(img, url, new ImageCallBack() {  
	        
	        @Override  
	        public void imageLoad(ImageView imageView, Bitmap bitmap) {  
	            // TODO Auto-generated method stub  
	        	if(imageView.getTag().equals(u)){
	        		imageView.setImageBitmap(bitmap);  
	        	}
	        }  
	    });
	}
}
