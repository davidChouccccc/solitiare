package solitaire.pile;

import solitaire.card.Card;

import java.util.ArrayList;


public class SuitPile extends CardPile { // SuitPile类，继承自CardPile类

	public SuitPile (int x, int y) { // 构造函数，接收x和y坐标
		super(x, y); // 调用父类的构造函数
		this.setType(SUIT_PILE); // 设置牌堆类型为SUIT_PILE
	}

	@Override
	public boolean isCanAdd(Card card) { // 重写isCanAdd方法，检查是否可以添加卡片

		if (isEmpty()) // 如果牌堆为空
			return card.getNum() == 0; // 只有当卡片的数字为0时才可以添加
		Card topCard = top(); // 获取牌堆顶部的卡片
		return (card.getType() == topCard.getType()) && // 如果卡片的类型与顶部卡片的类型相同
				(card.getNum() ==  topCard.getNum() + 1); // 并且卡片的数字是顶部卡片的数字加1，则可以添加

	}

	public int size(){ // 获取牌堆的大小
		return thePile.size();
	}

	@Override
	public void addCard(Object card) { // 重写addCard方法，添加卡片到牌堆
		thePile.push((Card)card); // 将卡片压入堆栈
	}

	public void addSingleCard(Object card){ // 添加单张卡片？？TODO
		ArrayList<Card> cardList = (ArrayList<Card>)card; // 将对象转换为卡片列表
		for (int i=0;i<cardList.size();i++){ // 遍历卡片列表
			System.out.println(cardList.get(i).getNum()+"         "+cardList.size()+"  i:"+i+"           "+cardList.get(i).getType()); // 打印卡片的信息
		}
		thePile.push(cardList.get(0)); // 将卡片列表的第一张卡片压入堆栈
	}
}