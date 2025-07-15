package com.FreshandClean.backend.DTOs;

import java.util.Collection;
import java.util.List;

public class BookPickupRequest
{
    public String userEmail;
    public List<BookItem> items;

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public List<BookItem> getItems() {
        return items;
    }

    public void setItems(List<BookItem> items) {
        this.items = items;
    }

    public static class Item {
        public String itemName;
        public int price;
        public int quantity;


    }




}


