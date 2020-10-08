package com.hello.hellovideo.entity;

import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Hello_Earther(ShenYongJian)
 * @date 2020/10/6  18:27
 */
@Data
@ToString
public class BaseEntity implements Serializable {

    @Id
    private Long id;
    private Date createTime;
    private Date updateTime;

    public BaseEntity() {
        //createTime = LocalDateTime.now();
        createTime = new Date();

    }

    private int del;
}
