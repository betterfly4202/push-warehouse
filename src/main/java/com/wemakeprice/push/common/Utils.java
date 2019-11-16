package com.wemakeprice.push.common;

import java.util.concurrent.Callable;
import java.util.function.Function;

/**
 * Created by 이충일 (betterfly@wemakeprice.com)
 * Date : 2019.11.22
 */


public class Utils {
    public static <T, R, E extends Exception> Function<T,R> wrapper(FunctionThrowException<T, R, E> fe){
        return args ->{
            try {
                return fe.apply(args);
            }catch (Exception e){
                throw new RuntimeException(e);
            }
        };
    }

    public static <T> T uncheckCall(Callable<T> callable) {
        try { return callable.call(); }
        catch (RuntimeException e) { throw e; }
        catch (Exception e) { throw new RuntimeException(e); }
    }
}
