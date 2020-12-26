package com.th.convert.tl;

import com.deepoove.poi.data.PictureRenderData;

import java.util.List;

public class DjTableData {
    private String dqjc_dxbh;
    private List<DjTableListData> djmsTables;

    public String getDqjc_dxbh() {
        return dqjc_dxbh;
    }

    public void setDqjc_dxbh(String dqjc_dxbh) {
        this.dqjc_dxbh = dqjc_dxbh;
    }

    public List<DjTableListData> getDjmsTables() {
        return djmsTables;
    }

    public void setDjmsTables(List<DjTableListData> djmsTables) {
        this.djmsTables = djmsTables;
    }
}

