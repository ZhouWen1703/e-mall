package com.emall.service.Impl;

import com.emall.common.ServiceResponse;
import com.emall.dao.CategoryMapper;
import com.emall.service.iservice.CateGoryService;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017-11-30.
 */
@Service("cateGoryService")
@RequestMapping
public class CateGoryServiceImpl implements CateGoryService{

    @Resource(name = "categoryMapper")
    private CategoryMapper categoryMapper;
    @Override
    public ServiceResponse getDeepCategory(Integer categoryId) {
//        int result=categoryMapper.checkCategoryId(categoryId);
//        if(result==0){
//            return null;
//        }
        List<Integer> list=categoryMapper.getDeepCategory(categoryId);

        return ServiceResponse.createSuccessResponse(list);
    }
}
