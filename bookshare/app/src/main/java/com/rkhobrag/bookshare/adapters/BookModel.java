package com.rkhobrag.bookshare.adapters;

/**
 * Created by rakesh on 19/10/17.
 */

public class BookModel {
    String id;
    String title;
    String author;
    int rating;
    String genre;
    String imgUrl;
    VolumeInfo volumeInfo = new VolumeInfo();
    String epubLink;
    String webReaderLink;

    public BookModel(String id, String title, String author, int rating, String genre, String imgUrl, String epubLink, String webReaderLink) {
        this.title = title;
        this.author = author;
        this.rating = rating;
        this.genre = genre;
        this.imgUrl = imgUrl;
        this.id = id;
        this.webReaderLink = webReaderLink;
        this.epubLink = epubLink;
    }

    public String getEpubLink() {
        return epubLink;
    }

    public void setEpubLink(String epubLink) {
        this.epubLink = epubLink;
    }

    public String getWebReaderLink() {
        return webReaderLink;
    }

    public void setWebReaderLink(String webReaderLink) {
        this.webReaderLink = webReaderLink;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
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

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public class VolumeInfo{
        public String title;
    }
}
