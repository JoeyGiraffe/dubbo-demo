package orz.joey.dubbo.api.filter;

import org.apache.dubbo.common.Constants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.Filter;
import org.apache.dubbo.rpc.Invocation;
import org.apache.dubbo.rpc.Invoker;
import org.apache.dubbo.rpc.Result;
import org.apache.dubbo.rpc.RpcContext;
import org.apache.dubbo.rpc.RpcException;
import org.springframework.util.StringUtils;
import orz.joey.dubbo.api.util.TraceIdUtil;

@Activate(group = {Constants.PROVIDER, Constants.CONSUMER})
public class TraceIdFilter implements Filter {
    private static final String TRACE_KEY = "traceId";

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        RpcContext context = RpcContext.getContext();
        if (context.isProviderSide()) {
            String traceId = context.getAttachment(TRACE_KEY);
            if (!StringUtils.hasLength(traceId)) traceId = TraceIdUtil.generateTraceId();
            TraceIdUtil.setTraceId(traceId);
        } else if (context.isConsumerSide()) {
            /*
              <p>不同请求有可能会使用到同一个线程</p>
              <blockquote>
              如果我们使用完ThreadLocal对象而没有手动删掉，那么后面的请求就有机会使用到被使用过的ThreadLocal对象
              如果一个请求在使用ThreadLocal的时候，是先get()来判断然后再set()，那就会有问题，因为get到的就可能是别的请求set的内容
              解决方案：
                1、保证每次都用新的值覆盖线程变量
                2、保证在每个请求结束后清空线程变量
              </blockquote>
             */
            /*String traceId = TraceIdUtil.getTraceId();
            if (!StringUtils.hasLength(traceId)) {
                traceId = TraceIdUtil.generateTraceId();
                System.out.println("consumer generate traceId: " + traceId);
                TraceIdUtil.setTraceId(traceId);
            }
            context.setAttachment(TRACE_KEY, traceId);*/


            //为实现一次请求中多次调用接口共用一个traceId，消费端需要自己在每一次请求开始时设置traceId
            context.setAttachment(TRACE_KEY, TraceIdUtil.getTraceId());
        }

        try {
            return invoker.invoke(invocation);
        } finally {
            if (context.isProviderSide()) TraceIdUtil.removeTraceId();
        }
    }

}
