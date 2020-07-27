# version 120

attribute vec3 verts;

void main() {
    gl_Position = vec4(verts, 1);
}