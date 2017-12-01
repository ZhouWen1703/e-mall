package com.emall.controller;

import com.emall.common.Const;
import com.emall.pojo.User;
import com.emall.service.iservice.UserService;
import com.emall.common.ServiceResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Administrator on 2017-11-28.
 */
@Controller
@RequestMapping("/user/")
public class UserAction {

    @Resource(name = "userService")
    private UserService userService;


    @RequestMapping(value = "/login.do", method = RequestMethod.POST)
    @ResponseBody
    public ServiceResponse<User> login(@RequestBody User user, HttpSession session) throws UnsupportedEncodingException, NoSuchAlgorithmException {

        ServiceResponse<User> responseResult = userService.login(user.getUsername(), user.getPassword());
        if (responseResult.isSuccess()) {
            session.setAttribute(Const.CURRENT_USER, responseResult.getData());
        }
        User u= responseResult.getData();
        u.setQuestion(StringUtils.EMPTY);
        u.setAnswer(StringUtils.EMPTY);
        return responseResult;

    }

    @RequestMapping(value = "/check_valid.do", method = RequestMethod.POST)
    @ResponseBody
    public ServiceResponse check_valid(String str, String type, User user) {
        if (type.equals("email")) {
            user.setEmail(str);
        } else if (type.equals("username")) {
            user.setUsername(str);
        }else {
            return ServiceResponse.createErrorMessageResponse("参数类型错误");
        }
        ServiceResponse responseResult = userService.checkNameOrEmail(user);
        return responseResult;
    }

    @RequestMapping(value = "/forget_get_question.do", method = RequestMethod.POST)
    @ResponseBody
    public ServiceResponse get_question(User user) {
        ServiceResponse responseResult = userService.question(user);
        return responseResult;
    }

    @RequestMapping(value = "/register.do", method = RequestMethod.POST)
    @ResponseBody
    public ServiceResponse check_valid(User user) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        ServiceResponse responseResult = userService.insert(user);
        return responseResult;
    }

    @RequestMapping(value = "/get_user_info.do", method = RequestMethod.POST)
    @ResponseBody
    public ServiceResponse getUserInfo(HttpSession session) {
        User user=(User) session.getAttribute(Const.CURRENT_USER);
        if (user!=null) {
            user.setQuestion(StringUtils.EMPTY);
            user.setAnswer(StringUtils.EMPTY);
            return ServiceResponse.createSuccessResponse(user);
        }
        return ServiceResponse.createErrorMessageResponse("用户未登录,无法获取当前用户信息");
    }

    @RequestMapping(value = "/forget_check_answer.do", method = RequestMethod.POST)
    @ResponseBody
    public ServiceResponse checkAnswer(String username, String question, String answer) {
        ServiceResponse responseResult = userService.checkAnswer(username, question, answer);
        return responseResult;

    }

    @RequestMapping(value = "/forget_reset_password.do", method = RequestMethod.POST)
    @ResponseBody
    public ServiceResponse resetPassword(String username, String passwordNew, String forgetToken) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        ServiceResponse responseResult = userService.forgetResetPassword(username, passwordNew, forgetToken);
        return responseResult;
    }

    @RequestMapping(value = "/reset_password.do", method = RequestMethod.POST)
    @ResponseBody
    public ServiceResponse forgetResetPassword(String passwordNew, String passwordOld,HttpSession session) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        User user=(User) session.getAttribute(Const.CURRENT_USER);
        ServiceResponse responseResult = userService.resetPassword(user.getId(),passwordNew, passwordOld);
        return responseResult;
    }

    @RequestMapping(value = "/update_information.do", method = RequestMethod.POST)
    @ResponseBody
    public ServiceResponse updateInformation(User user,HttpSession session) {
        User u=(User) session.getAttribute(Const.CURRENT_USER);
        if (u==null){
            return ServiceResponse.createErrorMessageResponse("用户未登录");
        }
        user.setId(u.getId());
        ServiceResponse responseResult = userService.updateInformation(user);
        ServiceResponse response=userService.getInformation(user.getId());
        session.setAttribute(Const.CURRENT_USER,response.getData());
        return responseResult;
    }

    @RequestMapping(value = "/get_information.do", method = RequestMethod.POST)
    @ResponseBody
    public ServiceResponse get_information(HttpSession session)  {
        User u=(User) session.getAttribute(Const.CURRENT_USER);
        if (u==null){
            return ServiceResponse.createErrorCodeMsg(10,"用户未登录,无法获取当前用户信息,status=10,强制登录");
        }
        ServiceResponse responseResult = userService.getInformation(u.getId());

        return responseResult;
    }

    @RequestMapping(value = "/logout.do", method = RequestMethod.POST)
    @ResponseBody
    public ServiceResponse logout(HttpSession session)  {
        User u=(User) session.getAttribute(Const.CURRENT_USER);
        if (u==null){
            return ServiceResponse.createErrorMessageResponse("服务端异常");
        }else {

            session.setAttribute(Const.CURRENT_USER,null);
        return ServiceResponse.createSuccessMessageResponse("退出成功");
        }
    }
}
