
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
