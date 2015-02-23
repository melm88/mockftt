package com.kmshack.newsstand;

/**
 * Created by hari on 2/23/2015.
 */
  import java.util.ArrayList;

        import android.annotation.SuppressLint;
        import android.os.Bundle;
        import android.view.View;
        import android.widget.ListView;

@SuppressLint("NewApi")
public class settings extends android.support.v4.app.Fragment{


    public android.view.View onCreateView(android.view.LayoutInflater inflater, android.view.ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.group, container, false);
        ListView lv = (ListView)view.findViewById(R.id.listView);

        ArrayList<Entry> sortedlist=new ArrayList<Entry>();
        sortedlist.add(new Entry("18 A travel Team", "hello", "Standford Girls Water Polo", "google"));
        sortedlist.add(new Entry("San Carlos Car pool!", "hello", "Standford Girls Water Polo", "google"));
        sortedlist.add(new Entry("18 A travel Team", "hello", "Standford Girls Water Polo", "google"));
        sortedlist.add(new Entry("18 A travel Team", "hello", "Standford Girls Water Polo", "google"));
        sortedlist.add(new Entry("Create a new group!", "", "", ""));
        EntryAdapter eadapter = new EntryAdapter(getActivity(),sortedlist);
        lv.setAdapter(eadapter);
        return view;

    };

}
