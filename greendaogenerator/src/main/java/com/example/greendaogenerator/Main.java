package com.example.greendaogenerator;


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
        Entity msgItem = schema.addEntity("MessageItem");
        msgItem.addIdProperty().primaryKey().autoincrement();
        msgItem.addLongProperty("createTime");
        msgItem.addStringProperty("fromUser");
        msgItem.addStringProperty("toUser");
        msgItem.addIntProperty("status");
        msgItem.addIntProperty("type");
        msgItem.addStringProperty("jsonData");
    }
}
