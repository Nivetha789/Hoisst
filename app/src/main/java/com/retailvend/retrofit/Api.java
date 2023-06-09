package com.retailvend.retrofit;

import com.retailvend.model.SendOtpModel;
import com.retailvend.model.VerifyOtpModel;
import com.retailvend.model.collateralsDetails.CollateralsDetailsModel;
import com.retailvend.model.collateralsmodule.CollateralsListResModel;
import com.retailvend.model.changePassword.ChangePasswordModel;
import com.retailvend.model.collectionmodel.CollectionDetailsListModel;
import com.retailvend.model.createOutSales.CreateOutSalesModel;
import com.retailvend.model.createOutSales.collectionModel.CollectionResModel;
import com.retailvend.model.createOutletModule.CreateOutletBeatListModel;
import com.retailvend.model.dashboard.SalesDashboardCountModel;
import com.retailvend.model.delManModels.delCollection.DeliveryCollectionListModel;
import com.retailvend.model.delManModels.delCollection.DetailOutletInvAmntBillModel;
import com.retailvend.model.delManModels.delCollection.addpayment.AddPaymentModel;
import com.retailvend.model.delManModels.delCollection.delManDeliDetails.DelManDelDetailsModel;
import com.retailvend.model.delManModels.delCollection.invoiceDetails.InvoiceDetailsModel;
import com.retailvend.model.delManModels.delCollection.outstand.OutstandModel;
import com.retailvend.model.delManModels.delCollection.paymentCollection.InvoiceTypeModel;
import com.retailvend.model.delManModels.delCollection.paymentCollection.PaymentCollectionModel;
import com.retailvend.model.delManModels.delCollection.paymentCollection.PaymentTypeModel;
import com.retailvend.model.delManModels.delCollection.paymentHistory.PaymentHistoryModel;
import com.retailvend.model.delManModels.delCollection.todayOutletsDetails.TodayOutletDetailsModel;
import com.retailvend.model.delManModels.delCollection.todayOutletsDetails.UpdateBillModel;
import com.retailvend.model.delManModels.delCollection.todayOutletsModel.DeliveryTodayOutletsModel;
import com.retailvend.model.endTempSales.EndTempModel;
import com.retailvend.model.invoiceListModel.InvoiceListModel;
import com.retailvend.model.login.LoginResModel;
import com.retailvend.model.loyalty.LoyaltyModel;
import com.retailvend.model.manageorder.OrderListModel;
import com.retailvend.model.noreasonOutlet.NoReasonMessageModel;
import com.retailvend.model.order.CreateOrderModel;
import com.retailvend.model.outletBalanceModule.OutletBalanceModel;
import com.retailvend.model.outlets.AddAttendanceModel;
import com.retailvend.model.outlets.AssignOutletsModel;
import com.retailvend.model.outlets.AttendanceTypeModel;
import com.retailvend.model.outlets.ProductNameResModel;
import com.retailvend.model.outlets.ProductTypeModel;
import com.retailvend.model.outlets.SalesAgentsListModel;
import com.retailvend.model.outlets.outletHistory.OutletHistoryModel;
import com.retailvend.model.outlets.productDetailsModel.ProductDetailsModel;
import com.retailvend.model.sales.SalesDetailsModel;
import com.retailvend.model.startTempSales.StartTempModel;
import com.retailvend.model.targetDetailssales.EmployeeTargetDetailsData.EmployeeTargetDetailsModel;
import com.retailvend.model.targetDetailssales.TargetDetailsModel;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

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
            @Field("offset") int offset,
            @Field("limit") int limit,
            @Field("employee_id") String emp_id,
            @Field("search") String search);

    //attendance list
    @FormUrlEncoded
    @POST("attendance/api/attendance_type")
    Call<AttendanceTypeModel> attendanceList(
            @Field("method") String method);

    //product name list
    @FormUrlEncoded
    @POST("catlog/api/product")
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
            @Field("due_days") String due_days,
            @Field("discount") String discount,
            @Field("order_type") String order_type,
            @Field("att_id") String att_id,
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
            @Field("attendance_id") String attendance_id,
            @Field("order_id") String order_id
    );

    //add attendance
    @Multipart
    @POST("attendance/api/add_attendance")
    Call<AddAttendanceModel> addAttendance(
            @Part("method") RequestBody method,
            @Part("employee_id") RequestBody employee_id,
            @Part("store_id") RequestBody store_id,
            @Part("latitude") RequestBody latitude,
            @Part("longitude") RequestBody longitude,
            @Part("upload_status") RequestBody upload_status,
            @Part MultipartBody.Part image
    );

    //product type
    @FormUrlEncoded
    @POST("catlog/api/productType")
    Call<ProductTypeModel> productType(
            @Field("method") String method,
            @Field("product_id") String product_id

    );

    //Detail product type
    @FormUrlEncoded
    @POST("catlog/api/productType")
    Call<ProductDetailsModel> productTypeDetails(
            @Field("method") String method,
            @Field("state_id") String state_id,
            @Field("city_id") String city_id,
            @Field("zone_id") String zone_id,
            @Field("outlet_id") String outlet_id,
            @Field("type_id") String type_id,
            @Field("view_type") String view_type

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
            @Field("limit") int limit,
            @Field("search") String search
    );

    //orderList
    @FormUrlEncoded
    @POST("order/api/invoice_manage_order")
    Call<InvoiceListModel> delManOrderList(
            @Field("method") String method,
            @Field("employee_id") String employee_id,
            @Field("offset") int offset,
            @Field("limit") int limit,
            @Field("search") String search
    );

    //sales details
    @FormUrlEncoded
    @POST("order/api/manage_order")
    Call<SalesDetailsModel> salesDetails(
            @Field("method") String method,
            @Field("random_value") String random_value
    );

    //no reason
    @FormUrlEncoded
    @POST("master/api/message")
    Call<NoReasonMessageModel> noReason(
            @Field("method") String method
    );

    //dashboard count
    @FormUrlEncoded
    @POST("login/api/employee_login")
    Call<SalesDashboardCountModel> dashboardCount(
            @Field("method") String method,
            @Field("employee_id") String employee_id
    );

    //change password
    @FormUrlEncoded
    @POST("login/api/employee_login")
    Call<ChangePasswordModel> changePassword(
            @Field("method") String method,
            @Field("employee_id") String employee_id,
            @Field("new_pword") String new_pword,
            @Field("c_pword") String c_pword
    );

    //outlet history
    @FormUrlEncoded
    @POST("attendance/api/outlet_history")
    Call<OutletHistoryModel> getOutletHistory(
            @Field("method") String method,
            @Field("outlet_id") String outlet_id
    );

    //target details
    @FormUrlEncoded
    @POST("login/api/employee_login")
    Call<TargetDetailsModel> targetDetails(
            @Field("method") String method,
            @Field("employee_id") String employee_id
    );

    //target details Data
    @FormUrlEncoded
    @POST("login/api/employee_login")
    Call<EmployeeTargetDetailsModel> employeeTargetDetails(
            @Field("method") String method,
            @Field("employee_id") String employee_id
    );


    //start temp
    @FormUrlEncoded
    @POST("login/api/employee_login")
    Call<StartTempModel> startTemp(
            @Field("method") String method,
            @Field("employee_id") String employee_id
    );

    //end temp
    @FormUrlEncoded
    @POST("login/api/employee_login")
    Call<EndTempModel> endTemp(
            @Field("method") String method,
            @Field("employee_id") String employee_id
    );

    //create outlet beat list
    @FormUrlEncoded
    @POST("assignshop/api/employee_wise_shop")
    Call<CreateOutletBeatListModel> createOutletBeat(
            @Field("method") String method,
            @Field("employee_id") String employee_id
    );

    //create outlet
    @FormUrlEncoded
    @POST("outlets/api/outlets")
    Call<CreateOutSalesModel> createOutletDist(
            @Field("method") String method,
            @Field("employee_id") String employee_id,
            @Field("store_name") String store_name,
            @Field("contact_name") String contact_name,
            @Field("mobile") String mobile,
            @Field("address") String address,
            @Field("state_id") String state_id,
            @Field("city_id") String city_id,
            @Field("zone_id") String zone_id,
            @Field("latitude") String latitude,
            @Field("longitude") String longitude
    );

    //create outlet collection list
    @FormUrlEncoded
    @POST("executive-collection/api/invoice_list")
    Call<CollectionResModel> collectionSales(
            @Field("method") String method,
            @Field("offset") int offset,
            @Field("limit") int limit,
            @Field("outlet_id") String outlet_id
//            @Field("search") String search
    );

    //collateral list
    @FormUrlEncoded
    @POST("collaterals/api/collaterals_list")
    Call<CollateralsListResModel> collateralsList(
            @Field("method") String method,
            @Field("offset") int offset,
            @Field("limit") int limit,
            @Field("search") String search);

    //collateral details
    @FormUrlEncoded
    @POST("collaterals/api/collaterals_list")
    Call<CollateralsDetailsModel> collateralsListDetails(
            @Field("method") String method,
            @Field("random_val") String random_val);

     //loyalty details
    @FormUrlEncoded
    @POST("loyalty/api/outlet-loyalty")
    Call<LoyaltyModel> loyaltyDetails(
            @Field("method") String method,
            @Field("outlet_id") String outlet_id,
            @Field("offset") int offset,
            @Field("limit") int limit,
            @Field("search") String search);

    //Outlet balance list
    @FormUrlEncoded
    @POST("outlets/api/outletPayment")
    Call<OutletBalanceModel> outletBalance(
            @Field("method") String method,
            @Field("employee_id") String employee_id,
            @Field("offset") int offset,
            @Field("limit") int limit,
            @Field("search") String search);

    //send otp
    @FormUrlEncoded
    @POST("outlet-order/api/outlet_otp")
    Call<SendOtpModel> sendOtp(
            @Field("method") String method,
            @Field("outlet_id") String outlet_id,
            @Field("employee_id") String employee_id,
            @Field("otp_value") String otp_value);

    //verify otp
    @FormUrlEncoded
    @POST("outlet-order/api/outlet_otp")
    Call<VerifyOtpModel> verifyOtp(
            @Field("method") String method,
            @Field("outlet_id") String outlet_id,
            @Field("employee_id") String employee_id,
            @Field("otp_value") String otp_value);





    /*Delivery man*/

    //Today outlets api
    @FormUrlEncoded
    @POST("assigninvoice/api/employee_wise_shop")
    Call<DeliveryTodayOutletsModel> assignDelManShop(
            @Field("method") String method,
            @Field("employee_id") String employee_id,
            @Field("offset") int offset,
            @Field("limit") int limit,
            @Field("search") String search
    );

    //Today outlets details api
    @FormUrlEncoded
    @POST("order/api/invoice_manage_order")
    Call<TodayOutletDetailsModel> delManOutletInvoice(
            @Field("method") String method,
            @Field("employee_id") String employee_id,
            @Field("random_value") String random_value
    );

    //update status
    @FormUrlEncoded
    @POST("assigninvoice/api/employee_wise_shop")
    Call<UpdateBillModel> delManOutletInvoiceUpdateStatus(
            @Field("method") String method,
            @Field("employee_id") String employee_id,
            @Field("random_value") String random_value,
            @Field("progress") String progress
    );

    //Collection api
    @FormUrlEncoded
    @POST("distributors/api/distributor_outlet_list")
    Call<DeliveryCollectionListModel> collectionList(
            @Field("method") String method,
            @Field("distributor_id") String distributor_id,
            @Field("offset") int offset,
            @Field("limit") int limit
    );

    //Delivery details
    @FormUrlEncoded
    @POST("assigninvoice/api/employee_wise_shop")
    Call<DelManDelDetailsModel> deliveryManDelList(
            @Field("method") String method,
            @Field("employee_id") String employee_id,
            @Field("offset") int offset,
            @Field("limit") int limit,
            @Field("search") String search
    );

    //Invoice details
    @FormUrlEncoded
    @POST("order/api/invoice_manage_order")
    Call<InvoiceDetailsModel> invoiceDetails(
            @Field("method") String method,
            @Field("random_value") String random_value
    );

    //Collection payment
    @FormUrlEncoded
    @POST("payment/api/outlet_payment")
    Call<PaymentCollectionModel> collectionPaymentList(
            @Field("method") String method,
            @Field("assign_id") String assign_id,
            @Field("offset") int offset,
            @Field("limit") int limit
    );


    //payment type
    @FormUrlEncoded
    @POST("payment/api/outlet_payment")
    Call<PaymentTypeModel> paymentTypeGet(
            @Field("method") String method
    );

    //get Invoice
    @FormUrlEncoded
    @POST("payment/api/outlet_payment")
    Call<InvoiceTypeModel> listOutletPaymentBill(
            @Field("method") String method,
            @Field("assign_id") String assign_id
    );

    //get Invoice base amount
    @FormUrlEncoded
    @POST("payment/api/outlet_payment")
    Call<DetailOutletInvAmntBillModel> detailOutletPaymentBill(
            @Field("method") String method,
            @Field("payment_id") String payment_id
    );

    //add payment
    @FormUrlEncoded
    @POST("payment/api/outlet_payment")
    Call<AddPaymentModel> addPayment(
            @Field("method") String method,
            @Field("assign_id") String assign_id,
            @Field("pay_id") String pay_id,
            @Field("employee_id") String employee_id,
            @Field("distributor_id") String distributor_id,
            @Field("outlet_id") String outlet_id,
            @Field("amount") String amount,
            @Field("discount") String discount,
            @Field("amt_type") String amt_type,
            @Field("description") String description,
            @Field("bank_name") String bank_name,
            @Field("cheque_no") String cheque_no,
            @Field("entry_type") String entry_type,
            @Field("entry_date") String entry_date,
            @Field("cheque_date") String cheque_date,
            @Field("collect_date") String collect_date
    );

    //Outstand List
    @FormUrlEncoded
    @POST("distributors/api/distributor_outlet_list")
    Call<OutstandModel> outstandList(
            @Field("method") String method,
            @Field("distributor_id") String distributor_id,
            @Field("offset") int offset,
            @Field("limit") int limit
    );

    //History List
    @FormUrlEncoded
    @POST("payment/api/outlet_payment")
    Call<PaymentHistoryModel> paymentHistory(
            @Field("method") String method,
            @Field("assign_id") String assign_id,
            @Field("offset") int offset,
            @Field("limit") int limit
    );

    //Collection Details
    @FormUrlEncoded
    @POST("login/api/employee_login")
    Call<CollectionDetailsListModel> getCollectionDetails(
            @Field("method") String method,
            @Field("outlet_id") String outlet_id,
            @Field("offset") int offset,
            @Field("limit") int limit
    );

}
