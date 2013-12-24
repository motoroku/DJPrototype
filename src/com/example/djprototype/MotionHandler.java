package com.example.djprototype;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;

public class MotionHandler {

	public boolean verticallSlide(SensorEvent event, float accYPoint) {
		if (event.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION) {
			return event.values[SensorManager.DATA_Y] < -5 && accYPoint - event.values[SensorManager.DATA_Y] > 10;
		} else {
			return false;
		}
	}

	public boolean sideSwing(SensorEvent event, float gyroZPoint) {
		if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
			return event.values[SensorManager.DATA_Z] - gyroZPoint < -5;
		} else {
			return false;
		}
	}

	public boolean frontSlide(SensorEvent event, float accZPoint) {
		if (event.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION) {
			return event.values[SensorManager.DATA_Z] < -5 && accZPoint - event.values[SensorManager.DATA_Z] > 10;
		} else {
			return false;
		}
	}
}
