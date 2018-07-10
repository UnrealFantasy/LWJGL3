package engine;

import org.joml.Matrix4f;
import org.lwjgl.system.MemoryStack;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.lwjgl.opengl.GL20.*;

public class Shader {
    private int programID;

    public Shader(String vertex_source, String fragment_source) {
        programID = glCreateProgram();

        glAttachShader(programID, shader(read(vertex_source), GL_VERTEX_SHADER));
        glAttachShader(programID, shader(read(fragment_source), GL_FRAGMENT_SHADER));

        glLinkProgram(programID);
        glValidateProgram(programID);
    }

    public void matrix(String uniform, Matrix4f matrix) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            FloatBuffer fb = stack.mallocFloat(16);

            matrix.get(fb);

            glUniformMatrix4fv(glGetUniformLocation(programID, uniform), false, fb);
        }
    }

    private String read(String file) {
        byte[] read = new byte[0];

        try {
            read = Files.readAllBytes(Paths.get(file));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new String(read, StandardCharsets.UTF_8);
    }

    public void use(boolean bind) {
        if (bind)
            glUseProgram(programID);
        else
            glUseProgram(0);
    }

    private int shader(String source, int type) {
        int shaderID = glCreateShader(type);

        glShaderSource(shaderID, source);
        glCompileShader(shaderID);

        System.out.println(glGetShaderInfoLog(shaderID));

        return shaderID;
    }
}
