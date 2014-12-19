/*
 * BrightnessContrastEffect.cpp
 *
 *  Created on: Dec 15, 2014
 *      Author: Kyle
 */

#include "BrightnessContrastEffect.h"
#include <cmath>

#define min(a, b) (a < b) ? a : b
#define max(a, b) (a > b) ? a : b

BrightnessContrastEffect::BrightnessContrastEffect(float alpha, int beta) {
	contrast = alpha;
	brightness = beta;
}

BrightnessContrastEffect::~BrightnessContrastEffect() {
	// TODO Auto-generated destructor stub
}

cv::Vec3b BrightnessContrastEffect::processPixel(cv::Vec3b pixel, int row, int col) {
	cv::Vec3b retval;
	retval.val[0] = min(max(0, pixel.val[0] * contrast + brightness), 255);
	retval.val[1] = min(max(0, pixel.val[1] * contrast + brightness), 255);
	retval.val[2] = min(max(0, pixel.val[2] * contrast + brightness), 255);
	return retval;
}
