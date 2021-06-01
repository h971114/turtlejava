package com.dasu.turtlejava;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class Sensor {
    public double sensor1;
    public double sensor2;
    public double sensor3;

    public Sensor() {

    }

    public Sensor(double sensor1, double sensor2, double sensor3){
        this.sensor1 = sensor1;
        this.sensor2 = sensor2;
        this.sensor3 = sensor3;
    }

   public double getSensor1(){
        return sensor1;
   }
    public double setSensor1(){
        return sensor1;
    }
    public double getSensor2(){
        return sensor2;
    }
    public double setSensor2(){
        return sensor2;
    }
    public double getSensor3(){
        return sensor3;
    }
    public double setSensor3(){
        return sensor3;
    }
    @Override
    public String toString() {
        return "Sensor{" +
                "sensor1='" + sensor1 + '\'' +
                ", sensor2='" + sensor2 + '\'' +
                ", sensor3='" + sensor3 + '\'' +
                '}';
    }

}
