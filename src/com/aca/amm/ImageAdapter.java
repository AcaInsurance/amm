package com.aca.amm;

import java.util.ArrayList;
import java.util.HashMap;

import com.aca.amm.R;
import com.aca.amm.R.id;
import com.aca.amm.R.layout;
 
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ImageAdapter extends BaseAdapter  {
	
	private Activity activity;
    private ArrayList<HashMap<String, String>> data;
    private static LayoutInflater inflater=null;
    public ImageLoader imageLoader; 
 
    public ImageAdapter(Activity a, ArrayList<HashMap<String, String>> d) {
        activity = a;
        data = d;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imageLoader = new ImageLoader(activity.getApplicationContext());
    }
 
    public int getCount() {
        return data.size();
    }
 
    public Object getItem(int position) {
        return position;
    }
 
    public long getItemId(int position) {
        return position;
    }
 
    public View getView(int position, View convertView, ViewGroup parent) {
        
    	View vi = convertView;
        
        if(convertView==null)
            vi = inflater.inflate(R.layout.list_item_photo, null);
        
        TextView rowid = (TextView)vi.findViewById(R.id.txtPhotoRowID);
        TextView desc = (TextView)vi.findViewById(R.id.description);
        TextView datetaken = (TextView)vi.findViewById(R.id.datetaken);
        ImageView img = (ImageView)vi.findViewById(R.id.img);
 
        HashMap<String, String> photo = new HashMap<String, String>();
        photo = data.get(position);
        
        rowid.setText(photo.get("PHOTO_ROWID"));
        desc.setText(photo.get("PHOTO_DESCRIPTION"));
        datetaken.setText(photo.get("PHOTO_DATE_TAKEN"));
        imageLoader.DisplayImage(photo.get("PHOTO_FILENAME"), img);
        
        return vi;
    }
}