package porn;

import csdn.CSDNProcessor;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.ConsolePipeline;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.scheduler.PriorityScheduler;

/**
 * Author: LeesangHyuk
 * Date: 2019/2/21 19:12
 * Description:
 */
public class PornProcessor implements PageProcessor {

    private Site site;
    private static int count=0;

    public static final String URL_LIST1 = "https://www.752ii.com/shipin/nvyou.*.html";
    public static final String URL_LIST12="https://www.752ii.com/shipin/list.*.html";
    public static final String URL_POST = "https://www.752ii.com/shipin/[0-9]*.html";

    public void process(Page page) {
        if (page.getUrl().regex(URL_POST).match()) {
            page.putField("片名",page.getHtml().xpath("//*[@id=\"shipin-detail-content-pull\"]/div[2]/div[1]/h2/text()"));
            if (page.getResultItems().get("片名") == null) {
                page.setSkip(true);
            }else {
                count++;
            }
//            page.putField("url",page.getUrl().toString());
            page.putField("actor",page.getHtml().xpath("//*[@id=\"shipin-detail-content-pull\"]/div[2]/div[3]/p/text()"));
            page.putField("下载链接",page.getHtml().xpath("//*[@id=\"lin1k0\"]/@value"));
        }else{
            page.addTargetRequests(page.getHtml().links().regex(URL_POST).all(), 1000);
            page.addTargetRequests(page.getHtml().links().regex(URL_LIST1).all(), 1);
            page.addTargetRequests(page.getHtml().links().regex(URL_LIST12).all(),1);
        }
    }

    public Site getSite() {
        if (site==null) {
            site=Site.me().setRetryTimes(3).setSleepTime(1000).setTimeOut(10000)
                    .addHeader("referer","ttps://www.752ii.com/shipin/nvyou-julia-3.html")
                    .setCharset("utf-8").setUserAgent("Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.25 Safari/537.36 Core/1.70.3633.400 QQBrowser/10.4.3232.400");

        }
        return site;
    }

    public static void main(String[] args) {
        long startTime, endTime;
        System.out.println("开始爬取...");
        startTime = System.currentTimeMillis();
        Spider.create(new PornProcessor())
                .setScheduler(new PriorityScheduler())
                .addPipeline(new ConsolePipeline())
                .addUrl("https://www.752ii.com/shipin/list-%E5%A5%B3%E4%BC%98%E4%B8%93%E8%BE%91-1.html")
                .thread(5).run();
        endTime = System.currentTimeMillis();
        System.out.println("爬取结束，耗时约" + ((endTime - startTime) / 1000) + "秒，抓取了"+count+"条记录");

    }
}
