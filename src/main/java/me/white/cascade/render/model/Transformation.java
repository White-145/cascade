package me.white.cascade.render.model;

import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class Transformation {
    private boolean hasChanged = true;
    private Matrix4f matrix = new Matrix4f();
    private Vector3f position = new Vector3f();
    private Quaternionf orientation = new Quaternionf();
    private float scale = 1;

    public Matrix4f getMatrix() {
        if (hasChanged) {
            matrix.translationRotateScale(position, orientation, scale);
            hasChanged = false;
        }
        return matrix;
    }

    public Vector3f getPosition() {
        return position;
    }

    public Quaternionf getOrientation() {
        return orientation;
    }

    public float getScale() {
        return scale;
    }

    public final void setPosition(Vector3f position) {
        hasChanged = true;
        this.position = position;
    }

    public void setOrientation(Quaternionf orientation) {
        hasChanged = true;
        this.orientation = orientation;
    }

    public void setScale(float scale) {
        hasChanged = true;
        this.scale = scale;
    }

    public void changed() {
        hasChanged = true;
    }
}
