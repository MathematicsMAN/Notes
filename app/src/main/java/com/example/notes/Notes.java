package com.example.notes;

import java.io.Serializable;
import java.util.Date;

public class Notes implements Serializable {
    private String id;
    private final String title;
    private String description;
    private Date dateOfCreated;
    private boolean asChecked;

    public Notes(String title, String description, Date dateOfCreated, boolean asChecked) {
        this.title = title;
        this.description = description;
        this.dateOfCreated = dateOfCreated;
        this.asChecked = asChecked;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDateOfCreated() {
        return dateOfCreated;
    }

    public void setDateOfCreated(Date dateOfCreated) {
        this.dateOfCreated = dateOfCreated;
    }

    public boolean isAsChecked() {
        return asChecked;
    }

    public void setAsChecked(boolean asChecked) {
        this.asChecked = asChecked;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
