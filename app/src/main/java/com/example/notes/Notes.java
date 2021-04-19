package com.example.notes;

import java.io.Serializable;

public class Notes implements Serializable {
    private final String title;
    private String description;
    private String dataOfCreated;
    private boolean asChecked;

    public Notes(String title, String description, String dataOfCreated, boolean asChecked) {
        this.title = title;
        this.description = description;
        this.dataOfCreated = dataOfCreated;
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

    public String getDataOfCreated() {
        return dataOfCreated;
    }

    public void setDataOfCreated(String dataOfCreated) {
        this.dataOfCreated = dataOfCreated;
    }

    public boolean isAsChecked() {
        return asChecked;
    }

}
