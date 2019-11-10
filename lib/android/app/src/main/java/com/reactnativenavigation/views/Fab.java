package com.reactnativenavigation.views;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.animation.Animation;

import com.github.clans.fab.FloatingActionButton;
import com.reactnativenavigation.anim.FabAnimator;
import com.reactnativenavigation.anim.FabCollapseBehaviour;
import com.reactnativenavigation.interfaces.ScrollEventListener;
import com.reactnativenavigation.parse.params.Colour;
import com.reactnativenavigation.utils.ImageLoader;
import com.reactnativenavigation.utils.ImageLoadingListenerAdapter;

import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


public class Fab extends FloatingActionButton implements FabAnimator, com.reactnativenavigation.views.FloatingActionButton {
    private String id;
    private FabCollapseBehaviour collapseBehaviour;

    public Fab(Context context, String id) {
        super(context);

        this.id = id;
        collapseBehaviour = new FabCollapseBehaviour(this);
    }

    public String getFabId() {
        return id;
    }

    @Override
    public void show() {
        show(true);
    }

    @Override
    public View asView() {
        return this;
    }

    @Override
    public void hide() {
        hide(true);
    }

    @Override
    public void hide(Animation.AnimationListener adapter) {
        hide(true);
        getAnimation().setAnimationListener(adapter);
    }

    public void enableCollapse(@Nullable ScrollEventListener scrollEventListener) {
        if (scrollEventListener == null) return;
        collapseBehaviour.enableCollapse(scrollEventListener);
    }

    public void disableCollapse() {
        collapseBehaviour.disableCollapse();
    }

    public void applyIcon(String icon, Colour color) {
        new ImageLoader().loadIcons(getContext(), Collections.singletonList(icon), new ImageLoadingListenerAdapter() {
            @Override
            public void onComplete(@NonNull List<Drawable> drawables) {
                if (color.hasValue()) drawables.get(0).setColorFilter(new PorterDuffColorFilter(color.get(), PorterDuff.Mode.SRC_IN));
                setImageDrawable(drawables.get(0));
            }

            @Override
            public void onError(Throwable error) {
                error.printStackTrace();
            }
        });
    }
}
