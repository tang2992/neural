package io.neural.micro;

import java.sql.Timestamp;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 微时间<br>
 * <br>
 * 高并发场景下System.currentTimeMillis()的性能问题的优化<br>
 * 
 * @author lry
 */
public class Systemclock {

	private final long period;
	private final AtomicLong now;

	private Systemclock(long period) {
		this.period = period;
		this.now = new AtomicLong(System.currentTimeMillis());
		scheduleClockUpdating();
	}

	private static class InstanceHolder {
		public static final Systemclock INSTANCE = new Systemclock(1);
	}

	private static Systemclock instance() {
		return InstanceHolder.INSTANCE;
	}

	private void scheduleClockUpdating() {
		ScheduledExecutorService scheduler = Executors
				.newSingleThreadScheduledExecutor(new ThreadFactory() {
					public Thread newThread(Runnable runnable) {
						Thread thread = new Thread(runnable, "System Clock");
						thread.setDaemon(true);
						return thread;
					}
				});
		scheduler.scheduleAtFixedRate(new Runnable() {
			public void run() {
				now.set(System.currentTimeMillis());
			}
		}, period, period, TimeUnit.MILLISECONDS);
	}

	private long currentTimeMillis() {
		return now.get();
	}

	public static long now() {
		return instance().currentTimeMillis();
	}

	public static String nowDate() {
		return new Timestamp(instance().currentTimeMillis()).toString();
	}

}