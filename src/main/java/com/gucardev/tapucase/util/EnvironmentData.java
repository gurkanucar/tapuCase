package com.gucardev.tapucase.util;

import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.net.MalformedURLException;
import java.net.URL;

@Component
public class EnvironmentData {

    public String getURLBase(HttpServletRequest request){
        URL requestURL = null;
        try {
            requestURL = new URL(request.getRequestURL().toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        String port = requestURL.getPort() == -1 ? "" : ":" + requestURL.getPort();
        return requestURL.getProtocol() + "://" + requestURL.getHost() + port;

    }

}
