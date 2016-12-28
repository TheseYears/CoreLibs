mport java.lang.reflect.Field;
import java.security.SecureRandom;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;

/**
 * Author: yangdm
 * Email:yangdm@bluemobi.cn
 * Description:(忽略证书访问HTTPS接口
 */


public class IgnoreCertificateOkHttpClient {
    public static OkHttpClient getOkHttpsClient() {
        OkHttpClient sClient = new OkHttpClient();
        SSLContext sc = null;
        try {  sc = SSLContext.getInstance("SSL");
            sc.init(null, new TrustManager[]{
                            new X509TrustManager() {
                                @Override
                                public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws java.security.cert.CertificateException {  }
                                @Override
                                public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws java.security.cert.CertificateException {  }
                                @Override public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                                    return null;}   }},
                    new SecureRandom()); } catch (Exception e) {
            e.printStackTrace(); }
        HostnameVerifier hv1 = new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;      }    };
        String workerClassName="okhttp3.OkHttpClient";
        try {
            Class workerClass = Class.forName(workerClassName);
            Field hostnameVerifier = workerClass.getDeclaredField("hostnameVerifier");
            hostnameVerifier.setAccessible(true);
            hostnameVerifier.set(sClient, hv1);
            Field sslSocketFactory = workerClass.getDeclaredField("sslSocketFactory");
            sslSocketFactory.setAccessible(true);
            sslSocketFactory.set(sClient, sc.getSocketFactory());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sClient;
    }

}
