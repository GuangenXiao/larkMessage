package com.example.larkmessage.entity;

import android.location.Location;

public class exLocation extends Location {

    public exLocation()
    {
        super(new Location(""));
    }
    public exLocation(String provider) {
        super(provider);
    }

    public exLocation(Location l) {
        super(l);
    }
}
