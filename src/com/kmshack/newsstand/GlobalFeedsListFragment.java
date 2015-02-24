package com.kmshack.newsstand;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;

import java.util.ArrayList;

public class GlobalFeedsListFragment extends ScrollTabHolderFragment implements OnScrollListener {

	private static final String ARG_POSITION = "position";

	private ListView mListView;
	private ArrayList<String> mListItems;

	private int mPosition;

	public static Fragment newInstance(int position) {
		GlobalFeedsListFragment f = new GlobalFeedsListFragment();
		Bundle b = new Bundle();
		b.putInt(ARG_POSITION, position);
		f.setArguments(b);
		return f;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mPosition = getArguments().getInt(ARG_POSITION);

		mListItems = new ArrayList<String>();

		for (int i = 1; i <= 100; i++) {
			mListItems.add(i + ". item - currnet page: " + (mPosition + 1));
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_list, null);

		mListView = (ListView) v.findViewById(R.id.listView);

		View placeHolderView = inflater.inflate(R.layout.view_header_placeholder, mListView, false);
		mListView.addHeaderView(placeHolderView);

		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		mListView.setOnScrollListener(this);

       // mListView.setAdapter(new ArrayAdapter<String>(getActivity(), R.layout.list_item, android.R.id.text1, mListItems));
        ArrayList<Entry> sortedlist=new ArrayList<Entry>();
        sortedlist.add(new Entry(" Team", "hello", "Standford Girls Water Polo", "google"));
        sortedlist.add(new Entry("San Carlos Car pool!", "hello", "Standford Girls Water Polo", "google"));
        sortedlist.add(new Entry("18 A travel Team", "hello", "Standford Girls Water Polo", "google"));
        sortedlist.add(new Entry("18 A travel Team", "hello", "Standford Girls Water Polo", "google"));
        sortedlist.add(new Entry("Create a new group!", "", "", ""));
        sortedlist.add(new Entry("18 A travel Team", "hello", "Standford Girls Water Polo", "google"));
        sortedlist.add(new Entry("San Carlos Car pool!", "hello", "Standford Girls Water Polo", "google"));
        sortedlist.add(new Entry("18 A travel Team", "hello", "Standford Girls Water Polo", "google"));
        sortedlist.add(new Entry("18 A travel Team", "hello", "Standford Girls Water Polo", "google"));
        sortedlist.add(new Entry("Create a new group!", "", "", ""));
        FeedEntryAdapter eadapter = new FeedEntryAdapter(getActivity(),sortedlist);
        mListView.setAdapter(eadapter);
	}

	@Override
	public void adjustScroll(int scrollHeight) {
		if (scrollHeight == 0 && mListView.getFirstVisiblePosition() >= 1) {
			return;
		}

		mListView.setSelectionFromTop(1, scrollHeight);

	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		if (mScrollTabHolder != null)
			mScrollTabHolder.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount, mPosition);
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// nothing
	}

}