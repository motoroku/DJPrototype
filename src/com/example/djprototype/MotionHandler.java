package com.example.djprototype;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;

public class MotionHandler {

	float[]	acceleration	= new float[3];
	float[]	gyroscope		= new float[3];

	boolean	swinged			= true;

	public boolean sideSwing(SensorEvent event) {
		if (event.sensor.getType() != Sensor.TYPE_GYROSCOPE) {
			return false;
		}
		if (event.values[SensorManager.DATA_Z] > 10 && swinged) {
			swinged = false;
			return true;
		}
		if (gyroscope[2] < 0) {
			swinged = true;
		}

		return false;
	}

	public boolean verticallSlide(SensorEvent event) {
		if (event.sensor.getType() != Sensor.TYPE_LINEAR_ACCELERATION) {
			return false;
		}
		return event.values[SensorManager.DATA_Y] < -5 && acceleration[1] - event.values[SensorManager.DATA_Y] > 10;
	}

	public boolean frontSlide(SensorEvent event) {
		if (event.sensor.getType() != Sensor.TYPE_LINEAR_ACCELERATION) {
			return false;
		}
		return event.values[SensorManager.DATA_Z] < -5 && acceleration[2] - event.values[SensorManager.DATA_Z] > 10;
	}

	public void reloadData(SensorEvent event) {
		if (event.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION) {
			// 変化のあった値をリストに追加
			if (-0.1 > event.values[SensorManager.DATA_X] || event.values[SensorManager.DATA_X] > 0.1) {
				acceleration[0] = event.values[SensorManager.DATA_X];
			}
			if (-0.1 > event.values[SensorManager.DATA_Y] || event.values[SensorManager.DATA_Y] > 0.1) {
				acceleration[1] = event.values[SensorManager.DATA_Y];
			}
			if (-0.1 > event.values[SensorManager.DATA_Z] || event.values[SensorManager.DATA_Z] > 0.1) {
				acceleration[2] = event.values[SensorManager.DATA_Z];
			}
		}
		if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
			if (-0.1 > event.values[SensorManager.DATA_X] || event.values[SensorManager.DATA_X] > 0.1) {
				gyroscope[0] = event.values[SensorManager.DATA_X];
			}
			if (-0.1 > event.values[SensorManager.DATA_Y] || event.values[SensorManager.DATA_Y] > 0.1) {
				gyroscope[1] = event.values[SensorManager.DATA_Y];
			}
			if (-0.1 > event.values[SensorManager.DATA_Z] || event.values[SensorManager.DATA_Z] > 0.1) {
				gyroscope[2] = event.values[SensorManager.DATA_Z];
			}
		}
	}
}
