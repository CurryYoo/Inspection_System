package com.example.emma.qrcode_iteration2;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by xie on 2018/4/14.
 */

public class FindRoad {
    private final int MAX = 1000000;
    private final int MAXN = 100;
    private float[][] arcs = new float[MAXN][MAXN];

    private float[] D = new float[MAXN];
    private int[] visit = new int[MAXN];
    private int[] Prev = new int[MAXN]; //前驱顶点
    private int n;
    private int v;
    private int w;

    public FindRoad(){}

    public FindRoad(float[][] arcs, int nums){
        this.n = nums;
        System.arraycopy(arcs, 0, this.arcs, 0, nums);
    }

    public void dijkstra(int v0){
        for(v = 0; v < n; v++){ //初始化
            visit[v] = 0;
            Prev[v] = -1;
            D[v] = arcs[v0][v];
        }
        D[v0] = 0;
        visit[v0] = 0;

        for(int i = 1; i < n; i++){ //开始主循环，每次求得v0到某个点的最短路径，并将该点设为已完成
            float min = MAX;
            for(w = 0; w < n; w++){ //找到距离v0点最近的点v
                if(visit[w] == 0){
                    if(D[w] < min){
                        v = w;
                        min = D[w];
                    }
                }
            }
            visit[v] = 1;
            for(w = 0; w < n; w++){ //以v为中间点计算到达每个点的路径，如果比D[w]小，则更新D[w]
                if((visit[w] == 0) && (min + arcs[v][w] < D[w])){
                    D[w] = min + arcs[v][w];
                    Prev[w] = v; //v为w的前驱
                }
            } 
        }

    }

    public Object[] get_path(int t){
        ArrayList<Integer> arrayList = new ArrayList<Integer>();
        for(; t != -1; t = Prev[t]){ //从终点往前遍历，得到唯一一条正确路径
            arrayList.add(t);
        }
        Collections.reverse(arrayList); //翻转
        return arrayList.toArray();
    }

    public Object[] getPath(int start, int destiny){
        dijkstra(start);
        return get_path(destiny);
    }

}
