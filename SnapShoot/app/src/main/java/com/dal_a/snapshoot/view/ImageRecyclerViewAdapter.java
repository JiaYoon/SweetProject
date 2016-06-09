package com.dal_a.snapshoot.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dal_a.snapshoot.model.BaseCard;
import com.dal_a.snapshoot.model.BaseCardView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by GA on 2015. 9. 7..
 */
public class ImageRecyclerViewAdapter extends MyRecyclerViewAdapter {
    private HashMap<Long, Integer> mIndexList = new HashMap<>();

    public ImageRecyclerViewAdapter(Context context) {
        super(context);
    }

    public ImageRecyclerViewAdapter(Context context, List<BaseCard> items) {
        super(context, items);
    }

    //카드를 모으고
    //카드를 리스트에 집어넣음
    //만약 이미지 아이디가 32이고 3번째인 카드를 선택한 후 다른 액티비티로 가서 변화를주고 다시 돌아올 경우
    //이미지 아이디가 32인 카드들만 모든 리스트에서 변화를 주면
    //변화의 데이터는 서버로 부터 받아옴
    //해야할일
    //0. 이미지 아이디를 키로 가지고 몇번째인지를 벨류로 가지는 해시맵을 만듬
    //1. 카드를 넣을 때 이미지 아이디와 키를 해시맵에 저장
    //2. 이미지를 선택했을 때 이미지 아이디를 콘스턴트 저장
    //3. 다시 빠져나왔을 때 콘스턴트에 저장된 이미지아이디로 데이터를 받아와서 새로운 카드를 만듬
    //4. 이미지 아이디와 Basecard를 인자로 하는 changeCard함수로 해당 이미지 아이디-> 몇번째인지 -> 변경
    //5. 적용(notifydatachanged)

    @Override
    public void setAllCards(List<BaseCard> items){
        super.setAllCards(items);
        int idsSize = getItemCount();
        for(int i = 0 ; i < idsSize; i++){
            mIndexList.put(items.get(i).getId(), i);
        }
    }

    @Override
    public void addAllCards(List<BaseCard> items){
        int bSize = getItemCount();
        super.addAllCards(items);
        int aSize = getItemCount();
        for(int i = bSize ; i < aSize; i++){
            mIndexList.put(items.get(i-bSize).getId(), i);
        }

    }

    public void changeCard(Long id, BaseCard card){
        if(mIndexList.get(id) == null)  return;
        super.changeCard(mIndexList.get(id), card);
    }


    public List<BaseCard> getAllCards(){
        return super.getAllCards();
    }

}
