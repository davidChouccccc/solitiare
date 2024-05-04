package com.company.Card;

import java.util.ArrayList;

/*
十三个牌堆的父类，用一个ArrayList数组维护各堆中的牌。
                   定义所有堆中的共同操作，如判断牌堆是否为空、添加牌，移除牌，获得堆顶牌等。

 */
public   class CardStack implements CardStack_Interface{
    private ArrayList<Card> pokers_card;

    /**
     * 构造函数
     */
    public CardStack()
    {
        pokers_card = new ArrayList<Card>();
    }

    /**
     * 获取牌堆中的卡牌数量
     * @return
     */
    public int size() {
        return this.pokers_card.size();
    }


    /**
     * 获取整个牌堆的牌
     * @return
     */
    public ArrayList<Card> getPokers_card()
    {
        return this.pokers_card;
    }


    /**
     * 获取牌堆中的顶部卡牌
     * @return
     */
    public Card peek() {
    //如果牌堆有牌则获得顶部卡牌，否则获得一张红桃A
       if(this.pokers_card.size() >0)
       {
           return this.pokers_card.get(this.pokers_card.size() - 1);
       }
       else
       {
           return new Card(Rank.ACE,Suit.HEARTS);
       }

       }
       //给牌堆添加一张卡牌，添加到最顶端

    /**
     * 在牌堆顶部添加一张卡牌
     * @param pCard
     */
    public void init(Card pCard)
    {
        this.pokers_card.add(pCard);
    }
    //获取指定位置的卡片

    /**
     * 获取牌堆中index位置的卡牌
     * @param index
     * @return
     */
    public Card peek(int index)
    {

        if(index >= 0 && index < size())
        {
            return this.pokers_card.get(index);
        }

            return null;


    }
    //list是否为空

    /**
     * 判断牌堆是否为空
     * @return
     */
    public boolean isEmpty() {

        return pokers_card.size() == 0;

    }
    //push一组卡牌

    /**
     * 将pStack中的卡牌从index开始push到当前牌堆
     * @param pStack
     * @param index
     */
    public void push(CardStack pStack, int index) {

        for(int i = index; i < pStack.size(); i++) {
            this.pokers_card.add(pStack.peek(i));
        }

    }

    /**
     * 将pStack中的卡牌全部push到当前牌堆
     * @param pStack
     */
    public void push(CardStack pStack) {
        for(int i = 0; i < pStack.size(); i++) {
            this.pokers_card.add(pStack.peek(i));

        }

    }
    //删除一组卡牌

    /**
     * 删除牌堆中花色和点数与pCard相同的卡牌
     * @param pCard
     */
    public void pop(Card pCard) {
        if(!isEmpty())
        {
            for(int i = 0 ; i <this.pokers_card.size() ; i++)
            {
                if(pCard.getSuit() == this.pokers_card.get(i).getSuit() &&pCard.getRank() == this.pokers_card.get(i).getRank() )
                    this.pokers_card.remove(i);
                }
        }

    }
    //删除顶部卡牌

    /**
     * 删除牌堆中的顶部卡牌
     */
    public void pop() {
        if(!isEmpty())
            this.pokers_card.remove(pokers_card.size()-1);
        }

    /**
     * 清空牌堆
     */
    public void clear() {
        this.pokers_card.clear();
    }


}
