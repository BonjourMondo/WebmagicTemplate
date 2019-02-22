package csdn;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.ConsolePipeline;
import us.codecraft.webmagic.pipeline.FilePipeline;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.scheduler.PriorityScheduler;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: LeesangHyuk
 * Date: 2019/2/21 14:21
 * Description:
 */
public class CSDNProcessor implements PageProcessor {

    private Site site;
    private static int count=0;

//    public static final String URL_LIST = "";
    public static final String URL_POST = "https://blog.csdn.net/No_Game_No_Life_/article/details/[0-9]{8}";

    public void process(Page page) {
        if (page.getUrl().regex(URL_POST).match()) {
            page.putField("title",page.getHtml().xpath("//*[@id=\"mainBox\"]/main/div[1]/div/div/div[1]/h1/text()"));
            if (page.getResultItems().get("title") == null) {
                page.setSkip(true);
            }else{
                count++;
            }
            page.putField("url",page.getUrl().toString());
            page.putField("time",page.getHtml().xpath("//*[@id=\"mainBox\"]/main/div[1]/div/div/div[2]/div[1]/span[1]/text()"));

        }else{
            page.addTargetRequests(page.getHtml().links().regex(URL_POST).all(), 1000);
            page.addTargetRequests(getListLink(), 1);
        }
    }

    public Site getSite() {
        if (site==null) {
            site=Site.me().setRetryTimes(3).setSleepTime(500).setTimeOut(10000)
                    .setDomain("blog.csdn.net").addCookie("blog.csdn.net", "__yadk_uid", "Qm7dEc8XUauSwGnL8pAdY60k4lzp8JVv")
                    .addHeader("referer","https://blog.csdn.net/No_Game_No_Life_")
                    .setCharset("utf-8").setUserAgent("Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.25 Safari/537.36 Core/1.70.3633.400 QQBrowser/10.4.3232.400");

        }
        return site;
    }

    public List<String> getListLink(){
        List<String> links=new ArrayList<String>();
        for (int i = 2; i < 13; i++) {
            links.add("https://blog.csdn.net/No_Game_No_Life_/article/list/"+i);
        }
        return links;
    }

    public static void main(String[] args) {
        while (true) {
            long startTime, endTime;
            System.out.println("开始爬取...");
            startTime = System.currentTimeMillis();
            Spider.create(new CSDNProcessor())
                    .setScheduler(new PriorityScheduler())
                    .addPipeline(new ConsolePipeline())
                    .addUrl("https://blog.csdn.net/No_Game_No_Life_/")
                    .thread(2).run();
            endTime = System.currentTimeMillis();
            System.out.println("爬取结束，耗时约" + ((endTime - startTime) / 1000) + "秒，抓取了" + count + "条记录");
            try {
                Thread.sleep(20000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
