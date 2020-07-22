package com.unicom.waslak_client.repository;

import com.unicom.waslak_client.di.scope.FragmentScope;
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
import com.unicom.waslak_client.remote.UserService;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

@FragmentScope
public class UserRepository {

    private UserService userService;

    @Inject
    public UserRepository(UserService userService) {
        this.userService = userService;
    }

    public Observable<ActivitiesModel> getActivities(String token) {
        return userService.requestActivities(token);
    }

    public Observable<StoresActivityModel> getStoresActivity(String token , RequestStore requestStore){
        return userService.storesActivity(token,requestStore);
    }
    public Single<ProductsStoreModel> getProducts(String token , int storeId){
        return userService.productsStore(token,storeId);
    }

    public Observable<CreateOrderModel> createOrder(String token , OrderUser orderUser){
        return userService.createOrder(token , orderUser);
    }

    public Observable<OrdersHistoryModel> getOrdersHistory(String token){
        return userService.ordersHistory(token);
    }

    public Observable<NotificationHistoryModel> getNotificationsHistory(String token){
        return userService.notificationsHistory(token);
    }

    public Single<AccountModel> getAccount(String token){
        return userService.account(token);
    }

    public Single<LogoutModel> getLogout(String token){
        return userService.logout(token);
    }

    public Observable<NotificationChangeModel> getChangeNotification(String token,NotificationUser notificationUser){
        return userService.updateNotification(token , notificationUser);
    }

    public Observable<ChangePassModel> getChangePass(String token , ChangePassUser changePassUser){
        return userService.changePass(token , changePassUser);
    }

    public Observable<RateModel> getRate(String token , RateUser rateUser){
        return userService.changeRate(token , rateUser);
    }
    public Observable<RateModel> getRates(String token , int store_id){
        return userService.rates(token , store_id);
    }

    public Observable<OrderDetailModel> getOrderDetail(String token , int orderId){
        return userService.orderDetail(token,orderId);
    }

    public Observable<UpdateOrderModel> getUpdateOrder(String token, UpdateOrder updateOrder){
        return userService.updateOrder(token , updateOrder);
    }

}
