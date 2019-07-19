package com.chryl.token;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.chryl.bean.User;
import org.springframework.stereotype.Service;

/**
 * 服务端生成token:
 * token生成:当用户登陆通过,将用户的id放入token中,降password作为秘钥生成token
 * <p>
 * Created By Chr on 2019/7/19.
 */
@Service
public class TokenService {

    public String getToken(User user) {
        String token = "";
        token = JWT.create().withAudience(user.getId())//userId 放到token
                /**
                 * 密码作为秘钥 不太好,建议自己定义
                 */
                .sign(Algorithm.HMAC256(user.getUserPassword()));//password作为token的秘钥
        /**
         * Token认证的五点认识:
         * 一个Token就是一些信息的集合；
         * 在Token中包含足够多的信息，以便在后续请求中减少查询数据库的几率；
         * 服务端需要对cookie和HTTP Authrorization Header进行Token信息的检查；
         * 基于上一点，你可以用一套token认证代码来面对浏览器类客户端和非浏览器类客户端；
         * 因为token是被签名的，所以我们可以认为一个可以解码认证通过的token是由我们系统发放的，其中带的信息是合法有效的；
         */
        return token;
    }

}
