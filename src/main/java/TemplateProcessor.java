import csdn.CSDNProcessor;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.ConsolePipeline;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.scheduler.PriorityScheduler;

/**
 * Author: LeesangHyuk
 * Date: 2019/2/21 10:49
 * Description:爬虫模版
 */
public class TemplateProcessor implements PageProcessor {

    private Site site;
    private static int count=0;

    public static final String URL_LIST = "";
    public static final String URL_POST = "";

    public void process(Page page) {
        if (page.getUrl().regex(URL_POST).match()) {
            page.putField("A",page.getHtml().xpath(""));
            if (page.getResultItems().get("A") == null) {
                page.setSkip(true);
            }else {
                count++;
            }
            page.putField("url",page.getUrl().toString());

        }else{
            page.addTargetRequests(page.getHtml().links().regex(URL_POST).all(), 1000);
            page.addTargetRequests(page.getHtml().links().regex(URL_LIST).all(), 1);
        }
    }

    public Site getSite() {
        if (site==null) {
            site=Site.me().setRetryTimes(3).setSleepTime(1000).setTimeOut(10000)
                    .setDomain("blog.csdn.net").addCookie("blog.csdn.net", "__yadk_uid", "Qm7dEc8XUauSwGnL8pAdY60k4lzp8JVv")
                    .addHeader("referer","https://blog.csdn.net/No_Game_No_Life_")
                    .setCharset("utf-8").setUserAgent("Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.25 Safari/537.36 Core/1.70.3633.400 QQBrowser/10.4.3232.400");

        }
        return site;
    }

    public static void main(String[] args) {
        long startTime, endTime;
        System.out.println("开始爬取...");
        startTime = System.currentTimeMillis();
        Spider.create(new TemplateProcessor())
                .setScheduler(new PriorityScheduler())
                .addPipeline(new ConsolePipeline())
                .addUrl("https://blog.csdn.net/No_Game_No_Life_/")
                .thread(5).run();
        endTime = System.currentTimeMillis();
        System.out.println("爬取结束，耗时约" + ((endTime - startTime) / 1000) + "秒，抓取了"+count+"条记录");

    }
}
