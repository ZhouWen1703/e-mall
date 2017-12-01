package com.emall.controller;

import com.emall.common.ServiceResponse;
import com.emall.service.iservice.CateGoryService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * Created by Administrator on 2017-11-30.
 */
@Controller
@RequestMapping("/category/")
public class CateGoryAction {

    @Resource(name = "cateGoryService")
    private CateGoryService cateGoryService;

    @RequestMapping(value = "get_deep_category",method = RequestMethod.GET)
    @ResponseBody
    public ServiceResponse getDeepCategory(Integer categoryId){
        ServiceResponse responseResult=cateGoryService.getDeepCategory(categoryId);
        return responseResult;
    }
}
