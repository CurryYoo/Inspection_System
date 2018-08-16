package com.example.emma.qrcode_iteration2;

import com.alibaba.fastjson.JSON;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 一个楼层的信息
 */
public class GraphInfor {
    private String biuldName;//建筑名字
    private int floor;
    private List<Location> points;//楼层所有的二维码
    private String matrix;///楼层所有的二维码的连通情况//链接矩阵
   // private QRcode CurPoint;

    public List<Location> getPoints() {
        return points;
    }

    public void setPoints(List<Location> points) {
        this.points = points;
    }

    public float[][] getMatrix() {
        float[][] arr=new float[points.size()][points.size()];
        List<ArrayList> arr1= JSON.parseArray(matrix,ArrayList.class);
        for(int i=0;i<arr.length;i++){
            for(int j=0;j<arr[i].length;j++){
                arr[i][j]= Float.parseFloat(arr1.get(i).get(j).toString());
            }
        }

        return arr;
    }

    public void setMatrix(String matrix) {
        this.matrix = matrix;
    }

    public String getBiuldName() {
        return biuldName;
    }

    public void setBiuldName(String biuldName) {
        this.biuldName = biuldName;
    }

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public GraphInfor(String biuldName, int floor, List<Location> points, String matrix) {
        this.biuldName = biuldName;
        this.floor = floor;
        this.points = points;
        this.matrix = matrix;
    }

    public GraphInfor(){}

    @Override
    public String toString() {
        return "GraphInfor{" +
                "biuldName='" + biuldName + '\'' +
                ", floor=" + floor +
                ", points=" + points +
                ", matrix=" + matrix +
                '}';
    }
}
