
public class Crawl {
	
	public static void main(String[] args) throws Exception{
		WebCrawler crawler = new WebCrawler("http://ciir.cs.umass.edu/");
		crawler.crawl();
	}
}
