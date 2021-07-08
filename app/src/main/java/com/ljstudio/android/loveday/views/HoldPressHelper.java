package com.ljstudio.android.loveday.views;

import android.view.MotionEvent;
import android.view.View;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class HoldPressHelper {

    public static void addHoldPressListener(View v, OnHoldPressListener onHoldPressListener) {
        addHoldPressListener(v, onHoldPressListener, 100);
    }

    public static void addHoldPressListener(View v, final OnHoldPressListener onHoldPressListener, final long time) {
        v.setOnTouchListener(new View.OnTouchListener() {
            private Disposable mDisposable;

            @Override
            public boolean onTouch(final View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        Observable.interval(0, time, TimeUnit.MILLISECONDS)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Observer<Long>() {
                                    @Override
                                    public void onSubscribe(Disposable d) {
                                        mDisposable = d;

                                    }

                                    @Override
                                    public void onNext(Long o) {
                                        onHoldPressListener.onHold(v);
                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        mDisposable.dispose();
                                    }

                                    @Override
                                    public void onComplete() {

                                    }
                                });

                        onHoldPressListener.onTouchDown(v);
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        if (mDisposable != null) {
                            mDisposable.dispose();
                        }
                        onHoldPressListener.onTouchUp(v);
                        break;
                }
                return true;
            }
        });
    }

    public interface OnHoldPressListener {
        void onTouchDown(View v);

        void onHold(View v);

        void onTouchUp(View v);
    }
}
