package com.rkhobrag.bookshare.adapters;

/**
 * Created by rakesh on 19/10/17.
 */

public class BookModel {
    String title;
    String Author;
    int rating;
    String genre;
    String img;
    VolumeInfo volumeInfo = new VolumeInfo();

    public BookModel(String title, String author, int rating, String genre) {
        this.title = title;
        Author = author;
        this.rating = rating;
        this.genre = genre;
    }

    public VolumeInfo getVolumeInfo() {
        return volumeInfo;
    }

    public void setVolumeInfo(VolumeInfo volumeInfo) {
        this.volumeInfo = volumeInfo;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return Author;
    }

    public void setAuthor(String author) {
        Author = author;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public class VolumeInfo{
        public String title;
    }
}
