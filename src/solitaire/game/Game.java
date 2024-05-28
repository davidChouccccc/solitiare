package solitaire.game;


import solitaire.card.Card;
import solitaire.pile.*;

import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;
//逻辑控制
public class Game {
    static public ArrayList<Card> allCard;
    static public CardPile allPiles[];
    static public DeckPile deckPile;//发牌堆
    static public DiscardPile discardPile;//弃牌堆
    static public TablePile tablePile[];//桌面上的牌堆
    static public SuitPile suitPile[]; //4个存放可以匹配的牌堆
    static public MoveCardPile moveCard;
    static public Stack<UsedPile> usedPile;//用过的牌堆，用于撤销

    static {
        init();
    }

    static void init(){
        int paddingY = 40;

        // 初始化所有牌
        allCard = new ArrayList<Card>();
        for (int i = 0; i < 4; i++)
            for (int j = 0; j <= 12; j++)
                allCard.add(new Card(j, i)); // 为每种花色的每个数字创建一张卡片并添加到allCard列表中
        Random generator = new Random();
        // 洗牌
        for (int i = 0; i < 52; i++) {
            int j = Math.abs(generator.nextInt() % 52); // 生成一个0到51的随机数
            // 交换两张牌的值
            Card temp = allCard.get(i);
            allCard.set(i, allCard.get(j));
            allCard.set(j, temp); // 交换allCard列表中的第i张和第j张卡片
        }
        // 初始化各个牌堆
        allPiles = new CardPile[13];
        suitPile = new SuitPile[4];
        tablePile = new TablePile[7];

        allPiles[0] = deckPile = new DeckPile(200, paddingY); // 初始化发牌堆
        allPiles[1] = discardPile = new DiscardPile(200 + Card.width + 50, paddingY); // 初始化弃牌堆
        for (int i = 0; i < 4; i++)
            allPiles[2 + i] = suitPile[i] = new SuitPile(200 + Card.width + 50 + Card.width + 150 + (40 + Card.width) * i, paddingY);
            // 初始化四个存放可以匹配的牌堆
        for (int i = 0; i < 7; i++)
            allPiles[6 + i] = tablePile[i] = new TablePile(200 + (50 + Card.width) * i, paddingY + Card.height + paddingY, i);
            // 初始化七个桌面上的牌堆
        for (int i = 0; i < 7; i++) {
            ArrayList<Card> al = new ArrayList<Card>();
            for (int j = 0; j < tablePile[i].getCardNum(); j++) {
                al.add(allCard.remove(allCard.size() - 1));
                // 从allCard列表的末尾移除卡片并添加到al列表中
            }
            tablePile[i].addCard(al); // 将al列表中的卡片添加到第i个桌面牌堆中
            tablePile[i].setCardNum(tablePile[i].getNotFlipNum() + 1); // 设置第i个桌面牌堆的卡片数量
            tablePile[i].top().setFaceup(true); // 将第i个桌面牌堆顶部的卡片设置为面朝上
        }
        int rest = allCard.size();
        for (int i = 0; i < rest; i++) {
            deckPile.addCard(allCard.remove(allCard.size() - 1)); // 从allCard列表的末尾移除卡片并添加到发牌堆中
        }
        moveCard = new MoveCardPile(); // 初始化移动卡片堆
        usedPile=new Stack<>(); // 初始化用过的牌堆，用于撤销
    }

    public static void transferFromDiscardToDeck() { //把弃牌堆重新转化为发牌堆

        while (!(discardPile.isEmpty())) {
            Card card = discardPile.pop();
            card.setFaceup(false);
            deckPile.addCard(card);

        }
    }

    public static TablePile findTablePile(int x,int y){
        for (int i=0;i<7;i++){
            if (tablePile[i].includes(x,y))return tablePile[i];
        }
        return null;
    }

    // 定义一个静态方法，用于查找指定坐标(x, y)所在的SuitPile
    public static SuitPile findSuitPile(int x,int y){
        // 遍历所有的SuitPile
        for (int i=0;i<4;i++){
            // 如果当前SuitPile包含指定的坐标，则返回该SuitPile
            if (suitPile[i].includes(x,y))return suitPile[i];
        }
        // 如果没有找到包含指定坐标的SuitPile，则返回null
        return null;
    }

    // 定义一个静态方法，用于查找指定CardPile的上一个CardPile
    public static CardPile findLastPile(CardPile pile){
        // 如果指定的CardPile是TablePile
        if (pile.getType()==CardPile.TABLE_PILE){
            // 遍历所有的TablePile
            for (int i=0;i<7;i++){
                // 如果当前TablePile与指定的CardPile相同，则返回该TablePile
                if (tablePile[i].isSame(pile))return tablePile[i];
            }
        }
        // 如果指定的CardPile是SuitPile
        else if(pile.getType()==CardPile.SUIT_PILE){
            // 遍历所有的SuitPile
            for (int i=0;i<4;i++){
                // 如果当前SuitPile与指定的CardPile相同，则返回该SuitPile
                if (suitPile[i].isSame(pile))return suitPile[i];
            }
        }
        // 如果指定的CardPile是DiscardPile，则返回DiscardPile
        else if(pile.getType()==CardPile.DISCARD_PILE)return discardPile;
        // 如果指定的CardPile是DeckPile，则返回DeckPile
        else if (pile.getType()==CardPile.DECK_PILE)return deckPile;
        // 如果没有找到与指定CardPile相同的CardPile，则返回null
        return null;
    }


    public static void undo(){ //定义一个静态方法，用于撤销上一步操作，可以连续撤销
        if (usedPile.isEmpty()){ //如果用过的牌堆为空
            return; //直接返回，不执行任何操作
        }

        UsedPile pile=usedPile.pop(); //从用过的牌堆中弹出最后一张牌
        if(pile.usedPile.getType()==CardPile.TABLE_PILE){ //如果这张牌来自桌面牌堆

            TablePile tp=findTablePile(pile.nowX,pile.nowY); //找到这张牌当前所在的桌面牌堆
            if (tp!=null){ //如果找到了这张牌
                CardPile last=findLastPile(pile.lastPile); //找到这张牌上一次所在的牌堆
                if (last!=null){ //如果找到了这张牌的上一次所在的牌堆
                    int count=pile.thePile.size(); //获取这张牌的数量
                    Stack<Card> stack=new Stack<>(); //创建一个新的牌堆
                    while (count>0){ //当牌的数量大于0时
                        count--; //牌的数量减1
                        Card t=tp.pop(); //从桌面牌堆中弹出一张牌
                        stack.push(t); //将这张牌压入新的牌堆

                    }
                    if (last.getType()==CardPile.TABLE_PILE) { //如果这张牌的上一次所在的牌堆是桌面牌堆
                        if (!last.isEmpty()) { //如果这个牌堆不为空
                            if (((TablePile)last).getNotFlipNum()==((TablePile) last).getCardNum()-1&&!pile.isIsup()){
                                last.top().setFaceup(false); //将这个牌堆顶部的牌设置为面朝下
                                ((TablePile) last).setNotFlipNum(((TablePile) last).getNotFlipNum() + 1);
                                //将这个牌堆的未翻转牌的数量加1
                            }
                        }
                    }else if(last.getType()==CardPile.DISCARD_PILE){ //如果这张牌的上一次所在的牌堆是弃牌堆
                        discardPile.addCard(stack.pop()); //将新的牌堆顶部的牌添加到弃牌堆中
                        return; //返回，结束方法
                    }

                    while (!stack.empty()){ //当新的牌堆不为空时
                        last.addSingleCard(stack.pop()); //将新的牌堆顶部的牌添加到上一次的牌堆中
                    }
                }
            }
        }
        else if(pile.usedPile.getType()==CardPile.DECK_PILE){ //如果这张牌来自发牌堆
            //此处省略了具体的操作，因为不可能有操作将牌放回发牌堆
        }
        else if(pile.usedPile.getType()==CardPile.DISCARD_PILE){ //如果这张牌来自弃牌堆
            Card temp=discardPile.pop(); //从弃牌堆中弹出一张牌
            if (temp!=null) { //如果弹出的牌不为空
                temp.setFaceup(false); //将这张牌设置为面朝下
                deckPile.addCard(temp); //将这张牌添加到发牌堆中
            }
        }
        else if(pile.usedPile.getType()==CardPile.SUIT_PILE){ //如果这张牌来自结果牌堆
            if (pile.lastPile.getType()==CardPile.DISCARD_PILE){ //如果这张牌的上一次所在的牌堆是弃牌堆
                discardPile.addCard(findSuitPile(pile.nowX,pile.nowY).pop()); //将这张牌添加到弃牌堆中
            }
            else if(pile.lastPile.getType()==CardPile.TABLE_PILE){ //如果这张牌的上一次所在的牌堆是桌面牌堆
                if(findTablePile(pile.x,pile.y).getNotFlipNum()==findTablePile(pile.x,pile.y).getCardNum()-1){
                    //如果这张牌的上一次所在的牌堆的未翻转牌的数量等于牌的数量减1
                    findTablePile(pile.x,pile.y).top().setFaceup(false);
                    //将这个牌堆顶部的牌设置为面朝下
                    findTablePile(pile.x,pile.y).setNotFlipNum(findTablePile(pile.x,pile.y).getNotFlipNum()+1);
                    //将这个牌堆的未翻转牌的数量加1
                }
                findTablePile(pile.x,pile.y).addSingleCard(findSuitPile(pile.nowX,pile.nowY).pop());
                //将这张牌添加到桌面牌堆中
            }
            else if(pile.lastPile.getType()==CardPile.SUIT_PILE){ //如果这张牌的上一次所在的牌堆是结果牌堆
                findSuitPile(pile.x,pile.y).addCard(findSuitPile(pile.nowX,pile.nowY).pop());
                //将这张牌添加到结果牌堆中
            }
        }
    }
    public static void cheat(){//作弊，使牌桌出现只差一步就能获胜的局面
        int paddingY = 40;

        // 初始化所有牌
        allCard = new ArrayList<Card>();
        for (int i = 0; i < 4; i++)
            for (int j = 12; j >= 0; j--)
                allCard.add(new Card(j, i)); // 为每种花色的每个数字创建一张卡片并添加到allCard列表中

        // 初始化各个牌堆
        allPiles = new CardPile[13];
        suitPile = new SuitPile[4];
        tablePile = new TablePile[7];

        allPiles[0] = deckPile = new DeckPile(200, paddingY); // 初始化发牌堆
        allPiles[1] = discardPile = new DiscardPile(200 + Card.width + 50, paddingY); // 初始化弃牌堆
        for (int i = 0; i < 4; i++)
            allPiles[2 + i] = suitPile[i] = new SuitPile(200 + Card.width + 50 + Card.width + 150 + (40 + Card.width) * i, paddingY);
        // 初始化四个存放可以匹配的牌堆
        for (int i = 0; i < 7; i++)
            allPiles[6 + i] = tablePile[i] = new TablePile(200 + (50 + Card.width) * i, paddingY + Card.height + paddingY, i);
        //按顺序将所有拍放入suitPile
        for (int i = 0; i < 4; i++) {
            for (int j = 12; j >= 0; j--) {
                suitPile[i].addCard(allCard.remove(allCard.size() - 1));
                //正面朝上
                suitPile[i].top().setFaceup(true);
            }
        }
        //移除最后一个suitPile的最后一张牌
        //将这张牌放到tablePile的第一堆
        ArrayList<Card> al = new ArrayList<Card>();
        al.add(suitPile[3].pop());
        tablePile[0].addCard(al); // 将al列表中的卡片添加到第0个桌面牌堆中
        tablePile[0].setCardNum(1); // 设置第0个桌面牌堆的卡片数量
        tablePile[0].top().setFaceup(true); // 将第0个桌面牌堆顶部的卡片设置为面朝上
        // 初始化七个桌面上的牌堆
        for (int i = 1; i < 7; i++) {
            al = new ArrayList<Card>();
            tablePile[i].addCard(al); // 将al列表中的卡片添加到第i个桌面牌堆中
            tablePile[i].setCardNum(0); // 设置第i个桌面牌堆的卡片数量
        }
        moveCard = new MoveCardPile(); // 初始化移动卡片堆
        usedPile=new Stack<>(); // 初始化用过的牌堆，用于撤销

    }



    public static boolean testDeckPile(int x, int y) {
        int selectNum = deckPile.select(x, y); // 从发牌堆中选择一张牌，返回选择的牌的索引，如果没有选择到牌，则返回-1或-2
        if (selectNum >= 0) { // 如果选择到了牌
            Card c=deckPile.pop(); // 从发牌堆中弹出一张牌
            discardPile.addCard(c); // 将这张牌添加到弃牌堆中

            // 记录到使用牌堆
            UsedPile up=new UsedPile(x,y,deckPile); // 创建一个新的UsedPile对象，记录这张牌的位置和来源
            up.setUsedPile(discardPile,discardPile.x,discardPile.y); // 设置这张牌的目标牌堆为弃牌堆
            up.addCard(c); // 将这张牌添加到UsedPile对象中
            usedPile.push(up); // 将这个UsedPile对象添加到usedPile堆栈中

            return true; // 返回true，表示操作成功
        } else if (selectNum == -2) { // 如果选择的结果为-2，表示发牌堆已经空了，需要将弃牌堆的牌重新放回发牌堆
            Game.transferFromDiscardToDeck(); // 将弃牌堆的牌重新放回发牌堆
            return true; // 返回true，表示操作成功
        } else { // 如果选择的结果为-1，表示没有选择到牌
            return false; // 返回false，表示操作失败
        }
    }

    //选中结果牌堆
    public static boolean testSuitPile(int x, int y) { // 定义一个静态方法，用于测试指定坐标(x, y)是否在SuitPile中
        for (int i = 0; i < 4; i++) { // 遍历所有的SuitPile
            int selectNum = suitPile[i].select(x, y); // 在当前SuitPile中选择一张牌，返回选择的牌的索引，如果没有选择到牌，则返回-1
            if (selectNum >= 0) { // 如果选择到了牌
                moveCard.clear(); // 清空moveCard
                Card t = suitPile[i].pop(); // 从当前SuitPile中弹出一张牌
                moveCard.addCard(t); // 将这张牌添加到moveCard中
                moveCard.setFromPile(suitPile[i]); // 设置moveCard的来源为当前SuitPile

                // 记录
                UsedPile up = new UsedPile(x, y, suitPile[i]); // 创建一个新的UsedPile对象，记录这张牌的位置和来源
                up.addCard(t); // 将这张牌添加到UsedPile对象中
                usedPile.push(up); // 将这个UsedPile对象添加到usedPile堆栈中

                return true; // 返回true，表示操作成功
            }
        }

        return false; // 如果没有选择到牌，则返回false，表示操作失败
    }

    public static boolean testDisCardPile(int x, int y) {
        int selectNum = discardPile.select(x, y); // 在弃牌堆中选择一张牌，返回选择的牌的索引，如果没有选择到牌，则返回-1或-2
        if (selectNum >= 0) { // 如果选择到了牌
            moveCard.clear(); // 清空moveCard
            Card c=discardPile.pop(); // 从弃牌堆中弹出一张牌
            moveCard.addCard(c); // 将这张牌添加到moveCard中
            moveCard.setFromPile(discardPile); // 设置moveCard的来源为弃牌堆

            // 记录
            UsedPile up=new UsedPile(x,y,discardPile); // 创建一个新的UsedPile对象，记录这张牌的位置和来源
            up.addCard(c); // 将这张牌添加到UsedPile对象中
            usedPile.push(up); // 将这个UsedPile对象添加到usedPile堆栈中

            return true; // 返回true，表示操作成功
        } else if (selectNum == -2) {
            // 如果选择的结果为-2，此处没有具体的操作
        } else {
            // 如果选择的结果为-1，表示没有选择到牌，此处没有具体的操作
        }
        return false; // 如果没有选择到牌，则返回false，表示操作失败
    }

    public static boolean testTablePile(int x, int y) {
        boolean isDrag = false; // 定义一个布尔变量，用于标记是否可以拖动
        for (int i = 0; i < tablePile.length; i++) { // 遍历所有的桌面牌堆
            int selectNum = tablePile[i].select(x, y); // 在当前桌面牌堆中选择一张牌，返回选择的牌的索引，如果没有选择到牌，则返回-1
            if (selectNum >= 0) { // 如果选择到了牌
                moveCard.clear(); // 清空moveCard

                int num = tablePile[i].getCardNum(); // 获取当前桌面牌堆的卡片数量
                UsedPile up=new UsedPile(x,y,tablePile[i]); // 创建一个新的UsedPile对象，记录这张牌的位置和来源
                for (int j = selectNum; j < num; j++) { // 从选择的牌开始，到当前桌面牌堆的末尾

                    Card temp=tablePile[i].pop(); // 从当前桌面牌堆中弹出一张牌
                    up.addCard(temp); // 将这张牌添加到UsedPile对象中
                    moveCard.addCard(temp); // 将这张牌添加到moveCard中
                }
                // 如果选择的牌的索引加上当前桌面牌堆的未翻转牌的数量小于当前桌面牌堆的卡片数量，记录是否撤回时该朝上
                if (selectNum+tablePile[i].getNotFlipNum()<tablePile[i].getCardNum())up.setIsup(true);
                moveCard.setFromPile(tablePile[i]); // 设置moveCard的来源为当前桌面牌堆
                usedPile.push(up); // 将这个UsedPile对象添加到usedPile堆栈中

                return true; // 返回true，表示操作成功
            } else {
                // 如果没有选择到牌，此处没有具体的操作
            }
        }

        return isDrag; // 如果没有选择到牌，则返回isDrag，表示操作失败
    }

    public static boolean isCanAddToSuitPile(int x, int y) {
        if (moveCard.size() == 1) { // 如果移动的牌的数量等于1
            for (int i = 0; i < 4; i++) { // 遍历所有的结果牌堆
                if (suitPile[i].includes(x, y)) { // 如果当前结果牌堆包含指定的坐标
                    if (suitPile[i].isCanAdd(moveCard.getCard())) { // 如果当前结果牌堆可以添加移动的牌
                        suitPile[i].addCard(moveCard.removeCard()); // 从移动的牌中移除一张牌并添加到当前结果牌堆中
                        usedPile.peek().setUsedPile(suitPile[i],x,y); // 记录这个操作
                        return true; // 返回true，表示操作成功
                    }
                }
            }
        }

        return false; // 如果移动的牌的数量不等于1或者没有找到可以添加的结果牌堆，则返回false，表示操作失败
    }

    public static boolean isCanAddtoTablePile(int x, int y) {
        for (int i = 0; i < 7; i++) { // 遍历所有的桌面牌堆
            if (tablePile[i].includes(x, y)) { // 如果当前桌面牌堆包含指定的坐标
                if (tablePile[i].hashCode() != moveCard.getFromPile().hashCode()) { // 如果当前桌面牌堆不是移动的牌的来源牌堆
                    if (tablePile[i].isCanAdd(moveCard.getCard())) { // 如果当前桌面牌堆可以添加移动的牌
                        tablePile[i].addCard(moveCard.clear()); // 清空移动的牌，并将清空的牌添加到当前桌面牌堆中
                        usedPile.peek().setUsedPile(tablePile[i],x,y); // 记录这个操作
                        return true; // 返回true，表示操作成功
                    }
                }
            }
        }
        return false; // 如果没有找到可以添加的桌面牌堆，则返回false，表示操作失败
    }

    public static boolean isWin() { // 定义一个静态方法，用于判断是否胜利
        for (int i = 0; i < 4; i++) { // 遍历所有的结果牌堆
            if (suitPile[i].size() < 13) return false; // 如果当前结果牌堆的牌的数量小于13，表示还没有胜利，返回false
        }
        return true; // 如果所有的结果牌堆的牌的数量都等于13，表示已经胜利，返回true
    }

    public static void refreshTablePile() { // 定义一个静态方法，用于刷新桌面牌堆

        for (int i = 0; i < 7; i++) { // 遍历所有的桌面牌堆
            if (tablePile[i].top() != null) // 如果当前桌面牌堆的顶部有牌
                if (!(tablePile[i].top().isFaceup())) { // 如果当前桌面牌堆顶部的牌是面朝下的
                    tablePile[i].top().setFaceup(true); // 将这张牌翻转为面朝上
                    tablePile[i].setNotFlipNum(tablePile[i].getNotFlipNum() - 1); // 将当前桌面牌堆的未翻转牌的数量减1
                }
        }
    }

    public static void returnToFromPile() {
        if (moveCard.getFromPile() != null) // 如果移动的牌的来源牌堆不为空
            if (moveCard.getFromPile().hashCode() == discardPile.hashCode()) { // 如果移动的牌的来源牌堆是弃牌堆
                while (!(moveCard.isEmpty())) { // 当移动的牌不为空时
                    moveCard.getFromPile().addCard(moveCard.removeCard()); // 从移动的牌中移除一张牌并添加到来源牌堆中
                }
            } else { // 如果移动的牌的来源牌堆不是弃牌堆
                ArrayList<Card> temp = moveCard.clear(); // 清空移动的牌，并将清空的牌保存到临时列表中
                if (moveCard.getFromPile().getClass().equals(SuitPile.class)) { // 如果移动的牌的来源牌堆是结果牌堆
                    Card t = temp.remove(0); // 从临时列表中移除第一张牌
                    moveCard.getFromPile().addCard(t); // 将移除的牌添加到来源牌堆中
                } else { // 如果移动的牌的来源牌堆不是结果牌堆
                    moveCard.getFromPile().addCard(temp); // 将临时列表中的所有牌添加到来源牌堆中
                }
            }
    }

    /**
     * 判断当前鼠标hover位置能否移动这张牌
     */
    public static boolean isCanChoose(int x, int y) {
        for(int i = 0; i < allPiles.length; i++){
            if(allPiles[i].select(x, y) >= 0)return true;
        }
        return false;
    }
}

