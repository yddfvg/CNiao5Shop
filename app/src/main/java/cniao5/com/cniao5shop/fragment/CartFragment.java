package cniao5.com.cniao5shop.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.util.List;

import cniao5.com.cniao5shop.MainActivity;
import cniao5.com.cniao5shop.R;
import cniao5.com.cniao5shop.adapter.CartAdapter;
import cniao5.com.cniao5shop.adapter.decoration.DividerItemDecoration;
import cniao5.com.cniao5shop.bean.ShoppingCart;
import cniao5.com.cniao5shop.utils.CartProvider;
import cniao5.com.cniao5shop.widget.CnToolbar;

/**
 * Created by Ivan on 15/9/22.
 */
public class CartFragment extends Fragment implements View.OnClickListener{


    public static final int ACTION_EDIT=1;
    public static final int ACTION_CAMPLATE=2;


    @ViewInject(R.id.recycler_view)
    private RecyclerView mRecyclerView;

    @ViewInject(R.id.checkbox_all)
    private CheckBox mCheckBox;

    @ViewInject(R.id.txt_total)
    private TextView mTextTotal;

    @ViewInject(R.id.btn_order)
    private Button mBtnOrder;

    @ViewInject(R.id.btn_del)
    private Button mBtnDel;

    private CnToolbar mToolbar;


    private CartAdapter mAdapter;
    private CartProvider cartProvider;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {



        View view =   inflater.inflate(R.layout.fragment_cart,container,false);

        ViewUtils.inject(this,view);

        cartProvider = new CartProvider(getContext());

        showData();
        
        return  view;

    }


    @OnClick(R.id.btn_del)
    public void delCart(View view){

        mAdapter.delCart();
    }


    private void showData(){

        List<ShoppingCart> carts = cartProvider.getAll();

        mAdapter = new CartAdapter(getContext(),carts,mCheckBox,mTextTotal);

        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL_LIST));

    }



    public void refData(){

        mAdapter.clear();
        List<ShoppingCart> carts = cartProvider.getAll();
        mAdapter.addData(carts);
        mAdapter.showTotalPrice();

    }


    @Override
    public void onAttach(Context context) {

        super.onAttach(context);
        if(context instanceof MainActivity){

            MainActivity activity = (MainActivity) context;

            mToolbar = (CnToolbar) activity.findViewById(R.id.toolbar);

            changeToolbar();


        }

    }

    public void changeToolbar(){

        mToolbar.hideSearchView();
        mToolbar.showTitleView();
        mToolbar.setTitle(R.string.cart);
        mToolbar.getRightButton().setVisibility(View.VISIBLE);
        mToolbar.setRightButtonText("编辑");

        mToolbar.getRightButton().setOnClickListener(this);

        mToolbar.getRightButton().setTag(ACTION_EDIT);


    }


    private void showDelControl(){
        mToolbar.getRightButton().setText("完成");
        mTextTotal.setVisibility(View.GONE);
        mBtnOrder.setVisibility(View.GONE);
        mBtnDel.setVisibility(View.VISIBLE);
        mToolbar.getRightButton().setTag(ACTION_CAMPLATE);

        mAdapter.checkAll_None(false);
        mCheckBox.setChecked(false);

    }

    private void  hideDelControl(){

        mTextTotal.setVisibility(View.VISIBLE);
        mBtnOrder.setVisibility(View.VISIBLE);


        mBtnDel.setVisibility(View.GONE);
        mToolbar.setRightButtonText("编辑");
        mToolbar.getRightButton().setTag(ACTION_EDIT);

        mAdapter.checkAll_None(true);
        mAdapter.showTotalPrice();

        mCheckBox.setChecked(true);
    }


    @Override
    public void onClick(View v) {



        int action = (int) v.getTag();
        if(ACTION_EDIT == action){

            showDelControl();
        }
        else if(ACTION_CAMPLATE == action){

            hideDelControl();
        }


    }
}
