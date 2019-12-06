package orz.joey.dubbo.provider.demo.impl;

import org.apache.dubbo.config.annotation.Service;
import org.apache.dubbo.rpc.RpcContext;
import orz.joey.dubbo.api.dto.BaseRqDto;
import orz.joey.dubbo.api.dto.MessageDto;
import orz.joey.dubbo.api.service.DemoService;
import orz.joey.dubbo.api.util.TraceIdUtil;

//注意是Dubbo的注解
@Service
public class DemoServiceImpl implements DemoService {
    @Override
    public String sendMessage(BaseRqDto<MessageDto> rq) {
        return String.format("[%s][msg: %s, ——from: %s]", TraceIdUtil.getTraceId(), rq.getData().getMessage(), RpcContext.getContext().getRemoteAddressString());
    }
}
