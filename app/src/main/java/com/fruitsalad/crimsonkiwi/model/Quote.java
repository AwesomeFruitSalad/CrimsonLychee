package com.fruitsalad.crimsonkiwi.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class Quote {

    @SerializedName("author")
    @Expose
    private String author;
    @SerializedName("category")
    @Expose
    private String category;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("quote")
    @Expose
    private String quote;

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQuote() {
        return quote;
    }

    public void setQuote(String quote) {
        this.quote = quote;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("author", author).append("category", category).append("id", id).append("quote", quote).toString();
    }

}