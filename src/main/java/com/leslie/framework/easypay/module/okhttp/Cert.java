package com.leslie.framework.easypay.module.okhttp;

import lombok.Getter;
import lombok.Setter;

import java.io.InputStream;

/**
 * @author leslie
 * @date 2020/9/15
 */
@Getter
@Setter
public class Cert {

    private InputStream certStream;

    private String storeType;

    private char[] storePass;

    private char[] keyPass;

}
