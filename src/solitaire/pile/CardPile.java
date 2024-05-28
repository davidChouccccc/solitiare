package solitaire.pile;

import solitaire.card.Card;

import java.awt.*;
import java.util.EmptyStackException;
import java.util.Stack;

public class CardPile {//牌堆超类

    public static final int TABLE_PILE=1;
    public static final int SUIT_PILE=2;
    public static final int DECK_PILE=3;
    public static final int DISCARD_PILE=4;
    public int type;
    public int x;
    public int y;
    public Stack<Card> thePile;

    public CardPile(int xl, int yl) {
        x = xl;
        y = yl;
        thePile = new Stack<Card>();

    }

    public void setType(int type){
        this.type=type;
    }

    public int getType(){
        return this.type;
    }

    public Card top() { //牌堆的第一张牌
        if (!(thePile.empty()))
            return (Card) thePile.peek();
        else
            return null;
    }

    public boolean isEmpty() {
        return thePile.empty();
    }

    public Card pop() {
        try {
            return (Card) thePile.pop();

        } catch (EmptyStackException e) {
            return null;
        }
    }

    public Card peek(){
        if (thePile.empty())return null;
        return (Card)thePile.peek();
    }


    public boolean includes(int tx, int ty) { //判断位置是否在合适的区域内
        return this.x <= tx && tx <= this.x + Card.width &&
                this.y <= ty && ty <= this.y + Card.height;
    }

    public boolean isSame(CardPile pile){//判断两个牌堆是否同一个牌堆
        return this.x<=pile.x&&pile.x<=this.x+Card.width&&this.y<=pile.y&&pile.y<=this.y+Card.height;
    }

    /**
     * 选择牌堆顶的一张牌
     * @param tx
     * @param ty
     * @return
     */
    public int select(int tx, int ty) {
        if (includes(tx, ty)) {
            if (isEmpty())
                return -2;
            else
                return thePile.size() - 1;
        } else
            return -1;
    }

    public void addCard(Object card) {
        thePile.push((Card) card);
    }

    public void addSingleCard(Object card){

    }

    public boolean isCanAdd(Card card) {
        return false;
    }

    public void display(Graphics g) {

        if (isEmpty()) { // 如果牌堆为空
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); // 开启抗锯齿
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.2f)); // 设置透明度
            g2d.setPaint(Color.WHITE); // 设置填充颜色为白色
            g2d.fillRect(x, y, Card.width, Card.height); // 填充矩形
            g2d.setColor(Color.GRAY); // 设置边框颜色为灰色
            g2d.setStroke(new BasicStroke(2)); // 设置边框宽度为2
            g2d.drawRect(x, y, Card.width, Card.height); // 绘制边框
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f)); // 重置透明度
        } else { // 如果牌堆不为空
            if(top()==null){ // 如果牌堆顶部的卡片为空
                System.out.println( this.getClass().toString()); // 打印当前类的名称
                System.out.println(isEmpty()); // 打印牌堆是否为空
            }
            top().setX(x); // 设置牌堆顶部卡片的x坐标
            top().setY(y); // 设置牌堆顶部卡片的y坐标
            top().draw(g); // 绘制牌堆顶部的卡片
        }
    }

}

