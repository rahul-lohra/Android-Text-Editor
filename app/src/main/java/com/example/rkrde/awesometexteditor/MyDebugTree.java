package com.example.rkrde.awesometexteditor;

import timber.log.Timber;

/**
 * Created by rkrde on 11-09-2017.
 */

public class MyDebugTree extends Timber.DebugTree {
    @Override
    protected String createStackElementTag(StackTraceElement element) {
        return String.format("[L:%s] [M:%s] [C:%s]",
                element.getLineNumber(),
                element.getMethodName(),
                super.createStackElementTag(element));
    }
}
