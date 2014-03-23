package com.worldclass.utils;

import android.opengl.GLSurfaceView;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by erz on 3/23/2014.
 */
public class MyGLRenderer implements GLSurfaceView.Renderer {
    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        JNIOnSurfaceCreated();
    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int i, int i2) {
        JNIOnSurfaceChanged(i, i2);
    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        JNIOnDrawFrame();
    }

    private static native void JNIOnSurfaceCreated();
    private static native void JNIOnSurfaceChanged(int w, int h);
    private static native void JNIOnDrawFrame();
}
