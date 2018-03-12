package paket;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

class MyTestThread extends Thread{
    String login;
    String password;
    StringBuffer result = null;
    PrintWriter printWriter = null;
    public MyTestThread(PrintWriter printWriter,String login,String password) {
        this.printWriter = printWriter;
        this.login = login;
        this.password = password;
    }

    @Override
    public void run() {

        try {
            TimeUnit.SECONDS.sleep(10);
        } catch (InterruptedException e) {
        }
        String xml = "<xml><action>login</action><login>"+login+"</login><password>"+password+"</password></xml>";
        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost("http://kover-samolet.333.kg/xml.php?utf=1");
        post.setHeader("User-Agent", null);
        List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
        urlParameters.add(new BasicNameValuePair("xml", xml));
        HttpResponse response = null;
        try {
            post.setEntity(new UrlEncodedFormEntity(urlParameters));
        } catch (IOException e) {
            e.printStackTrace();
        }
        BufferedReader rd = null;
        String line = "";
        Document document = null;
        Elements row = null;
        String str = null;
        String tmpRow = "";
        while(true) {
            try {
                response = client.execute(post);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                rd = new BufferedReader(
                        new InputStreamReader(response.getEntity().getContent()));
                result = new StringBuffer();
                while ((line = rd.readLine()) != null) {
                    result.append(line);
                }
                document = Jsoup.parse(result.toString());
                row = document.getElementsByTag("xml");
                str = row.toString();
                if((row!=null)&&(!tmpRow.equals(str))){
                    printWriter.println(result.toString());
                }
                tmpRow = str;
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
            }
        }

    }
}