package git.snippets.juc;

import com.google.common.util.concurrent.RateLimiter;

import java.util.List;
import java.util.concurrent.Executor;

/**
 * @author <a href="mailto:410486047@qq.com">Grey</a>
 * @date 2021/4/21
 * @since
 */
public class RateLimiterUsage {
    //每秒只发出2个令牌
    static final RateLimiter rateLimiter = RateLimiter.create(2.0);
    static void submitTasks(List<Runnable> tasks, Executor executor) {
        for (Runnable task : tasks) {
            rateLimiter.acquire(); // 也许需要等待
            executor.execute(task);
        }
    }
}
