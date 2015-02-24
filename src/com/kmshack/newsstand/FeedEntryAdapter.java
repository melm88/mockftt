package com.kmshack.newsstand;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tara.lazylist.ImageLoader;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

@SuppressLint("SimpleDateFormat") 
public class FeedEntryAdapter extends ArrayAdapter<Entry>
{

	private Context context;
	private ArrayList<Entry> items;
	private static LayoutInflater inflater=null;
	public ImageLoader imageLoader;


	public static class ViewHolder
	{

		public TextView title;
		public TextView subtitle;
		public TextView time;
		public ImageView image;

	}

	public FeedEntryAdapter(Context context, ArrayList<Entry> items)
	{
		super(context,0, items);
		this.context = context;
		this.items = items;
		inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		imageLoader=new ImageLoader(context);
	}

	public void updateentries(ArrayList<Entry> items)
	{
		this.items.addAll(items);
	}


	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		Entry ei  = (Entry)items.get(position);	
		View vi=convertView;	
		ViewHolder holder;
		if(convertView==null)
		{			
			vi = inflater.inflate(R.layout.feedelement, null);
			holder = new ViewHolder();
			holder.title = (TextView) vi.findViewById(R.id.title);
			holder.subtitle=(TextView)vi.findViewById(R.id.ds1);
			holder.image=(ImageView)vi.findViewById(R.id.img1);
			//holder.time=(TextView)vi.findViewById(R.id.date);		
			vi.setTag( holder );
		}
		else
			holder=(ViewHolder)vi.getTag();

		Typeface tf = Typeface.createFromAsset(context.getAssets(),
				"fonts/Georgia.ttf");
		holder.title.setTypeface(tf);
		holder.title.setText(ei.title);
		SimpleDateFormat newstring = new SimpleDateFormat("dd MMM hh:mm a");
		String pubTime ="";
		try
		{
			Date d = newstring.parse(ei.pubdate);
			DateFormat time1 = new SimpleDateFormat("hh:mm a");
			pubTime = time1.format(d);
			System.out.println(pubTime);
		}
		catch (ParseException e)
		{			
			e.printStackTrace();
		}

		if(holder.subtitle != null)
		{
			String sub = ei.description;				
			if(sub.length()>160)
				if(sub.indexOf(" ", 160)>160)
				sub=sub.substring(0,sub.indexOf(" ", 160));		
				else
					sub=sub.substring(0,160);		
			String content = sub;
			System.out.println(content);
			holder.subtitle.setText(content);
			
		}
		ImageView image = holder.image;		
		imageLoader.DisplayImage(ei.img, image);   	
		return vi;
	}	

}
