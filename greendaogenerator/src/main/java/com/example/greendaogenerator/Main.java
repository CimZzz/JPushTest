package com.example.greendaogenerator;

import java.io.IOException;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

public class Main {
    public static void main(String[] args) throws Exception {
        Schema schema = new Schema(1, "com.example.yumi.jpushtest");
        addMessageItem(schema);
        new DaoGenerator().generateAll(schema, "./app/src/main/java-green");
    }


    private static void addMessageItem(Schema schema) {
        Entity note = schema.addEntity("MessageItem");
        note.addIdProperty().primaryKey().autoincrement();
        note.addStringProperty("text").notNull();
        note.addStringProperty("comment");
        note.addDateProperty("date");
    }
}
