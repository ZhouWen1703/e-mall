package com.emall.service.Impl;

import com.emall.common.Const;
import com.emall.common.ServiceResponse;
import com.emall.dao.UserMapper;
import com.emall.pojo.User;
import com.emall.service.iservice.UserService;
import com.emall.util.AddSalt;
import com.emall.util.TokenCache;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Administrator on 2017-11-28.
 */
@Service(value = "userService")
@RequestMapping
public class UserServiceImpl implements UserService {

    @Resource(name = "userMapper")
    private UserMapper userMapper;

    @Override
    public ServiceResponse<User> checkNameOrEmail(User user) {
        User u = userMapper.findAll(user);
        if (u == null) {
            return ServiceResponse.createSuccessMessageResponse("校验成功");
        }
        return ServiceResponse.createErrorMessageResponse("用户名已存在");
    }

    @Override
    public ServiceResponse<User> login(String username, String password) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        //1-验证用户名是否存在，不存在，返回错误
        int resultCount = userMapper.checkUsername(username);
        if (resultCount == 0) {
            return ServiceResponse.createErrorMessageResponse("用户名不存在");
        }
        //TODO 2-对password进行MD5加密处理
        String addSaltPassword = AddSalt.EncoderByMd5(password);
        //3-登录验证
        User user = userMapper.login(username, addSaltPassword);
        if (user == null) {
            //用户已经提前验证过，因此，只可能是密码错误
            return ServiceResponse.createErrorMessageResponse("密码错误");
        }
        //4-如果登录成功，返回数据中不应该包含密码，因此对密码进行处理
        //应用了 org.apache.commons.lang3.StringUtils工具类，返回空字符串""
        user.setPassword(StringUtils.EMPTY);
        return ServiceResponse.createSuccessResponse("登录成功", user);
    }

    @Override
    public ServiceResponse question(User user) {
        User u = userMapper.findAll(user);
        if (u == null) {
            return ServiceResponse.createErrorMessageResponse("该用户未设置找回密码问题");
        }
        return ServiceResponse.createSuccessResponse(u.getQuestion());
    }

    @Override
    public ServiceResponse insert(User user) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        //对密码进行加密
        String addSaltPassword = AddSalt.EncoderByMd5(user.getPassword());
        user.setPassword(addSaltPassword);
        int rs = userMapper.checkUsername(user.getUsername());
        if (rs > 0) {
            return ServiceResponse.createErrorMessageResponse("用户名已存在");
        }
        if (user.getEmail() != null) {
            int rs2 = userMapper.checkEmail(user.getEmail());
            if (rs2 > 0) {
                return ServiceResponse.createErrorMessageResponse("邮箱已存在");
            }
        }
            user.setRole(Const.Role.ROLE_USER);
            int result = userMapper.insert(user);
            if (result == 0) {
                return ServiceResponse.createErrorMessageResponse("注册失败");
            }
            return ServiceResponse.createSuccessMessageResponse("校验成功");
        }


    @Override
    public ServiceResponse checkAnswer(String username, String question, String answer) {
        //封装
        User user = new User();
        user.setUsername(username);
        user.setQuestion(question);
        user.setAnswer(answer);
        //查找
        User u = userMapper.findAll(user);
        if (u==null){
            return ServiceResponse.createErrorMessageResponse("问题答案错误");
        }
        String forgetToken= UUID.randomUUID().toString();
        TokenCache.setKey("token_"+username,forgetToken);
        return ServiceResponse.createSuccessResponse(forgetToken);
    }

    @Override
    public ServiceResponse forgetResetPassword(String username, String passwordNew, String forgetToken) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        //1-校验令牌是否存在
        if (StringUtils.isBlank(forgetToken)){
            return ServiceResponse.createErrorMessageResponse("参数错误，需要令牌");
        }

        //2-判断用户名是否存在
        int rs=userMapper.checkUsername(username);
        if (rs==0){
            return ServiceResponse.createErrorMessageResponse("用户名不存在");
        }
        //3-从guova缓存中获取Token令牌进行非空校验
        String token = TokenCache.getKey(TokenCache.TOKEN_PREFIX+username);
        if (StringUtils.isBlank(token)){
            return ServiceResponse.createErrorMessageResponse("token令牌无效或已过期");
        }
        if (StringUtils.equals(token,forgetToken)){
        //加盐
        String addSaltPassword = AddSalt.EncoderByMd5(passwordNew);
        int result=userMapper.forgetResetPassword(username,addSaltPassword);
        if (result==0){
            return ServiceResponse.createErrorMessageResponse("修改密码操作失效");
        }
        }else {
            ServiceResponse.createErrorMessageResponse("token无效");
        }
        //修改密码
        return ServiceResponse.createSuccessMessageResponse("修改密码成功");
    }

    @Override
    public ServiceResponse resetPassword(Integer id,String passwordNew, String passwordOld) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        //验证旧密码
        String password = AddSalt.EncoderByMd5(passwordOld);
        int rs=userMapper.checkPasswordOld(password,id);
        if (rs==0){
            return ServiceResponse.createErrorMessageResponse("旧密码输入错误");
        }
        //加盐
        String addSaltPassword = AddSalt.EncoderByMd5(passwordNew);
        int result=userMapper.resetPassword(addSaltPassword,password);
        if (result==0){
            return ServiceResponse.createErrorMessageResponse("修改密码失败");
        }
        return ServiceResponse.createSuccessMessageResponse("修改密码成功");
    }

    @Override
    public ServiceResponse updateInformation( User user) {
        //验证邮箱
        int rs=userMapper.checkEmail(user.getEmail());
        if (rs!=0){
            return ServiceResponse.createErrorMessageResponse("邮箱已存在");
        }
        //更新
        int result=userMapper.updateByPrimaryKeySelective(user);
        if (result==0){
            return ServiceResponse.createErrorMessageResponse("更新个人信息失败");
        }
        return ServiceResponse.createSuccessMessageResponse("更新个人信息成功");
    }

    @Override
    public ServiceResponse getInformation(Integer id) {
        User user = userMapper.selectByPrimaryKey(id);
        if(user!=null){
        user.setPassword(StringUtils.EMPTY);
        }
        return ServiceResponse.createSuccessResponse(user);
    }
}
