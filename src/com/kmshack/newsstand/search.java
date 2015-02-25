package com.kmshack.newsstand;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;

public class search extends ScrollTabHolderFragment implements OnScrollListener {

    private static final String ARG_POSITION = "position";


    private int mPosition;

    public static Fragment newInstance(int position) {
        search f = new search();
        Bundle b = new Bundle();
        b.putInt(ARG_POSITION, position);
        f.setArguments(b);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPosition = getArguments().getInt(ARG_POSITION);



    }
   // View v=null;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

       //  v = inflater.inflate(R.layout.activity_main, null);



        return inflater.inflate(R.layout.activity_search, container, false);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

//        TextView title = (TextView) v.findViewById(R.id.title);
//        title.setText("Hello");
//        title.setTextColor(Color.BLUE);
    }

    @Override
    public void adjustScroll(int scrollHeight) {



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