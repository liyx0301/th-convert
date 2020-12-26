package com.th.convert.bean;

import java.util.ArrayList;
import java.util.List;

public class RowData {
    private int width;//宽度
    private int heigth;//高度
    private List<ColData> colDataList = new ArrayList<ColData>();//列数据

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeigth() {
        return heigth;
    }

    public void setHeigth(int heigth) {
        this.heigth = heigth;
    }

    public List<ColData> getColDataList() {
        return colDataList;
    }

    public void setColDataList(List<ColData> colDataList) {
        this.colDataList = colDataList;
    }

    public void addColData(ColData colData) {
        this.colDataList.add(colData);
    }
}
