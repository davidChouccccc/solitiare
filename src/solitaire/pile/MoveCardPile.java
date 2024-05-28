package solitaire.pile;

import solitaire.card.Card;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;

public class MoveCardPile {  //当前选中的牌所在的牌堆
	private ArrayList<Card> cardList ; // 用于存储卡片的列表
	private CardPile fromPile; // 表示卡片来源的牌堆
	private int mouseOffsetX = 0; // 鼠标点击的位置相对于卡片左上角的x偏移量
	private int mouseOffsetY = 0; // 鼠标点击的位置相对于卡片左上角的y偏移量
	private int x; // 表示x坐标
	private int y; // 表示y坐标
	final public static int separation  = 35; // 表示卡片之间的间隔

	public MoveCardPile(){ // 构造函数
		cardList = new ArrayList<Card>(); // 初始化卡片列表
	}

	public int size(){ // 获取卡片列表的大小
		return cardList.size();
	}

	public boolean isEmpty(){ // 检查卡片列表是否为空
		return cardList.isEmpty();
	}

	public void addCard(Card card){ // 向卡片列表添加卡片
		cardList.add(0, card);
	}

	public Card getCard(){ // 获取卡片列表的第一张卡片
		if(cardList.size()>0)
			return cardList.get(0);
		else
			return null;
	}

	public Card removeCard(){ // 移除卡片列表的第一张卡片
		if(cardList.size()>0)
			return  cardList.remove(0);
		else
			return null;
	}

	public ArrayList<Card> clear(){ // 清空卡片列表
		ArrayList<Card> list = cardList;
		cardList = new ArrayList<Card>();
		return list;
	}

	public void display(Graphics g,int tx,int ty){ // 显示卡片列表
		double scale = 1.1; // 放大的比例
		x = tx - (int)(Card.width * scale / 2); // 计算并设置牌的中心x坐标
		y = ty - (int)(Card.height * scale / 2); // 计算并设置牌的中心y坐标
		int localy = y; // 设置局部变量localy为牌的y坐标
		for (int i = 0;i < cardList.size();i++) { // 遍历卡片列表
			Card aCard = (Card) cardList.get(i); // 获取当前卡片
			aCard.setX(x); // 设置当前卡片的x坐标
			aCard.setY(localy); // 设置当前卡片的y坐标
			if(!(aCard.isFaceup())) // 如果当前卡片是面朝下的
				aCard.setFaceup(true); // 翻转卡片使其面朝上
			Graphics2D g2d = (Graphics2D) g;
			AffineTransform old = g2d.getTransform(); // 保存旧的变换状态
			g2d.translate(x + Card.width * scale / 2, y + Card.height * scale / 2); // 将图像的中心点移动到 (0,0)
			g2d.scale(scale, scale); // 放大图像
			g2d.translate(-x - Card.width * scale / 2, -y - Card.height * scale / 2); // 将图像的中心点移动回原来的位置
			g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.85f)); // 设置透明度
			aCard.draw(g2d); // 在图形上下文g中绘制当前卡片
			g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f)); // 重置透明度
			g2d.setTransform(old); // 重置变换状态
//			aCard.draw(g); // 在图形上下文g中绘制当前卡片
			localy += separation; // 更新下一张卡片的y坐标
		}
	}

	public ArrayList<Card> getCardList() { // 获取卡片列表
		return cardList;
	}

	public void setCardList(ArrayList<Card> cardList) { // 设置卡片列表
		this.cardList = cardList;
	}

	public CardPile getFromPile() { // 获取卡片来源的牌堆
		return fromPile;
	}

	public void setFromPile(CardPile fromPile) { // 设置卡片来源的牌堆
		this.fromPile = fromPile;
	}

	public int getX() { // 获取x坐标
		return x;
	}

	public void setX(int x) { // 设置x坐标
		this.x = x;
	}

	public int getY() { // 获取y坐标
		return y;
	}

	public void setY(int y) { // 设置y坐标
		this.y = y;
	}

	public int getMouseOffsetX() {
		return mouseOffsetX;
	}

	public void setMouseOffsetX(int mouseOffsetX) {
		this.mouseOffsetX = mouseOffsetX;
	}

	public int getMouseOffsetY() {
		return mouseOffsetY;
	}

	public void setMouseOffsetY(int mouseOffsetY) {
		this.mouseOffsetY = mouseOffsetY;
	}
}
