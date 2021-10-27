package com.retailvend.retrofit;

import com.retailvend.model.login.LoginResModel;
import com.retailvend.model.manageorder.OrderListModel;
import com.retailvend.model.order.CreateOrderModel;
import com.retailvend.model.outlets.AddAttendanceModel;
import com.retailvend.model.outlets.AssignOutletsModel;
import com.retailvend.model.outlets.AttendanceTypeModel;
import com.retailvend.model.outlets.ProductNameResModel;
import com.retailvend.model.outlets.ProductTypeModel;
import com.retailvend.model.outlets.SalesAgentsListModel;
import com.retailvend.model.sales.SalesDetailsModel;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface Api {

    //login
    @FormUrlEncoded
    @POST("login/api/employee_login")
    Call<LoginResModel> userlogin(
            @Field("method") String method,
            @Field("username") String username,
            @Field("password") String password);

    //today outlet list
    @FormUrlEncoded
    @POST("assignshop/api/employee_wise_shop")
    Call<AssignOutletsModel> todayOutletList(
            @Field("method") String method,
            @Field("employee_id") String emp_id);

    //attendance list
    @FormUrlEncoded
    @POST("attendance/api/attendance_type")
    Call<AttendanceTypeModel> attendanceList(
            @Field("method") String method);

    //product name list
    @FormUrlEncoded
    @POST("order/api/manage_items")
    Call<ProductNameResModel> getProductName(
            @Field("method") String method,
            @Field("order_type") String order_type,
            @Field("offset") int offset,
            @Field("limit") int limit,
            @Field("search") String search
            );

    //create order
    @FormUrlEncoded
    @POST("order/api/create_order")
    Call<CreateOrderModel> createOrder(
            @Field("method") String method,
            @Field("employee_id") String employee_id,
            @Field("store_id") String store_id,
            @Field("bill_type") String bill_type,
            @Field("order_type") String order_type,
            @Field("salesagents_id") String salesagents_id,
            @Field("sales_order") String sales_order);

    //update attendance
    @FormUrlEncoded
    @POST("attendance/api/add_attendance")
    Call<AddAttendanceModel> updateAttendance(
            @Field("method") String method,
            @Field("employee_id") String employee_id,
            @Field("store_id") String store_id,
            @Field("latitude") String latitude,
            @Field("longitude") String longitude,
            @Field("attendance_type") String attendance_type,
            @Field("reason") String reason,
            @Field("attendance_id") String attendance_id
    );

    //add attendance
    @FormUrlEncoded
    @POST("attendance/api/add_attendance")
    Call<AddAttendanceModel> addAttendance(
            @Field("method") String method,
            @Field("employee_id") String employee_id,
            @Field("store_id") String store_id,
            @Field("latitude") String latitude,
            @Field("longitude") String longitude
    );

    //product type
    @FormUrlEncoded
    @POST("catlog/api/productType")
    Call<ProductTypeModel> productType(
            @Field("method") String method,
            @Field("product_id") String product_id
    );

    //sales agent types
    @FormUrlEncoded
    @POST("salesagents/api/salesagents")
    Call<SalesAgentsListModel> salesAgentsType(
            @Field("method") String method,
            @Field("offset") int offset,
            @Field("limit") int limit,
            @Field("search") String search
    );

    //orderList
    @FormUrlEncoded
    @POST("order/api/manage_order")
    Call<OrderListModel> orderList(
            @Field("method") String method,
            @Field("employee_id") String employee_id,
            @Field("offset") int offset,
            @Field("limit") int limit
    );

    //sales details
    @FormUrlEncoded
    @POST("order/api/manage_order")
    Call<SalesDetailsModel> salesDetails(
            @Field("method") String method,
            @Field("order_id") String order_id
    );
}
