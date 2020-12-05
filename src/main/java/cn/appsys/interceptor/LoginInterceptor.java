package cn.appsys.interceptor;

import cn.appsys.pojo.BackendUser;
import cn.appsys.pojo.DevUser;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LoginInterceptor extends HandlerInterceptorAdapter {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {

        HttpSession session = request.getSession();

        BackendUser backendUser = (BackendUser)session.getAttribute("userSession");
        DevUser devUser = (DevUser)session.getAttribute("devUserSession");

        if(null != devUser){ //dev SUCCESS
            return true;
        }else if(null != backendUser){ //backend SUCCESS
            return true;
        }else{
            response.sendRedirect(request.getContextPath()+"/403.jsp");
            return false;
        }

    }



}
