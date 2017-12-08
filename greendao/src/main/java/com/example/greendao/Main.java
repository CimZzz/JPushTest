package com.example.greendao;

import org.greenrobot.greendao.generator.DaoGenerator;
import org.greenrobot.greendao.generator.Entity;
import org.greenrobot.greendao.generator.Schema;

public class Main {
    public static void main(String[] args) throws Exception {

        Schema schema = new Schema(1, "com.company.project.datamodel");
        schema.enableKeepSectionsByDefault();

        // Define entities
        Entity primaryKey = schema.addEntity("CDPrimaryKey");
        Entity installation = schema.addEntity("CDInstallation");

        primaryKey.setTableName("Z_PRIMARYKEY");
        primaryKey.addLongProperty("ENT").columnName("Z_ENT").primaryKey();
        primaryKey.addIntProperty("MAX").columnName("Z_MAX");
        primaryKey.addStringProperty("NAME").columnName("Z_NAME");
        primaryKey.addIntProperty("SUPER").columnName("Z_INT");

        // CDInstallation
        installation.setTableName("ZCDINSTALLATION");
        installation.addLongProperty("packageDate").columnName("ZPACKAGEDATE");


        // **** Generate Schema ****
        new DaoGenerator().generateAll(schema, "app/src/main/java");
    }
}
