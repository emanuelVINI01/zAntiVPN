package com.zantivpn;

import com.google.gson.Gson;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class ProxyDetector {
    public static String readInputStreamAsString(InputStream in)
            throws IOException {

        BufferedInputStream bis = new BufferedInputStream(in);
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        int result = bis.read();
        while(result != -1) {
            byte b = (byte)result;
            buf.write(b);
            result = bis.read();
        }
        return buf.toString();
    }
    public static boolean detectProxy(String ip) throws IOException {
        URL url = new URL(String.format("http://proxycheck.io/v2/%s?key=111111-222222-333333-444444", ip));
        InputStream stream = url.openStream();
        Gson gson = new Gson(); // conversor
        IPResponde objCliente = gson.fromJson(readInputStreamAsString(stream), IPResponde.class);
        return objCliente.getProxy();
    }
}
