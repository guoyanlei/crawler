package com.crawler.util;

import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * Created by guoyanlei
 * Date：2017/11/1
 * Description：
 */
public class QiniuUtil {

    private static final String accessKey = "5A3XXlqI3eYst3qIVyE4QRiipJyELkVnVi0dUpFJ";
    private static final String secretKey = "rU6hPs2TWDI_vNCS3eXu5sIp05KGvzhUaRUJVIst";
    private static final String bucket = "yunziru";
    private static final String domain = "http://oyqte78ri.bkt.clouddn.com/";

    private static QiniuUtil instance = null;
    private QiniuUtil(){}

    public static QiniuUtil getInstance(){
        if (instance == null) {
            instance = new QiniuUtil();
        }
        return instance;
    }

    public String storeQiniuAndGetUrl(String folder, String imgUrl) {

        Configuration cfg = new Configuration(Zone.zone1());

        UploadManager uploadManager = new UploadManager(cfg);

        String key = folder + "/" + this.getFileName(imgUrl);

        Auth auth = Auth.create(accessKey, secretKey);
        String upToken = auth.uploadToken(bucket);

        try {

            CloseableHttpClient httpClient = HttpClients.createDefault();

            HttpGet httpGet = new HttpGet(imgUrl);
            httpGet.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/45.0.2454.101 Safari/537.36");
            // 执行请求
            HttpResponse httpResponse = httpClient.execute(httpGet);

            if(httpResponse.getStatusLine().getStatusCode() == 200){

                //得到实体
                HttpEntity entity = httpResponse.getEntity();

                byte[] data = EntityUtils.toByteArray(entity);

                Response response = uploadManager.put(data, key, upToken);
                //解析上传成功的结果
                DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);

                //System.out.println(putRet.hash);
                return domain + putRet.key;
            }

        } catch (QiniuException ex) {
            Response r = ex.response;
            System.err.println(r.toString());
            try {
                System.err.println(r.bodyString());
            } catch (QiniuException ex2) {
                //ignore
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public String getFileName(String imgUrl) {
        String[] files = imgUrl.split("/");
        return files[files.length-1];
    }

    public static void main(String[] args) {

        QiniuUtil qiniuUtil = QiniuUtil.getInstance();
        String url = "http://www.xixi89.com/data/attachment/forum/201710/29/083844llxl49qxld69dd9j.jpg";
        System.out.println(qiniuUtil.storeQiniuAndGetUrl("movie", url));


    }
}
