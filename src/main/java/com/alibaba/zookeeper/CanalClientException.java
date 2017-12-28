package com.alibaba.zookeeper;

import org.apache.commons.lang.exception.NestableRuntimeException;

/**
 * Created by ziyu.zhang on 2017/12/28.
 * Description 请用一句话描述
 */
public class CanalClientException extends NestableRuntimeException {

    private static final long serialVersionUID = -7545341502620139031L;

    public CanalClientException(String errorCode){
        super(errorCode);
    }

    public CanalClientException(String errorCode, Throwable cause){
        super(errorCode, cause);
    }

    public CanalClientException(String errorCode, String errorDesc){
        super(errorCode + ":" + errorDesc);
    }

    public CanalClientException(String errorCode, String errorDesc, Throwable cause){
        super(errorCode + ":" + errorDesc, cause);
    }

    public CanalClientException(Throwable cause){
        super(cause);
    }
}
