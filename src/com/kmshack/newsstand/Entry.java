package com.kmshack.newsstand;

public class Entry {

	public Entry(CharSequence title, String pubdate, String description,
			String img) {
		super();
		this.title = title;
		this.pubdate = pubdate;
		this.description = description;
		this.img = img;
	}
	public CharSequence title;
	public String pubdate;
	public String description;
	public String img;

}
