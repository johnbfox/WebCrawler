import java.io.*;
import java.net.*;
import java.util.*;

public class WebCrawler {
	
	URL url;
	ArrayList<URL> urls;
	Queue<URL> urlQueue;
	
	public WebCrawler(String startURL) throws Exception{
		urls = new ArrayList<URL>();
		urlQueue = new LinkedList<URL>();
		urls.add(new URL(startURL));
		urlQueue.add(new URL(startURL));
	}
	
	public void crawl() throws IOException{
		try {
			while(urlQueue.size()!=0 && urls.size()<100){
				url = urlQueue.remove();
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setRequestMethod("GET");
				BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				System.out.println("Request made to: " + url.toString());
				String line;
				while ((line = rd.readLine()) != null)
				{
					processLine(line);
				}
				rd.close();
				conn.disconnect();
				waitFive();
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(urls);
		FileWriter outFile = new FileWriter("urlList.txt");
        PrintWriter out = new PrintWriter(outFile);
		for(URL u : urls){
			out.println(u.toString());
		}
		out.close();
	}
	
	private void processLine(String line) throws MalformedURLException{
		String lowerLine = line.toLowerCase();
		for(int i = 0; i < line.length(); i++){
			if(lowerLine.charAt(i)=='<' && i+6 < line.length()){
				String rest = "" + lowerLine.charAt(i+1) + lowerLine.charAt(i+2) + lowerLine.charAt(i+3) + lowerLine.charAt(i+4)
				+ lowerLine.charAt(i+5) + lowerLine.charAt(i+6);
				if(rest.equals("a href")){
					i = i+9;
					String link = "";
					while(lowerLine.charAt(i)!= 34){
						link += lowerLine.charAt(i);
						i++;
					}
					
					if(!link.contains("#")){
						URL parsedLink = parseLink(link);
						if(parsedLink.getHost().contains("cs.umass.edu") && !urls.contains(parsedLink)){
							urls.add(parsedLink);
							urlQueue.add(parsedLink);
						}
					}
				}
			}
		}
		//System.out.println(urls);
	}
	
	private URL parseLink(String s) throws MalformedURLException{
		if(s.length()>7 && s.substring(0, 7) == "http://"){
			return new URL(s);
		}else{
			if(s.substring(0,3).equals("../")){
				return new URL(url.getProtocol() + "://" + url.getHost() + "/" + s.substring(3));
			}else{
				return new URL(url, s);
			}
		}
	}
	
	private void waitFive(){
		long time0, time1;
		time0 = System.currentTimeMillis();
		do{
			time1 = System.currentTimeMillis();
		}
		while ((time1-time0) < 5 * 1000);
	}
	

}
