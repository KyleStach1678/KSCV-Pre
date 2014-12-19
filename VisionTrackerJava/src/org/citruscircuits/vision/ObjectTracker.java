package org.citruscircuits.vision;

import org.citruscircuits.vision.detectors.ContourDetector;
import org.citruscircuits.vision.detectors.ObjectDetector;
import org.citruscircuits.vision.effects.BleedEffect;
import org.citruscircuits.vision.effects.EffectChain;
import org.citruscircuits.vision.effects.FramesEffect;
import org.citruscircuits.vision.effects.ImageEffect;
import org.citruscircuits.vision.effects.IteratePixelEffect;
import org.citruscircuits.vision.effects.SelectColorEffect;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.highgui.VideoCapture;


public class ObjectTracker {
	float currentError;
	Mat frame;
	EffectChain frameEffects, detectorEffects;
	ObjectDetector detector;
	VideoCapture cap;

	public class BrightContrastEffect extends IteratePixelEffect
	{
		private float contrast = 1;
		private int brightness = 0;
		public BrightContrastEffect(float nContrast, int nBrightness) {
			this.contrast = nContrast;
			this.brightness = nBrightness;
		}

		@Override
		public double[] processPixel(double[] in, int row, int col, int depth) {
			in[0] *= contrast;
			in[1] *= contrast;
			in[2] *= contrast;
			in[0] += brightness;
			in[1] += brightness;
			in[2] += brightness;
			return in;
		}
	}
	public float getError() // Return the error from -1 (Far left) to 1 (Far right)
	{
		return currentError;
	}

	public Mat getFrame()
	{
		return frame;
	}

	private Mat captureFrame() // Return new frame from camera
	{
		Mat retval = new Mat();
		cap.read(retval);
		return retval;
	}

	private Mat processFrame() {
		Mat frameproc = detectorEffects.process(frame.clone());
		Rect obj = detector.findTarget(frameproc);
		Core.rectangle(frame, obj.br(), obj.tl(), new Scalar(0, 255, 127));
		if (obj.width * obj.height > 0)
		{
			currentError = ((obj.x + obj.width / 2) - (frame.cols()  / 2));
			currentError /= (frame.cols() / 2);
		}
		else
		{
			currentError = 0;
		}

		frame = frameEffects.process(frame);

		return frame;
	}

	public void closeCap() {
		cap.release();
	}

	public void start() {
		cap = new VideoCapture();
		//		cap.open("http://192.168.1.7:8081/video.mjpg");
		cap.open(0);
		ImageEffect effect1 = new SelectColorEffect(40, 80, 64, 192, 64, 255);
		detectorEffects = new EffectChain();
		frameEffects = new EffectChain();
		detectorEffects.addToEnd(effect1);

		frameEffects.addToEnd(new FramesEffect());
		//effects.addToEnd(new BrightContrastEffect(2.0F, 50));
		//		effects.addToEnd(new BleedEffect(2F, 11));
		detector = new ContourDetector();
	}

	public void update() {
		frame = captureFrame();
		frame = processFrame();
		System.gc(); // Java GC was not being called at good intervals
	}

	public ObjectTracker() {
		currentError = 0;
	}

}
