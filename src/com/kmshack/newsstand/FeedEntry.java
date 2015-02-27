package com.kmshack.newsstand;

public class FeedEntry {

	public FeedEntry(CharSequence title, String pubdate, String description,
                     String img, int BandColor,String PostImg) {
		super();
		this.title = title;
		this.pubdate = pubdate;
		this.description = description;
		this.img = img;
        this.BandColor = BandColor;
        this.PostImg = PostImg;
	}
	public CharSequence title;
	public String pubdate;
	public String description;
	public String img;
    public int BandColor;
    public String PostImg;
}
