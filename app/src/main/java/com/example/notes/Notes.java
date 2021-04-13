package com.example.notes;

import java.io.Serializable;

public class Notes implements Serializable {
    private final String title;
    private String description;
    private String dateOfCreated;
    private boolean asChecked;

    public Notes(String title, String description, String dateOfCreated, boolean asChecked) {
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

    public String getDateOfCreated() {
        return dateOfCreated;
    }

    public void setDateOfCreated(String dateOfCreated) {
        this.dateOfCreated = dateOfCreated;
    }

    public boolean isAsChecked() {
        return asChecked;
    }

}
