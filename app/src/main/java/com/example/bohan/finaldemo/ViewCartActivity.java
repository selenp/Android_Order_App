package com.example.bohan.finaldemo;

/**
 * Created by bohan on 11/29/17.
 */

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bohan.finaldemo.adapter.cartAdapter;
import com.example.bohan.finaldemo.entity.Customer;
import com.example.bohan.finaldemo.entity.GoodsInfo;
import com.example.bohan.finaldemo.entity.Order;
import com.example.bohan.finaldemo.entity.OrderItem;
import com.example.bohan.finaldemo.entity.StoreInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ViewCartActivity extends AppCompatActivity implements cartAdapter.CheckInterface,
        cartAdapter.ModifyCountInterface, cartAdapter.GroupEdtorListener{
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.subtitle)
    TextView subtitle;
    @BindView(R.id.top_bar)
    LinearLayout topBar;
    @BindView(R.id.exListView)
    ExpandableListView exListView;
    @BindView(R.id.tv_total_price)
    TextView tvTotalPrice;
    @BindView(R.id.all_chekbox)
    CheckBox allChekbox;
    @BindView(R.id.tv_delete)
    TextView tvDelete;
    @BindView(R.id.tv_go_to_pay)
    TextView tvGoToPay;

    @BindView(R.id.ll_shar)
    LinearLayout llShar;
    @BindView(R.id.ll_info)
    LinearLayout llInfo;

    @BindView(R.id.tv_share)
    TextView tvShare;
    @BindView(R.id.tv_save)
    TextView tvSave;
    @BindView(R.id.ll_cart)
    LinearLayout llCart;
    @BindView(R.id.layout_cart_empty)
    LinearLayout cart_empty;
    private Context context;
    private List<StoreInfo> groups = new ArrayList<StoreInfo>();// 组元素数据列表
    private Map<String, List<GoodsInfo>> children = new HashMap<String, List<GoodsInfo>>();// 子元素数据列表
    private cartAdapter cartadapter;
    private double totalPrice = 0.00;// 购买的商品总价
    private int totalCount = 0;// 购买的商品总数量
    public int flag = 0;//用于
    private DatabaseReference mDatabase;//use firebase
    private int orderId;
    FirebaseDatabase database;
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_view_cart);
        context = this;
        initDatas();
        ButterKnife.bind(this);
        initEvents();
        readAndStoreOrderId();
//        mDatabase = FirebaseDatabase.getInstance().getReference();
//        FirebaseStorage firebaseStorage=FirebaseStorage.getInstance();
//        StorageReference storageRef=firebaseStorage.getReferenceFromUrl("gs://ruixinli-43b20.appspot.com").child("burger.jpeg");
//        try {
//            final File localFile = File.createTempFile("image", "jpeg");
//            storageRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
//                @Override
//                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
//                    Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
//                    back.setImageBitmap(bitmap);
//                }
//            }).addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception exception) {
//                }
//            });
//        } catch (IOException e ) {
//            System.out.print("");
//        }

    }
    private void initDatas(){


        int[] img = {R.drawable.burger, R.drawable.frenchfries, R.drawable.onionrings, R.drawable.pizza};
        String[] groupnames={"Burgers","Pizzas","French Fries","Onion Rings"};
        for (int i = 0; i < 4; i++) {
            groups.add(new StoreInfo(i + "",  (groupnames[i]) + ":"));
            List<GoodsInfo> products = new ArrayList<GoodsInfo>();
            for (int j = 0; j <= i; j++) {
                products.add(new GoodsInfo(j + "", groupnames[i]+(j+1)+"", groups.get(i)
                        .getName() + "的第" + (j + 1) + "个商品", 12.00 + new Random().nextInt(23), new Random().nextInt(5) + 1, "1", img[i], 6.00 + new Random().nextInt(13)));
            }
            children.put(groups.get(i).getId(), products);// 将组元素的一个唯一值，这里取Id，作为子元素List的Key
        }
    }
    private void initEvents(){
        cartadapter = new cartAdapter(groups, children, this);
        cartadapter.setCheckInterface(this);// 关键步骤1,设置复选框接口
        cartadapter.setModifyCountInterface(this);// 关键步骤2,设置数量增减接口
        cartadapter.setmListener(this);
        exListView.setAdapter(cartadapter);
        for (int i = 0; i < cartadapter.getGroupCount(); i++) {
            exListView.expandGroup(i);// 关键步骤3,初始化时，将ExpandableListView以展开的方式呈现
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        setCartNum();
    }
    private void setCartNum() {
        int count = 0;
        for (int i = 0; i < groups.size(); i++) {//遍历groups中的每个group,查看group是否被选中
            groups.get(i).setChoosed(allChekbox.isChecked());
            StoreInfo group = groups.get(i);
            List<GoodsInfo> childs = children.get(group.getId());//根据key去取value
            for (GoodsInfo goodsInfo : childs) {//收集每个组的数量
                count += 1;
            }
        }

        //购物车已清空
        if(count==0){
            clearCart();
        } else{
            title.setText("Shopping Cart" + "(" + count + ")");
        }
    }
    private void clearCart() {
        title.setText("Shopping Cart" + "(" + 0 + ")");
        subtitle.setVisibility(View.GONE);
        llCart.setVisibility(View.GONE);
        cart_empty.setVisibility(View.VISIBLE);
    }
    private void doCheckAll() {//全选摁钮
        for (int i = 0; i < groups.size(); i++) {
            groups.get(i).setChoosed(allChekbox.isChecked());
            StoreInfo group = groups.get(i);
            List<GoodsInfo> childs = children.get(group.getId());
            for (int j = 0; j < childs.size(); j++) {
                childs.get(j).setChoosed(allChekbox.isChecked());
            }
        }
        cartadapter.notifyDataSetChanged();
        calculate();
    }
    private void calculate() {
        totalCount = 0;
        totalPrice = 0.00;
        for (int i = 0; i < groups.size(); i++) {
            StoreInfo group = groups.get(i);
            List<GoodsInfo> childs = children.get(group.getId());
            for (int j = 0; j < childs.size(); j++) {
                GoodsInfo product = childs.get(j);
                if (product.isChoosed()) {
                    totalCount++;
                    totalPrice += product.getPrice() * product.getCount();
                }
            }
        }

        tvTotalPrice.setText("￥" + totalPrice);
        tvGoToPay.setText("Checkout(" + totalCount + ")");
        //计算购物车的金额为0时候清空购物车的视图
        if(totalCount==0){
            setCartNum();
        } else{
            title.setText("Shopping Cart" + "(" + totalCount + ")");
        }
    }
    /**
     * 删除操作<br>
     * 1.不要边遍历边删除，容易出现数组越界的情况<br>
     * 2.现将要删除的对象放进相应的列表容器中，待遍历完后，以removeAll的方式进行删除
     */
    protected void doDelete() {
        List<StoreInfo> toBeDeleteGroups = new ArrayList<StoreInfo>();// 待删除的组元素列表
        for (int i = 0; i < groups.size(); i++) {
            StoreInfo group = groups.get(i);
            if (group.isChoosed()) {
                toBeDeleteGroups.add(group);
            }
            List<GoodsInfo> toBeDeleteProducts = new ArrayList<GoodsInfo>();// 待删除的子元素列表
            List<GoodsInfo> childs = children.get(group.getId());
            for (int j = 0; j < childs.size(); j++) {
                if (childs.get(j).isChoosed()) {
                    toBeDeleteProducts.add(childs.get(j));
                }
            }
            childs.removeAll(toBeDeleteProducts);//删除collection中的所有元素
        }
        groups.removeAll(toBeDeleteGroups);
        //记得重新设置购物车
        setCartNum();
        cartadapter.notifyDataSetChanged();
    }

    @Override
    public void childDelete(int groupPosition, int childPosition) {
        children.get(groups.get(groupPosition).getId()).remove(childPosition);
        StoreInfo group = groups.get(groupPosition);//position是个问题
        List<GoodsInfo> childs = children.get(group.getId());
        if (childs.size() == 0) {
            groups.remove(groupPosition);
        }
        cartadapter.notifyDataSetChanged();
        //     handler.sendEmptyMessage(0);
        calculate();
    }

    @Override
    public void checkChild(int groupPosition, int childPosiTion,
                           boolean isChecked) {
        boolean allChildSameState = true;// 判断改组下面的所有子元素是否是同一种状态
        StoreInfo group = groups.get(groupPosition);
        List<GoodsInfo> childs = children.get(group.getId());
        for (int i = 0; i < childs.size(); i++) {
            // 不全选中
            if (childs.get(i).isChoosed() != isChecked) {
                allChildSameState = false;
                break;
            }
        }
        //获取店铺选中商品的总金额
        if (allChildSameState) {
            group.setChoosed(isChecked);// 如果所有子元素状态相同，那么对应的组元素被设为这种统一状态
        } else {
            group.setChoosed(false);// 否则，组元素一律设置为未选中状态
        }

        if (isAllCheck()) {
            allChekbox.setChecked(true);// 全选
        } else {
            allChekbox.setChecked(false);// 反选
        }
        cartadapter.notifyDataSetChanged();
        calculate();

    }
    private boolean isAllCheck() {//判断是否所有组商品全被选中

        for (StoreInfo group : groups) {
            if (!group.isChoosed())
                return false;

        }

        return true;
    }

    @Override
    public void doIncrease(int groupPosition, int childPosition,//单个商品的增加减少
                           View showCountView, boolean isChecked) {
        GoodsInfo product = (GoodsInfo) cartadapter.getChild(groupPosition,
                childPosition);
        int currentCount = product.getCount();
        currentCount++;
        product.setCount(currentCount);
        ((TextView) showCountView).setText(currentCount + "");
        cartadapter.notifyDataSetChanged();
        calculate();
    }

    @Override
    public void doDecrease(int groupPosition, int childPosition,
                           View showCountView, boolean isChecked) {

        GoodsInfo product = (GoodsInfo) cartadapter.getChild(groupPosition,
                childPosition);
        int currentCount = product.getCount();
        if (currentCount == 1)
            return;
        currentCount--;
        product.setCount(currentCount);
        ((TextView) showCountView).setText(currentCount + "");
        cartadapter.notifyDataSetChanged();
        calculate();
    }

    @Override
    public void groupEdit(int groupPosition) {
        groups.get(groupPosition).setIsEdtor(true);
        cartadapter.notifyDataSetChanged();//Notifies the attached observers that the underlying data has been changed and any View reflecting the data set should refresh itself.
    }

    @Override
    public void checkGroup(int groupPosition, boolean isChecked) {//选中一个group，所有产品都被标记为选中
        StoreInfo group = groups.get(groupPosition);
        List<GoodsInfo> childs = children.get(group.getId());
        for (int i = 0; i < childs.size(); i++) {
            childs.get(i).setChoosed(isChecked);
        }
        if (isAllCheck())
            allChekbox.setChecked(true);
        else
            allChekbox.setChecked(false);
        cartadapter.notifyDataSetChanged();
        calculate();
    }

    public void readAndStoreOrderId(){
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();
        databaseReference.child("OrderId").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String id = dataSnapshot.getValue(String.class);
                orderId = Integer.parseInt(id);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void sendOrder(){//
        //new order:
        final Order order = new Order();
        order.setOrderID(orderId++);
        databaseReference.child("OrderId").setValue(String.valueOf(orderId));
        //set customer:
        Customer customer = new Customer();
        customer.setName("gbw");
        order.setCustomer(customer);
        Log.d("V",order.getCustomer().getName());
        //new orderItem in this order:
        OrderItem oi = new OrderItem();
        oi.setProductName("Burger");
        oi.setQuantity(10);
        oi.setActualPrice(15.5);
        order.getOrderItemList().add(oi);

        //new another orderItem in this order:
        oi = new OrderItem();
        oi.setProductName("coke");
        oi.setQuantity(5);
        oi.setActualPrice(10.25);
        order.getOrderItemList().add(oi);
        order.setStatus("submitting");
        try{
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference databaseReference = database.getReference("Order");
            databaseReference.push().setValue(order);
        }catch (Exception e) {
            e.printStackTrace();
        }

    }
    @OnClick({R.id.all_chekbox, R.id.tv_delete, R.id.tv_go_to_pay, R.id.subtitle, R.id.tv_save, R.id.tv_share})
    public void onClick(View view) {
        AlertDialog alert;
        switch (view.getId()) {
            case R.id.all_chekbox:
                doCheckAll();
                break;
            case R.id.tv_delete:
                if (totalCount == 0) {
                    Toast.makeText(context, "请选择要移除的商品", Toast.LENGTH_LONG).show();
                    return;
                }
                alert = new AlertDialog.Builder(context).create();
                alert.setTitle("操作提示");
                alert.setMessage("您确定要将这些商品从购物车中移除吗？");
                alert.setButton(DialogInterface.BUTTON_NEGATIVE, "取消",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {//??
                                return;
                            }
                        });
                alert.setButton(DialogInterface.BUTTON_POSITIVE, "确定",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                doDelete();
                            }
                        });
                alert.show();
                break;
            case R.id.tv_go_to_pay:
                if (totalCount == 0) {
                    Toast.makeText(context, "请选择要支付的商品", Toast.LENGTH_LONG).show();
                    return;
                }
                alert = new AlertDialog.Builder(context).create();
                alert.setTitle("操作提示");
                alert.setMessage("总计:\n" + totalCount + "种商品\n" + totalPrice + "元");
                alert.setButton(DialogInterface.BUTTON_NEGATIVE, "取消",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                return;
                            }
                        });
                alert.setButton(DialogInterface.BUTTON_POSITIVE, "确定",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //
                                sendOrder();//ready to send order
                                return;
                            }
                        });
                alert.show();
                break;
            case R.id.subtitle:
                if (flag == 0) {
                    llInfo.setVisibility(View.GONE);
                    tvGoToPay.setVisibility(View.GONE);
                    llShar.setVisibility(View.VISIBLE);
                    subtitle.setText("完成");
                } else if (flag == 1) {
                    llInfo.setVisibility(View.VISIBLE);
                    tvGoToPay.setVisibility(View.VISIBLE);
                    llShar.setVisibility(View.GONE);
                    subtitle.setText("编辑");
                }
                flag = (flag + 1) % 2;//其余得到循环执行上面2个不同的功能
                break;
            case R.id.tv_share:
                if (totalCount == 0) {
                    Toast.makeText(context, "请选择要分享的商品", Toast.LENGTH_LONG).show();
                    return;
                }
                Toast.makeText(ViewCartActivity.this, "分享成功", Toast.LENGTH_SHORT).show();
                break;
            case R.id.tv_save:
                if (totalCount == 0) {
                    Toast.makeText(context, "请选择要收藏的商品", Toast.LENGTH_LONG).show();
                    return;
                }
                Toast.makeText(ViewCartActivity.this, "收藏成功", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
