package com.th.convert.tl;

import com.deepoove.poi.data.PictureRenderData;

public class SafeHiddenTroubles {
    private String qxms;
    private String dxbh;
    private String zgyj;
    private PictureRenderData zp;
    private String fjsc;
    private String fjsc_showname;

    public String getQxms() {
        return qxms;
    }

    public void setQxms(String qxms) {
        this.qxms = qxms;
    }

    public String getDxbh() {
        return dxbh;
    }

    public void setDxbh(String dxbh) {
        this.dxbh = dxbh;
    }

    public String getZgyj() {
        return zgyj;
    }

    public void setZgyj(String zgyj) {
        this.zgyj = zgyj;
    }

    public PictureRenderData getZp() {
        return zp;
    }

    public void setZp(PictureRenderData zp) {
        this.zp = zp;
    }

    public String getFjsc() {
        return fjsc;
    }

    public String getFjsc_showname() {
        return fjsc_showname;
    }

    public void setFjsc_showname(String fjsc_showname) {
        this.fjsc_showname = fjsc_showname;
    }

    public void setFjsc(String fjsc) {
        this.fjsc = "{{@"+fjsc+"}}";
    }
}
