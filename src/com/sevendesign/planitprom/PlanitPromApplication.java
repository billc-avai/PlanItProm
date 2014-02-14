package com.sevendesign.planitprom;

import android.app.Application;
import com.sevendesign.planitprom.database.manager.DataAdapter;
import com.testflightapp.lib.TestFlight;

public class PlanitPromApplication extends Application {
    private final String TEST_FLIGHT_APP_TOKEN = "de6d188f-16f7-4f05-b70b-d4ca63c59963";

    @Override
    public void onCreate() {
        super.onCreate();
        initTestFly();
        initDatabase();
    }

    private void initDatabase() {
        DataAdapter.initInstance(this);
    }

    private void initTestFly() {
        TestFlight.takeOff(this, TEST_FLIGHT_APP_TOKEN);
    }
}
