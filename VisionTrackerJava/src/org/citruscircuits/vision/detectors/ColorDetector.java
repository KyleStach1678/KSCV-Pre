package org.citruscircuits.vision.detectors;

import java.util.ArrayList;

import org.opencv.core.*;
import org.opencv.imgproc.*;

public class ColorDetector extends ObjectDetector {
	float[][] HSV = {{0, 0}, {0, 0}, {0, 0}};
	Mat colorimg;
	
	public ColorDetector(float lowH, float highH, float lowS, float highS, float lowV, float highV) 
	{
		HSV[0][0] = lowH;
		HSV[0][1] = highH;
		HSV[1][0] = lowS;
		HSV[1][1] = highS;
		HSV[2][0] = lowV;
		HSV[2][1] = highV;
	}
	
	public Rect getBoundingBox(Mat source)
	{
		Rect retval = new Rect(0, 0, 0, 0);
		
		// Find contours
		ArrayList<MatOfPoint> contours = new ArrayList<MatOfPoint>();
        Mat hierarchy = new Mat();
        Imgproc.findContours(source.clone(), contours, hierarchy, Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_NONE);
		
        // Find biggest bounding box
        float largestArea = 0;
        for (int i = 0; i < contours.size(); i++) 
        {
        	Imgproc.drawContours(colorimg, contours, i, new Scalar(255, 0, 255), 3);
        	if(Imgproc.contourArea(contours.get(i)) > largestArea)
        	{
        		retval = Imgproc.boundingRect(contours.get(i));
        	}
        }
        
		return retval;
	}
	
	@Override
	public Rect findTarget(Mat source) {
		colorimg = source;
		Mat bw = source.clone();
		Core.inRange(source, new Scalar(HSV[0][0], HSV[1][0], HSV[2][0]), new Scalar(HSV[0][1], HSV[1][1], HSV[2][1]), source);
		Imgproc.cvtColor(source, bw, Imgproc.COLOR_BGR2GRAY);
//		Imgproc.blur(source, source, new Size(10, 10));
//		Imgproc.dilate(source, source, Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE, new Size(10, 10)));
//		Imgproc.erode(source, source, Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE, new Size(10, 10)));

		return getBoundingBox(bw);
	}

}
