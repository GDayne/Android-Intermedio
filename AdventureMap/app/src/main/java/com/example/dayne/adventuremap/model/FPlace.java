package com.example.dayne.adventuremap.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "Places")
public class FPlace extends Model {
    @Column(name = "Latitude")
    public double lat;

    @Column(name = "Longitude")
    public double lng;

    @Column(name = "Place")
    public String place;

}
