package com.uic.cs478.Shanmathi;

import java.io.InputStream;
import java.net.URL;

import com.uic.cs478.Shanmathi.R;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;

/*
 * DetailsFragment is attached to ImageViewer Activity.
 * It displays the images associated with a Title in Table Layout.
 * When a user clicks on an Image , this fragment gets replaced with ImageFragment.
 */
public class DetailsFragment extends Fragment {

	public static int mCurrIdx = -1;
	private int mQuoteArrayLen = 0;
	private TableLayout mTableLayout ;

	// String to display when Status button is pressed
	public static String imageStatus = "idle";
	public static int imageToView;

	// Local Bitmaps to download the images related to the titles
	public static Bitmap[] result1 = new Bitmap[6];
	public static Bitmap[] result2 = new Bitmap[6];
	public static Bitmap[] result3 = new Bitmap[6];

	// Flags to check if images related to a title has been downloaded
	public static int flag1 = 0;
	public static int flag2 = 0;
	public static int flag3 = 0;


	private static final String TAG = "QuotesFragment";
	private ImageFragment mImageFragment = new ImageFragment();

	// Returns the Index of the title selected
	public int getShownIndex() {
		return mCurrIdx;
	}

	// Called when a fragment is first attached to its activity.
	@Override
	public void onAttach(Activity activity) {
		Log.i(TAG, getClass().getSimpleName() + ":entered onAttach()");
		super.onAttach(activity);
	}

	// Show the images based on position newIndex
	public void displayTableView(int newIndex) {
		if (newIndex < 0 || newIndex >= mQuoteArrayLen)
			return;
		mCurrIdx = newIndex;

		// Execute the AssyncTask with the URL associated with the title that
		// was chosen
		new ReadPageTask().execute(ImageViewerActivity.mDetailsArray[mCurrIdx]
				.split(","));
	}

	// Called to do initial creation of a fragment.
	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.i(TAG, getClass().getSimpleName() + ":entered onCreate()");
		super.onCreate(savedInstanceState);

		// Control whether a fragment instance is retained across Activity
		// re-creation (such as from a configuration change).
		setRetainInstance(true);
	}

	// Called to create the content view for this Fragment
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		// Inflate the layout defined in details_fragment.xml
		// The last parameter is false because the returned view does not need
		// to be attached to the container ViewGroup

		return inflater.inflate(R.layout.details_fragment, container, false);


	}

	// Called when the fragment's activity has been created and this fragment's
	// view hierarchy instantiated.
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

	
		// Look for a child view with the given id displayTable
		mTableLayout = (TableLayout) getView().findViewById(R.id.displayTable);

		// Assign the length of the mDetailsArray
		mQuoteArrayLen = ImageViewerActivity.mDetailsArray.length;

		// Display the images based on the item selected in the Title
		displayTableView(mCurrIdx);
		
		

	}

	// Interface definition for a callback to be invoked when a view is clicked.
	View.OnClickListener mListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			
			imageStatus = "Showing selected picture";
			
			
			imageToView = v.getId();
		
			
			/*
			 * Return the FragmentManager for interacting with fragments associated with this fragment's activity.
			 * Start a series of edit operations on the Fragments associated with this FragmentManager. 
			 */
			FragmentTransaction fragTransaction = getFragmentManager()
					.beginTransaction();
			fragTransaction.replace(R.id.details, new ImageFragment(),"image_tag");
			// Add this transaction to the back stack.
			fragTransaction.addToBackStack(null);
			// Schedules a commit of this transaction.
			fragTransaction.commit();
			return;
		}
	};

	public class ReadPageTask extends AsyncTask<String, Integer, Bitmap[]> {
		
		// Runs on the UI thread before doInBackground.
		protected void onPreExecute() {

		}
		
		// Override this method to perform a computation on a background thread.
		@Override
		protected Bitmap[] doInBackground(String... strings) {

			URL[] aUrl = new URL[6];
			Bitmap[] result = new Bitmap[6];
			// Assign the String URL and its associated Bitmaps to a Bitmap Array
			try {
				int cnt = 0;
				if (strings != null) {
					// Checks if the images have already been downloaded
					if (mCurrIdx == 0 && flag1 == 1)
						return result1;
					if (mCurrIdx == 1 && flag2 == 1)
						return result2;
					if (mCurrIdx == 2 && flag3 == 1)
						return result3;
					
					// If not already downloaded, images are downloaded into Bitmap Array result
					imageStatus = "downloading pictures";
					for (String imgString : strings)
						aUrl[cnt++] = new URL(imgString);
					cnt = 0;
					for (URL url : aUrl) {
						// Decode an input stream into a bitmap
						result[cnt++] = BitmapFactory
								.decodeStream((InputStream) url.getContent());
						System.out.println(url);
					}
				}
			} catch (Exception e) {
				System.out.println("could not read web site: " + strings[0]
						+ ".");
			}
			;
			return result;
		}

		// Runs on the UI thread after doInBackground. The specified result is the value returned by doInBackground.
		protected void onPostExecute(Bitmap[] bitmaps) {
			
			//getView().setVisibility(View.VISIBLE);

			mTableLayout.removeAllViews();
			TableRow row;
			ImageView img;
			
			// If images are downloaded for the first time, images are saved in the local bitmap arrays
			if (mCurrIdx == 0 && flag1 == 0) {
				flag1 = 1;
				result1 = bitmaps;
			}
			if (mCurrIdx == 1 && flag2 == 0) {
				flag2 = 1;
				result2 = bitmaps;
			}
			if (mCurrIdx == 2 && flag3 == 0) {
				flag3 = 1;
				result3 = bitmaps;
			}
			
			// The resulting bitmap array is dynamically added to the Table Layout of the DetailsFragment

			for (int i = 0; i < bitmaps.length; i += 2) {
				row = new TableRow(getActivity());
				row.setLayoutParams(new TableRow.LayoutParams(
						LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
				row.setPadding(10, 5, 10, 5);

				TableRow.LayoutParams lp = new TableRow.LayoutParams(530, 340);
				lp.setMarginEnd(10);
				
				// Create a new ImageView and set the Bitmap to it
				img = new ImageView(getActivity());
				img.setId(i);
				// Sets a Bitmap as the content of this ImageView.
				img.setImageBitmap(bitmaps[i]);
				// Controls how the image should be resized or moved to match the size of this ImageView.
				img.setScaleType(ImageView.ScaleType.CENTER_CROP);
				img.setLayoutParams(lp);
				// Register a callback to be invoked when this view is clicked. 
				img.setOnClickListener(mListener);
				row.addView(img);
				
				// Create a new ImageView and set the Bitmap to it
				img = new ImageView(getActivity());
				img.setId(i + 1);
				// Sets a Bitmap as the content of this ImageView.
				img.setImageBitmap(bitmaps[i + 1]);
				// Controls how the image should be resized or moved to match the size of this ImageView.
				img.setScaleType(ImageView.ScaleType.CENTER_CROP);
				img.setLayoutParams(lp);
				// Register a callback to be invoked when this view is clicked. 
				img.setOnClickListener(mListener);
				row.addView(img);

				System.out.println(bitmaps[i]);
				// Adds a child view(row) with the specified layout parameters.
				mTableLayout.addView(row, new TableLayout.LayoutParams(
						LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

			}
			
			// Set the button status to show that downloading has been finished
			imageStatus = "Showing downloaded thumbnails";
		}

	}


	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		Log.i(TAG, getClass().getSimpleName() + ":onConfigurationChanged()");
		super.onConfigurationChanged(newConfig);
	}

	@Override
	public void onDestroy() {
		Log.i(TAG, getClass().getSimpleName() + ":onDestroy()");
		super.onDestroy();
	}

	@Override
	public void onDestroyView() {
		Log.i(TAG, getClass().getSimpleName() + ":onDestroyView()");
		super.onDestroyView();
	}

	@Override
	public void onDetach() {
		Log.i(TAG, getClass().getSimpleName() + ":onDetach()");
		super.onDetach();
	}

	@Override
	public void onPause() {
		Log.i(TAG, getClass().getSimpleName() + ":onPause()");
		super.onPause();
	}

	@Override
	public void onResume() {
		Log.i(TAG, getClass().getSimpleName() + ":onResume()");
		super.onResume();
	}

	@Override
	public void onStart() {
		Log.i(TAG, getClass().getSimpleName() + ":onStart()");
		super.onStart();
	}

	@Override
	public void onStop() {
		Log.i(TAG, getClass().getSimpleName() + ":onStop()");
		super.onStop();
	}

}
