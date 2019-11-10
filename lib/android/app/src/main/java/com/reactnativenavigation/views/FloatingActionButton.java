package com.reactnativenavigation.views;

import android.view.View;
import android.view.animation.Animation;

public interface FloatingActionButton {
    void show();

    void hide();

    void hide(Animation.AnimationListener adapter);

    View asView();
}
