package com.hello.hellovideo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;

/**
 * @author Hello_Earther(ShenYongJian)
 * @date 2020/10/6  18:22
 */

@Data
@ToString
@TableName("t_video")
public class Video extends BaseEntity{

    private String vname;
    private String orinaName;
    private String mainClass;//分类 1 电影 2 电视剧 3
    private String calssName;//国产剧
    private String type;//都市
    private String region;//地区
    private String vyear;//年份
    private String fistName;//字母
    private double score;//评分
    private String imgUrl;//剧照
    private String state;//状态 已完结 / 更新中
    private String allEpisodes;//集数
    private String lastEpisodes;//最新集数
    private int season;//季
    private String actor;//主演
    private String lang;//语言
    private String vdesc;
    private String resolution;//分辨率
    private String time;//时长


}
