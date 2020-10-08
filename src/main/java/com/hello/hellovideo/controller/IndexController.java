package com.hello.hellovideo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Hello_Earther(ShenYongJian)
 * @date 2020/10/6  11:33
 */
@Controller
@RequestMapping("/index")
public class IndexController {


    @RequestMapping("/index")
    public String index(){
        return "/dff";
    }


}
