package com.example.laba_5.Database;

import android.content.Context;

public class DatabaseManager {
    private static Database database;

    public static void setDatabase(Context context) {
        database = new Database(context);
    }

    public static Database getDatabase() {
        return database;
    }
}
