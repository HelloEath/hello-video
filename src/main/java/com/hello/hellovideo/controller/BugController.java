package com.hello.hellovideo.controller;

import com.hello.hellovideo.service.BugService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Hello_Earther(ShenYongJian)
 * @date 2020/10/6  18:56
 */
@Controller
@RequestMapping("/bug")
public class BugController {


    @Autowired
    BugService bugService;
    @RequestMapping("/allVideo")
    public void bug(){
       bugService.startBug();
    }


    @RequestMapping("/lastVideo")
    public void lastVideo(){
        bugService.lastVideoBug();
    }
}
