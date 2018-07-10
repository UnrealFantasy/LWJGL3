package engine;

import de.matthiasmann.twl.utils.PNGDecoder;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;

public class Texture {
    private int textureID;

    public Texture(String file) throws IOException {
        PNGDecoder decoder = new PNGDecoder(new FileInputStream(new File(file)));

        ByteBuffer buffer = ByteBuffer.allocateDirect(4 * decoder.getWidth() * decoder.getHeight());

        decoder.decode(buffer, decoder.getWidth() * 4, PNGDecoder.Format.RGBA);

        buffer.flip();

        textureID = glGenTextures();

        glBindTexture(GL_TEXTURE_2D, textureID);

        glPixelStorei(GL_UNPACK_ALIGNMENT, 1);

        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, decoder.getWidth(), decoder.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

        glGenerateMipmap(GL_TEXTURE_2D);
    }

    public void use(boolean bind) {
        if(bind)
            glBindTexture(GL_TEXTURE_2D, textureID);
        else
            glBindTexture(GL_TEXTURE_2D, 0);
    }
}
