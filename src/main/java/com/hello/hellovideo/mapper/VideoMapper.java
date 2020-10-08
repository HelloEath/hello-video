package com.hello.hellovideo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hello.hellovideo.entity.Video;

/**
 * @author Hello_Earther(ShenYongJian)
 * @date 2020/10/7  14:41
 */
public interface VideoMapper extends BaseMapper<Video> {
    void selectList();
}
