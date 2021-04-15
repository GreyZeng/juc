package git.snippets.juc.okhttp;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

/**
 * @author zenghui
 * @date 2020/3/23
 */
public class OkHttpClientNotSharePool {

    public static void main(String[] args) throws IOException {
        for (int i = 0; i < 10; i++) {
            String url = "http://www.baidu.com.com";
            OkHttpClient client = getHttpClient();
            Request request = new Request.Builder()
                    .url(url)
                    .get()  //默认为GET请求，可以不写
                    .build();
            final Response call = client.newCall(request).execute();
            System.out.println(call.body());
        }
    }

    /**
     * @return
     */
    private static OkHttpClient getHttpClient() {
        return HttpClientUtils.createNotShareConnectionPool();
    }
}
