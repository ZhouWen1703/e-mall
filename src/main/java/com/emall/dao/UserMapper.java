package com.emall.dao;

import com.emall.pojo.User;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    /**
     * 按任意条件查询所有
     * @param user
     * @return
     */
    User findAll(User user);

    /**
     *验证登录时用户名是否存在
     * @param username
     * @return
     */
    int checkUsername(String username);

    /**
     * 验证用户名及密码
     * @param username
     * @param password
     * @return
     */
    User login(@Param("username") String username,@Param("password") String password);

    /**
     *
     * @param username
     * @param addSaltPassword
     * @return
     */
    int forgetResetPassword(@Param("username")String username,@Param("passwordNew")String addSaltPassword);

    /**
     * 已登录修改密码
     * @param addSaltPassword
     * @param password
     * @return
     */
    int resetPassword(@Param("passwordNew")String addSaltPassword,@Param("password") String password);

    /**
     * 检测旧密码是否正确
     * @param password
     * @param id
     * @return
     */
    int checkPasswordOld(@Param("password")String password, @Param("id")Integer id);

    /**
     * 检测邮箱是否存在
     * @param email
     * @return
     */
    int checkEmail(String email);
}