package orz.joey.dubbo.api.filter;

import org.apache.dubbo.common.Constants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.Filter;
import org.apache.dubbo.rpc.Invocation;
import org.apache.dubbo.rpc.Invoker;
import org.apache.dubbo.rpc.Result;
import org.apache.dubbo.rpc.RpcContext;
import org.apache.dubbo.rpc.RpcException;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import orz.joey.dubbo.api.util.TraceIdUtil;

@Activate(group = {Constants.PROVIDER, Constants.CONSUMER})
public class TraceIdFilter implements Filter {
    private static final String TRACE_KEY = "traceId";

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        RpcContext context = RpcContext.getContext();
        if (context.isProviderSide()) {
            TraceIdUtil.setTraceId(context.getAttachment(TRACE_KEY));
        } else if (context.isConsumerSide()) {
            /**
             * <title>不同请求有可能会使用到同一个线程</title>
             *
             * <blockquote>
             * 如果我们使用完ThreadLocal对象而没有手动删掉，那么后面的请求就有机会使用到被使用过的ThreadLocal对象
             * 如果一个请求在使用ThreadLocal的时候，是先get()来判断然后再set()，那就会有问题，因为get到的就可能是别的请求set的内容
             * </blockquote>
             */
            String traceId = TraceIdUtil.getTraceId();
//            Assert.notNull(traceId, "you have to set traceId before call dubbo api");
            if (!StringUtils.hasLength(traceId)) {
                traceId = TraceIdUtil.generateTraceId();
                System.out.println("consumer generate traceId: " + traceId);
                TraceIdUtil.setTraceId(traceId);
            }

            context.setAttachment(TRACE_KEY, traceId);
        }

        try {
            return invoker.invoke(invocation);
        } finally {
            if (context.isProviderSide()) TraceIdUtil.removeTraceId();
        }
    }

}
