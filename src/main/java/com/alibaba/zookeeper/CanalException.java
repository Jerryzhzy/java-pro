package com.alibaba.zookeeper;

import org.apache.commons.lang.exception.NestableRuntimeException;

/**
 * Created by ziyu.zhang on 2017/12/28.
 * Description 请用一句话描述
 */
public class CanalException extends NestableRuntimeException {
    private static final long serialVersionUID = -654893533794556357L;

    public CanalException(String errorCode){
        super(errorCode);
    }

    public CanalException(String errorCode, Throwable cause){
        super(errorCode, cause);
    }

    public CanalException(String errorCode, String errorDesc){
        super(errorCode + ":" + errorDesc);
    }

    public CanalException(String errorCode, String errorDesc, Throwable cause){
        super(errorCode + ":" + errorDesc, cause);
    }

    public CanalException(Throwable cause){
        super(cause);
    }

    public Throwable fillInStackTrace() {
        return this;
    }
}
