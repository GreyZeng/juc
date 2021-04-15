package git.snippets.juc.okhttp;

import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;

import static java.util.concurrent.TimeUnit.MINUTES;

/**
 * 获取OkhttpClient客户端，
 * TODO 封装常用的请求方法，后续考虑Apache HttpClient
 *
 * @author zenghui
 * @date 2020/3/20
 */
public final class HttpClientUtils {
    /**
     * 默认连接池配置
     */
    public static final ConnectionPool CONNECTION_POOL = new ConnectionPool(256, 5L, MINUTES);

    /**
     * 获取OkhttpClient(复用连接池）
     *
     * @return
     */
    public static OkHttpClient createShareConnectionPool() {
        return new OkHttpClient().newBuilder().connectionPool(CONNECTION_POOL).build();
    }

    /**
     * 获取OkhttpClient(复用连接池）
     *
     * @return
     */
    public static OkHttpClient createNotShareConnectionPool() {
        return new OkHttpClient.Builder().build();
    }
}
