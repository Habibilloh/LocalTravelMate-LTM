package com.example.abdul.ltm;

public class Countries {

    public String nameCountry;
    public String urlCountry;
    public String imageCountry;

    public Countries() {
    }

    public Countries(String nameCountry, String urlCountry, String imageCountry) {
        this.nameCountry = nameCountry;
        this.urlCountry = urlCountry;
        this.imageCountry = imageCountry;
    }

    public String getNameCountry() {
        return nameCountry;
    }

    public void setNameCountry(String nameCountry) {
        this.nameCountry = nameCountry;
    }

    public String getUrlCountry() {
        return urlCountry;
    }

    public void setUrlCountry(String urlCountry) {
        this.urlCountry = urlCountry;
    }

    public String getImageCountry() {
        return imageCountry;
    }

    public void setImageCountry(String imageCountry) {
        this.imageCountry = imageCountry;
    }
}
