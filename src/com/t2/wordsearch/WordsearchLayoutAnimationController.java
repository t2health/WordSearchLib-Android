/*
 * 
 * WordSearchLib
 * 
 * Copyright © 2009-2012 United States Government as represented by 
 * the Chief Information Officer of the National Center for Telehealth 
 * and Technology. All Rights Reserved.
 * 
 * Copyright © 2009-2012 Contributors. All Rights Reserved. 
 * 
 * THIS OPEN SOURCE AGREEMENT ("AGREEMENT") DEFINES THE RIGHTS OF USE, 
 * REPRODUCTION, DISTRIBUTION, MODIFICATION AND REDISTRIBUTION OF CERTAIN 
 * COMPUTER SOFTWARE ORIGINALLY RELEASED BY THE UNITED STATES GOVERNMENT 
 * AS REPRESENTED BY THE GOVERNMENT AGENCY LISTED BELOW ("GOVERNMENT AGENCY"). 
 * THE UNITED STATES GOVERNMENT, AS REPRESENTED BY GOVERNMENT AGENCY, IS AN 
 * INTENDED THIRD-PARTY BENEFICIARY OF ALL SUBSEQUENT DISTRIBUTIONS OR 
 * REDISTRIBUTIONS OF THE SUBJECT SOFTWARE. ANYONE WHO USES, REPRODUCES, 
 * DISTRIBUTES, MODIFIES OR REDISTRIBUTES THE SUBJECT SOFTWARE, AS DEFINED 
 * HEREIN, OR ANY PART THEREOF, IS, BY THAT ACTION, ACCEPTING IN FULL THE 
 * RESPONSIBILITIES AND OBLIGATIONS CONTAINED IN THIS AGREEMENT.
 * 
 * Government Agency: The National Center for Telehealth and Technology
 * Government Agency Original Software Designation: WordSearchLib001
 * Government Agency Original Software Title: WordSearchLib
 * User Registration Requested. Please send email 
 * with your contact information to: robert.kayl2@us.army.mil
 * Government Agency Point of Contact for Original Software: robert.kayl2@us.army.mil
 * 
 */

package com.t2.wordsearch;

import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.view.animation.LayoutAnimationController;

public class WordsearchLayoutAnimationController extends LayoutAnimationController {

    private static final int[] INDEXES = new int[] {
            2, 2, 3, 3, 3, 3, 2, 2,
            2, 2, 2, 2, 2, 2, 2, 2,
            0, 1, 1, 1, 1, 1, 1, 0,
            0, 0, 1, 0, 0, 1, 0, 0,
            0, 0, 1, 0, 0, 1, 0, 0,
            0, 1, 1, 1, 1, 1, 1, 0,
            2, 2, 2, 2, 2, 2, 2, 2,
            2, 2, 3, 3, 3, 3, 2, 2
    };

    /**
     * @param animation
     * @param delay
     */
    private WordsearchLayoutAnimationController(Animation animation, float delay) {
        super(animation, delay);
    }

    /**
     * @param animation
     */
    public WordsearchLayoutAnimationController(Animation animation) {
        super(animation);
    }

    /**
     * @param context
     * @param attrs
     */
    public WordsearchLayoutAnimationController(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected int getTransformedIndex(android.view.animation.LayoutAnimationController.AnimationParameters params) {
        return INDEXES[params.index];
    }

}
