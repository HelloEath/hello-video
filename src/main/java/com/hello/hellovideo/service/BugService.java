package com.hello.hellovideo.service;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hello.hellovideo.entity.Video;
import com.hello.hellovideo.entity.VideoDownLoad;
import com.hello.hellovideo.entity.VideoOnline;
import com.hello.hellovideo.mapper.VideoMapper;
import com.hello.hellovideo.mapper.VideoDownLoadMapper;
import com.hello.hellovideo.mapper.VideoOnlineMapper;
import com.hello.hellovideo.util.SnowFlake;
import lombok.Data;
import lombok.Synchronized;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.SimpleFormatter;

/**
 * @author Hello_Earther(ShenYongJian)
 * @date 2020/10/6  18:57
 */
@Service
public class BugService {
    SnowFlake snowFlake = new SnowFlake(2, 1);

    @Autowired
    VideoMapper videoMapper;
    @Autowired
    VideoDownLoadMapper videoDownLoadMapper;
    @Autowired
    VideoOnlineMapper videoOnlineMapper;
    Set<String> videlSet = new HashSet<>();
    private Logger logger = LoggerFactory.getLogger(getClass());
    String indexUrl = "http://m.yyets8.com";
    int x = 0;
    Random r = new Random();

    RequestParams requestParams = new RequestParams(null, "*/*", "Keep-Alive", "", indexUrl);
    String[] ua = {"Mozilla/5.0 (Windows NT 6.1; WOW64; rv:46.0) Gecko/20100101 Firefox/46.0",
            "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/50.0.2661.87 Safari/537.36 OPR/37.0.2178.32",
            "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/534.57.2 (KHTML, like Gecko) Version/5.1.7 Safari/534.57.2",
            "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/45.0.2454.101 Safari/537.36",
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2486.0 Safari/537.36 Edge/13.10586",
            "Mozilla/5.0 (Windows NT 10.0; WOW64; Trident/7.0; rv:11.0) like Gecko",
            "Mozilla/5.0 (compatible; MSIE 10.0; Windows NT 6.1; WOW64; Trident/6.0)",
            "Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; WOW64; Trident/5.0)",
            "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.1; WOW64; Trident/4.0)",
            "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2526.106 BIDUBrowser/8.3 Safari/537.36",
            "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2526.80 Safari/537.36 Core/1.47.277.400 QQBrowser/9.4.7658.400",
            "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/48.0.2564.116 UBrowser/5.6.12150.8 Safari/537.36",
            "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/38.0.2125.122 Safari/537.36 SE 2.X MetaSr 1.0",
            "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/48.0.2564.116 Safari/537.36 TheWorld 7",
            "Mozilla/5.0 (Windows NT 6.1; W…) Gecko/20100101 Firefox/60.0"};

    @Scheduled(cron = "0 00 23 ? * *")
    public void scheduledTask1(){
        lastVideoBug();
    }

    @Scheduled(cron = "0 00 12 ? * *")
    public void scheduledTask2(){
        lastVideoBug();
    }

    @Scheduled(cron = "0 00 18 ? * *")
    public void scheduledTask3(){
        lastVideoBug();
    }
    public void lastVideoBug() {
        logger.info("爬取最新列表任务开始");
        long startTime=System.currentTimeMillis();
        String url = "http://m.yyets8.com/new.html";
        int i = r.nextInt(14);
        try {
            Document doc = Jsoup.connect(url)
                    .userAgent(ua[i])
                    .header("accept", "*/*")
                    .header("connection", "Keep-Alive")
                    //.referrer(indexUrl)
                    .timeout(10000)
                    .get();
            Elements elements = doc.select("ul.txt-list-new li");
            elements.forEach(x -> {
                String vname = x.select("a").attr("title");
                String href = indexUrl + x.select("a").attr("href");
                String updateDateString = x.select("span ").text();
                updateDateString = updateDateString.substring(0, 10);
                Date currentDate = new Date();
                SimpleDateFormat simpleFormatter = new SimpleDateFormat("yyyy-MM-dd");
                String curentDateString = simpleFormatter.format(currentDate);
                if (curentDateString.equals(updateDateString)) {
                    Wrapper<Video> wrapper = new QueryWrapper<>();
                    ((QueryWrapper<Video>) wrapper).like("vname", vname);

                    Video video = videoMapper.selectOne(wrapper);
                    if (video != null) {
                        Wrapper<VideoOnline> videoOnlineWrapper = new QueryWrapper<>();
                        ((QueryWrapper<VideoOnline>) videoOnlineWrapper).eq("video_Id", video.getId());
                        List<VideoOnline> videoOnlineList = videoOnlineMapper.selectList(videoOnlineWrapper);
                        bugDetail(video, href, videoMapper, videoOnlineMapper, videoDownLoadMapper, videoOnlineList);
                    }
                }
            });

        } catch (IOException e) {
            if (x > 3) {
                x = 0;
                return;
            } else {
                synchronized (this) {
                    x++;
                }
            }
            logger.error("获取最新列表失败，地址=" + url + "重试" + x + "次");
            lastVideoBug();
        }
        long endTime=System.currentTimeMillis();
        logger.info("爬取最新列表任务结束");
        logger.info("耗时:"+(endTime-startTime)/1000/60+"分钟");

    }

    public void startBug() {
        logger.info("开始爬取页面信息");
        long starTime = System.currentTimeMillis();
        Map<String, String> bugList = new HashMap<>();
        bugList.put("1", indexUrl + "/list/dianshiju_____.html");
        bugList.put("2", indexUrl + "/list/dianying_____.html");
        bugList.put("3", indexUrl + "/list/dongman_____.html");
        bugList.put("4", indexUrl + "/list/zongyi_____.html");
        ExecutorService newCachedThreadPool = Executors.newFixedThreadPool(4);
        for (String key : bugList.keySet()) {
            newCachedThreadPool.execute(new BugService().new Task(key, bugList.get(key), videoMapper, videoDownLoadMapper, videoOnlineMapper));
        }
        long endTime = System.currentTimeMillis();
        logger.info("爬取页面信息结束");
        logger.info("总耗时:" + (endTime - starTime) / 1000 / 60 + "分钟");

    }

    private void bugList(String key, String url, VideoMapper videoMapper, VideoDownLoadMapper videoDownLoadMapper, VideoOnlineMapper videoOnlineMapper) {
        // String responseString=requestNet(url);
        try {
            int i = r.nextInt(14);
            Document doc = Jsoup.connect(url)
                    .userAgent(ua[i])
                    .header("accept", "*/*")
                    .header("connection", "Keep-Alive")
                    //.referrer(indexUrl)
                    .timeout(10000)
                    .get();

            Elements elements = doc.select("ul#content li");
            elements.forEach(x -> {
                String detailUrl = indexUrl + x.select("a[href]").attr("href");
                String name = x.select("div.name").text();
                if (videlSet.contains(name)) {
                    logger.info(name + "  重复爬取");
                    return;
                } else {
                    videlSet.add(name);
                }
                String imgUrl = x.select("img.lazyload").attr("data-original");

                String scoreString = x.select("span.score").text();
                double score = scoreString == null ? 0 : Double.parseDouble(scoreString);
                String lastEpisodes = x.select("span.vtitle ").text();
                Video video = new Video();
                video.setMainClass(key);
                video.setVname(name);
                video.setImgUrl(imgUrl);
                video.setScore(score);
                video.setLastEpisodes(lastEpisodes);
                bugDetail(video, detailUrl, videoMapper, videoOnlineMapper, videoDownLoadMapper, null);
            });
            //分页
            elements = doc.select("div#page li a");
            elements.forEach(x -> {
                String nextPage = x.text();
                if ("下一页".equals(nextPage)) {
                    String nextPaheUrl = indexUrl + x.attr("href");
                    logger.info("下一页地址" + nextPaheUrl);
                    bugList(key, nextPaheUrl, videoMapper, videoDownLoadMapper, videoOnlineMapper);
                }
            });

        } catch (IOException e) {
            if (x > 3) {
                x = 0;
                return;
            } else {
                synchronized (this) {
                    x++;
                }
            }
            logger.error("爬取列表失败，地址=" + url + "重试" + x + "次");
            bugList(key, url, videoMapper, videoDownLoadMapper, videoOnlineMapper);
        }

    }

    private void bugDetail(Video video, String detailUrl, VideoMapper videoMapper, VideoOnlineMapper videoOnlineMapper, VideoDownLoadMapper videoDownLoadMapper, List<VideoOnline> videoOnlineList) {
        int i = r.nextInt(14);
        Document doc = null;

        try {
            doc = Jsoup.connect(detailUrl)
                    .userAgent(ua[i])
                    .header("accept", "*/*")
                    .header("connection", "Keep-Alive")
                    //.referrer(indexUrl)
                    .timeout(10000)
                    .get();

            Elements elements = doc.select("div.vod-detail-info li");
            String AllEpisodes = elements.eq(1).text().replaceAll("状态：", "");
            String orinaName = elements.eq(2).text().replaceAll("别名：", "");
            String type = elements.eq(3).text().replaceAll("类型：", "");
            String actor = elements.eq(4).text().replaceAll("主演：", "");

            video.setOrinaName(orinaName);
            video.setActor(actor);
            video.setAllEpisodes(AllEpisodes);
            video.setType(type);
            if (video.getId()==null) {
                long videoId = snowFlake.nextId();
                video.setId(videoId);
                videoMapper.insert(video);
            } else {
                videoMapper.updateById(video);
            }
            //播放集数列表
            elements = doc.select("ul#con_playlist_1 a");//在线看地址列表
            for (Element element : elements) {

                VideoOnline videoOnline = new VideoOnline();
                String onlinePlayUrl = indexUrl + element.attr("href");
                String seasonName = element.text();
                videoOnline.setVideoId(video.getId());
                long videoSeasonId = snowFlake.nextId();
                videoOnline.setId(videoSeasonId);
                videoOnline.setVname(seasonName);
                if (videoOnlineList != null) {
                    if (!videoOnlineList.contains(videoOnline)) {
                        String script = getOnlineUrl(onlinePlayUrl);
                        onlinePlayUrl = "//www.meijupa.com/m3u8.php?url=" + script;
                        //logger.info(seasonName+"在线看全地址="+onlinePlayUrl);

                        videoOnline.setOnlineWatchUrl(onlinePlayUrl);
                        videoOnline.setLinkeUrl(script);
                        videoOnlineMapper.insert(videoOnline);
                    }


                } else {
                    String script = getOnlineUrl(onlinePlayUrl);
                    onlinePlayUrl = "//www.meijupa.com/m3u8.php?url=" + script;
                    //logger.info(seasonName+"在线看全地址="+onlinePlayUrl);
                    videoOnline.setOnlineWatchUrl(onlinePlayUrl);
                    videoOnline.setLinkeUrl(script);
                    videoOnlineMapper.insert(videoOnline);

                }


            }

            elements = doc.select("div#downlist_1 input");//下载地址列表
            for (Element element : elements) {
                VideoDownLoad videoDownLoad = new VideoDownLoad();
                String downloadUrl = element.attr("value");
                String seasonName = element.attr("file_name");
                logger.info(seasonName + "下载地址=" + downloadUrl);

                videoDownLoad.setVideoId(video.getId());
                long videoSeasonId = snowFlake.nextId();
                videoDownLoad.setId(videoSeasonId);
                videoDownLoad.setVname(seasonName);
                videoDownLoad.setDownloadUrl(downloadUrl);
                videoDownLoadMapper.insert(videoDownLoad);
            }

        } catch (IOException e) {
            if (x > 3) {
                x = 0;
                return;
            } else {
                synchronized (this) {
                    x++;
                }
            }
            logger.error("爬取" + detailUrl + "失败，重试中,第" + x + "次");
            bugDetail(video, detailUrl, videoMapper, videoOnlineMapper, videoDownLoadMapper, videoOnlineList);
        }

    }

    public String getOnlineUrl(String url) {
        int i = r.nextInt(14);
        Document doc = null;
        String script = null;
        try {
            doc = Jsoup.connect(url)
                    .userAgent(ua[i])
                    .header("accept", "*/*")
                    .header("connection", "Keep-Alive")
                    //.referrer(indexUrl)
                    .timeout(10000)
                    .get();
            script = doc.select("div#cms_play script").html();
            script = StringUtils.substringBetween(script, "url", ",");
            script = script.substring(3, script.length() - 1);
        } catch (IOException e) {
            if (x > 3) {
                x = 0;
                return script;
            } else {
                synchronized (this) {
                    x++;
                }
            }
            logger.error("爬取" + url + "失败，重试中,第" + x + "次");
            getOnlineUrl(url);
        }
        return script;

    }

    public String requestNet(String requestUrl) {
        URL realUrl = null;
        Random r = new Random();
        String[] ua = {"Mozilla/5.0 (Windows NT 6.1; WOW64; rv:46.0) Gecko/20100101 Firefox/46.0",
                "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/50.0.2661.87 Safari/537.36 OPR/37.0.2178.32",
                "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/534.57.2 (KHTML, like Gecko) Version/5.1.7 Safari/534.57.2",
                "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/45.0.2454.101 Safari/537.36",
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2486.0 Safari/537.36 Edge/13.10586",
                "Mozilla/5.0 (Windows NT 10.0; WOW64; Trident/7.0; rv:11.0) like Gecko",
                "Mozilla/5.0 (compatible; MSIE 10.0; Windows NT 6.1; WOW64; Trident/6.0)",
                "Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; WOW64; Trident/5.0)",
                "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.1; WOW64; Trident/4.0)",
                "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2526.106 BIDUBrowser/8.3 Safari/537.36",
                "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2526.80 Safari/537.36 Core/1.47.277.400 QQBrowser/9.4.7658.400",
                "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/48.0.2564.116 UBrowser/5.6.12150.8 Safari/537.36",
                "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/38.0.2125.122 Safari/537.36 SE 2.X MetaSr 1.0",
                "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/48.0.2564.116 Safari/537.36 TheWorld 7",
                "Mozilla/5.0 (Windows NT 6.1; W…) Gecko/20100101 Firefox/60.0"};
        int i = r.nextInt(14);
        requestParams.setRequestUrl(requestUrl);
        requestParams.setUserAgent(ua[i]);
        StringBuilder result = new StringBuilder();
        BufferedReader in = null;
        try {
            realUrl = new URL(requestParams.getRequestUrl());
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            conn.setRequestProperty("accept", requestParams.getAccept());
            conn.setRequestProperty("connection", requestParams.getConnection());
            conn.setRequestProperty("user-agent", requestParams.getUserAgent());
            conn.setRequestProperty("referer", requestParams.getReferer());
            //conn.setConnectTimeout(10*1000);
            // 建立实际的连接
            conn.connect();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result.append(line);
            }

        } catch (IOException e) {
            logger.error("请求失败,地址:" + requestUrl);
        } finally {

            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result.toString();
    }

    class Task implements Runnable {
        private String key;
        private String url;
        private VideoMapper videoMapper;
        private VideoDownLoadMapper videoDownLoadMapper;
        private VideoOnlineMapper videoOnlineMapper;

        public Task(String key, String url, VideoMapper videoMapper, VideoDownLoadMapper videoDownLoadMapper, VideoOnlineMapper videoOnlineMapper) {
            this.key = key;
            this.url = url;
            this.videoMapper = videoMapper;
            this.videoDownLoadMapper = videoDownLoadMapper;
            this.videoOnlineMapper = videoOnlineMapper;
        }

        @Override
        public void run() {
            long starTime = System.currentTimeMillis();
            logger.info("key=" + this.key + ",url=" + this.url);
            bugList(this.key, this.url, videoMapper, videoDownLoadMapper, videoOnlineMapper);
            long endTime = System.currentTimeMillis();
            logger.info("爬取" + this.url + ",耗时:" + (endTime - starTime) / 1000 / 60 + "分钟");
        }
    }


    private class RequestParams {
        private String requestUrl;
        private String accept;
        private String connection;
        private String userAgent;
        private String referer;

        public synchronized String getRequestUrl() {
            return requestUrl;
        }

        public synchronized void setRequestUrl(String requestUrl) {
            this.requestUrl = requestUrl;
        }

        public synchronized String getAccept() {
            return accept;
        }

        public synchronized void setAccept(String accept) {
            this.accept = accept;
        }

        public String getConnection() {
            return connection;
        }

        public void setConnection(String connection) {
            this.connection = connection;
        }

        public String getUserAgent() {
            return userAgent;
        }

        public void setUserAgent(String userAgent) {
            this.userAgent = userAgent;
        }

        public String getReferer() {
            return referer;
        }

        public void setReferer(String referer) {
            this.referer = referer;
        }

        public RequestParams(String requestUrl, String accept, String connection, String userAgent, String referer) {
            this.requestUrl = requestUrl;
            this.accept = accept;
            this.connection = connection;
            this.userAgent = userAgent;
            this.referer = referer;
        }
    }

    public static void main(String[] args) {


    }
}
