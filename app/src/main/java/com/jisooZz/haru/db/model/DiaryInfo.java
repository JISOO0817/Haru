package com.jisooZz.haru.db.model;

public class DiaryInfo {

    private int no; // colunm number
    private String ddate; // 시간
    private String dtitle; // 제목
    private String dcontent; // 내용
    private String dimg; // 이미지
    private String id; // 사용자 아이디

    public String getDdate() {
        return ddate;
    }

    public void setDdate(String ddate) {
        this.ddate = ddate;
    }

    public String getDcontent() {
        return dcontent;
    }

    public void setDcontent(String dcontent) {
        this.dcontent = dcontent;
    }

    public String getDtitle() {
        return dtitle;
    }

    public void setDtitle(String dtitle) {
        this.dtitle = dtitle;
    }

    public String getDimg() {
        return dimg;
    }

    public void setDimg(String dimg) {
        this.dimg = dimg;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }
}
