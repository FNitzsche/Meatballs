kernel void pix(global const float sizeX, global float *answer, global const float3 *balls, global const uint number, global const float *sp, global const float *sw){
    unsigned int id = get_global_id(0);
    unsigned int idx = id % sizeX;
    unsigned int idy = id / sizeX;
    float dist = 0;

    float px = (sw[0]*idx/sp[0])-(sw[0]/2)
    float py = (sw[1]*idy/sp[1])-(sw[1]/2)

    for (int i = 0; i < number; i++){
        float lx = balls[i].x;
        float ly = balls[i].y;
        dist += (1/(lx - px, 2) + (ly - py, 2)))*balls[i].z;
    }
    answer[id]
}