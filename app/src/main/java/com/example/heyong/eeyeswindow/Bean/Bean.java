package com.example.heyong.eeyeswindow.Bean;

public class Bean{

    /**
     * id : 496
     * taici : 无限接近于零，却又不等于零的可能性。
     * cat : d
     * catcn : 小说
     * show : null
     * source : NO GAME NO LIFE
     */

    private String id;
    private String taici;
    private String cat;
    private String catcn;
    private Object show;
    private String source;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTaici() {
        return taici;
    }

    public void setTaici(String taici) {
        this.taici = taici;
    }

    public String getCat() {
        return cat;
    }

    public void setCat(String cat) {
        this.cat = cat;
    }

    public String getCatcn() {
        return catcn;
    }

    public void setCatcn(String catcn) {
        this.catcn = catcn;
    }

    public Object getShow() {
        return show;
    }

    public void setShow(Object show) {
        this.show = show;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}