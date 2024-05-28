package solitaire.pile;


import solitaire.card.Card;

import java.awt.*;
import java.util.ArrayList;
import java.util.Enumeration;

public class TablePile extends CardPile {
	private int notFlipNum; // 未翻转的卡片数量
	private int  cardNum; // 卡片总数
	final public static int separation  = 35; // 翻转的卡片之间的间隔
	final public static int unFlipCardSeparation = 20; // 未翻转的卡片之间的间隔

	public TablePile(int x, int y,int notFlipNum){ // 构造函数，接收x坐标，y坐标和未翻转的卡片数量
		super(x, y); // 调用父类的构造函数
		this.notFlipNum = notFlipNum; // 设置未翻转的卡片数量
		cardNum = notFlipNum+1; // 设置卡片总数
		this.setType(TABLE_PILE); // 设置牌堆类型为TABLE_PILE
	}

	public TablePile(int x,int y){ // 构造函数，接收x坐标和y坐标
		super(x,y); // 调用父类的构造函数
		cardNum=0; // 初始化卡片总数为0
		this.notFlipNum=0; // 初始化未翻转的卡片数量为0
		this.setType(TABLE_PILE); // 设置牌堆类型为TABLE_PILE
	}

	/**
	 * 判断给定的坐标是否在牌堆中
	 * @param tx
	 * @param ty
	 * @return
	 */
	@Override
	public boolean includes(int tx, int ty) {
		int beginX,beginY,endX,endY;
		beginX = x; // 设置开始的x坐标为牌堆的x坐标
		beginY = y ; // 设置开始的y坐标为牌堆的y坐标
		endX = x + Card.width; // 设置结束的x坐标为牌堆的x坐标加上卡片的宽度
		if(thePile.size() > 0) // 如果牌堆不为空
			endY =  beginY  + unFlipCardSeparation * notFlipNum + separation * (thePile.size() - 1 - notFlipNum) + Card.height ;
		// 设置结束的y坐标为开始的y坐标加上未翻转的卡片数量乘以未翻转的卡片间隔，再加上翻转的卡片数量乘以翻转的卡片间隔，再加上卡片的高度
		else // 如果牌堆为空
			endY =  beginY  + Card.height; // 设置结束的y坐标为开始的y坐标加上卡片的高度
		boolean isInclude =  beginX <= tx && tx <= endX && beginY <= ty && ty <= endY;
		// 判断给定的坐标是否在开始和结束的坐标之间
		return isInclude; // 返回判断结果
	}

	/**
	 * 选择给定坐标的卡片
	 * @param tx
	 * @param ty
	 * @return
	 */
	@Override
	public int select(int tx, int ty) {
		if(!(isEmpty())){ // 如果牌堆不为空
			int beginX,beginY,endX,endY;
			beginX = x; // 设置开始的x坐标为牌堆的x坐标
			beginY = y + unFlipCardSeparation * notFlipNum; // 设置开始的y坐标为牌堆的y坐标加上未翻转的卡片数量乘以未翻转的卡片间隔
			endX = x + Card.width; // 设置结束的x坐标为牌堆的x坐标加上卡片的宽度
			endY =  beginY  + unFlipCardSeparation * notFlipNum + separation * (thePile.size() - 1 - notFlipNum) + Card.height;
			// 设置结束的y坐标为开始的y坐标加上未翻转的卡片数量乘以未翻转的卡片间隔，再加上翻转的卡片数量乘以翻转的卡片间隔，再加上卡片的高度
			boolean flip_include =  beginX <= tx && tx <= endX && beginY <= ty && ty <= endY;
			// 判断给定的坐标是否在开始和结束的坐标之间
			if(flip_include){ // 如果给定的坐标在开始和结束的坐标之间
				int c = (ty - beginY)/separation + notFlipNum; // 计算选中的卡片的索引
				if(c >= thePile.size()){ // 如果计算出的索引大于或等于牌堆的大小
					c =  thePile.size() - 1; // 将索引设置为牌堆的大小减1
				}
				return c; // 返回索引
			}
			else // 如果给定的坐标不在开始和结束的坐标之间
				return -1; // 返回-1
		}
		else // 如果牌堆为空
			return -1; // 返回-1
	}

	/**
	 * 将一系列卡片添加到牌堆顶
	 * @param card
	 */
	@Override
	public void addCard(Object card) {

		ArrayList<Card> cardList = (ArrayList<Card>)card; // 将传入的对象转换为卡片列表
		// 注意：这里可能会抛出ClassCastException异常，如果传入的对象不能被转换为ArrayList<Card>类型
		cardNum += cardList.size(); // 将卡片列表的大小加到卡片总数上

		for(int i = 0;i < cardList.size();i++){ // 遍历卡片列表
			thePile.push(cardList.get(i)); // 将每一张卡片压入堆栈
		}
	}

	/**
	 * 将一张卡片添加到牌堆顶
	 * @param card
	 */
	@Override
	public void addSingleCard(Object card){
	 	Card card1=(Card)card;
	 	cardNum++;
	 	thePile.push(card1);
	}


	/**
	 * 弹出栈顶的一张牌
	 * @return
	 */
	@Override
	public Card pop() {

		cardNum--;
		return super.pop();
	}

	/**
	 * 判断是否可以添加给定的卡片
	 * @param card
	 * @return
	 */
	@Override
	public boolean isCanAdd(Card card){
		if ( isEmpty()) // 如果牌堆为空
			return card.getNum() == 12; // 只有当卡片的数字为12时才可以添加
		Card topCard = top(); // 获取牌堆顶部的卡片
		return (card.getColor() != topCard.getColor()) && // 如果卡片的颜色与顶部卡片的颜色不同
				(card.getNum() ==  topCard.getNum()-1 ); // 并且卡片的数字是顶部卡片的数字减1，则可以添加
	}

	/**
	 * 绘制牌堆
	 * @param g
	 */
	@Override
	public void display(Graphics g) {
		if (isEmpty()){ // 如果牌堆为空
			super.display(g); // 调用父类的display方法
		}
		else{ // 如果牌堆不为空
			int localy = y; // 初始化局部变量localy为牌堆的y坐标
			for (Enumeration e = thePile.elements(); e.hasMoreElements(); ) { // 遍历牌堆中的每一张卡片

				Card aCard = (Card) e.nextElement(); // 获取下一张卡片
				aCard.setX(x); // 设置卡片的x坐标
				aCard.setY(localy); // 设置卡片的y坐标
				aCard.draw(g); // 绘制卡片
				if(aCard.isFaceup()) // 如果卡片是面朝上的
					localy += separation; // localy增加翻转的卡片之间的间隔
				else // 如果卡片是面朝下的
					localy += unFlipCardSeparation; // localy增加未翻转的卡片之间的间隔

			}
		}
	}
	public int getNotFlipNum() {
		return notFlipNum;
	}
	public void setNotFlipNum(int notFlipNum) {
		this.notFlipNum = notFlipNum;
	}
	public int getCardNum() {
		return cardNum;
	}
	public void setCardNum(int cardNum) {
		this.cardNum = cardNum;
	}
	public static int getSeparation() {
		return separation;
	}
	public static int getUnflipcardseparation() {
		return unFlipCardSeparation;
	}

	
}