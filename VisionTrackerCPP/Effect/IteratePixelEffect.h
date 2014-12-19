/*
 * IteratePixelEffect.h
 *
 *  Created on: Dec 15, 2014
 *      Author: Kyle
 */

#ifndef EFFECT_ITERATEPIXELEFFECT_H_
#define EFFECT_ITERATEPIXELEFFECT_H_

#include "ImageEffect.h"

class IteratePixelEffect: public ImageEffect {
public:
	virtual cv::Mat process(cv::Mat in);
	IteratePixelEffect();
	virtual ~IteratePixelEffect();
	virtual cv::Vec3b processPixel(cv::Vec3b pixel, int row, int col) = 0;
};

#endif /* EFFECT_ITERATEPIXELEFFECT_H_ */
