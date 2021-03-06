/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.alipay.sofa.jraft.util;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author jiachun.fjc
 */
public final class ThreadPoolUtil {

    /**
     * The default rejected execution handler
     */
    private static final RejectedExecutionHandler defaultHandler = new ThreadPoolExecutor.AbortPolicy();

    /**
     * Creates a new {@code MetricThreadPoolExecutor} or {@code LogThreadPoolExecutor}
     * with the given initial parameters and default rejected execution handler.
     *
     * @param poolName         the name of the thread pool
     * @param enableMetric     if metric is enabled
     * @param coreThreads      the number of threads to keep in the pool, even if they are
     *                         idle, unless {@code allowCoreThreadTimeOut} is set.
     * @param maximumThreads   the maximum number of threads to allow in the pool
     * @param keepAliveSeconds when the number of threads is greater than the core, this
     *                         is the maximum time (seconds) that excess idle threads will
     *                         wait for new tasks before terminating.
     * @param workQueue        the queue to use for holding tasks before they are executed.
     *                         This queue will hold only the {@code Runnable} tasks submitted
     *                         by the {@code execute} method.
     * @param threadFactory    the factory to use when the executor creates a new thread
     * @throws IllegalArgumentException if one of the following holds:<br>
     *         {@code corePoolSize < 0}<br>
     *         {@code keepAliveSeconds < 0}<br>
     *         {@code maximumPoolSize <= 0}<br>
     *         {@code maximumPoolSize < corePoolSize}
     * @throws NullPointerException if {@code workQueue}
     *         or {@code threadFactory} or {@code handler} is null
     */
    public static ThreadPoolExecutor newThreadPool(final String poolName, final boolean enableMetric,
                                                   final int coreThreads, final int maximumThreads,
                                                   final long keepAliveSeconds,
                                                   final BlockingQueue<Runnable> workQueue,
                                                   final ThreadFactory threadFactory) {
        return newThreadPool(poolName, enableMetric, coreThreads, maximumThreads, keepAliveSeconds, workQueue,
            threadFactory, defaultHandler);
    }

    /**
     * Creates a new {@code MetricThreadPoolExecutor} or {@code LogThreadPoolExecutor}
     * with the given initial parameters.
     *
     * @param poolName         the name of the thread pool
     * @param enableMetric     if metric is enabled
     * @param coreThreads      the number of threads to keep in the pool, even if they are
     *                         idle, unless {@code allowCoreThreadTimeOut} is set.
     * @param maximumThreads   the maximum number of threads to allow in the pool
     * @param keepAliveSeconds when the number of threads is greater than the core, this
     *                         is the maximum time (seconds) that excess idle threads will
     *                         wait for new tasks before terminating.
     * @param workQueue        the queue to use for holding tasks before they are executed.
     *                         This queue will hold only the {@code Runnable} tasks submitted
     *                         by the {@code execute} method.
     * @param threadFactory    the factory to use when the executor creates a new thread
     * @param handler          the handler to use when execution is blocked because the
     *                         thread bounds and queue capacities are reached
     * @throws IllegalArgumentException if one of the following holds:<br>
     *         {@code corePoolSize < 0}<br>
     *         {@code keepAliveSeconds < 0}<br>
     *         {@code maximumPoolSize <= 0}<br>
     *         {@code maximumPoolSize < corePoolSize}
     * @throws NullPointerException if {@code workQueue}
     *         or {@code threadFactory} or {@code handler} is null
     */
    public static ThreadPoolExecutor newThreadPool(final String poolName, final boolean enableMetric,
                                                   final int coreThreads, final int maximumThreads,
                                                   final long keepAliveSeconds,
                                                   final BlockingQueue<Runnable> workQueue,
                                                   final ThreadFactory threadFactory,
                                                   final RejectedExecutionHandler handler) {
        final TimeUnit unit = TimeUnit.SECONDS;
        if (enableMetric) {
            return new MetricThreadPoolExecutor(coreThreads, maximumThreads, keepAliveSeconds, unit, workQueue,
                threadFactory, handler, poolName);
        } else {
            return new LogThreadPoolExecutor(coreThreads, maximumThreads, keepAliveSeconds, unit, workQueue,
                threadFactory, handler, poolName);
        }
    }

    private ThreadPoolUtil() {
    }
}
