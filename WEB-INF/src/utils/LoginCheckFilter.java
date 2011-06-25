package utils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Created by IntelliJ IDEA.
 * User: root
 * Date: 2010-3-4
 * Time: 13:21:07
 * 用于检测用户是否登陆的过滤器，如果未登录，则重定向到指的登录页面
 * 配置参数
 * checkSessionKey 需检查的在 Session 中保存的关键字
 * redirectURL 如果用户未登录，则重定向到指定的页面，URL不包括 ContextPath
 * notCheckURLList 不做检查的URL列表，以分号分开，并且 URL 中不包括 ContextPath
 * checkPrefixList 定义检查的前缀，以分号分开，并且 URL 中不包括 ContextPath
 * checkSuffixList 定义检查的后缀，以分号分开，并且 URL 中不包括 ContextPath
 */
public class LoginCheckFilter implements Filter {
    protected FilterConfig filterConfig = null;
    private String redirectURL = null;
    private List<String> notCheckURLList = new ArrayList<String>();
    private String sessionKey = null;
    private List<String> checkSuffixList = new ArrayList<String>();
    private List<String> checkPrefixList = new ArrayList<String>();

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpSession session = request.getSession(false);
        if (sessionKey == null) {
            filterChain.doFilter(request, response);
            return;
        }
        if (checkRequestURIIntNotFilterList(request)) {
            filterChain.doFilter(request, response);
        } else {
            if (session==null || session.getAttribute(sessionKey) == null) {
                System.out.println(request.getRequestURI() + ": send redirecting...");
                response.sendRedirect(request.getContextPath() + redirectURL);
            }else{
                filterChain.doFilter(request, response);
            }
        }
    }

    public void destroy() {
        notCheckURLList.clear();
    }

    private boolean checkRequestURIIntNotFilterList(HttpServletRequest request) {
        String reqInfo =request.getServletPath() == null ? "" : request.getServletPath();
//        System.out.println("ok the reqInfo is "+reqInfo);
        for (String end : notCheckURLList) {//只要任意命中即确定忽略
            if (reqInfo.endsWith(end)) return true;
        }
        for (String end : checkPrefixList) {//只要任意命中即确定跳转
//            System.out.println("reqInfo|end==="+reqInfo+"|"+end+"   ===  "+reqInfo.contains(end));
            if (reqInfo.contains(end)) return false;
        }
        for (String end : checkSuffixList) {//只要任意命中即确定跳转
            if (reqInfo.endsWith(end)) return false;
        }
        return true;
    }

    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
        redirectURL = filterConfig.getInitParameter("redirectURL");
        sessionKey = filterConfig.getInitParameter("checkSessionKey");

        String notCheckURLListStr = filterConfig.getInitParameter("notCheckURLList");
        if (notCheckURLListStr != null) {
            StringTokenizer st = new StringTokenizer(notCheckURLListStr, ";");
            notCheckURLList.clear();
            while (st.hasMoreTokens()) {
                notCheckURLList.add(st.nextToken());
            }
        }

        String checkSuffixListStr = filterConfig.getInitParameter("checkSuffixList");
        if (checkSuffixListStr != null) {
            StringTokenizer st = new StringTokenizer(checkSuffixListStr, ";");
            checkSuffixList.clear();
            while (st.hasMoreTokens()) {
                checkSuffixList.add(st.nextToken());
            }
        }

        String checkPrefixListStr = filterConfig.getInitParameter("checkPrefixList");
        if (checkPrefixListStr != null) {
            StringTokenizer st = new StringTokenizer(checkPrefixListStr, ";");
            checkPrefixList.clear();
            while (st.hasMoreTokens()) {
                checkPrefixList.add(st.nextToken());
            }
        }

        System.out.println("ok notCheckURLList size is "+notCheckURLList.size());
        System.out.println("ok checkSuffixList size is "+checkSuffixList.size());
        System.out.println("ok checkPrefixList size is "+checkPrefixList.size());
    }
}

