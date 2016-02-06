package io.github.zebMcCorkle.FontInstaller;

import com.google.gson.Gson;
import io.github.zebMcCorkle.FontInstaller.github.Release;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/*
 * Copyright (c) 2016 Zeb McCorkle
 *
 * All Rights Reserved
 */
public class AutoUpdate implements Runnable {
    private static final boolean ENABLED = true;
    public static final String VERSION = "v1.1.1";

    public boolean uptodate;
    public String updateUrl;
    public String newVersion;

    @Override
    public void run() {
        if (!ENABLED) return;
        Map<String, String> headers = new HashMap<>();
        headers.put("User-Agent", "github.com/zebMcCorkle/FontInstaller auto updater");
        headers.put("Accept", "application/vnd.github.v3+json");
        String endpoint = "https://api.github.com";
        String res;
        try {
            res = get(endpoint, "/repos/zebMcCorkle/FontInstaller/releases/latest", headers);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        Gson gson = new Gson();
        Release release = gson.fromJson(res, Release.class);
        if (release.tag_name.equals(VERSION)) {
            uptodate = true;
            System.out.println(VERSION + " up to date");
        } else {
            uptodate = false;
            updateUrl = release.assets[0].browser_download_url;
            newVersion = release.tag_name;
            System.out.println("outdated, you have " + VERSION + " but latest is " + release.tag_name);
        }
    }

    private static String get(String endpoint, String url, Map<String, String> headers) throws IOException {
        return get(endpoint + url, headers);
    }
    private static String get(String surl, Map<String, String> headers) throws IOException {
        URL url = new URL(surl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            connection.setRequestProperty(entry.getKey(), entry.getValue());
        }
        connection.setUseCaches(false);
        connection.setDoOutput(true);
        InputStream is = connection.getInputStream();
        BufferedReader rd = new BufferedReader(new InputStreamReader(is));
        StringBuilder response = new StringBuilder();
        String l;
        while ((l = rd.readLine()) != null) {
            response.append(l);
            response.append('\n');
        }
        rd.close();
        return response.toString();
    }
}
