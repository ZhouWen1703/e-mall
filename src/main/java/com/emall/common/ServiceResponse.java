package com.emall.common;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.io.Serializable;

/**
 * Created by Administrator on 2017-11-28.
 */
@JsonSerialize(include= JsonSerialize.Inclusion.NON_NULL)
public class ServiceResponse<T> implements Serializable {

    private String msg;
    private int  status;
    private T data;
    //编写状态枚举类
    @JsonIgnore
    public boolean isSuccess(){
        return this.status==ResponseCode.SUCCESS.getCode();
    }
    //私有区分返回类型
    private ServiceResponse(int status) {
        this.status = status;
    }
    //匹配除了String类型以外的其他类型数据
    private ServiceResponse( int status,T data) {
        this.data = data;
        this.status = status;
    }
    //精确匹配String类型参数
    private ServiceResponse( int status,String msg) {
        this.msg = msg;
        this.status = status;
    }

    private ServiceResponse(int status, String msg,T data) {
        this.data = data;
        this.msg = msg;
        this.status = status;
    }

    //运行成功的返回对象
    public  static <T> ServiceResponse<T> createSuccessResponse(){
        return new ServiceResponse<T>(ResponseCode.SUCCESS.getCode());
    }
    public  static <T> ServiceResponse<T> createSuccessResponse(T data){
        return new ServiceResponse<T>(ResponseCode.SUCCESS.getCode(),data);
    }
    public  static <T> ServiceResponse<T> createSuccessResponse(String msg,T data){
        return new ServiceResponse<T>(ResponseCode.SUCCESS.getCode(),msg,data);
    }
    public  static <T> ServiceResponse<T> createSuccessMessageResponse(String msg){
        return new ServiceResponse<T>(ResponseCode.SUCCESS.getCode(),msg);
    }

    //运行失败的返回对象
    public  static <T> ServiceResponse<T> createErrorResponse(){
        return new ServiceResponse<T>(ResponseCode.ERROR.getCode());
    }
    public  static <T> ServiceResponse<T> createErrorResponse(T data){
        return new ServiceResponse<T>(ResponseCode.ERROR.getCode(),data);
    }
    public  static <T> ServiceResponse<T> createErrorResponse(String msg,T data){
        return new ServiceResponse<T>(ResponseCode.ERROR.getCode(),msg,data);
    }
    public  static <T> ServiceResponse<T> createErrorMessageResponse(String msg){
        return new ServiceResponse<T>(ResponseCode.ERROR.getCode(),msg);
    }
    /*
    * 其他返回类型
    * 例如：
    * ILLEGAL_ARGUMENT(2, "ILLEGAL_ARGUMENT"),
    NEED_LOGIN(20, "NEED_LOGIN");
    设定一个通用的返回应答对象方法
    * */

    public  static <T> ServiceResponse<T> createErrorCodeMsg(int errCode,String errMsg){
        return new ServiceResponse<T>(errCode,errMsg);
    }
    public T getData() {
        return data;
    }

    public String getMsg() {
        return msg;
    }

    public int getStatus() {
        return status;
    }
}
