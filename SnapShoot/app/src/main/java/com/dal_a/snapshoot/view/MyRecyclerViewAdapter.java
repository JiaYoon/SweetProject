package com.dal_a.snapshoot.view;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dal_a.snapshoot.model.BaseCard;
import com.dal_a.snapshoot.model.BaseCardView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by GA on 2015. 9. 7..
 */
public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder> {
    private List<BaseCard> mCardList = new ArrayList<>();
    //imageid -> cardlistid로 이어주는 indexlist
//    private List<HeaderCard> mHeaderList = new ArrayList<>();
//
//    public Parcelable onSaveInstanceState() {
//        return null;
//    }
//
//    /**
//     * Restore the instance state of the {@link ArrayAdapter}. It re-initializes
//     * the array of objects being managed by this adapter with the state retrieved
//     * from {@code savedInstanceState}
//     *
//     * @param savedInstanceState
//     *          The bundle containing the previously saved state
//     */
//    public void onRestoreInstanceState(Bundle savedInstanceState) {
//        if (savedInstanceState.containsKey(KEY_ADAPTER_STATE)) {
//            ArrayList<T> objects = savedInstanceState
//                    .getParcelableArrayList(KEY_ADAPTER_STATE);
//            setItems(objects);
//        }
//    }


    public static class ViewHolder<T extends BaseCard> extends RecyclerView.ViewHolder {
        private final BaseCardView<T> view;

        public ViewHolder(View v) {
            super(v);
            view = (BaseCardView<T>) v;
        }

        public void build(T card) {
            view.build(card);
        }

    }


    public MyRecyclerViewAdapter(Context context) {
    }

    public MyRecyclerViewAdapter(Context context, List<BaseCard> items) {
        //context.getTheme().resolveAttribute(R.attr.selectableItemBackground, mTypedValue, true);
        //mBackground = mTypedValue.resourceId;
        mCardList = items;
    }

    public BaseCard getValueAt(int position) {
        return mCardList.get(position);
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.build(mCardList.get(position));
    }

    @Override
    public int getItemViewType(final int position) {
        return mCardList.get(position).getLayout();
    }

    public void setAllCards(List<BaseCard> items){
        mCardList = items;
        notifyDataSetChanged();
    }

    //카드를 모으고
    //카드를 리스트에 집어넣음
    //만약 이미지 아이디가 32이고 3번째인 카드를 선택한 후 다른 액티비티로 가서 변화를주고 다시 돌아올 경우
    //이미지 아이디가 32인 카드들만 모든 리스트에서 변화를 주면
    //변화의 데이터는 서버로 부터 받아옴
    //해야할일
    //0. 이미지 아이디를 키로 가지고 몇번째인지를 벨류로 가지는 해시맵을 만듬
    //1. 카드를 넣을 때 이미지 아이디와 키를 해시맵에 저장
    //  1.1. 루프를 돌것인가, 따로 넣을 것인가.
    //2. 이미지를 선택했을 때 이미지 아이디를 콘스턴트 저장
    //3. 다시 빠져나왔을 때 콘스턴트에 저장된 이미지아이디로 데이터를 받아와서 새로운 카드를 만듬
    //4. 이미지 아이디와 Basecard를 인자로 하는 changeCard함수로 해당 이미지 아이디-> 몇번째인지 -> 변경
    //5. 적용(notifydatachanged)

    public void addAllCards(List<BaseCard> items){
        mCardList.addAll(items);
        notifyDataSetChanged();
    }

    public void changeCard(int index, BaseCard card){
        mCardList.set(index, card);
        notifyItemChanged(index);
    }

    public List<BaseCard> getAllCards(){
        return mCardList;
    }

//    @Override
//    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext())
//                .inflate(R.layout.list_item1, parent, false);
//        //view.setBackgroundResource(mBackground);
//        return new ViewHolder(view);
//    }

//    @Override
//    public void onBindViewHolder(final ViewHolder holder, int position) {
//        holder.mBoundString = mValues.get(position);
//        holder.mTextView.setText(mValues.get(position));
//
//        holder.mView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Context context = v.getContext();
//                Intent intent = new Intent(context, CheeseDetailActivity.class);
//                intent.putExtra(CheeseDetailActivity.EXTRA_NAME, holder.mBoundString);
//
//                context.startActivity(intent);
//            }
//        });
//
//        Glide.with(holder.mImageView.getContext())
//                .load(Cheeses.getRandomCheeseDrawable())
//                .fitCenter()
//                .into(holder.mImageView);
//    }

    @Override
    public int getItemCount() {
        return mCardList.size();
    }
}
