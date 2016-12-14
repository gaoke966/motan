/*
 *  Copyright 2009-2016 Weibo, Inc.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.weibo.api.motan.filter;

import com.weibo.api.motan.common.MotanConstants;
import com.weibo.api.motan.common.URLParamType;
import com.weibo.api.motan.core.extension.Activation;
import com.weibo.api.motan.core.extension.SpiMeta;
import com.weibo.api.motan.rpc.*;
import com.weibo.api.motan.util.LoggerUtil;
import com.weibo.api.motan.util.NetUtils;
import com.weibo.api.motan.util.StringTools;

/**
 * <pre>
 * Access Authority filter
 *
 * 对访问来源host进行验证.
 *
 * </pre>
 * 
 * @author kegao
 * @version V1.0 created at: 2016-12-15
 */
@SpiMeta(name = "accessAuthority")
@Activation(sequence = 2)
public class AccessAuthorityFilter implements Filter {

    @Override
    public Response filter(Caller<?> caller, Request request) {
        String enabledIPCheck = caller.getUrl().getParameters().get("enabledIPCheck");
        String allowedIPs = caller.getUrl().getParameters().get("allowedIPs");
        String clientIP = request.getAttachments().get("host");
        if(validatedIPs(enabledIPCheck,allowedIPs,clientIP)){
            return caller.call(request);
        } else {
            long t1 = System.currentTimeMillis();
            try {
                Response response = new DefaultResponse();
                response.setAttachment("info","accessAuthority validate failed");
                return response;
            } finally {
                long consumeTime = System.currentTimeMillis() - t1;
                logAccess(caller, request, consumeTime, false);
            }
        }
    }

    private boolean validatedIPs(String enabledIPCheck,String allowedIPs,String clientIP){
        if(!"true".equals(enabledIPCheck)) {
            return true;
        }else {
            if(allowedIPs!=null&&!"".equals(allowedIPs.trim())) {
                String[] allowedIPsArray = allowedIPs.split(",");
                for (String s : allowedIPsArray) {
                    if (clientIP.equals(s)) return true;
                }
            }
            return false;
        }
    }

    private void logAccess(Caller<?> caller, Request request, long consumeTime, boolean success) {
        String side = MotanConstants.NODE_TYPE_REFERER;

        StringBuilder builder = new StringBuilder(128);
        append(builder, side);
        append(builder, caller.getUrl().getParameter(URLParamType.application.getName()));
        append(builder, caller.getUrl().getParameter(URLParamType.module.getName()));
        append(builder, NetUtils.getLocalAddress().getHostAddress());
        append(builder, request.getInterfaceName());
        append(builder, request.getMethodName());
        append(builder, request.getParamtersDesc());
        // 对于client，url中的remote ip, application, module,referer 和 service获取的地方不同
        if (MotanConstants.NODE_TYPE_REFERER.equals(side)) {
            append(builder, caller.getUrl().getHost());
            append(builder, caller.getUrl().getParameter(URLParamType.application.getName()));
            append(builder, caller.getUrl().getParameter(URLParamType.module.getName()));
        } else {
            append(builder, request.getAttachments().get(URLParamType.host.getName()));
            append(builder, request.getAttachments().get(URLParamType.application.getName()));
            append(builder, request.getAttachments().get(URLParamType.module.getName()));
        }

        append(builder, success);
        append(builder, request.getAttachments().get(URLParamType.requestIdFromClient.getName()));
        append(builder, consumeTime);

        LoggerUtil.accessLog(builder.substring(0, builder.length() - 1));
    }

    private void append(StringBuilder builder, Object field) {
        if (field != null) {
            builder.append(StringTools.urlEncode(field.toString()));
        }
        builder.append(MotanConstants.SEPERATOR_ACCESS_LOG);
    }


}
