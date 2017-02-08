package com.aca.amm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.aca.amm.AlphabetListAdapter.Item;
import com.aca.amm.AlphabetListAdapter.Row;
import com.aca.amm.AlphabetListAdapter.Section;
import com.aca.util.Var;

import static com.aca.amm.R.id.flag;

public class ChooseCountryActivity extends ControlListActivity {

	private Bundle b;
	private ListView lv;
	
	private AlphabetListAdapter adapter = new AlphabetListAdapter();
    private GestureDetector mGestureDetector;
    private List<Object[]> alphabet = new ArrayList<Object[]>();
    private HashMap<String, Integer> sections = new HashMap<String, Integer>();
    private int sideIndexHeight;
    private static float sideIndexX;
    private static float sideIndexY;
    private int indexListSize;

    class SideIndexGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            sideIndexX = sideIndexX - distanceX;
            sideIndexY = sideIndexY - distanceY;

            if (sideIndexX >= 0 && sideIndexY >= 0) {
                displayListItem();
            }

            return super.onScroll(e1, e2, distanceX, distanceY);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_choose_country);

        Intent i = getIntent();
		b = i.getExtras();
		
        mGestureDetector = new GestureDetector(this, new SideIndexGestureListener());
        int negaraTujuan = b.getInt("NEGARA_TUJUAN", 1);

        List<Utility.Country> countries;

        if (negaraTujuan == Var.LABBAIK_DEFAULT_NEGARA_TUJUAN)
            countries = populateCountries();
        else
            countries = populateCountries(negaraTujuan);

        Collections.sort(countries);
        

        List<Row> rows = new ArrayList<Row>();
        int start = 0;
        int end = 0;
        String previousLetter = null;
        Object[] tmpIndexItem = null;
        Pattern numberPattern = Pattern.compile("[0-9]");

        for (Utility.Country country : countries) {
            String firstLetter = country.text.substring(0, 1);

            // Group numbers together in the scroller
            if (numberPattern.matcher(firstLetter).matches()) {
                firstLetter = "#";
            }

            // If we've changed to a new letter, add the previous letter to the alphabet scroller
            if (previousLetter != null && !firstLetter.equals(previousLetter)) {
                end = rows.size() - 1;
                tmpIndexItem = new Object[3];
                tmpIndexItem[0] = previousLetter.toUpperCase(Locale.UK);
                tmpIndexItem[1] = start;
                tmpIndexItem[2] = end;
                alphabet.add(tmpIndexItem);

                start = end + 1;
            }

            // Check if we need to add a header row
            if (!firstLetter.equals(previousLetter)) {
                rows.add(new Section(firstLetter));
                sections.put(firstLetter, start);
            }

            // Add the country to the list
            rows.add(new Item(country.id, country.text));
            previousLetter = firstLetter;
        }

        if (previousLetter != null) {
            // Save the last letter
            tmpIndexItem = new Object[3];
            tmpIndexItem[0] = previousLetter.toUpperCase(Locale.UK);
            tmpIndexItem[1] = start;
            tmpIndexItem[2] = rows.size() - 1;
            alphabet.add(tmpIndexItem);
        }

        adapter.setRows(rows);
        setListAdapter(adapter);

      
        
        updateList();
    }

    
    @Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
 
		//get selected items
    	if (getListAdapter().getItem(position).getClass() == AlphabetListAdapter.Item.class){
			String KODE_NEGARA = ((Item)getListAdapter().getItem(position)).id;
			String NAMA_NEGARA = ((Item)getListAdapter().getItem(position)).text;
			
			
			Intent i = new Intent(getBaseContext(), FillTravelActivity.class);
			b.putString("KODE_NEGARA", KODE_NEGARA);
			b.putString("NAMA_NEGARA", NAMA_NEGARA);
			b.putString("PRODUCT_ACTION", "SELECT_COUNTRY");
			i.putExtras(b);
			setResult(100, i);
			ChooseCountryActivity.this.finish();

    	}
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mGestureDetector.onTouchEvent(event)) {
            return true;
        } else { 
            return false;
        }
    }

    public void updateList() {
        LinearLayout sideIndex = (LinearLayout) findViewById(R.id.sideIndex);
        sideIndex.removeAllViews();
        indexListSize = alphabet.size();
        if (indexListSize < 1) {
            return;
        }

        int indexMaxSize = (int) Math.floor(sideIndex.getHeight() / 20);
        int tmpIndexListSize = indexListSize;
        while (tmpIndexListSize > indexMaxSize) {
            tmpIndexListSize = tmpIndexListSize / 2;
        }
        double delta;
        if (tmpIndexListSize > 0) {
            delta = indexListSize / tmpIndexListSize;
        } else {
            delta = 1;
        }

        TextView tmpTV;
        for (double i = 1; i <= indexListSize; i = i + delta) {
            Object[] tmpIndexItem = alphabet.get((int) i - 1);
            String tmpLetter = tmpIndexItem[0].toString();

            tmpTV = new TextView(this);
            tmpTV.setText(tmpLetter);
            tmpTV.setGravity(Gravity.CENTER);
            tmpTV.setTextSize(15);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
            tmpTV.setLayoutParams(params);
            sideIndex.addView(tmpTV);
        }

        sideIndexHeight = sideIndex.getHeight();

        sideIndex.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // now you know coordinates of touch
                sideIndexX = event.getX();
                sideIndexY = event.getY();

                // and can display a proper item it country list
                displayListItem();

                return false;
            }
        });
    }

    public void displayListItem() {
        LinearLayout sideIndex = (LinearLayout) findViewById(R.id.sideIndex);
        sideIndexHeight = sideIndex.getHeight();
        // compute number of pixels for every side index item
        double pixelPerIndexItem = (double) sideIndexHeight / indexListSize;

        // compute the item index for given event position belongs to
        int itemPosition = (int) (sideIndexY / pixelPerIndexItem);

        // get the item (we can do it since we know item index)
        if (itemPosition < alphabet.size()) {
            Object[] indexItem = alphabet.get(itemPosition);
            int subitemPosition = sections.get(indexItem[0]);

            //ListView listView = (ListView) findViewById(android.R.id.list);
            getListView().setSelection(subitemPosition);
        }
    }

    private List<Utility.Country> populateCountries(int flag) {
        List<Utility.Country> countries = new ArrayList<Utility.Country>();
        Utility.BindNegaraTujuan(countries, getBaseContext(), flag);
        return countries;
    }

    private List<Utility.Country> populateCountries() {
        List<Utility.Country> countries = new ArrayList<Utility.Country>();
        Utility.BindNegaraTujuan(countries, getBaseContext(), 1);
        Utility.BindNegaraTujuan(countries, getBaseContext(), 2);
        return countries;
    }

}
