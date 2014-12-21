package org.citruscircuits.vision.detectors;

import java.util.ArrayList;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Rect;
import org.opencv.imgproc.Imgproc;

public class ContourDetector extends ObjectDetector {
	@Override
	public DetectedObject findTarget(Mat source) {
		MatOfPoint ret = null;
		
		// Find contours
		ArrayList<MatOfPoint> contours = new ArrayList<MatOfPoint>();
        Mat hierarchy = new Mat();
        Imgproc.findContours(source.clone(), contours, hierarchy, Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_NONE);
        // Find biggest bounding box
        float largestArea = 0;
        for (int i = 0; i < contours.size(); i++) 
        {
        	if(Imgproc.contourArea(contours.get(i)) > largestArea)
        	{
        		ret = contours.get(i);
        		largestArea = (float) Imgproc.contourArea(contours.get(i));
        	}
        }

    	DetectedContour rv = new DetectedContour();
        if (ret != null)
        {
        	MatOfPoint2f approx = new MatOfPoint2f();
        	Imgproc.approxPolyDP(new MatOfPoint2f(ret.toArray()), approx, 5, true);
        	rv.detected.add(new MatOfPoint(approx.toArray()));
        }
		return rv;
	}

}
