package com.emall.service.iservice;

import com.emall.common.ServiceResponse;
import com.emall.pojo.User;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Administrator on 2017-11-28.
 */
public interface UserService {

    /**
     * 验证用户名或邮箱
     * @param user
     * @return
     */
    ServiceResponse checkNameOrEmail(User user);
    /**
     *
     * @param username
     * @param password
     * @return
     */
    ServiceResponse<User> login(String username, String password) throws UnsupportedEncodingException, NoSuchAlgorithmException;

    /**
     * 查找问题
     * @param user
     * @return
     */
    ServiceResponse question(User user);

    /**
     * 注册
     * @param record
     * @return
     */
    ServiceResponse insert(User record) throws UnsupportedEncodingException, NoSuchAlgorithmException;

    /**
     * 通过验证问题答案，查找令牌
     * @param username
     * @param question
     * @param answer
     * @return
     */
    ServiceResponse checkAnswer(String username, String question, String answer);

    /**
     * 更改密码
     * @param username 用户名
     * @param addSaltPassword 新密码
     * @param forgetToken 旧密码
     * @return
     */
    ServiceResponse forgetResetPassword(String username, String addSaltPassword, String forgetToken) throws UnsupportedEncodingException, NoSuchAlgorithmException;

    /**
     * 根据旧密码改密码
     *
     * @param id
     * @param passwordNew
     * @param passwordOld
     * @return
     */
    ServiceResponse resetPassword(Integer id, String passwordNew, String passwordOld) throws UnsupportedEncodingException, NoSuchAlgorithmException;

    /**
     * 登录后更新个人信息
     * @param user
     * @return
     */
    ServiceResponse updateInformation( User user);

    /**
     * 登录后通过id查所有信息
     * @param id
     * @return
     */
    ServiceResponse getInformation(Integer id);
}
