package com.ahao.util.commons.net;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.net.InetAddress;

public class DNSTest {

    @Test
    public void localhost() throws Exception {
        long start = System.currentTimeMillis();
        InetAddress hostName = InetAddress.getLocalHost();
        long end = System.currentTimeMillis();
        System.out.println(hostName);
        long duration = end - start;
        System.out.println(duration);
        Assertions.assertTrue(duration < 5000);
    }

    @Test
    public void baidu() throws Exception {
        String domain = "www.baidu.com";
        InetAddress inetAddress = InetAddress.getByName(domain);
        String ip = inetAddress.getHostAddress();

        System.out.println(domain);
        System.out.println(ip);

        HttpGet get1 = new HttpGet("http://" + domain);
        HttpGet get2 = new HttpGet("http://" + ip);
        try (CloseableHttpClient client = HttpClients.createDefault();
             CloseableHttpResponse response1 = client.execute(get1);
             CloseableHttpResponse response2 = client.execute(get2)) {

            HttpEntity entity1 = response1.getEntity();
            HttpEntity entity2 = response2.getEntity();

            String html1 = EntityUtils.toString(entity1);
            String html2 = EntityUtils.toString(entity2);

            Assertions.assertEquals(html1, html2);

            EntityUtils.consume(entity1);
            EntityUtils.consume(entity2);
        }
    }
}
