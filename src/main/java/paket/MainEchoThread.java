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
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

class MainEchoThread extends Thread{
    Socket l_socket = null;
    StringBuffer result = null;
    HttpClient client = null;
    HttpPost post = null;
    List<NameValuePair> urlParameters = null;
    HttpResponse response = null;
    public MainEchoThread(Socket socket){
        l_socket = socket;
    }
    @Override
    public void run() {
        try(    PrintWriter out = new PrintWriter(l_socket.getOutputStream(),true);
                BufferedReader in = new BufferedReader(new InputStreamReader(l_socket.getInputStream(), Charset.forName("cp1251")))
        ){
            System.out.println("new client : "+l_socket.getInetAddress());
            String line = null;
            while ((line = in.readLine()) != null) {
                Document d = Jsoup.parse(line);
                Elements elementLogin = d.getElementsByTag("login");
                Elements elementPassword = d.getElementsByTag("password");
                String login = null;
                String password = null;
                for(Element e :elementLogin) {
                    login = e.text();
                }
                for(Element e :elementPassword) {
                    password = e.text();
                }
                new MyTestThread(out,login,password).start();
            }
            System.out.println("Связь с клиентом потеряно!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}