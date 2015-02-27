package com.kmshack.newsstand;

import android.graphics.Color;

public class Entry {

	public Entry(CharSequence title, String pubdate, String description,
			String img,int BandColor) {
		super();
		this.title = title;
		this.pubdate = pubdate;
		this.description = description;
		this.img = img;
        this.BandColor = BandColor;
	}
	public CharSequence title;
	public String pubdate;
	public String description;
	public String img;
    public int BandColor;

}
