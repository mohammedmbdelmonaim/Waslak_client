package com.unicom.waslak_client.remote;

import com.unicom.waslak_client.model.user.AccountModel;
import com.unicom.waslak_client.model.user.ActivitiesModel;
import com.unicom.waslak_client.model.user.ChangePassModel;
import com.unicom.waslak_client.model.user.ChangePassUser;
import com.unicom.waslak_client.model.user.CreateOrderModel;
import com.unicom.waslak_client.model.user.LogoutModel;
import com.unicom.waslak_client.model.user.NotificationChangeModel;
import com.unicom.waslak_client.model.user.NotificationHistoryModel;
import com.unicom.waslak_client.model.user.NotificationUser;
import com.unicom.waslak_client.model.user.OrderDetailModel;
import com.unicom.waslak_client.model.user.OrderUser;
import com.unicom.waslak_client.model.user.OrdersHistoryModel;
import com.unicom.waslak_client.model.user.ProductsStoreModel;
import com.unicom.waslak_client.model.user.RateModel;
import com.unicom.waslak_client.model.user.RateUser;
import com.unicom.waslak_client.model.user.RequestStore;
import com.unicom.waslak_client.model.user.StoresActivityModel;
import com.unicom.waslak_client.model.user.UpdateOrder;
import com.unicom.waslak_client.model.user.UpdateOrderModel;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface UserService {
    @GET("api/Activity/GetAllActivitys")
    Observable<ActivitiesModel> requestActivities(@Header("Authorization") String token);

    @POST("api/Store/GetActivityStores")
    Observable<StoresActivityModel> storesActivity(@Header("Authorization") String token , @Body RequestStore requestStore);

    @GET("api/Product/GetVendorProductsForCustomer")
    Single<ProductsStoreModel> productsStore(@Header("Authorization") String token , @Query("vendorId") int id);

    @POST("api/Order/CreateOrder")
    Observable<CreateOrderModel> createOrder(@Header("Authorization") String token , @Body OrderUser orderUser);

    @GET("api/Order/GetClientOrders")
    Observable<OrdersHistoryModel> ordersHistory(@Header("Authorization") String token);

    @GET("api/Notification/GetCustomerNotifications")
    Observable<NotificationHistoryModel> notificationsHistory(@Header("Authorization") String token);

    @GET("api/Account/GetCustomerProfile/GetCustomerProfile")
    Single<AccountModel> account(@Header("Authorization") String token);

    @POST("api/Account/CustomerLogout")
    Single<LogoutModel> logout(@Header("Authorization") String token);

    @POST("api/Account/CustomerChangePassword")
    Observable<ChangePassModel> changePass(@Header("Authorization") String token , @Body ChangePassUser changePassUser);

    @POST("api/rate")
    Observable<RateModel> changeRate(@Header("Authorization") String token , @Body RateUser rateUser);

    @GET("api/rate")
    Observable<RateModel> rates(@Header("Authorization") String token ,@Query("storeId") int id);

    @POST("api/Account/UpdateCustomerNotificationStatus")
    Observable<NotificationChangeModel> updateNotification(@Header("Authorization") String token, @Body NotificationUser notificationUser);

    @GET("api/Order/GetOrderDetials")
    Observable<OrderDetailModel> orderDetail(@Header("Authorization") String token , @Query("orderId") int id);

    @POST("api/Order/ChangeOrderStatus")
    Observable<UpdateOrderModel> updateOrder(@Header("Authorization") String token, @Body UpdateOrder updateOrder);

}
