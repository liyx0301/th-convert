package com.th.convert.tl;

import com.deepoove.poi.data.PictureRenderData;

public class DjTableListData {
    private String qxlx;
    private String qxms;
    private String qxpj;
    private String zgyj;
    private PictureRenderData zp;
    private String fjsc;
    private String fjsc_showname;

    public String getQxlx() {
        return qxlx;
    }

    public void setQxlx(String qxlx) {
        this.qxlx = qxlx;
    }

    public String getQxpj() {
        return qxpj;
    }

    public void setQxpj(String qxpj) {
        this.qxpj = qxpj;
    }

    public String getQxms() {
        return qxms;
    }

    public void setQxms(String qxms) {
        this.qxms = qxms;
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
