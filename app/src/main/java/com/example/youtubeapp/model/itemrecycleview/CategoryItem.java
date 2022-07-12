package com.example.youtubeapp.model.itemrecycleview;

public class CategoryItem {
    String idCategory;
    String titleCate;
    boolean check;

    public CategoryItem(String idCategory, String titleCate, boolean check) {
        this.idCategory = idCategory;
        this.titleCate = titleCate;
        this.check = check;
    }

    public String getIdCategory() {
        return idCategory;
    }

    public void setIdCategory(String idCategory) {
        this.idCategory = idCategory;
    }

    public String getTitleCate() {
        return titleCate;
    }

    public void setTitleCate(String titleCate) {
        this.titleCate = titleCate;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }
}
