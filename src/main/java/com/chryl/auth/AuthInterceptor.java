package com.chryl.auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.chryl.annotation.PassToken;
import com.chryl.annotation.UserLoginToken;
import com.chryl.bean.User;
import com.chryl.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.invoke.MethodHandle;
import java.lang.reflect.Method;

/**
 * 是否需要权限验证:
 * (1)有userLoginToken注解需要验证
 * (2)有PassToken注解直接放行
 * <p>
 * Created By Chr on 2019/7/19.
 */
public class AuthInterceptor implements HandlerInterceptor {

    @Autowired
    private UserMapper userMapper;

    //执行方法前
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //从http请求头 去除token
        String token = request.getHeader("token");
        //不是映射到方法,为静态资源请求
        if (!(handler instanceof MethodHandle)) {
            return true;
        }
        //判断方法是否是登陆方法:passToken
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        if (method.isAnnotationPresent(PassToken.class)) {//如果是请求的登陆方法,返回true
            PassToken passToken = method.getAnnotation(PassToken.class);
            if (passToken.required()) {
                return true;
            }
        }
        //判断是否需要权限验证的方法:userLoginToken
        if (method.isAnnotationPresent(UserLoginToken.class)) {//判断是否有UserLoginToken注解
            UserLoginToken userLoginToken = method.getAnnotation(UserLoginToken.class);
            if (userLoginToken.required()) {
                //执行认证
                if (token == null) {
                    throw new RuntimeException("无 token 无法登陆");
                }
                //获取 token 中的 userid
                String useId = null;
                try {
                    useId = JWT.decode(token).getAudience().get(0);
                } catch (JWTDecodeException j) {
                    j.printStackTrace();
                }
                /**
                 * 这里模拟数据库 查询到信息
                 */
                //模拟 数据库 查询 :userService.selectByUserId(userId)
                User user = userMapper.selectByUserId(useId);
                if (user == null) {
                    throw new RuntimeException(" 用户 不  存在 ");
                }
                /**
                 * 验证token,尽量 不要用密码这种 敏感字段 作为token
                 */
                //验证 token
                JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(user.getUserPassword())).build();
                //验证token的正确性
                try {
                    jwtVerifier.verify(token);
                } catch (JWTVerificationException e) {
                    throw new RuntimeException("401");
                }
                return true;
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
