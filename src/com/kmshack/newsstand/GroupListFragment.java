package com.kmshack.newsstand;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

public class GroupListFragment extends ScrollTabHolderFragment implements OnScrollListener {

	private static final String ARG_POSITION = "position";

	private ListView mListView;


	private int mPosition;

	public static Fragment newInstance(int position) {
		GroupListFragment f = new GroupListFragment();
		Bundle b = new Bundle();
		b.putInt(ARG_POSITION, position);
		f.setArguments(b);
		return f;
	}

	//Default list view elements -generated dynamically
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mPosition = getArguments().getInt(ARG_POSITION);



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
        sortedlist.add(new Entry("18 A travel Team", "hello", "Standford Girls Water Polo", "google"));
        sortedlist.add(new Entry("San Carlos Car pool!", "hello", "Standford Girls Water Polo", "google"));
        sortedlist.add(new Entry("18 A travel Team", "hello", "Standford Girls Water Polo", "google"));
        sortedlist.add(new Entry("18 A travel Team", "hello", "Standford Girls Water Polo", "google"));
        sortedlist.add(new Entry("Create a new group!", "", "", ""));
        sortedlist.add(new Entry("18 A travel Team", "hello", "Standford Girls Water Polo", "google"));
        sortedlist.add(new Entry("San Carlos Car pool!", "hello", "Standford Girls Water Polo", "google"));
        sortedlist.add(new Entry("18 A travel Team", "hello", "Standford Girls Water Polo", "google"));
        sortedlist.add(new Entry("18 A travel Team", "hello", "Standford Girls Water Polo", "google"));
        sortedlist.add(new Entry("Create a new group!", "", "", ""));
        EntryAdapter eadapter = new EntryAdapter(getActivity(),sortedlist);
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
    int mLastFirstVisibleItem = 0;
    boolean mIsScrollingUp = true;
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
        LinearLayout lv = (LinearLayout) getActivity().findViewById(R.id.toolbardown);



        if (view.getId() == mListView.getId()) {
            final int currentFirstVisibleItem = mListView.getFirstVisiblePosition();
            if (currentFirstVisibleItem > mLastFirstVisibleItem) {
                mIsScrollingUp = false;
                lv.setVisibility(View.INVISIBLE);
                Log.d("MYLOG", mLastFirstVisibleItem + " " + mIsScrollingUp);

            } else if (currentFirstVisibleItem < mLastFirstVisibleItem) {
                mIsScrollingUp = true;
                lv.setVisibility(View.VISIBLE);
                Log.d("MYLOG",mLastFirstVisibleItem+" "+mIsScrollingUp);
            }

            mLastFirstVisibleItem = currentFirstVisibleItem;
        }
	}

}