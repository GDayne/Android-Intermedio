package com.example.dayne.adventuremap.model;


import com.parse.ParseClassName;
import com.parse.ParseObject;

public class ParsePlace {


    @ParseClassName("Place")
    public class Place extends ParseObject {
        public Place(){

        }



        public double getLatitude(){
            return getDouble("lat");
        }

        public double getLongitude(){
            return getDouble("lat");
        }



       /*public void setDescription(String description){
            put("description", description);
        }*/
    }


}




