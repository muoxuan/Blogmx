package com.blogmx.pojo;


import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import javax.persistence.Id;
import java.util.Date;
import java.util.Objects;

@Document(indexName = "blog",type = "docs", shards = 1, replicas = 0)
public class SearchBlog {

    @Id
    private Long id;

    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String titleName;

    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String subTitle;

    @Field(type = FieldType.Keyword)
    private Boolean isTop;

    @Field(type = FieldType.Keyword)
    private Boolean isHot;


    @Field(type = FieldType.Keyword)
    private Long watchNum;

    @Field(type = FieldType.Keyword)
    private String image;


    @Field(type = FieldType.Keyword)
    private String url;

    @Field(type = FieldType.Keyword)
    private String day;

    @Field(type = FieldType.Keyword)
    private String month;

    @Field(type = FieldType.Keyword)
    private String year;

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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SearchBlog that = (SearchBlog) o;
        return Objects.equals(id, that.id);
    }
}
