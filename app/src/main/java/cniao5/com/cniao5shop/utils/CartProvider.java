package cniao5.com.cniao5shop.utils;

import android.content.Context;
import android.util.Log;
import android.util.SparseArray;

import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import cniao5.com.cniao5shop.bean.ShoppingCart;

/**
 * Created by <a href="http://www.cniao5.com">菜鸟窝</a>
 * 一个专业的Android开发在线教育平台
 */
public class CartProvider {



    public static final String CART_JSON="cart_json";
    private static final String TAG = "CartProvider";

    // 类似 hashMap
    private SparseArray<ShoppingCart> datas =null;


    private  Context mContext;


    public CartProvider(Context context){

        mContext = context;
       datas = new SparseArray<>(10);
        listToSparse();

    }



    public void put(ShoppingCart cart){


       ShoppingCart temp =  datas.get(cart.getId().intValue());

        if(temp !=null){
            temp.setCount(temp.getCount()+1);
        }
        else{
            temp = cart;
            temp.setCount(1);
        }

        datas.put(cart.getId().intValue(),temp);

        commit();

    }

    public void update(ShoppingCart cart){

        datas.put(cart.getId().intValue(),cart);
        commit();
    }

    public void delete(ShoppingCart cart){
        datas.delete(cart.getId().intValue());
        commit();
    }

    public List<ShoppingCart> getAll(){

        return  getDataFromLocal();
    }


    public void commit(){


        List<ShoppingCart> carts = sparseToList();

        PreferencesUtils.putString(mContext,CART_JSON,JSONUtil.toJSON(carts));

    }


    private List<ShoppingCart> sparseToList(){


        int size = datas.size();

        List<ShoppingCart> list = new ArrayList<>(size);
        for (int i=0;i<size;i++){

            list.add(datas.valueAt(i));
        }
        return list;

    }

    /**
     * 数据源 放入内存中去
     */

    private void listToSparse(){

        // 本页 数据的list集合
        List<ShoppingCart> carts =  getDataFromLocal();

        if(carts!=null && carts.size()>0){

            for (ShoppingCart cart:
                    carts) {

                datas.put(cart.getId().intValue(),cart);
            }
        }

    }



    public  List<ShoppingCart> getDataFromLocal(){

        String json = PreferencesUtils.getString(mContext,CART_JSON);
        List<ShoppingCart> carts =null;
        if(json !=null ){

            carts = JSONUtil.fromJson(json,new TypeToken<List<ShoppingCart>>(){}.getType());
        }

        return  carts;

    }




}
