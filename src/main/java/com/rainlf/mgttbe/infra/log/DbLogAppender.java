package com.rainlf.mgttbe.infra.log;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.IThrowableProxy;
import ch.qos.logback.classic.spi.ThrowableProxyUtil;
import ch.qos.logback.core.AppenderBase;
import com.rainlf.mgttbe.infra.db.dataobj.MgttLog;
import com.rainlf.mgttbe.infra.db.manager.MgttLogManager;
import com.rainlf.mgttbe.infra.interceptor.BIzContext;

public class DbLogAppender extends AppenderBase<ILoggingEvent> {


    @Override
    protected void append(ILoggingEvent eventObject) {
        Level level = eventObject.getLevel();
        String message = eventObject.getFormattedMessage();
        IThrowableProxy throwableProxy = eventObject.getThrowableProxy();
        String stackTrace = null;
        if (throwableProxy != null) {
            stackTrace = ThrowableProxyUtil.asString(throwableProxy);
        }

        MgttLog log = new MgttLog();
        log.setLevel(level.levelStr);
        log.setThread(eventObject.getThreadName());
        log.setMessage(message);
        log.setStackTrace(stackTrace);
        log.setBizId(BIzContext.getContext().getBizId());

        MgttLogManager.getInstance().saveLog(log);
    }
}
