package com.th.convert.bean;


import java.util.ArrayList;
import java.util.List;

public class Table {
    private int rowNum;//行数
    private int colNum;//列数
    private int border;//边框宽度
    private String backgroundColor;//背景色
    private List<RowData> rowList = new ArrayList<RowData>();//行数据

    public int getRowNum() {
        return rowNum;
    }

    public void setRowNum(int rowNum) {
        this.rowNum = rowNum;
    }

    public int getColNum() {
        return colNum;
    }

    public void setColNum(int colNum) {
        this.colNum = colNum;
    }

    public int getBorder() {
        return border;
    }

    public void setBorder(int border) {
        this.border = border;
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public List<RowData> getRowList() {
        return rowList;
    }

    public void setRowList(List<RowData> rowList) {
        this.rowList = rowList;
    }

    public void addRow(RowData rowData) {
        this.rowList.add(rowData);
    }
}
