package com.example.touch;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.GLU;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class MyOpenGLView extends GLSurfaceView implements Renderer {

    public MyOpenGLView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setRenderer(this);
    }

    public MyOpenGLView(Context context) {
        this(context, null);
    }



    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int actionMasked = event.getActionMasked();
        switch(actionMasked){
            case MotionEvent.ACTION_DOWN:
                savedClickLocationX = (int) event.getX();
                break;
            case MotionEvent.ACTION_UP:
                savedClickLocationX = null;
                break;
            case MotionEvent.ACTION_MOVE:
                Integer newClickLocationX =  (int) event.getX();
                int dx = newClickLocationX - savedClickLocationX;
                angle += dx / 180.0f * 3.14159265f;
                break;
        }
        return true;
    }



    @Override
    public void onDrawFrame(GL10 gl) {
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
        gl.glLoadIdentity();
        GLU.gluLookAt(gl,
                0f, 0f, 5f,
                0f, 0f, 0f,
                0f, 1f, 0f
        );

        gl.glRotatef(angle, 0f, 1f, 0f);
        gl.glColor4f(1f, 0f, 0f, 0f);
        gl.glVertexPointer(2, GL10.GL_FLOAT, 0, triangleCoordsBuff);
        gl.glDrawArrays(GL10.GL_TRIANGLES, 0, 3);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        gl.glViewport(0, 0, width, height);
        gl.glMatrixMode(GL10.GL_PROJECTION);
        gl.glLoadIdentity();
        GLU.gluPerspective(gl, 60f, (float) width / height, 0.1f, 10f);
        gl.glMatrixMode(GL10.GL_MODELVIEW);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        gl.glEnable(GL10.GL_DEPTH_TEST);
        gl.glClearDepthf(1.0f);
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);

        buildTriangleCoordsBuffer();
    }

    private void buildTriangleCoordsBuffer() {
        ByteBuffer buffer = ByteBuffer.allocateDirect(4*triangleCoords.length);
        buffer.order(ByteOrder.nativeOrder());
        triangleCoordsBuff = buffer.asFloatBuffer();
        triangleCoordsBuff.put(triangleCoords);
        triangleCoordsBuff.rewind();
    }

    private float [] triangleCoords = {-1f, -1f,
            +1f, -1f,
            +1f, +1f};

    private FloatBuffer triangleCoordsBuff;
    private float angle = 0f;

    private Integer savedClickLocationX;

}