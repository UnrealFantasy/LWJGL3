package lwjgl3;

import engine.*;

import org.joml.*;

import java.io.IOException;
import java.lang.Math;

import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.glEnable;

public class Main {
    public static void main(String[] args) throws Exception {
        Window window = new Window(1270, 700, "GLFW");

        window.create(false);

        Mesh mesh = OBJ.load("./models/cube.obj");
        Shader shader = new Shader("./shaders/vertex.txt", "./shaders/fragment.txt");

        Vector3f rotation = new Vector3f(0, 0, 0);

        Camera camera = new Camera(new Vector3f(0, 0, 0), new Vector3f(0, 0, 0));

        while (!glfwWindowShouldClose(window.getHandle())) {
            window.clear();

            Matrix4f transform = new Matrix4f().translate(new Vector3f(0, -1, -4))
                    .rotateX((float) Math.toRadians(rotation.x))
                    .rotateY((float) Math.toRadians(rotation.y))
                    .rotateZ((float) Math.toRadians(rotation.z))
                    .scale(1);

            shader.use(true);

            shader.matrix("transform", transform);
            shader.matrix("view", camera.view());
            shader.matrix("project", new Matrix4f().perspective(70, 1270f / 700f, 0.01f, 128f));

            glEnable(GL_DEPTH_TEST);

            mesh.rnd();

            shader.use(false);

            camera.input(window.getHandle());

            window.update();

            rotation.add(new Vector3f(0.2f, 0.2f, 0.2f));
        }

        window.close();
    }
}
