package com.gr.imserver.feignclient;

import feign.Param;
import feign.RequestLine;

/**
 * @author WSS
 * @descripton
 * @date 2020-04-12 14:50
 */
public interface UserAction {

    @RequestLine("GET /login/{username}/{password}")
    public String loginAction(@Param("username") String username,
                              @Param("password") String password);

    @RequestLine("GET /{userid}")
    public String getById(@Param("userid") Integer userid);

}
