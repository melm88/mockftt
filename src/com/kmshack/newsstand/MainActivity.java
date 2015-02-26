package com.kmshack.newsstand;

import java.io.FileNotFoundException;
import java.io.IOException;

import android.annotation.TargetApi;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.util.SparseArrayCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;
import com.flavienlaurent.notboringactionbar.AlphaForegroundColorSpan;
import com.flavienlaurent.notboringactionbar.KenBurnsSupportView;
import com.nineoldandroids.view.ViewHelper;
import com.squareup.picasso.Picasso;

public class MainActivity extends ActionBarActivity implements ScrollTabHolder, ViewPager.OnPageChangeListener {

	private static AccelerateDecelerateInterpolator sSmoothInterpolator = new AccelerateDecelerateInterpolator();
	public static int PickImageId = 1000;

	private KenBurnsSupportView mHeaderPicture;
	private View mHeader;

	private PagerSlidingTabStrip mPagerSlidingTabStrip;
	private ViewPager mViewPager;
	private PagerAdapter mPagerAdapter;

	private int mActionBarHeight;
	private int mMinHeaderHeight;
	private int mHeaderHeight;
	private int mMinHeaderTranslation;
	private ImageView mHeaderLogo;

	private RectF mRect1 = new RectF();
	private RectF mRect2 = new RectF();

	private TypedValue mTypedValue = new TypedValue();
	private SpannableString mSpannableString;
	private AlphaForegroundColorSpan mAlphaForegroundColorSpan;
	
	private Toolbar toolbar;
	private TextView title;
	private ImageView icon;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mMinHeaderHeight = getResources().getDimensionPixelSize(R.dimen.min_header_height);
		mHeaderHeight = getResources().getDimensionPixelSize(R.dimen.header_height);
		mMinHeaderTranslation = -mMinHeaderHeight + getActionBarHeight();

		setContentView(R.layout.activity_main);
		
		toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		icon = (ImageView) findViewById(R.id.icon);
		title = (TextView) findViewById(R.id.title);
		
		//mHeaderPicture is the KenBurnsSupportView which provides the background images
		mHeaderPicture = (KenBurnsSupportView) findViewById(R.id.header_picture);
		mHeaderPicture.setResourceIds(R.drawable.red,R.drawable.red);
		//mHeaderLogo is the profile picture holder
		mHeaderLogo = (ImageView) findViewById(R.id.header_logo);
		mHeader = findViewById(R.id.header);
        PagerSlidingTabStrip.lv = (LinearLayout) findViewById(R.id.toolbardown);
		//the pager-styled-tab layout and its view-pager (right-left swipe screen change)
		mPagerSlidingTabStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);


		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setOffscreenPageLimit(4);

		mPagerAdapter = new PagerAdapter(getSupportFragmentManager());
		mPagerAdapter.setTabHolderScrollingContent(this);

		mViewPager.setAdapter(mPagerAdapter);

		mPagerSlidingTabStrip.setViewPager(mViewPager);
		mPagerSlidingTabStrip.setOnPageChangeListener(this);
		mSpannableString = new SpannableString(getString(R.string.actionbar_title));
		mAlphaForegroundColorSpan = new AlphaForegroundColorSpan(0xffffffff);
		Log.d("hell", getActionBarIconView()+"");
		ViewHelper.setAlpha(getActionBarIconView(), 0f);
		
		mHeaderLogo.setOnTouchListener(imageTouchListener);
		
		getSupportActionBar().setBackgroundDrawable(null);
	}
	
	
	//OnTouch listener (on profile image) which will launch image picker
	private View.OnTouchListener imageTouchListener = new View.OnTouchListener() {
	    @Override
	    public boolean onTouch(View v, MotionEvent event) {
	        if (event.getAction() == MotionEvent.ACTION_DOWN) {
	            // pointer goes down
	        	//mHeaderLogo.setImageResource(R.drawable.ic_launcher);
	        	
	        	//ImagePicker Intent
	        	Intent iimg= new Intent();
	        	iimg.setType("image/*");
	        	iimg.setAction(iimg.ACTION_GET_CONTENT);
	        	Log.d("clicker", "Launching...");
	    	    startActivityForResult(iimg, PickImageId);
	        }
	        // also let the framework process the event
	        return false;
	    }
		
	};
	
	//Based on the recieved intent from the image-picker perform image-fitting
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
	    if ((requestCode == PickImageId) && (resultCode == RESULT_OK) && (data != null))
	    {
	        Uri uri = data.getData();
	        Log.d("clicker", "OnAct: "+uri);	        
	        //Toast.makeText(this, ""+uri, Toast.LENGTH_SHORT/1500).show();

	        String path = getPathToImage(uri);
	        //Toast.makeText(this, path, Toast.LENGTH_SHORT).show();;
	        
	        //If image is not retrievable from image picker then show a toast
	        if(path != null)
	        	resizedPic(path, uri, mHeaderLogo);
	        else
	        	Toast.makeText(this, "Unable to get image. Please select image from Gallery", Toast.LENGTH_SHORT/1000).show();
	        
	        
	        /*mHeaderLogo.setImageURI(uri);
	        mHeaderLogo.setMaxWidth(100);
	        mHeaderLogo.setMaxHeight(100);
	        mHeaderLogo.setMinimumWidth(50);
	        mHeaderLogo.setMinimumHeight(50);*/
	    }
	}
	
	private Bitmap getOrientationImage(String src, Bitmap bitmap, int newWidth, int newHeight){
		ExifInterface exifInterface;
		try {
			//ExIfInterface is used on the source image to understand the orientation
			exifInterface = new ExifInterface(src);
			int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
			Log.d("clickerz","Init... EXIF "+orientation);
			
			//Get Image width and height
			int width = bitmap.getWidth();
		    int height = bitmap.getHeight();
		    Log.d("clickerz", "Image H:"+height+" | W:"+width + " || Rec H:"+newHeight+" | W:"+newWidth);
		    
		    //Re-scaled image width and height factor
		    float scaleWidth = ((float) newWidth) / width;
		    float scaleHeight = ((float) newHeight) / height;
		    Log.d("clickerz", "Scaled H:"+scaleHeight+" | W:"+scaleWidth);
		    
			Matrix matrix = new Matrix();			
			
			//According to the orientation, rotate the image using matrix.rotate()
			switch (orientation) {
		    case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
		        //matrix.setScale(-1, 1);
		        break;
		    case ExifInterface.ORIENTATION_ROTATE_180:
		        matrix.setRotate(180);
		        break;
		    case ExifInterface.ORIENTATION_FLIP_VERTICAL:
		        matrix.setRotate(180);
		        //matrix.postScale(-1, 1);
		        break;
		    case ExifInterface.ORIENTATION_TRANSPOSE:
		        matrix.setRotate(90);
		        //matrix.postScale(-1, 1);
		        break;
		    case ExifInterface.ORIENTATION_ROTATE_90:
		        matrix.setRotate(90);
		        break;
		    case ExifInterface.ORIENTATION_TRANSVERSE:
		        matrix.setRotate(-90);
		        //matrix.postScale(-1, 1);
		        break;
		    case ExifInterface.ORIENTATION_ROTATE_270:
		        matrix.setRotate(-90);
		        break;
		    default:
		        Log.d("clicker", "returning Bitmap");
		    }			
		    
			//matrix.postScale(0.064f, 0.036f);
			
			//Conditions to check un-even sizing and to ensure that the final image appears
			//as a square (in most conditions)
			if((float)width/(float)height < 1.75f && (orientation != 1)) {
				matrix.postScale(scaleWidth, scaleHeight);
				Log.d("clickerz","First: "+((float)width/(float)height)+ " | OR: "+orientation);
			} else if(orientation == 1 || orientation == 0){
				matrix.postScale(scaleWidth, scaleHeight);
				Log.d("clickerz","Match: "+((float)width/(float)height)+ " | OR: "+orientation);
			} else {
				matrix.postScale(scaleHeight, scaleWidth);
				Log.d("clickerz","Second: "+((float)width/(float)height)+ " | OR: "+orientation);
			}
			
			//Call createBitmap() to re-scale the image based on the conditions
			//and then call RoundedTransformation function from picasso to
			//get image with rounded corners
			Bitmap oriented = null;
			 try {
				 oriented = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
				 Bitmap rounded_oriented = new RoundedTransformation(75, 4).transform(oriented);
				 mHeaderLogo.setImageDrawable(new BitmapDrawable(rounded_oriented));
				 if(bitmap!=null && !bitmap.isRecycled())
					 bitmap.recycle();
				 return oriented;
				 } catch (OutOfMemoryError e) {
				 e.printStackTrace();
				 if(oriented == null)
					 return bitmap;
				 else
					 return oriented;
				 } 
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return bitmap;
	}
	
	private void resizedPic(String path, Uri uri, ImageView destination){
		//Setting height and width to a size 250x250.
		//h and w are used for the re-scaling factor.
		int h = 250;
		int w = 250;
		try {
			
			//Bitmap.createScaledBitmap(bmp, w, h, true);
			//mHeaderLogo.setImageBitmap(bmp);
			//mHeaderLogo.setImageDrawable(getResizedBitmap(bmp, h, w));
			
			//Create BitmapFactory options and read in  the image from the received uri.
			//Pass the bitmap to 'getOrientationImage()' to re-scale and maintain
			//orientation
			Log.d("clicker","Initiate...");
			Options options = new BitmapFactory.Options();
		    options.inScaled = false;
		    Bitmap bmp = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri), null, options);
			getOrientationImage(path, bmp, w, h);
			//mHeaderLogo.setImageDrawable(new BitmapDrawable(getOrientationImage(path, bmp, w, h)));
			Log.d("clicker","Done...");	
			
			//Re-cyle the bitmaps
			if(bmp!=null && !bmp.isRecycled()){
				bmp.recycle();
			}
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//Return an image (string) path from the given Uri
	private String getPathToImage(Uri uri)
	{
	    String path = null;
	    // The projection contains the columns we want to return in our query.
	    String[] projection = new String[] { MediaStore.Images.Media.DATA };
	    Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
	    //Cursor cursor = getContentResolver().query(uri, null, null, null, null);
	    Log.d("clicker", "Cursor");
	    if (cursor != null)
	    {
	    	Log.d("clicker", "NOT NULL Cursor");
	        int columnIndex = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
	        Log.d("clicker", "Cursor Index: "+columnIndex);
	        cursor.moveToFirst();
	        path = cursor.getString(columnIndex);
	        Log.d("clicker", "Cursor Path: "+path);
	        cursor.close();
	    }
	    return path;
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		// nothing
	}

	@Override
	public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
		// nothing
	}

	@Override
	public void onPageSelected(int position) {
		//Identify which tab we are in, currently.
		SparseArrayCompat<ScrollTabHolder> scrollTabHolders = mPagerAdapter.getScrollTabHolders();
		ScrollTabHolder currentHolder = scrollTabHolders.valueAt(position);

		currentHolder.adjustScroll((int) (mHeader.getHeight() + ViewHelper.getTranslationY(mHeader)));
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount, int pagePosition) {
		//Scroll effect for making the transitions from main background image to a simple action bar
		if (mViewPager.getCurrentItem() == pagePosition) {
			int scrollY = getScrollY(view);
			ViewHelper.setTranslationY(mHeader, Math.max(-scrollY, mMinHeaderTranslation));
			float ratio = clamp(ViewHelper.getTranslationY(mHeader) / mMinHeaderTranslation, 0.0f, 1.0f);
			interpolate(mHeaderLogo, getActionBarIconView(), sSmoothInterpolator.getInterpolation(ratio));
			setTitleAlpha(clamp(5.0F * ratio - 4.0F, 0.0F, 1.0F));
		}
	}

	@Override
	public void adjustScroll(int scrollHeight) {
		// nothing
	}

	public int getScrollY(AbsListView view) {
		View c = view.getChildAt(0);
		if (c == null) {
			return 0;
		}

		int firstVisiblePosition = view.getFirstVisiblePosition();
		int top = c.getTop();

		int headerHeight = 0;
		if (firstVisiblePosition >= 1) {
			headerHeight = mHeaderHeight;
		}

		return -top + firstVisiblePosition * c.getHeight() + headerHeight;
	}

	public static float clamp(float value, float max, float min) {
		return Math.max(Math.min(value, min), max);
	}

	private void interpolate(View view1, View view2, float interpolation) {
		getOnScreenRect(mRect1, view1);
		getOnScreenRect(mRect2, view2);

		float scaleX = 1.0F + interpolation * (mRect2.width() / mRect1.width() - 1.0F);
		float scaleY = 1.0F + interpolation * (mRect2.height() / mRect1.height() - 1.0F);
		float translationX = 0.5F * (interpolation * (mRect2.left + mRect2.right - mRect1.left - mRect1.right));
		float translationY = 0.5F * (interpolation * (mRect2.top + mRect2.bottom - mRect1.top - mRect1.bottom));

		ViewHelper.setTranslationX(view1, translationX);
		ViewHelper.setTranslationY(view1, translationY - ViewHelper.getTranslationY(mHeader));
		ViewHelper.setScaleX(view1, scaleX);
		ViewHelper.setScaleY(view1, scaleY);
	}

	private RectF getOnScreenRect(RectF rect, View view) {
		rect.set(view.getLeft(), view.getTop(), view.getRight(), view.getBottom());
		return rect;
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public int getActionBarHeight() {
		if (mActionBarHeight != 0) {
			return mActionBarHeight;
		}
		
		if(Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB){
			getTheme().resolveAttribute(android.R.attr.actionBarSize, mTypedValue, true);
		}else{
			getTheme().resolveAttribute(R.attr.actionBarSize, mTypedValue, true);
		}
		
		mActionBarHeight = TypedValue.complexToDimensionPixelSize(mTypedValue.data, getResources().getDisplayMetrics());
		
		return mActionBarHeight;
	}

	private void setTitleAlpha(float alpha) {
		/*mAlphaForegroundColorSpan.setAlpha(alpha);
		mSpannableString.setSpan(mAlphaForegroundColorSpan, 0, mSpannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		getSupportActionBar().setTitle(mSpannableString);*/
		
		mAlphaForegroundColorSpan.setAlpha(alpha);
	    mSpannableString.setSpan(mAlphaForegroundColorSpan, 0, mSpannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
	    title.setText(mSpannableString);
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private ImageView getActionBarIconView() {
		
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
			//return (ImageView)findViewById(android.R.id.home);
			return icon;
		}

		return (ImageView)findViewById(android.support.v7.appcompat.R.id.home);
	}

	//View pager class for changing pages based on tab selection (or) swipe (right-left)
	public class PagerAdapter extends FragmentPagerAdapter {

		private SparseArrayCompat<ScrollTabHolder> mScrollTabHolders;
		private final String[] TITLES = { "WORLD", "GROUPS", "SEARCH", "SETTINGS"};
		private ScrollTabHolder mListener;

		public PagerAdapter(FragmentManager fm) {
			super(fm);
			mScrollTabHolders = new SparseArrayCompat<ScrollTabHolder>();
		}

		public void setTabHolderScrollingContent(ScrollTabHolder listener) {
			mListener = listener;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return TITLES[position];
		}

		@Override
		public int getCount() {
			return TITLES.length;
		}

		@Override
		public Fragment getItem(int position) {
            Fragment fragment1=null;
            android.support.v4.app.FragmentManager fm =   getSupportFragmentManager();
			ScrollTabHolderFragment fragment = null;
            if (position == 0){
                //Toast.makeText(MainActivity.this,"tab1",Toast.LENGTH_SHORT).show();
            fragment = (ScrollTabHolderFragment) GlobalFeedsListFragment.newInstance(position);
            }
            else if (position == 1)
            {
                fragment = (ScrollTabHolderFragment) GroupListFragment.newInstance(position);
                //Toast.makeText(MainActivity.this,"tab2",Toast.LENGTH_SHORT).show();
            }
            else {
                fragment = (ScrollTabHolderFragment) settings.newInstance(position);

                //Toast.makeText(MainActivity.this,"tab3/4",Toast.LENGTH_SHORT).show();
            }
            if(fragment!=null){
			mScrollTabHolders.put(position, fragment);


            fragment.setScrollTabHolder(mListener);}

			if (mListener != null) {
                if(fragment!=null)

				fragment.setScrollTabHolder(mListener);
			}


			return fragment;
		}

		public SparseArrayCompat<ScrollTabHolder> getScrollTabHolders() {
			return mScrollTabHolders;
		}

	}
}


//Class which implements transformation effects from Picasso project
class RoundedTransformation implements com.squareup.picasso.Transformation {
    private final int radius;
    private final int margin;  // dp
  
    // radius is corner radii in dp
    // margin is the board in dp
    public RoundedTransformation(final int radius, final int margin) {
        this.radius = radius;
        this.margin = margin;
    }
  
    //Transform method to apply rounded edges effect to the given image.
    @Override
    public Bitmap transform(final Bitmap source) {
        final Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setShader(new BitmapShader(source, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
  
        Bitmap output = Bitmap.createBitmap(source.getWidth(), source.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        canvas.drawRoundRect(new RectF(margin, margin, source.getWidth() - margin, source.getHeight() - margin), radius, radius, paint);
  
        if (source != output) {
            source.recycle();
        }
  
        return output;
    }
  
    @Override
    public String key() {
        return "rounded";
    }
}
