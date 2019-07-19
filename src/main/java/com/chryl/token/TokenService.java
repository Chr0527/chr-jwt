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
        return token;
    }

}
