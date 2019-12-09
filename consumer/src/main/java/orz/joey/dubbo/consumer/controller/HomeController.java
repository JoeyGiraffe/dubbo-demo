package orz.joey.dubbo.consumer.controller;

import org.apache.dubbo.config.annotation.Reference;
import org.apache.dubbo.rpc.RpcException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import orz.joey.dubbo.api.constant.AppNameEnum;
import orz.joey.dubbo.api.dto.BaseRqDto;
import orz.joey.dubbo.api.dto.MessageDto;
import orz.joey.dubbo.api.service.DemoService;
import orz.joey.dubbo.api.util.TraceIdUtil;

@RequestMapping("/")
@RestController
public class HomeController {

    //注意为Dubbo的注解，check=false就可以关闭该服务的启动检测
    @Reference(check = false)
    private DemoService demoService;

    @GetMapping("/send")
    public String send(@RequestParam String msg) {
        //可使用拦截器完成traceId的设置与清除
        TraceIdUtil.init();

        MessageDto msgDto = new MessageDto();
        msgDto.setMessage(msg);

        StringBuilder sb = new StringBuilder("initial traceId:" + TraceIdUtil.getTraceId());
        String firstCallRs = demoService.sendMessage(new BaseRqDto<>(AppNameEnum.CONSUMER, "joey", msgDto));
        sb.append("<br/>").append(firstCallRs).append("<br/>").append("traceId after first request:").append(TraceIdUtil.getTraceId());
        String secondCallRs = demoService.sendMessage(new BaseRqDto<>(AppNameEnum.CONSUMER, "joey", msgDto));
        sb.append("<br/>").append(secondCallRs).append("<br/>").append("traceId after second request:").append(TraceIdUtil.getTraceId());
        return sb.toString();
    }


    @ExceptionHandler
    public String rqcExceptionHandler(RpcException e) {
        return e.getMessage();
    }
}
