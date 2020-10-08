package com.hello.hellovideo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;

/**
 * @author Hello_Earther(ShenYongJian)
 * @date 2020/10/6  18:25
 */
@ToString
@Data
@TableName("t_video_down_load")
public class VideoDownLoad extends BaseEntity{

    private long videoId;//videoId主键
    private String vname;
    private String onlineType;//地址类型
    private String downloadUrl;//下载全地址
}
