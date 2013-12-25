package com.example.djprototype;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;

public class MotionHandler {

	float	accX	= 0;
	float	accY	= 0;
	float	accZ	= 0;
	float	gyroX	= 0;
	float	gyroY	= 0;
	float	gyroZ	= 0;

	public boolean verticallSlide(SensorEvent event) {
		if (event.sensor.getType() != Sensor.TYPE_LINEAR_ACCELERATION) {
			return false;
		}
		return event.values[SensorManager.DATA_Y] < -5 && accY - event.values[SensorManager.DATA_Y] > 10;
	}

	public boolean sideSwing(SensorEvent event) {
		if (event.sensor.getType() != Sensor.TYPE_GYROSCOPE) {
			return false;
		}
		return event.values[SensorManager.DATA_Z] - gyroZ < -5;
	}

	public boolean frontSlide(SensorEvent event) {
		if (event.sensor.getType() != Sensor.TYPE_LINEAR_ACCELERATION) {
			return false;
		}
		return event.values[SensorManager.DATA_Z] < -5 && accZ - event.values[SensorManager.DATA_Z] > 10;
	}

	public void reloadData(SensorEvent event) {
		if (event.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION) {
			// •Ï‰»‚Ì‚ ‚Á‚½’l‚ðƒŠƒXƒg‚É’Ç‰Á
			if (-0.1 > event.values[SensorManager.DATA_X] || event.values[SensorManager.DATA_X] > 0.1) {
				accX = event.values[SensorManager.DATA_X];
			}
			if (-0.1 > event.values[SensorManager.DATA_Y] || event.values[SensorManager.DATA_Y] > 0.1) {
				accY = event.values[SensorManager.DATA_Y];
			}
			if (-0.1 > event.values[SensorManager.DATA_Z] || event.values[SensorManager.DATA_Z] > 0.1) {
				accZ = event.values[SensorManager.DATA_Z];
			}
		}
		if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
			if (-0.1 > event.values[SensorManager.DATA_X] || event.values[SensorManager.DATA_X] > 0.1) {
				gyroX = event.values[SensorManager.DATA_X];
			}
			if (-0.1 > event.values[SensorManager.DATA_Y] || event.values[SensorManager.DATA_Y] > 0.1) {
				gyroY = event.values[SensorManager.DATA_Y];
			}
			if (-0.1 > event.values[SensorManager.DATA_Z] || event.values[SensorManager.DATA_Z] > 0.1) {
				gyroZ = event.values[SensorManager.DATA_Z];
			}
		}
	}
}
