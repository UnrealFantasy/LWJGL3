package engine;

import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL11.*;

public class Mesh {
    private int count;

    Mesh(float[] vertex, float[] texture, float[] normal, int[] index) {
        count = index.length;

       for(int v = 0; v < vertex.length; v++) {
           System.out.println(v + ": " + vertex[v]);
       }

        int vaoID = glGenVertexArrays();

        glBindVertexArray(vaoID);

        atr(0, 3, vertex);
        atr(1, 2, texture);
        atr(2, 3, normal);

        elm_atr(index);
    }

    public void rnd() {
        glDrawElements(GL_TRIANGLES, count, GL_UNSIGNED_INT, 0);
    }

    private void atr(int index, int vertex, float[] data) {
        int vboID = glGenBuffers();

        FloatBuffer d_buffer = MemoryUtil.memAllocFloat(data.length);

        d_buffer.put(data).flip();

        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, d_buffer, GL_STATIC_DRAW);

        glVertexAttribPointer(index, vertex, GL_FLOAT, false, 0, 0);
        glEnableVertexAttribArray(index);
    }

    private void elm_atr(int[] data) {
        int vboID = glGenBuffers();

        IntBuffer d_buffer = MemoryUtil.memAllocInt(data.length);

        d_buffer.put(data).flip();

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, d_buffer, GL_STATIC_DRAW);
    }
}