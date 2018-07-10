package engine;

import org.joml.*;

import java.lang.Math;

import static org.lwjgl.glfw.GLFW.*;

public class Camera {
    private Vector3f position, rotation;

    public Camera(Vector3f position, Vector3f rotation) {
        this.position = position;
        this.rotation = rotation;
    }

    public Matrix4f view() {
        return new Matrix4f().identity().translate(new Vector3f(-position.x, -position.y, -position.z))
                .rotateX((float) Math.toRadians(rotation.x))
                .rotateY((float) Math.toRadians(rotation.y))
                .rotateZ((float) Math.toRadians(rotation.z));
    }

    public void input(long window) {
        if(key(window, GLFW_KEY_W)) {
            position.z -= 0.02f;
        }

        if(key(window, GLFW_KEY_A)) {
            position.x -= 0.02f;
        }

        if(key(window, GLFW_KEY_S)) {
            position.z += 0.02f;
        }

        if(key(window, GLFW_KEY_D)) {
            position.x += 0.02f;
        }
    }

    private boolean key(long window, int key) {
        return glfwGetKey(window, key) == GLFW_PRESS;
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public Vector3f getRotation() {
        return rotation;
    }

    public void setRotation(Vector3f rotation) {
        this.rotation = rotation;
    }
}
