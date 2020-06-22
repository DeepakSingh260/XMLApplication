package com.example.top10songs;

public class FeedEntry {
    private String titile;

    public String getTitile() {
        return titile;
    }

    public void setTitile(String titile) {
        this.titile = titile;
    }

    @Override
    public String toString() {
        return
                "titile='" + titile + '\''+"\n\n"
                ;
    }
}
