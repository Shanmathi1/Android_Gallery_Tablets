package com.uic.cs478.Shanmathi;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import com.uic.cs478.Shanmathi.R;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/*
 * ImageFragment replaces the DetailsFragment whenever the user selects a image in the Details Fragment TableLayout
 */
public class ImageFragment extends Fragment {

	private ImageView mImageView;
	private Bitmap mBitmap;
	private String imageUrl;

	// Callback interface that allows this Fragment to notify the
	// ImageViewerActivity when
	// user clicks on a List Item
	public interface ListSelectionListener {
		public void onListSelection(int index);
	}

	// Called when a fragment is first attached to its activity.
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		System.out.println("till here");
	}

	// Called to do initial creation of a fragment.
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		
		// Control whether a fragment instance is retained across Activity re-creation (such as from a configuration change). 
		setRetainInstance(true);
	}

	// Called to have the fragment instantiate its user interface view. 
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.image_fragment, container, false);
	}

	// Called when the fragment's activity has been created and this fragment's view hierarchy instantiated. 
	@Override
	public void onActivityCreated(Bundle savedState) {
		super.onActivityCreated(savedState);
		
		// Selects the image that has to be displayed based on the current selection in the DetailsFragment
		imageUrl = ImageViewerActivity.mDetailsArray[DetailsFragment.mCurrIdx]
				.split(",")[DetailsFragment.imageToView];
		
		// Look for a child view with the given id single_image
		mImageView = (ImageView) getView().findViewById(R.id.single_image);

		Runnable r = new Runnable() {
			public void run() {
				try {
					// Decode an input stream into a bitmap. 
					mBitmap = BitmapFactory.decodeStream((InputStream) new URL(
							imageUrl).getContent());

				} catch (MalformedURLException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				// Calls the displayImage method to display the image that was selected
				displayImage();
			};
		};

		Thread t1 = new Thread(r);
		t1.start();
	}

	// Method that displays the image that was selected in the DetailsFragment
	public void displayImage() {
		
		// Runs the specified action on the UI thread. 
		getActivity().runOnUiThread(new Runnable() {
			public void run() {
				synchronized (mBitmap) {
					// Sets a Bitmap as the content of this ImageView.
					mImageView.setImageBitmap(mBitmap);
				}
			}
		});

	}

}
