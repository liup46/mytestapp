package com.lp.common.logger;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by pengliu2 on 6/26/15.
 */
public class LogView extends TextView implements LogNode {

    private LogNode mNext;

    public LogView(Context context) {
        super(context);
    }

    public LogView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LogView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void println(int priority, String tag, String msg, Throwable tr) {
        String priorityStr = null;
        switch (priority) {
            case android.util.Log.VERBOSE:
                priorityStr = "VERBOSE";
                break;
            case android.util.Log.DEBUG:
                priorityStr = "DEBUG";
                break;
            case android.util.Log.INFO:
                priorityStr = "INFO";
                break;
            case android.util.Log.WARN:
                priorityStr = "WARN";
                break;
            case android.util.Log.ERROR:
                priorityStr = "ERROR";
                break;
            case android.util.Log.ASSERT:
                priorityStr = "ASSERT";
                break;
            default:
                break;
        }

        String exceptionStr = null;
        if (tr != null) {
            exceptionStr = android.util.Log.getStackTraceString(tr);
        }

        final StringBuilder outputBuilder = new StringBuilder();
        String delimiter = "\t";
        appendIfNotNull(outputBuilder, priorityStr, delimiter);
        appendIfNotNull(outputBuilder, tag, delimiter);
        appendIfNotNull(outputBuilder, msg, delimiter);
        appendIfNotNull(outputBuilder, exceptionStr, delimiter);

        ((Activity) getContext()).runOnUiThread(new Thread(new Runnable() {
            @Override
            public void run() {
                appendToLog(outputBuilder.toString());
            }
        }));

        if (mNext != null) {
            mNext.println(priority, tag, msg, tr);
        }

    }

    private StringBuilder appendIfNotNull(StringBuilder source, String addStr, String delimiter) {
        if (addStr != null) {
            if (addStr.length() == 0) {
                delimiter = "";
            }
            return source.append(addStr).append(delimiter);
        }
        return source;
    }

    public void appendToLog(String s) {
        append("\n" + s);

    }


    public LogNode getNext() {
        return mNext;
    }

    public void setNext(LogNode mNext) {
        this.mNext = mNext;
    }

}
