package com.uic.cs478.Shanmathi;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.res.Configuration;
import android.os.Bundle;

import com.uic.cs478.Shanmathi.TitlesFragment.ListSelectionListener;

/*
 * Development of Mobile Apps Project 3
 * Created By: Shanmathi Mayuram Krithivasan
 * Date : 23rd March 2015
 */

public class ImageViewerActivity extends Activity implements
		ListSelectionListener {

	// String to hold the Titles
	public static String[] mTitleArray;

	// String to hold the URL of the titles
	public static String[] mDetailsArray;

	// Instance of the Details Fragment which holds the images of the titles
	private DetailsFragment mDetailsFragment = new DetailsFragment();
	private ImageFragment mImageFragment = new ImageFragment();



	/*
	 * Called when the activity is starting. This is where most initialization
	 * is provided
	 */
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		// Get the string arrays with the titles and URL
		mTitleArray = getResources().getStringArray(R.array.Titles);
		mDetailsArray = getResources().getStringArray(R.array.Details);

		/*
		 * Checks the configuration
		 * If the configuration is portrait , then main.xml is assigned
		 * If the configuration is landscape, then main_land.xml is assigned
		 */
		if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
			/* Set the activity content from a layout resource. 
			 * The resource will be inflated, adding all top-level views to the activity.
			 */
			setContentView(R.layout.main);
		} 
		else {
			/* Set the activity content from a layout resource. 
			 * The resource will be inflated, adding all top-level views to the activity.
			 */
			setContentView(R.layout.main_land);
		}

		// Checks if the DetailsFragment is already added
	
				if (!mDetailsFragment.isAdded()) {
					
					
					 // Return the FragmentManager for interacting with fragments associated with this activity.
					 // Start a series of edit operations on the Fragments associated with this FragmentManager.
					 
					 
					FragmentTransaction fragTransaction = getFragmentManager()
							.beginTransaction();
					
					
					 // Adds the fragment and Add this transaction to the back stack. 
					 // This means that the transaction will be remembered after it is committed, and will reverse its operation when later popped off the stack.
					 
					 
					fragTransaction.add(R.id.details, mDetailsFragment).addToBackStack(
							null);

					// Schedules a commit of this transaction.
					fragTransaction.commit();

				}
		
	}
	

	
	
	// Called when the user selects an item in the TitlesFragment
	@Override
	public void onListSelection(int index) {
		
		
		if (mDetailsFragment.getShownIndex() != index) {

			// Tell the QuoteFragment to show the images at position index
			mDetailsFragment.displayTableView(index);
		}
	}

	}