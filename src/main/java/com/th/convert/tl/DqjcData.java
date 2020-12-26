package com.th.convert.tl;

import com.deepoove.poi.data.PictureRenderData;

import java.util.List;

public class DqjcData {
    private String dqjc_dxbh;
    private String dqjc_table_name;
    private String zdm;
    private String dxbh;
    private String azfs;
    private String dxgg;

    private List<DqjcTable> dqjcTables;

    private String jcrq;
    private String jcry;
    private String shry;

    public String getDqjc_dxbh() {
        return dqjc_dxbh;
    }

    public void setDqjc_dxbh(String dqjc_dxbh) {
        this.dqjc_dxbh = dqjc_dxbh;
    }

    public String getDqjc_table_name() {
        return dqjc_table_name;
    }

    public void setDqjc_table_name(String dqjc_table_name) {
        this.dqjc_table_name = dqjc_table_name;
    }

    public String getZdm() {
        return zdm;
    }

    public void setZdm(String zdm) {
        this.zdm = zdm;
    }

    public String getDxbh() {
        return dxbh;
    }

    public void setDxbh(String dxbh) {
        this.dxbh = dxbh;
    }

    public String getAzfs() {
        return azfs;
    }

    public void setAzfs(String azfs) {
        this.azfs = azfs;
    }

    public String getDxgg() {
        return dxgg;
    }

    public void setDxgg(String dxgg) {
        this.dxgg = dxgg;
    }

    public List<DqjcTable> getDqjcTables() {
        return dqjcTables;
    }

    public void setDqjcTables(List<DqjcTable> dqjcTables) {
        this.dqjcTables = dqjcTables;
    }

    public String getJcrq() {
        return jcrq;
    }

    public void setJcrq(String jcrq) {
        this.jcrq = jcrq;
    }

    public String getJcry() {
        return jcry;
    }

    public void setJcry(String jcry) {
        this.jcry = jcry;
    }

    public String getShry() {
        return shry;
    }

    public void setShry(String shry) {
        this.shry = shry;
    }
}

class DqjcTable {
    private String jcxm;
    private String jccs;
    private String jczt;

    public String getJcxm() {
        return jcxm;
    }

    public void setJcxm(String jcxm) {
        this.jcxm = jcxm;
    }

    public String getJccs() {
        return jccs;
    }

    public void setJccs(String jccs) {
        this.jccs = jccs;
    }

    public String getJczt() {
        return jczt;
    }

    public void setJczt(String jczt) {
        this.jczt = jczt;
    }
}
