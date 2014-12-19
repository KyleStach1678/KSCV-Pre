package org.citruscircuits.vision.effects;

import java.util.Date;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;

public class FramesEffect extends ImageEffect {
	long lastTime;
	int frames;
	long lastUpdate;
	
	public FramesEffect() {
		frames = 0;
		lastTime = new Date().getTime();
	}
	
	@Override
	public Mat process(Mat original) {
		long curTime = new Date().getTime();
		if (curTime - lastUpdate > 100) {
			frames = (int) (1000 / (curTime - lastTime));
			lastUpdate = curTime;
		}
		lastTime = curTime;
		Core.putText(original, new Integer(frames).toString(), new Point(20, 50), Core.FONT_HERSHEY_TRIPLEX, 2, new Scalar(255, 0, 0), 3);
		return original;
	}

}
