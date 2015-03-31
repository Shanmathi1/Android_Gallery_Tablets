package com.uic.cs478.Shanmathi;

import com.uic.cs478.Shanmathi.R;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

/*
 * TitlesFragment is the fragment that displays the list of titles.
 * Based on the item that is selected from the titles the images get displayed on the DetailsFragment.
 */
public class TitlesFragment extends Fragment {
	
	private ListSelectionListener mListener = null;
	private static final String TAG = "TitlesFragment";
	private int mCurrIdx = -1;
	private ListView mListView;
	private Button mButton;

	/*
	 *  Callback interface that allows this Fragment to notify the ImageViewerActivity when  
	 *  user clicks on a List Item  
	 */
 
	public interface ListSelectionListener {
		public void onListSelection(int index);
	}
	
	/*
	 * Called when a fragment is first attached to its activity. 
	 * onCreate(Bundle) will be called after this.
	 */
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {

			// Set the ListSelectionListener for communicating with the ImageViewerActivity
			mListener = (ListSelectionListener) activity;
		
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnArticleSelectedListener");
		}
	}
	
	// Called to do initial creation of a fragment. 
	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.i(TAG, getClass().getSimpleName() + ":entered onCreate()");
		super.onCreate(savedInstanceState);
		
		//Control whether a fragment instance is retained across Activity re-creation (such as from a configuration change).
		setRetainInstance(true);
	}

	// Called to have the fragment instantiate its user interface view.
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.i(TAG, getClass().getSimpleName() + ":entered onCreate()");
		return inflater.inflate(R.layout.list_fragment, container,false);
	}

	/*
	 * Called when the fragment's activity has been created and this fragment's view hierarchy instantiated. 
	 * It can be used to do final initialization once these pieces are in place, such as retrieving views or restoring state.
	 */
	@Override
	public void onActivityCreated(Bundle savedState) {
		super.onActivityCreated(savedState);
		

		// Look for a child view with the given id listView which is a ListView
		mListView = (ListView) getView().findViewById(R.id.listView);
		
		// Look for a child view with the given id statusButton which is a button
		mButton = (Button) getView().findViewById(R.id.statusButton);
		
		// Register a callback to be invoked when this view is clicked. 
		mButton.setOnClickListener(new OnClickListener(){
			
			// Called when a view has been clicked.
			@Override
			public void onClick(View arg0){
				Toast.makeText(getActivity(),  DetailsFragment.imageStatus, Toast.LENGTH_SHORT).show();
			}
			
			
		});


	// Register a callback to be invoked when an item in this AdapterView has been clicked.
	mListView.setOnItemClickListener(new OnItemClickListener(){
		
		// Callback method to be invoked when an item in this AdapterView has been clicked.
		@Override
		public void onItemClick(AdapterView<?> l ,View v, int pos, long id)
		{
			if(mCurrIdx != pos)
			{
				mCurrIdx = pos;
				
				mListener.onListSelection(pos);
			}
			// Defines the choice behavior for the List. The list allows up to one choice.
			((ListView) l).setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		}
	});
	
	// The list allows up to one choice
	mListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
	
	// Sets the data behind this ListView.
	mListView.setAdapter(new ArrayAdapter<String>(getActivity(), R.layout.list_item,ImageViewerActivity.mTitleArray));
	
	// Sets the checked state of the specified position. 
	if(-1 != mCurrIdx)
		mListView.setItemChecked(mCurrIdx, true);
}
}


	