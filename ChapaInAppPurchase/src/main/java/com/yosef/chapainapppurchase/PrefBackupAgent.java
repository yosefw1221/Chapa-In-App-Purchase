package com.yosef.chapainapppurchase;

import android.app.backup.BackupAgentHelper;
import android.app.backup.SharedPreferencesBackupHelper;

public class PrefBackupAgent extends BackupAgentHelper {
    static final String PAYED_ITEM_PREFS = "PAYED_ITEMS";
    static final String BACKUP_KEY = "CHAPA_PAYED_ITEMS";
    static final String CHAPA_PREF = "CHAPA_IN_APP_PAYMENT";

    public void onCreate() {
        SharedPreferencesBackupHelper backupHelper = new SharedPreferencesBackupHelper(this, PAYED_ITEM_PREFS, CHAPA_PREF);
        addHelper(BACKUP_KEY, backupHelper);
    }
}
