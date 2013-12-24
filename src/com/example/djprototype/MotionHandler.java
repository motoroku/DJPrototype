package com.example.djprototype;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;

public class MotionHandler {

	public boolean verticallSwing(SensorEvent event, float accYPoint) {
		if (event.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION) {
			if (event.values[SensorManager.DATA_Y] < -5 && accYPoint - event.values[SensorManager.DATA_Y] > 10) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	public boolean sideSwing(SensorEvent event, float gyroZPoint) {
		if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
			if (event.values[SensorManager.DATA_Z] - gyroZPoint < -5) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
}
