package com.hello.hellovideo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

/**
 * @author Hello_Earther(ShenYongJian)
 * @date 2020/10/6  18:26
 */
@ToString
@Data
@TableName("t_video_online")
public class VideoOnline extends BaseEntity {

    private long videoId;//videoId主键
    private String vname;
    private String onlineWatchUrl;//在线看地址
    private String onlineType;//地址类型
    private String linkeUrl;//连接地址
    @Override
    public boolean equals(Object obj) {
        if(this == obj){
            return true;//地址相等
        }

        if(obj == null){
            return false;//非空性：对于任意非空引用x，x.equals(null)应该返回false。
        }

        if(obj instanceof VideoOnline){
            VideoOnline other = (VideoOnline) obj;
            //需要比较的字段相等，则这两个对象相等
            if(equalsStr(this.vname, other.vname)){
                return true;
            }
        }

        return false;
    }

    private boolean equalsStr(String str1, String str2){
        if(StringUtils.isEmpty(str1) && StringUtils.isEmpty(str2)){
            return true;
        }
        if(!StringUtils.isEmpty(str1) && str1.equals(str2)){
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + (vname == null ? 0 : vname.hashCode());
        return result;
    }

}
