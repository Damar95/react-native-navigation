package com.reactnativenavigation.presentation;


import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;

import com.reactnativenavigation.parse.FabOptions;
import com.reactnativenavigation.parse.Options;
import com.reactnativenavigation.utils.CoordinatorLayoutUtils;
import com.reactnativenavigation.utils.UiUtils;
import com.reactnativenavigation.viewcontrollers.ChildController;
import com.reactnativenavigation.viewcontrollers.ViewController;
import com.reactnativenavigation.views.Fab;
import com.reactnativenavigation.views.FabMenu;
import com.reactnativenavigation.views.FloatingActionButton;

import androidx.coordinatorlayout.widget.CoordinatorLayout;

import static com.github.clans.fab.FloatingActionButton.SIZE_MINI;
import static com.github.clans.fab.FloatingActionButton.SIZE_NORMAL;
import static com.reactnativenavigation.utils.CollectionUtils.*;

public class FabPresenter {
    FloatingActionButton fab;
    private final int margin;
    private Options defaultOptions;

    public void setDefaultOptions(Options defaultOptions) {
        this.defaultOptions = defaultOptions;
    }

    public FabPresenter(Context context, Options defaultOptions) {
        this.defaultOptions = defaultOptions;
        margin = UiUtils.dpToPx(context, 16);
    }

    public void applyBottomInset(int bottomInset) {
        applyBottomInsets(bottomInset);
    }

    public void applyOptions(ChildController view, FabOptions options) {
        if (!view.getId().equals(options.layoutId)) return;
        FabOptions withDefault = options.copy().mergeWithDefault(defaultOptions.fabOptions);
        if (withDefault.hasValue()) {
            if (fab != null) {
                fab.hide(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        showFab(withDefault, view);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
            } else {
                showFab(withDefault, view);
            }

//            if (fab != null && fab.getFabId().equals(withDefault.id.get())) {
//                fabMenu.bringToFront();
//                applyFabMenuOptions(fabMenu, withDefault, view);
//                fabMenu.setLayoutParams(createLayoutParams(withDefault));
//            } else if (fab != null && fab.getFabId().equals(withDefault.id.get())) {
//                fab.bringToFront();
//                applyFabOptions(fab, withDefault, view);
//                fab.setLayoutParams(createLayoutParams(withDefault));
//                fab.setOnClickListener(v -> view.sendOnNavigationButtonPressed(withDefault.id.get()));
//            } else {
//                FloatingActionButton fab = createFab(withDefault, view);
//                fab.asView().setVisibility(View.INVISIBLE);
//                getFabContainer(view).getView().addView(fab.asView());
//                fab.asView().post(fab::show);
//            }
        } else {
            removeFab(view);
            removeFabMenu(view);
        }
    }

    private void showFab(FabOptions withDefault, ChildController view) {
        FloatingActionButton fab = createFab(withDefault, view);
        fab.asView().setVisibility(View.INVISIBLE);
        getFabContainer(view).getView().addView(fab.asView());
        fab.asView().post(fab::show);
    }

    public void mergeOptions(ChildController view, FabOptions options) {
        if (options.hasValue()) {
//            if (fabMenu != null && fabMenu.getFabId().equals(options.id.get())) {
//                fabMenu.setLayoutParams(createLayoutParams(options));
//                fabMenu.bringToFront();
//                mergeFabMenuOptions(fabMenu, options, view);
//            } else if (fab != null && fab.getFabId().equals(options.id.get())) {
//                fab.setLayoutParams(createLayoutParams(options));
//                fab.bringToFront();
//                mergeFabOptions(fab, options, view);
//                fab.setOnClickListener(v -> view.sendOnNavigationButtonPressed(options.id.get()));
//            } else {
//                getFabContainer(view).getView().addView(createFab(options, view).asView());
//            }
        }
    }

    private FloatingActionButton createFab(FabOptions options, ViewController view) {
//        if (options.actionsArray.size() > 0) {
//            fabMenu = new FabMenu(view.getActivity(), options.id.get());
//            fabMenu.setLayoutParams(createLayoutParams(options));
//            applyFabMenuOptions(fabMenu, options, view);
//            return fabMenu;
//        } else {
            fab = new Fab(view.getActivity(), options.id.get());
            {
                View fab = this.fab.asView();
                fab.setLayoutParams(createLayoutParams(options));
                applyFabOptions((Fab) fab, options, view);
                fab.setOnClickListener(v -> view.sendOnNavigationButtonPressed(options.id.get()));
            }
            return fab;
//        }
    }

    private void removeFabMenu(ChildController view) {
//        if (fabMenu != null) {
//            fabMenu.hideMenuButton(true);
//            getFabContainer(view).getView().removeView(fabMenu);
//            fabMenu = null;
//        }
    }

    private void removeFab(ChildController view) {
        if (fab != null) {
            fab.hide();
            getFabContainer(view).getView().removeView(fab.asView());
            fab = null;
        }
    }

    private CoordinatorLayout.LayoutParams createLayoutParams(FabOptions options) {
        CoordinatorLayout.LayoutParams lp = CoordinatorLayoutUtils.wrapContent(margin, Gravity.BOTTOM);
        FabOptions withDefault = options.copy().mergeWithDefault(defaultOptions.fabOptions);

        switch (withDefault.alignHorizontally.get("end")) {
            case "right":
                lp.gravity = lp.gravity | Gravity.RIGHT;
                break;
            case "left":
                lp.gravity = lp.gravity | Gravity.LEFT;
                break;
            case "center":
                lp.gravity = lp.gravity | Gravity.CENTER_HORIZONTAL;
                break;
            case "start":
                lp.gravity = lp.gravity | Gravity.START;
                break;
            default:
            case "end":
                lp.gravity = lp.gravity | Gravity.END;
                break;
        }
        return lp;
    }

    private void applyFabOptions(Fab fab, FabOptions options, ViewController view) {
        if (options.visible.isTrueOrUndefined()) fab.show(true);
        if (options.visible.isFalse()) fab.hide(true);
        if (options.backgroundColor.hasValue()) fab.setColorNormal(options.backgroundColor.get());
        if (options.clickColor.hasValue()) fab.setColorPressed(options.clickColor.get());
        if (options.rippleColor.hasValue()) fab.setColorRipple(options.rippleColor.get());
        if (options.icon.hasValue()) fab.applyIcon(options.icon.get(), options.iconColor);
        if (options.size.hasValue()) fab.setButtonSize("mini".equals(options.size.get()) ? SIZE_MINI : SIZE_NORMAL);
        if (options.hideOnScroll.isTrue()) fab.enableCollapse(view.getScrollEventListener());
        if (options.hideOnScroll.isFalseOrUndefined()) fab.disableCollapse();
    }

    private void mergeFabOptions(Fab fab, FabOptions options, ViewController view) {
        if (options.visible.isTrue()) fab.show(true);
        if (options.visible.isFalse()) fab.hide(true);
        if (options.backgroundColor.hasValue()) fab.setColorNormal(options.backgroundColor.get());
        if (options.clickColor.hasValue()) fab.setColorPressed(options.clickColor.get());
        if (options.rippleColor.hasValue()) fab.setColorRipple(options.rippleColor.get());
        if (options.icon.hasValue()) fab.applyIcon(options.icon.get(), options.iconColor);
        if (options.size.hasValue()) fab.setButtonSize("mini".equals(options.size.get()) ? SIZE_MINI : SIZE_NORMAL);
        if (options.hideOnScroll.isTrue()) fab.enableCollapse(view.getScrollEventListener());
        if (options.hideOnScroll.isFalse()) fab.disableCollapse();
    }

    private void applyFabMenuOptions(FabMenu fabMenu, FabOptions options, ViewController child) {
        if (options.visible.isTrueOrUndefined()) fabMenu.showMenu(true);
        if (options.visible.isFalse()) fabMenu.hideMenu(true);

        if (options.backgroundColor.hasValue()) fabMenu.setMenuButtonColorNormal(options.backgroundColor.get());
        if (options.clickColor.hasValue()) fabMenu.setMenuButtonColorPressed(options.clickColor.get());
        if (options.rippleColor.hasValue()) fabMenu.setMenuButtonColorRipple(options.rippleColor.get());

        forEach(fabMenu.getActions(), fabMenu::removeMenuButton);
        fabMenu.getActions().clear();
        for (FabOptions fabOption : options.actionsArray) {
            Fab fab = new Fab(child.getActivity(), fabOption.id.get());
            applyFabOptions(fab, fabOption, child);
            fab.setOnClickListener(v -> child.sendOnNavigationButtonPressed(options.id.get()));

            fabMenu.getActions().add(fab);
            fabMenu.addMenuButton(fab);
        }
        if (options.hideOnScroll.isTrue()) fabMenu.enableCollapse(child.getScrollEventListener());
        if (options.hideOnScroll.isFalseOrUndefined()) fabMenu.disableCollapse();
    }

    private void mergeFabMenuOptions(FabMenu fabMenu, FabOptions options, ViewController child) {
        if (options.visible.isTrue()) fabMenu.showMenu(true);
        if (options.visible.isFalse()) fabMenu.hideMenu(true);

        if (options.backgroundColor.hasValue()) fabMenu.setMenuButtonColorNormal(options.backgroundColor.get());
        if (options.clickColor.hasValue()) fabMenu.setMenuButtonColorPressed(options.clickColor.get());
        if (options.rippleColor.hasValue()) fabMenu.setMenuButtonColorRipple(options.rippleColor.get());
        if (options.actionsArray.size() > 0) {
            forEach(fabMenu.getActions(), fabMenu::removeMenuButton);
            fabMenu.getActions().clear();

            for (FabOptions fabOption : options.actionsArray) {
                Fab fab = new Fab(child.getActivity(), fabOption.id.get());
                applyFabOptions(fab, fabOption, child);
                fab.setOnClickListener(v -> child.sendOnNavigationButtonPressed(options.id.get()));

                fabMenu.getActions().add(fab);
                fabMenu.addMenuButton(fab);
            }
        }
        if (options.hideOnScroll.isTrue()) fabMenu.enableCollapse(child.getScrollEventListener());
        if (options.hideOnScroll.isFalse()) fabMenu.disableCollapse();
    }

    private void applyBottomInsets(int bottomInset) {
        if (fab == null) return;
        View view = fab.asView();
        CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) view.getLayoutParams();
        lp.bottomMargin = bottomInset + margin;
        view.requestLayout();
    }

    private ViewController getFabContainer(ChildController view) {
        if (view.isRoot()) return view;
        return getFabContainer(view.getParentController());
    }
}