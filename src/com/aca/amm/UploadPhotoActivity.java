package com.aca.amm;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Calendar;

import org.apache.http.conn.ConnectTimeoutException;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import com.aca.amm.R;
import com.aca.database.DBA_PRODUCT_MAIN;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;


public class UploadPhotoActivity extends ControlNormalActivity {

	private Bundle b;
	private long SPPA_ID;
	private String PRODUCT_ACTION;
	private String STATUS;
	private Context context = null;

	private TableLayout image_table = null;
    
	private ArrayList<String> image_list = new ArrayList<String>();
	private ArrayList<Drawable> image_drawable = new ArrayList<Drawable>();
	private String path="";

	private Button btnSelect = null;
    private Button btnClear = null;
    private Button btnSync  = null;
	
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_upload_photo);

        context = UploadPhotoActivity.this;

		btnSelect = (Button)findViewById(R.id.btnSelect);
		btnClear = (Button)findViewById(R.id.btnClear);
		btnSync = (Button)findViewById(R.id.btnSync);
		image_table = (TableLayout)findViewById(R.id.image_table);

		registerForContextMenu(btnSelect);

	
		btnSelect.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v) 
            {
            	
                openContextMenu(btnSelect);
            }
        });
		
		btnClear.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v) 
            {
            	image_list.clear();
                image_drawable.clear();
                deletePhotos();
                updateImageTable();
            }
        });
		
		btnSync.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v) 
            {
            	new SyncImage(UploadPhotoActivity.this, SPPA_ID, null).execute();
            	
//            	Intent i = null;
//				i = new Intent(context,  FirstActivity.class);
//
//				startActivity(i);
            }
        });

		
		//
		DBA_PRODUCT_MAIN db = null;
		Cursor c = null;
		
		try{
			Intent i = getIntent();
			b = i.getExtras();
			SPPA_ID = b.getLong("SPPA_ID");
			PRODUCT_ACTION = b.getString("PRODUCT_ACTION");
			STATUS = b.getString("STATUS");
			
			if (STATUS != null && STATUS.equals("APPROVED")) 
			{
				btnSync.setVisibility(View.GONE);
				btnSelect.setVisibility(View.GONE);
				btnClear.setVisibility(View.GONE);
			}
			else if(STATUS != null && STATUS.equals("NOTAPPROVED")) {
				btnClear.setVisibility(View.GONE);
			}
			retrivePhotos();
			
			
			
			db = new DBA_PRODUCT_MAIN(getBaseContext());
            db.open();
            
            c = db.getRow(SPPA_ID);
            
            c.moveToNext();
			
            String E_SPPA_NO = c.getString(5);
            
            if (E_SPPA_NO == null || E_SPPA_NO.length() == 0)
            {
            	btnSync.setVisibility(View.GONE);
            }
		}
		catch(Exception e){
			e.printStackTrace();
		}
		finally
		{
			if (c != null)
				c.close();
			
			if (db != null)
				db.close();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.upload_photo, menu);
		return true;
	}
	
	public void btnHomeClick(View v) {
		Intent i = new Intent(getBaseContext(),  FirstActivity.class);
		startActivity(i);
		this.finish();
	}
	
	public void btnBackClick(View v) {
		Back();
	}
	
	@Override
	public void onBackPressed() {
		Back();
	}
	
	private void Back() {
		Intent i = new Intent(getBaseContext(),  ConfirmActivity.class);
		i.putExtras(b);
		startActivity(i);
		this.finish();
	}

	public boolean cekFoto () {	
		boolean isAddedPhoto = true;
		String productName;

		try {
			productName = b.getString("PRODUCT_TYPE");
			
			if (productName.equalsIgnoreCase("OTOMATE") 		||
				productName.equalsIgnoreCase("OTOMATESYARIAH")  ||
				productName.equalsIgnoreCase("KONVENSIONAL") 	  )
			{
				isAddedPhoto =  image_list.size() == 0 ? false :  true;
//				isAddedPhoto = Utility.isAddedPhoto(SPPA_ID);
			}
			else
			{
				isAddedPhoto = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
		return isAddedPhoto;
	}
	
	public void btnNextClick(View v) {
		if (!cekFoto())
		{
			Utility.showCustomDialogInformation(UploadPhotoActivity.this, "Informasi", "Foto belum ada");
			return;
		}

		Intent i = new Intent(getBaseContext(),  SignatureActivity.class);
		i.putExtras(b);
		startActivity(i);
		this.finish();
		
	}
	
	public void retrivePhotos() {
		String folder = Environment.getExternalStorageDirectory() +"/LoadImg/" + String.valueOf(SPPA_ID);
        File f = new File(folder);
        if(f.isDirectory()) {
            File[] files=f.listFiles();
            for(int i=0;i<files.length;i++) {
            	if (files[i].getName().toString().trim().startsWith("signature"))
            		continue;
                String fpath = folder + File.separator + files[i].getName().toString().trim();
                image_list.add(fpath);
            }
            
            new GetImages().execute();
        }
	}
	
	public void deletePhotos()
    {
        String folder = Environment.getExternalStorageDirectory() +"/LoadImg/" + String.valueOf(SPPA_ID);
        File f = new File(folder);
        if(f.isDirectory()) {
            File[] files=f.listFiles();
            for(int i=0;i<files.length;i++)
            {
            	if (files[i].getName().toString().trim().startsWith("signature"))
            		continue;
            	
                String fpath=folder+File.separator+files[i].getName().toString().trim();
                File nf = new File(fpath);
                if(nf.exists())
                    nf.delete();
            }
        }
    }

	
	@Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("Post Image");
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.camer_menu, menu);
    }

	
	@Override
    public boolean onContextItemSelected(MenuItem item) {
      switch (item.getItemId())
      {
          case R.id.take_photo:
              takePhoto();
              break;
          case R.id.choose_gallery:
              Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
              photoPickerIntent.setType("image/*");
              startActivityForResult(photoPickerIntent, 1);
              break;
          case R.id.share_cancel:
              closeContextMenu();
              break;
          default:
            return super.onContextItemSelected(item);
      }
      return true;
    }
    
    public void takePhoto() {
         Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");

         File folder = new File(Environment.getExternalStorageDirectory() + "/LoadImg/" + String.valueOf(SPPA_ID));

         if(!folder.exists())
             folder.mkdirs();

         final Calendar c = Calendar.getInstance();
         String new_Date= c.get(Calendar.DAY_OF_MONTH) + "_" + ((c.get(Calendar.MONTH))+1) + "_" + c.get(Calendar.YEAR) + "_" + c.get(Calendar.HOUR) + "_" + c.get(Calendar.MINUTE) + "_" + c.get(Calendar.SECOND);
         path = String.format(Environment.getExternalStorageDirectory() + "/LoadImg/" + String.valueOf(SPPA_ID) + "/%s.png","LoadImg("+new_Date+")");
         		
         File photo = new File(path); 
         intent.putExtra(MediaStore.EXTRA_OUTPUT,Uri.fromFile(photo));
         startActivityForResult(intent, 2);
    }

    
	@Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) 
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK)
        {
	        if(requestCode==1)
	        {
	            Uri photoUri = data.getData();
	            if (photoUri != null)
	            {
	                String[] filePathColumn = {MediaStore.Images.Media.DATA};
	                Cursor cursor = getContentResolver().query(photoUri, filePathColumn, null, null, null);
	                cursor.moveToFirst();
	                
	                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
	                String filePath = cursor.getString(columnIndex);
	                cursor.close();
	                image_list.add(filePath);

	                File folder = new File(Environment.getExternalStorageDirectory() + "/LoadImg/" + String.valueOf(SPPA_ID));

	                if(!folder.exists())
	                    folder.mkdirs();

	                final Calendar c = Calendar.getInstance();
	                String new_Date= c.get(Calendar.DAY_OF_MONTH) + "_" + ((c.get(Calendar.MONTH))+1) + "_" +c.get(Calendar.YEAR) + "_" + c.get(Calendar.HOUR) + "_" + c.get(Calendar.MINUTE)+ "_"+ c.get(Calendar.SECOND);
	                String destPath = String.format(Environment.getExternalStorageDirectory() + "/LoadImg/" + String.valueOf(SPPA_ID) + "/%s.png","LoadImg("+new_Date+")");
	                
	                
	                File f = new File(destPath);
                    if (!f.exists())
                    {
                        try {
                            f.createNewFile();
                            copyFile(new File(filePath), f);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    DBA_PRODUCT_MAIN dba = new DBA_PRODUCT_MAIN(context);		
        			dba.open();
        			dba.updateCompletePhoto(SPPA_ID, "FALSE");
        			dba.close();
        			
	                new GetImages().execute();
	            }
	        }
	        else if(requestCode==2) {
	        	
	        	//File file = new File(path);
	        	//Log.d("test---", String.valueOf(file.exists()));
	        	
	        	//if (file.exists())
	        	//{
	        	//	Utility.resizeImageFile(file, 500);
	        	
	        		image_list.add(path);
	        		
                    DBA_PRODUCT_MAIN dba = new DBA_PRODUCT_MAIN(context);		
        			dba.open();
        			dba.updateCompletePhoto(SPPA_ID, "FALSE");
        			dba.close();

        			
	 	            new GetImages().execute();
                //}
	        }
        }
    }

	@SuppressWarnings("resource")
	private void copyFile(File sourceFile, File destFile) throws IOException
	{
        if (!sourceFile.exists()) {
            return;
        }

        FileChannel source = null;
        FileChannel destination = null;
        
        source = new FileInputStream(sourceFile).getChannel();
        destination = new FileOutputStream(destFile).getChannel();
        
        if (destination != null && source != null) {
            destination.transferFrom(source, 0, source.size());
        }
        
        if (source != null) {
            source.close();
        }
        
        if (destination != null) {
            destination.close();
        }
	}

	
	public void updateImageTable()
    {
        image_table.removeAllViews();
        
        if(image_drawable.size() > 0)
        {
            for(int i=0; i<image_drawable.size(); i++) {
                
            	TableRow tableRow = new TableRow(this);
                tableRow.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
                tableRow.setGravity(Gravity.CENTER_HORIZONTAL);
                tableRow.setPadding(5, 5, 5, 5);
                
                ImageView image = new ImageView(this);
                image.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
                image.setBackgroundDrawable(image_drawable.get(i));
                
                Button button = new Button(this);
                button.setTag(image_list.get(i).toString().trim() + "@" + String.valueOf(i));
                button.setText("Delete");
                button.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        //System.out.println("v.getid is:- " + v.getTag().toString());
                    
                    	String fpath = "";
                    	int idx = 0;
                    	
                    	String[] part = v.getTag().toString().split("@");
                    	fpath = part[0];
                    	idx = Integer.parseInt(part[1]);

                    	File nf = new File(fpath);
                        if(nf.exists())
                        {
                        	image_list.remove(idx);
                            image_drawable.remove(idx);
                             
                            nf.delete();
                            updateImageTable();
                        }
                        
                    }
                });
               if(STATUS != null && (
            		   STATUS.equals("NOTAPPROVED") || STATUS.equals("APPROVED"))) {
            	   button.setVisibility(View.GONE);
    			}

                
                tableRow.addView(image, 200, 200);
                tableRow.addView(button, 1);
                
                image_table.addView(tableRow);
            }
        }
    }

	public Drawable loadImagefromurl(Bitmap icon) {
        Drawable d=new BitmapDrawable(icon);        
        return d;
    }

	public class GetImages extends AsyncTask<Void, Void, Void> 
    {
        public ProgressDialog progDialog=null;
        
        protected void onPreExecute()  {
            progDialog = ProgressDialog.show(context, "", "Loading...",false);
        }
        
        @Override
        protected Void doInBackground(Void... params) {
            image_drawable.clear();
            for(int i=0; i<image_list.size(); i++) {
            	try {
            		
            		File file = new File(image_list.get(i).toString().trim()); 
            		Utility.resizeImageFile(file, 500);
            		
            		BitmapFactory.Options options = new BitmapFactory.Options();
            		options.inSampleSize = 2;
            		options.inPreferredConfig = Config.RGB_565;
            		
            		Bitmap bitmap = BitmapFactory.decodeFile(image_list.get(i).toString().trim(), options);
	                bitmap = Bitmap.createScaledBitmap(bitmap,500, 500, true);
	                Drawable d = loadImagefromurl(bitmap);
	                
	            	image_drawable.add(d);
            	}
            	catch(Exception e)
            	{
            		
            		//e.printStackTrace();
            	}
            }
            return null;
        }    
            
        protected void onPostExecute(Void result) 
        {
        	try
        	{
        		//if(progDialog != null progDialog.isShowing())
        		progDialog.dismiss();
        		progDialog = null;
        	}
            catch(Exception ex)
            {
            
            }
        	
            updateImageTable();
        }
    }
	
}
