package orz.joey.dubbo.api.dto;

import org.springframework.util.Assert;
import orz.joey.dubbo.api.constant.AppNameEnum;

import java.io.Serializable;

/**
 * <p>Description: Dubbo接口请求传输对象</p>
 * <p>Create: 2019-12-04 18:23:53</p>
 * @author JoeyGiraffe
 */
public class BaseRqDto<T> implements Serializable {
    private static final long serialVersionUID = 7859059523617336591L;
    /**
     * 调用方所属项目
     */
    private AppNameEnum appName;
    /**
     * 调用人
     */
    private String operator;
    /**
     * 请求的数据
     */
    private T data;

    /**
     * PS.凡是用作dubbo进行传输的对象都提供一个空参的默认构造方法，并且该构造方法里不做任何的业务操作
     * 以避免针对参数最少的构造方法制造伪参数时(基本类型取默认值，对象类型统一为null)导致业务处理失败！
     */
    private BaseRqDto() {}

    public BaseRqDto(AppNameEnum appName, String operator, T data) {
        Assert.notNull(appName, "appName must not be null");
        Assert.hasText(operator, "operator must have text");
        Assert.notNull(data, "data must not be null");
        this.appName = appName;
        this.operator = operator;
        this.data = data;
    }

    public AppNameEnum getAppName() {
        return appName;
    }

    public String getOperator() {
        return operator;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
