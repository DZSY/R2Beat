package com.positif.r2beat.Game;

import android.graphics.Canvas;

/**
 * Created by positif on 10/12/2016.
 */
public interface ActElement {
    boolean draw(Canvas canvas, boolean stay);

    int getCount();

    void setCount(int count);

    int getType();
}
