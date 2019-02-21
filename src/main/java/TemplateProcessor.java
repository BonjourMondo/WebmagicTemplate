import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

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
            }
            page.putField("B",page.getHtml().xpath(""));
        }else{
            page.addTargetRequests(page.getHtml().links().regex(URL_POST).all(), 1000);
            page.addTargetRequests(page.getHtml().links().regex(URL_LIST).all(), 1);
        }
    }

    public Site getSite() {
        if (site==null) {
            site.me().setRetryTimes(3).setSleepTime(500).setTimeOut(10000)
                    .setDomain("blog.csdn.net").addCookie("blog.csdn.net", "__yadk_uid", "Qm7dEc8XUauSwGnL8pAdY60k4lzp8JVv")
                    .setCharset("utf-8").setUserAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_7_2) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.65 Safari/537.31");

        }
        return site;
    }

    public static void main(String[] args) {
        long startTime, endTime;
        System.out.println("开始爬取...");
        startTime = System.currentTimeMillis();
        Spider.create(new TemplateProcessor()).addUrl("https://blog.csdn.net/No_Game_No_Life_").thread(5).run();
        endTime = System.currentTimeMillis();
        System.out.println("爬取结束，耗时约" + ((endTime - startTime) / 1000) + "秒，抓取了"+count+"条记录");

    }
}
