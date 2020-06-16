package com.gyx.superscheduled.core.RunnableInterceptor.strengthen;

import com.gyx.superscheduled.common.annotation.SuperScheduledInteriorOrder;
import com.gyx.superscheduled.exception.SuperScheduledException;
import com.gyx.superscheduled.model.ScheduledRunningContext;
import com.gyx.superscheduled.properties.ZooKeeperProperties;
import org.I0Itec.zkclient.ZkClient;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

@SuperScheduledInteriorOrder(Integer.MAX_VALUE)
public class ColonyStrengthen implements BaseStrengthen {
    protected final Log logger = LogFactory.getLog(getClass());
    @Autowired
    private ZkClient zk;
    @Autowired
    private ZooKeeperProperties zooKeeperProperties;

    /**
     * 前置强化方法
     *
     * @param bean    bean实例（或者是被代理的bean）
     * @param method  执行的方法对象
     * @param args    方法参数
     * @param context 任务线程运行时的上下文
     */
    @Override
    public void before(Object bean, Method method, Object[] args, ScheduledRunningContext context) {
        boolean exists = zk.exists(zooKeeperProperties.getZkParentNodePath());

        if (exists) {
            List<String> children = zk.getChildren(zooKeeperProperties.getZkParentNodePath());
            if (children != null && children.size() > 0) {
                Collections.sort(children);
                if (zooKeeperProperties.getZkPath().equals(zooKeeperProperties.getZkParentNodePath() + '/' + children.get(0))) {
                    return;
                }
            } else {
                throw new SuperScheduledException("zookeeper连接出现异常");
            }
            context.setCallOff(true);
            context.setCallOffRemark("任务已交由其他服务运行");
        }
    }

    /**
     * 后置强化方法
     *
     * @param bean    bean实例（或者是被代理的bean）
     * @param method  执行的方法对象
     * @param args    方法参数
     * @param context 任务线程运行时的上下文
     */
    @Override
    public void after(Object bean, Method method, Object[] args, ScheduledRunningContext context) {
    }

    /**
     * 异常强化方法
     *
     * @param bean    bean实例（或者是被代理的bean）
     * @param method  执行的方法对象
     * @param args    方法参数
     * @param context 任务线程运行时的上下文
     */
    @Override
    public void exception(Object bean, Method method, Object[] args, ScheduledRunningContext context) {

    }

    /**
     * Finally强化方法，出现异常也会执行
     *
     * @param bean    bean实例（或者是被代理的bean）
     * @param method  执行的方法对象
     * @param args    方法参数
     * @param context 任务线程运行时的上下文
     */
    @Override
    public void afterFinally(Object bean, Method method, Object[] args, ScheduledRunningContext context) {
    }

}

