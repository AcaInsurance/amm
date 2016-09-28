package com.aca.amm;

import java.io.File;
import java.io.FileOutputStream;

import com.aca.amm.R;
import com.aca.amm.R.id;
import com.aca.amm.R.layout;
import com.aca.amm.R.menu;
import com.aca.database.DBA_PRODUCT_MAIN;

import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class SignatureActivity extends ControlNormalActivity {

	private Bundle b;
	private long SPPA_ID;
	private LinearLayout mContent;
	private signature mSignature;
	private Button mClear, mGetSign;
    private Bitmap mBitmap;
    private View mView;
    private String CurrentPath;

    public static final int SIGNATURE_ACTIVITY = 1;
	public static String tempDir;	
	public int count = 1;
	public String current = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_signature);
		
		mGetSign = (Button) findViewById(R.id.btnNext);
		mClear = (Button) findViewById(R.id.btnSign);
		
		try{
			Intent i = getIntent();
			b = i.getExtras();
			SPPA_ID = b.getLong("SPPA_ID");
			
			tempDir = Environment.getExternalStorageDirectory() +"/LoadImg/" + String.valueOf(SPPA_ID);
			
			CheckSign();
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		mClear.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                mSignature.clear();
                mGetSign.setEnabled(false);
            }
        });
	}
	
	private void CheckSign() {

		DBA_PRODUCT_MAIN dba = null;
		Cursor c = null;
		
		try{
			
			dba = new DBA_PRODUCT_MAIN(getBaseContext());
			dba.open();
			
			c = dba.getRow(SPPA_ID);
			c.moveToFirst();
			
			CurrentPath = c.getString(10);
			
			if(c.getString(10).isEmpty()){
				
				InitCanvas();
				
				ImageView myImage = (ImageView) findViewById(R.id.imageView1);
				myImage.setVisibility(View.GONE);
			}
			else{
				ImageView myImage = (ImageView) findViewById(R.id.imageView1);
				myImage.setVisibility(View.VISIBLE);
				LinearLayout ll = (LinearLayout) findViewById(R.id.linearLayout);
				ll.setVisibility(View.GONE);
				
				GetSignature(tempDir + "/" + c.getString(10));
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}finally{
			
			if (c != null)
				c.close();
			
			if (dba != null)
				dba.close();
		}
	}

	private void InitCanvas() {

        prepareDirectory();
        
        mContent = (LinearLayout) findViewById(R.id.linearLayout);
        mSignature = new signature(this, null);
        mSignature.setBackgroundColor(Color.WHITE);
        mContent.addView(mSignature, LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
        mGetSign.setEnabled(false);
        mView = mContent;
	}

    private boolean prepareDirectory() {
        try {
            if (makedirs()) 
                return true;
            else 
                return false;
            
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Could not initiate File System.. Is Sdcard mounted properly?", 1000).show();
            return false;
        }
    }
 
    private boolean makedirs() 
    {
        File tempdir = new File(tempDir);
        
        if (!tempdir.exists())
            tempdir.mkdirs();
        
        return (tempdir.isDirectory());
    }
    
    private void GetSignature(String path){
    	File imgFile = new  File(path);
        if(imgFile.exists()){

            ImageView myImage = (ImageView) findViewById(R.id.imageView1);
            myImage.setImageBitmap(Utility.GetImage(imgFile.getAbsolutePath()));
            
            Button b = (Button) findViewById(R.id.btnSign);
            b.setVisibility(View.GONE);
            
            //Toast.makeText(this, "Signature capture successful!", Toast.LENGTH_SHORT).show();
            	                        
        }else{
        	Toast.makeText(this, "Signature not found!", Toast.LENGTH_SHORT).show();
        }
    }

	protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        switch(requestCode) {
        
        	case SIGNATURE_ACTIVITY: 
        		if (resultCode == RESULT_OK) {
 
	                Bundle bundle = data.getExtras();
	                String status  = bundle.getString("status");
                
	                if(status.equalsIgnoreCase("done")){
		                b.putString("SIGN_PATH", bundle.getString("path"));
		                
		                File imgFile = new  File(bundle.getString("path"));
		                if(imgFile.exists()){
		
		                    ImageView myImage = (ImageView) findViewById(R.id.imageView1);
		                    myImage.setImageBitmap(Utility.GetImage(imgFile.getAbsolutePath()));
		                    
		                    Button b = (Button) findViewById(R.id.btnSign);
		                    b.setVisibility(View.GONE);
		                    
			                Toast.makeText(this, "Signature capture successful!", Toast.LENGTH_SHORT).show();                     
		                }
		                else
		                	Toast.makeText(this, "Signature not found!", Toast.LENGTH_SHORT).show();
	                }

        		}
        		break;
        }
 
    }  
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putAll(b);
	}
	
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		
		b = savedInstanceState;
		SPPA_ID = b.getLong("PROD_MAIN_ID");
		
		try{
			
			File imgFile = new  File(b.getString("SIGN_PATH"));
            if (imgFile.exists()) {

                ImageView myImage = (ImageView) findViewById(R.id.imageView1);
                myImage.setImageBitmap(Utility.GetImage(imgFile.getAbsolutePath()));
                
                Button b = (Button) findViewById(R.id.btnSign);
                b.setVisibility(View.GONE);
            }
            else{
            	Toast.makeText(this, "Signature not found!", Toast.LENGTH_SHORT).show();
            }
            
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.signature, menu);
		return true;
	}
	
	public void btnHomeClick(View v) {
		Intent i = new Intent(getBaseContext(),  FirstActivity.class);
		startActivity(i);
		this.finish();
	}
	
	public void btnBackClick(View v) {		
		Intent i = new Intent(getBaseContext(),  UploadPhotoActivity.class);
		i.putExtras(b);
		startActivity(i);
		this.finish();
	}
	
	public void btnNextClick(View v){

		Log.d("foto", CurrentPath);
		
        boolean error = false;
        
        if(!error){
        	
        	if (CurrentPath.isEmpty())
        	{
	            mView.setDrawingCacheEnabled(true);
	            mSignature.save(mView);
	            
	            DBA_PRODUCT_MAIN dba = null;
	            try{
	            	
	            	dba = new DBA_PRODUCT_MAIN(getBaseContext());
	            	dba.open();
	            	
	            	if(dba.updateSignature(SPPA_ID, "signature.jpeg")) {
	            		dba.updateCompleteDate(SPPA_ID, Utility.DateToString(Utility.GetToday(), "dd/MM/yyyy"));
	            		dba.updateISPPA(SPPA_ID);	
	            	}
	            	
	            }catch(Exception ex){
	            	ex.printStackTrace();
	            }finally{
	            	if (dba != null)
	            		dba.close();
	            }
        	}
        	
            Intent i = new Intent(getBaseContext(),  FirstActivity.class);
    		startActivity(i);
    		this.finish();
        }
	}
	

	@Override
	public void onBackPressed() {
		Intent i = new Intent(getBaseContext(),  UploadPhotoActivity.class);
		i.putExtras(b);
		startActivity(i);
		this.finish();
	}
	
	public class signature extends View  {
        private static final float STROKE_WIDTH = 5f;
        private static final float HALF_STROKE_WIDTH = STROKE_WIDTH / 2;
        private Paint paint = new Paint();
        private Path path = new Path();
 
        private float lastTouchX;
        private float lastTouchY;
        private final RectF dirtyRect = new RectF();
 
        public signature(Context context, AttributeSet attrs) {
            super(context, attrs);
            paint.setAntiAlias(true);
            paint.setColor(Color.BLACK);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeJoin(Paint.Join.ROUND);
            paint.setStrokeWidth(STROKE_WIDTH);
        }
 
        public void save(View v) 
        {
            if(mBitmap == null)
                mBitmap =  Bitmap.createBitmap (mContent.getWidth(), mContent.getHeight(), Bitmap.Config.RGB_565);
            
            Canvas canvas = new Canvas(mBitmap);
           
            try {
                FileOutputStream mFileOutStream = new FileOutputStream(tempDir + "/" + "signature.jpeg");
 
                v.draw(canvas); 
                mBitmap.compress(Bitmap.CompressFormat.JPEG, 80, mFileOutStream); 
                mFileOutStream.flush();
                mFileOutStream.close();
            }
            catch(Exception e)  { 
                e.printStackTrace(); 
            } 
        }
 
        public void clear() {
            path.reset();
            invalidate();
        }
 
        @Override
        protected void onDraw(Canvas canvas) {
            canvas.drawPath(path, paint);
        }
 
        @Override
        public boolean onTouchEvent(MotionEvent event) {
            float eventX = event.getX();
            float eventY = event.getY();
            mGetSign.setEnabled(true);
 
            switch (event.getAction()) 
            {
	            case MotionEvent.ACTION_DOWN:
	                path.moveTo(eventX, eventY);
	                lastTouchX = eventX;
	                lastTouchY = eventY;
	                return true;
	 
	            case MotionEvent.ACTION_MOVE:
	 
	            case MotionEvent.ACTION_UP:
	 
	                resetDirtyRect(eventX, eventY);
	                int historySize = event.getHistorySize();
	                for (int i = 0; i < historySize; i++) {
	                    float historicalX = event.getHistoricalX(i);
	                    float historicalY = event.getHistoricalY(i);
	                    expandDirtyRect(historicalX, historicalY);
	                    path.lineTo(historicalX, historicalY);
	                }
	                path.lineTo(eventX, eventY);
	                break;
	 
	            default:
	                debug("Ignored touch event: " + event.toString());
	                return false;
            }
 
            invalidate((int) (dirtyRect.left - HALF_STROKE_WIDTH),
                    (int) (dirtyRect.top - HALF_STROKE_WIDTH),
                    (int) (dirtyRect.right + HALF_STROKE_WIDTH),
                    (int) (dirtyRect.bottom + HALF_STROKE_WIDTH));
 
            lastTouchX = eventX;
            lastTouchY = eventY;
 
            return true;
        }
 
        private void debug(String string){
        
        }
 
        private void expandDirtyRect(float historicalX, float historicalY) {
            if (historicalX < dirtyRect.left) 
                dirtyRect.left = historicalX;
            else if (historicalX > dirtyRect.right) 
                dirtyRect.right = historicalX;
 
            if (historicalY < dirtyRect.top) 
                dirtyRect.top = historicalY;
            else if (historicalY > dirtyRect.bottom) 
                dirtyRect.bottom = historicalY;
        }
 
        private void resetDirtyRect(float eventX, float eventY)  {
            dirtyRect.left = Math.min(lastTouchX, eventX);
            dirtyRect.right = Math.max(lastTouchX, eventX);
            dirtyRect.top = Math.min(lastTouchY, eventY);
            dirtyRect.bottom = Math.max(lastTouchY, eventY);
        }
    }
}
