package com.example.notes;

import com.google.firebase.Timestamp;

import java.util.HashMap;
import java.util.Map;

public class NotesMapping {

    public static class Fields {
        public static final String TITLE = "title";
        public static final String DESCRIPTION = "description";
        public static final String DATEOFCREATED = "dateOfCreated";
        public static final String CHECKED = "asChecked";
    }

    public static Notes toNotes (String id, Map<String, Object> doc) {
        Timestamp dateOfCreated = (Timestamp) doc.get(Fields.DATEOFCREATED);
        Notes note = new Notes((String) doc.get(Fields.TITLE),
                (String) doc.get(Fields.DESCRIPTION),
                dateOfCreated.toDate(),
                (Boolean) doc.get(Fields.CHECKED));
        note.setId(id);
        return note;
    }

    public static Map<String, Object> toDocument(Notes note) {
        Map<String, Object> result = new HashMap<>();
        result.put(Fields.TITLE, note.getTitle());
        result.put(Fields.DESCRIPTION, note.getDescription());
        result.put(Fields.DATEOFCREATED, new Timestamp(note.getDateOfCreated()));
        result.put(Fields.CHECKED, note.isAsChecked());
        return result;
    }
}
