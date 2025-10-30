package com.example.pos_project.dto;

import com.google.gson.annotations.SerializedName;

public class PaginationMeta {
    @SerializedName("total")
    private int total;
    
    @SerializedName("last_page")
    private int lastPage;
    
    @SerializedName("current_page")
    private int currentPage;
    
    @SerializedName("per_page")
    private int perPage;

    // Constructors
    public PaginationMeta() {}

    // Getters and Setters
    public int getTotal() { return total; }
    public void setTotal(int total) { this.total = total; }

    public int getLastPage() { return lastPage; }
    public void setLastPage(int lastPage) { this.lastPage = lastPage; }

    public int getCurrentPage() { return currentPage; }
    public void setCurrentPage(int currentPage) { this.currentPage = currentPage; }

    public int getPerPage() { return perPage; }
    public void setPerPage(int perPage) { this.perPage = perPage; }
}