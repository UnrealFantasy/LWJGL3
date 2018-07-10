package engine;

import org.joml.*;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class OBJ {
    public static Mesh load(String file) throws Exception {
        List<String> lines = Files.readAllLines(Paths.get(file));

        List<Vector3f> vertices = new ArrayList<>();
        List<Vector2f> textures = new ArrayList<>();
        List<Vector3f> normals = new ArrayList<>();

        List<Face> faces = new ArrayList<>();

        for (String line : lines) {
            System.out.println(line);

            String[] tokens = line.split("\\s+");

            switch (tokens[0]) {
                case "v":
                    Vector3f vertex = new Vector3f(Float.parseFloat(tokens[1]), Float.parseFloat(tokens[2]), Float.parseFloat(tokens[3]));

                    vertices.add(vertex);

                    System.out.println("Vertex: " + vertex.toString());

                    break;
                case "vt":
                    Vector2f texture = new Vector2f(Float.parseFloat(tokens[1]), Float.parseFloat(tokens[2]));

                    textures.add(texture);

                    System.out.println("Texture: " + texture.toString());

                    break;
                case "vn":
                    Vector3f normal = new Vector3f(Float.parseFloat(tokens[1]), Float.parseFloat(tokens[2]), Float.parseFloat(tokens[3]));

                    normals.add(normal);

                    System.out.println("Normal: " + normal.toString());

                    break;
                case "f":
                    Face face = new Face(tokens[1], tokens[2], tokens[3]);

                    faces.add(face);

                    break;
                default:
                    break;
            }
        }

        return load(vertices, textures, normals, faces);
    }

    protected static class Group {
        static final int NULL = -1;

        int position, texture, normal;

        Group() {
            this.position = NULL;
            this.texture = NULL;
            this.normal = NULL;
        }
    }

    protected static class Face {
        Group[] groups = new Group[3];

        Face(String vf, String vs, String vl) {
            groups[0] = parse(vf);
            groups[1] = parse(vs);
            groups[2] = parse(vl);
        }

        private Group parse(String line) {
            Group group = new Group();

            String[] tokens = line.split("/");

            int length = tokens.length;

            group.position = Integer.parseInt(tokens[0]) - 1;

            if (length > 1) {
                String texture = tokens[1];

                group.texture = texture.length() > 0 ? Integer.parseInt(texture) - 1 : Group.NULL;

                if (length > 2) {
                    group.normal = Integer.parseInt(tokens[2]) - 1;
                }
            }

            return group;
        }

        Group[] getGroups() {
            return groups;
        }
    }

    private static Mesh load(List<Vector3f> vertices, List<Vector2f> textures, List<Vector3f> normals, List<Face> faces) {
        List<Integer> indices = new ArrayList<>();

        float[] vertices_array = new float[vertices.size() * 3];

        int index = 0;

        for(Vector3f vertex : vertices) {
            vertices_array[index * 3] = vertex.x;
            vertices_array[index * 3 + 1] = vertex.y;
            vertices_array[index * 3 + 2] = vertex.z;

            index++;
        }

        float[] textures_array = new float[vertices.size() * 2];
        float[] normals_array = new float[vertices.size() * 3];

        for(Face face : faces) {
            Group[] groups = face.getGroups();

            for(Group group : groups) {
                face(group, textures, normals, indices, textures_array, normals_array);
            }
        }

        return new Mesh(vertices_array, textures_array, normals_array, indices.stream().mapToInt((Integer v) -> v).toArray());
    }

    private static void face(Group group, List<Vector2f> textures, List<Vector3f> normals, List<Integer> indices, float[] textures_array, float[] normals_array) {
        int position_index = group.position;

        indices.add(position_index);

        if(group.texture >= 0) {
            Vector2f texture = textures.get(group.texture);

            textures_array[position_index * 2] = texture.x;
            textures_array[position_index * 2 + 1] = texture.y;
        }

        if(group.normal >= 0) {
            Vector3f normal = normals.get(group.normal);

            normals_array[position_index * 3] = normal.x;
            normals_array[position_index * 3 + 1] = normal.y;
            normals_array[position_index * 3 + 2] = normal.z;
        }
    }
}
