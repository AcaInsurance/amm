package com.aca.amm;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.aca.navigationdrawer.CustomDrawerAdapter;
import com.aca.navigationdrawer.DrawerItem;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SimpleCursorAdapter;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

public class NavDrawerActivity extends Activity  {
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;

	private CharSequence mDrawerTitle;
	private CharSequence mTitle;
	CustomDrawerAdapter adapter;

	List<DrawerItem> dataList;
	
	 @Override
	 protected void onCreate(Bundle savedInstanceState) {
	 super.onCreate(savedInstanceState);
	 setContentView(R.layout.activity_nav_drawer);
	  
	 	// Initializing
		dataList = new ArrayList<DrawerItem>();
		mTitle = mDrawerTitle = getTitle();
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.left_drawer);

		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
				GravityCompat.START);

		// Add Drawer Item to dataList
//				dataList.add(new DrawerItem(true)); // adding a spinner to the list

		dataList.add(new DrawerItem("Main Menu")); // adding a header to the list
		dataList.add(new DrawerItem("Customer", R.drawable.ic_sync));
		dataList.add(new DrawerItem("Produk", R.drawable.ic_sync));
		dataList.add(new DrawerItem("Produksi", R.drawable.ic_sync));
		dataList.add(new DrawerItem("Sinkron", R.drawable.ic_sync));
		dataList.add(new DrawerItem("News", R.drawable.ic_sync));
		dataList.add(new DrawerItem("More", R.drawable.ic_sync));

		 dataList.add(new DrawerItem("Account"));// adding a header to the list
		dataList.add(new DrawerItem("My Account", R.drawable.ic_action_group));
		dataList.add(new DrawerItem("Change Password", R.drawable.ic_action_cloud));
		dataList.add(new DrawerItem("Sign Out", R.drawable.ic_sync));
		
		 dataList.add(new DrawerItem("Other Option")); // adding a header to the list
		dataList.add(new DrawerItem("About", R.drawable.ic_action_about));
		dataList.add(new DrawerItem("Settings", R.drawable.ic_action_settings));
		dataList.add(new DrawerItem("Help", R.drawable.ic_action_help));

		adapter = new CustomDrawerAdapter(this, R.layout.custom_drawer_item,
				dataList);

		mDrawerList.setAdapter(adapter);

		mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

		getActionBar().setBackgroundDrawable(new ColorDrawable(Color.BLACK));
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);
		
		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.ic_drawer, R.string.drawer_open,
				R.string.drawer_close) {
			public void onDrawerClosed(View view) {
				getActionBar().setTitle( Html.fromHtml("<font color='" + getResources().getColor(R.color.AMMC) + "'>"
	                    + mTitle + "</font>"));

				invalidateOptionsMenu(); // creates call to
											// onPrepareOptionsMenu()
			}					
			
			public void onDrawerOpened(View drawerView) {
				getActionBar().setTitle( Html.fromHtml("<font color='" + getResources().getColor(R.color.AMMC) + "'>"
	                    + mDrawerTitle + "</font>"));

				invalidateOptionsMenu(); // creates call to
											// onPrepareOptionsMenu()
			}
		};

		mDrawerLayout.setDrawerListener(mDrawerToggle);

	 }
	  
	 @Override
	 protected void onPostCreate(Bundle savedInstanceState) {
		 super.onPostCreate(savedInstanceState);
		 mDrawerToggle.syncState();
	 }
	  
	 @Override
	 public void onConfigurationChanged(Configuration newConfig) {
		 super.onConfigurationChanged(newConfig);
		 mDrawerToggle.onConfigurationChanged(newConfig);
	 }
	  
	 @Override
	 public boolean onPrepareOptionsMenu(Menu menu) {
		 boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
		  
		 for (int index = 0; index < menu.size(); index++) {
			 MenuItem menuItem = menu.getItem(index);
			 if (menuItem != null) {
				 // hide the menu items if the drawer is open
				 menuItem.setVisible(!drawerOpen);
			 }
		 }
	  
		 return super.onPrepareOptionsMenu(menu);
	 }
	  
	  
	 @Override
	 public boolean onOptionsItemSelected(MenuItem item) {
		 if (mDrawerToggle.onOptionsItemSelected(item)) {
			 return true;
		 }
		 return super.onOptionsItemSelected(item);
	 }
	  
	 private class DrawerItemClickListener implements ListView.OnItemClickListener {
		  
		 @Override
		 public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
			 boolean ceklogin = Utility.cekLogin(getBaseContext());
			 if (!ceklogin) {
				Intent i = new Intent (getBaseContext(), LoginActivity.class);
				Bundle b = new Bundle ();
				b.putString("act", "sppa");
				i.putExtras(b);
				startActivity(i);
			 }
			 else {
				 switch (position) {
				 
				 case 1: {
					 Intent intent = new Intent(getBaseContext(), ChooseCustomerActivity.class);
					 startActivity(intent);
					 break;
				 }
				 case 2: {
					 Intent intent = new Intent(getBaseContext(), ChooseProductActivity.class);
					 startActivity(intent);
					 break;
				 }
				 case 3: {
					 Intent intent = new Intent(getBaseContext(), SPPAActivity.class);
					 startActivity(intent);
					 break;
				 }
				 case 4: {
					 Intent intent = new Intent(getBaseContext(), SyncActivity.class);
					 startActivity(intent);
					 break;
				 }
				 case 5: {
					 Intent intent = new Intent(getBaseContext(), NewsActivity.class);
					 startActivity(intent);
					 break;
				 }
				 case 6: {
					 Intent intent = new Intent(getBaseContext(), MoreActivity.class);
					 startActivity(intent);
					 break;
				 }
				 /*****ACCOUNT******/
				 
				 case 8: {
					 Intent intent = new Intent(getBaseContext(), CustomerActivity.class);
					 startActivity(intent);
					 break;
				 }
				 case 9: {
					 Intent intent = new Intent(getBaseContext(), CustomerActivity.class);
					 startActivity(intent);
					 break;
				 }
				 case 10: {
					 Utility.ConfirmDialog(NavDrawerActivity.this, "Confirm", "Anda yakin ingin keluar dari aplikasi ini?", "Ya", "Tidak");
					 break;
				 }
				 /***** Option ****/
				 
				 case 11: {
					 Intent intent = new Intent(getBaseContext(), CustomerActivity.class);
					 startActivity(intent);
					 break;
				 }
				 case 12: {
					 Intent intent = new Intent(getBaseContext(), CustomerActivity.class);
					 startActivity(intent);
					 break;
				 }
				 case 13: {
					 Intent intent = new Intent(getBaseContext(), CustomerActivity.class);
					 startActivity(intent);
					 break;
				 }
				 
				 default:
				 break;	
			}
			
		 }
		 mDrawerLayout.closeDrawer(mDrawerList);
		 }
	 }
}