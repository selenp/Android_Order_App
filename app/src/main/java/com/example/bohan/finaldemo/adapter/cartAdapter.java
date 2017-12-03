package com.example.bohan.finaldemo.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.bohan.finaldemo.R;
import com.example.bohan.finaldemo.entity.GoodsInfo;
import com.example.bohan.finaldemo.entity.StoreInfo;

import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 18910931590163.com on 11/21/17.
 */

public class cartAdapter extends BaseExpandableListAdapter {
    private List<StoreInfo> groups;
    private Map<String, List<GoodsInfo>> children;
    private Context context;
    private CheckInterface checkInterface;
    private ModifyCountInterface modifyCountInterface;
    private GroupEdtorListener mListener;
    public cartAdapter(List<StoreInfo> groups, Map<String, List<GoodsInfo>> children, Context context) {
        this.groups = groups;
        this.children = children;
        this.context = context;
    }
    public void setCheckInterface(CheckInterface checkInterface) {
        this.checkInterface = checkInterface;
    }

    public void setModifyCountInterface(ModifyCountInterface modifyCountInterface) {
        this.modifyCountInterface = modifyCountInterface;
    }
    public void setmListener(GroupEdtorListener mListener) {
        this.mListener = mListener;
    }

    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        Log.d("V","I am getgroup view"+groupPosition);
        final GroupViewHolder gholder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.cart_group, null);
            gholder = new GroupViewHolder(convertView);
            convertView.setTag(gholder);
        } else {
            gholder = (GroupViewHolder) convertView.getTag();
        }
        final StoreInfo group = (StoreInfo) getGroup(groupPosition);

        gholder.tvSourceName.setText(group.getName());
        gholder.determineChekbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)

            {
                group.setChoosed(((CheckBox) v).isChecked());
                checkInterface.checkGroup(groupPosition, ((CheckBox) v).isChecked());// 暴露组选接口
            }
        });
        gholder.determineChekbox.setChecked(group.isChoosed());
        if (group.isEdtor()) {
            gholder.tvStoreEdtor.setText("Done");
        } else {
            gholder.tvStoreEdtor.setText("Edit");
        }
        gholder.tvStoreEdtor.setOnClickListener(new GroupViewClick(groupPosition, gholder.tvStoreEdtor, group));
        notifyDataSetChanged();
        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, final boolean isLastChild, View convertView, final ViewGroup parent) {
//覆盖getchildview来填充视图
        Log.d("V","I am getchild view"+groupPosition+childPosition);
        final ChildViewHolder cholder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.cart_item, null);
            cholder = new ChildViewHolder(convertView);
            convertView.setTag(cholder);
        } else {
            cholder = (ChildViewHolder) convertView.getTag();
        }

        if (groups.get(groupPosition).isEdtor() == true) {
            cholder.llEdtor.setVisibility(View.VISIBLE);
            cholder.rlNoEdtor.setVisibility(View.GONE);
        } else {
            cholder.llEdtor.setVisibility(View.GONE);
            cholder.rlNoEdtor.setVisibility(View.VISIBLE);
        }
        final GoodsInfo goodsInfo = (GoodsInfo) getChild(groupPosition, childPosition);


        if(isLastChild&&getChild(groupPosition,childPosition)!=null){
            cholder.stub.setVisibility(View.VISIBLE);
            //  TextView tv= (TextView) cholder.stub.findViewById(R.id.txtFooter);//这里用来动态显示店铺满99元包邮文字内容
        }else{
            cholder.stub.setVisibility(View.GONE);
        }
        if (goodsInfo != null) {


            cholder.tvIntro.setText(goodsInfo.getDesc());
            cholder.tvPrice.setText("￥" + goodsInfo.getPrice() + "");
            cholder.etNum.setText(goodsInfo.getCount() + "");
            cholder.ivAdapterListPic.setImageResource(goodsInfo.getGoodsImg());
            cholder.tvColorsize.setText( "尺码：" + goodsInfo.getSize() + "瓶/斤");
            //SpannableString spanString = new SpannableString("￥" + String.valueOf(goodsInfo.getDiscountPrice()));
            //StrikethroughSpan span = new StrikethroughSpan();
            //spanString.setSpan(span, 0, String.valueOf(goodsInfo.getDiscountPrice()).length() + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            //避免无限次的appand
//            if (cholder.tvDiscountPrice.getText().toString().length() > 0) {
//                cholder.tvDiscountPrice.setText("");
//            }
//            cholder.tvDiscountPrice.append(spanString);
            cholder.tvBuyNum.setText("x" + goodsInfo.getCount());
            cholder.checkBox.setChecked(goodsInfo.isChoosed());
            cholder.checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    goodsInfo.setChoosed(((CheckBox) v).isChecked());
                    cholder.checkBox.setChecked(((CheckBox) v).isChecked());
                    checkInterface.checkChild(groupPosition, childPosition, ((CheckBox) v).isChecked());// 暴露子选接口
                }
            });
            cholder.btAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    modifyCountInterface.doIncrease(groupPosition, childPosition, cholder.etNum, cholder.checkBox.isChecked());// 暴露增加接口
                }
            });
            cholder.btReduce.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    modifyCountInterface.doDecrease(groupPosition, childPosition, cholder.etNum, cholder.checkBox.isChecked());// 暴露删减接口
                }
            });
            /********************方案一：弹出软键盘修改数量，应为又不知名的bug会使然键盘强行关闭***********************/
            /****在清单文件的activity下设置键盘：
             android:windowSoftInputMode="adjustUnspecified|stateHidden|adjustPan"
             android:configChanges="orientation|keyboardHidden"****/
            cholder.etNum.addTextChangedListener(new GoodsNumWatcher(goodsInfo));//监听文本输入框的文字变化，并且刷新数据
            notifyDataSetChanged();
            cholder.tvGoodsDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog alert = new AlertDialog.Builder(context).create();
                    alert.setTitle("Seriously?It's yummy!");
                    alert.setMessage("Are you sure you want to delete this item？");
                    alert.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    return;
                                }
                            });
                    alert.setButton(DialogInterface.BUTTON_POSITIVE, "Delete",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    modifyCountInterface.childDelete(groupPosition, childPosition);

                                }
                            });
                    alert.show();

                }
            });

        }

        return convertView;
    }
    /**
     * 使某个组处于编辑状态
     * <p/>
     * groupPosition组的位置
     */

    class GroupViewClick implements View.OnClickListener {
        private int groupPosition;
        private Button edtor;
        private StoreInfo group;

        public GroupViewClick(int groupPosition, Button edtor, StoreInfo group) {
            this.groupPosition = groupPosition;
            this.edtor = edtor;
            this.group = group;
        }

        @Override
        public void onClick(View v) {
            int groupId = v.getId();
            if (groupId == edtor.getId()) {
                if (group.isEdtor()) {
                    group.setIsEdtor(false);
                } else {
                    group.setIsEdtor(true);

                }
                notifyDataSetChanged();
            }
        }
    }
    @Override
    public int getGroupCount() {
        return groups.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        String groupId = groups.get(groupPosition).getId();

        return children.get(groupId).size();
    }
    @Override
    public Object getGroup(int groupPosition) {
        return groups.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        List<GoodsInfo> childs = children.get(groups.get(groupPosition).getId());
        return childs.get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    /**
     * 购物车的数量修改编辑框的内容监听
     */
    class GoodsNumWatcher implements TextWatcher {
        GoodsInfo   goodsInfo;
        public GoodsNumWatcher(GoodsInfo goodsInfo) {
            this.goodsInfo = goodsInfo;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if(!TextUtils.isEmpty(s.toString())){//当输入的数字不为空时，更新数字
                goodsInfo.setCount(Integer.valueOf(s.toString().trim()));
            }
        }

    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;

    }

    /**
     * 组元素绑定器
     */
    static class GroupViewHolder {

        @BindView(R.id.determine_chekbox)
        CheckBox determineChekbox;
        @BindView(R.id.tv_source_name)
        TextView tvSourceName;
        @BindView(R.id.tv_store_edtor)
        Button tvStoreEdtor;

        GroupViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    /**
     * 子元素绑定器
     */
    static class ChildViewHolder {
        @BindView(R.id.check_box)
        CheckBox checkBox;
        @BindView(R.id.iv_adapter_list_pic)
        ImageView ivAdapterListPic;
        @BindView(R.id.tv_intro)
        TextView tvIntro;
        @BindView(R.id.tv_color_size)
        TextView tvColorSize;
        @BindView(R.id.tv_price)
        TextView tvPrice;
//        @BindView(R.id.tv_discount_price)
//        TextView tvDiscountPrice;
        @BindView(R.id.tv_buy_num)
        TextView tvBuyNum;
        @BindView(R.id.rl_no_edtor)
        RelativeLayout rlNoEdtor;
        @BindView(R.id.bt_reduce)
        Button btReduce;
        @BindView(R.id.et_num)
        EditText etNum;
        @BindView(R.id.bt_add)
        Button btAdd;
        @BindView(R.id.ll_change_num)
        RelativeLayout llChangeNum;
        @BindView(R.id.tv_colorsize)
        TextView tvColorsize;
        @BindView(R.id.tv_goods_delete)
        TextView tvGoodsDelete;
        @BindView(R.id.ll_edtor)
        LinearLayout llEdtor;
        @BindView(R.id.stub)
        ViewStub stub;
        ChildViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

    }








    /**
     * 复选框接口
     */
    public interface CheckInterface {
        /**
         * 组选框状态改变触发的事件
         *
         * @param groupPosition 组元素位置
         * @param isChecked     组元素选中与否
         */
        void checkGroup(int groupPosition, boolean isChecked);

        /**
         * 子选框状态改变时触发的事件
         *
         * @param groupPosition 组元素位置
         * @param childPosition 子元素位置
         * @param isChecked     子元素选中与否
         */
        void checkChild(int groupPosition, int childPosition, boolean isChecked);
    }

    /**
     * 改变数量的接口
     */
    public interface ModifyCountInterface {
        /**
         * 增加操作
         *
         * @param groupPosition 组元素位置
         * @param childPosition 子元素位置
         * @param showCountView 用于展示变化后数量的View
         * @param isChecked     子元素选中与否
         */
        void doIncrease(int groupPosition, int childPosition, View showCountView, boolean isChecked);

        /**
         * 删减操作
         *
         * @param groupPosition 组元素位置
         * @param childPosition 子元素位置
         * @param showCountView 用于展示变化后数量的View
         * @param isChecked     子元素选中与否
         */
        void doDecrease(int groupPosition, int childPosition, View showCountView, boolean isChecked);

        /**
         * 删除子item
         *
         * @param groupPosition
         * @param childPosition
         */
        void childDelete(int groupPosition, int childPosition);
    }

    /**
     * 监听编辑状态
     */
    public interface GroupEdtorListener {
        void groupEdit(int groupPosition);
    }

}
