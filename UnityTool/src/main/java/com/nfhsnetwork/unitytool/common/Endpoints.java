package com.nfhsnetwork.unitytool.common;

import com.nfhsnetwork.unitytool.utils.IOUtils;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

public final class Endpoints {
    public static boolean isProd = true;
    
    private static final String jsonURL = "https://s3.amazonaws.com/assets.nfhsnetwork.com/console/nfhs_console.json";
    
    private static JSONObject json;
    
    private static JSONObject ENDPOINTS;
    
    public static void genEndpoints()
    {
        try {
            json = IOUtils.readJSONFromURL(jsonURL);
            setEndpoints();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    private static void setEndpoints()
    {
       ENDPOINTS = json.getJSONObject("endpoints");
    }
    
    public static String getConsoleEndpoint()
    {
        final JSONObject j = ENDPOINTS.getJSONObject("console_home");

        if (isProd)
            return j.getString("prod");
        else
            return j.getString("stage");
    }
    
    public static String getMConsoleEndpoint()
    {
        final JSONObject j = ENDPOINTS.getJSONObject("mobile_console");

        if (isProd)
            return j.getString("prod");
        else
            return j.getString("stage");
    }
    
    public static String getSSOEndpoint()
    {
        final JSONObject j = ENDPOINTS.getJSONObject("sso_root");

        if (isProd)
            return j.getString("prod");
        else
            return j.getString("stage");
    }
    
    public static String getSSOLoginEndpoint()
    {
        return getSSOEndpoint() + ENDPOINTS.getString("sso_sign_in");
    }
    
    public static String getSSOLogOutEndpoint()
    {
        return ENDPOINTS.getString("sso_sign_out");
    }
    
    public static String getUserNotificationAPIEndpoint()
    {
        final JSONObject j = ENDPOINTS.getJSONObject("user_notification_api");

        if (isProd)
            return j.getString("prod");
        else
            return j.getString("stage");
    }
    
    public static final String UNITY_BASE = "http://unity.nfhsnetwork.com/v2";
    
    public static String getUnityBroadcastLink(final String bdc)
    {
        return UNITY_BASE +  "/broadcasts/" + bdc;
    }
    
    public static String getUnityGameOrEventLink(final String gam)
    {
        return UNITY_BASE + "/games/" + gam;
    }
}
