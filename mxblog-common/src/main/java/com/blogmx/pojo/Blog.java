package com.blogmx.pojo;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Table(name = "article")
public class Blog implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titleName;

    private String subTitle;

    private Boolean isTop;

    private Boolean isHot;

    @Column(name = "`index`")
    private String index;

    private Date createTime;

    private Long watchNum;

    private String image;

    private String file;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitleName() {
        return titleName;
    }

    public void setTitleName(String titleName) {
        this.titleName = titleName;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public Boolean getIsTop() {
        return isTop;
    }

    public void setIsTop(Boolean top) {
        isTop = top;
    }

    public Boolean getIsHot() {
        return isHot;
    }

    public void setIsHot(Boolean hot) {
        isHot = hot;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Long getWatchNum() {
        return watchNum;
    }

    public void setWatchNum(Long watchNum) {
        this.watchNum = watchNum;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }
}
