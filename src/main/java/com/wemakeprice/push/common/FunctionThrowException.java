package com.wemakeprice.push.common;

/**
 * Created by 이충일 (betterfly@wemakeprice.com)
 * Date : 2019.11.22
 */

@FunctionalInterface
public interface FunctionThrowException <T, R, E extends Exception>{
    R apply(T t) throws E;
}
