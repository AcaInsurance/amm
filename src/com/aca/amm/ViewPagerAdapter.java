package com.aca.amm;

import com.aca.amm.R;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class ViewPagerAdapter extends PagerAdapter  {
	
	Context context;
	int[] flag;    
	LayoutInflater inflater;    
	
	private int[] pageIDsArray;
    private int count;

   
	public ViewPagerAdapter(Context context, int[] flag, final ViewPager pager) 
	{        
		super();
		
		this.context = context;        
		this.flag = flag;    
		
		
		int actualNoOfIDs = flag.length;
		count = actualNoOfIDs + 2;
		pageIDsArray = new int[count];

		for (int i = 0; i < actualNoOfIDs; i++) {
	            pageIDsArray[i + 1] = flag[i];
		}

		pageIDsArray[0] = flag[actualNoOfIDs - 1];
        pageIDsArray[count - 1] = flag[0];
        
        pager.setOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                int pageCount = getCount();
                if (position == 0){
                    pager.setCurrentItem(pageCount-2,false);
                } else if (position == pageCount-1){
                    pager.setCurrentItem(1,false);
                    
                }
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
	}
	
	@Override    
	public int getCount() 
	{        
		return count;
	}     
	
	@Override    
	public boolean isViewFromObject(View view, Object object) 
	{        
		return view == ((RelativeLayout) object);    
	}
	
	@Override    
	public Object instantiateItem(ViewGroup container, int position) 
	{         
		// Declare Variables              
		ImageView imgflag;         
		
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);        
		
		View itemView = inflater.inflate(R.layout.viewpager_item, container, false);         
		
		// Locate the ImageView in viewpager_item.xml        
		imgflag = (ImageView) itemView.findViewById(R.id.flag);       
		
		// Capture position and set to the ImageView        
		imgflag.setImageResource(pageIDsArray[position]);     
  
		((ViewPager) container).addView(itemView, 0);         
		
		return itemView;    
	}
	
	@Override   
	public void destroyItem(ViewGroup container, int position, Object object)  {             
		((ViewPager) container).removeView((RelativeLayout) object);     
	}
}
