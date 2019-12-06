package orz.joey.dubbo.api.service;

import orz.joey.dubbo.api.dto.BaseRqDto;
import orz.joey.dubbo.api.dto.MessageDto;

/**
 * <p>Description: Demo</p>
 * <p>Create: 2019-12-05 14:32:24</p>
 * @author joey.zhu
 */
public interface DemoService {

    String sendMessage(BaseRqDto<MessageDto> rq);

}
