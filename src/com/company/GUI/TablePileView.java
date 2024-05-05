package com.company.GUI;

import com.company.Card.*;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.StackPane;
public class TablePileView extends StackPane implements GameModelListener
{
    private static final int PADDING = 5;
    private static final int Y_OFFSET = 17;
    private static final String SEPARATOR = ";";
    private int aIndex;
    private static final String BORDER_STYLE = "-fx-border-color: lightgray;"
            + "-fx-border-width: 2.8;" + " -fx-border-radius: 10.0";
    TablePileView(TablePile pIndex)
    {
        aIndex = pIndex.ordinal()+2;//返回枚举类的序数  2-8
        setPadding(new Insets(PADDING));
        setStyle(BORDER_STYLE);
        setAlignment(Pos.TOP_CENTER);
        final ImageView image = new ImageView(CardImages.getBack());
        image.setVisible(false);
        getChildren().add(image);
        buildLayout();
        GameModel.instance().addListener(this);

    }


    private void buildLayout()
    {
        getChildren().clear();
        TableStack stack = (TableStack) GameModel.instance().getStack(aIndex);
        if( stack.isEmpty() )
        {
            ImageView image = new ImageView(CardImages.getBack());
            image.setVisible(false);
            getChildren().add(image);
            return;
        }
        for(int i = 0 ; i< stack.size() ; i ++)
        {
            Card cardView = stack.peek(i);
            if(cardView.isFaceUp() == true)
            { final ImageView image = new ImageView(CardImages.getCard(cardView));
                image.setTranslateY(Y_OFFSET * i);//根据i值设置Y轴的偏移量
                getChildren().add(image);//将image添加到getChildren()中，getChildren()是一个ObservableList<Node>类型的对象，可以添加任何Node类型的对象。
                setOnDragOver(createDragOverHandler(image, cardView));//当你拖动到目标上方的时候，会不停的执行。
                setOnDragEntered(createDragEnteredHandler(image, cardView));// 当你拖动到目标控件的时候，会执行这个事件回调。
                setOnDragExited(createDragExitedHandler(image, cardView));//当你拖动移出目标控件的时候，执行这个操作。
                setOnDragDropped(createDragDroppedHandler(image, cardView));//当你拖动到目标并松开鼠标的时候，执行这个DragDropped事件。
                image.setOnDragDetected(createDragDetectedHandler(image, cardView));//当你从一个Node上进行拖动的时候，会检测到拖动操作，将会执行这个EventHandler。
            }
            else

            {
                final ImageView image = new ImageView(CardImages.getBack());
                image.setTranslateY(Y_OFFSET * i);
                getChildren().add(image);
            }


        }
    }

    private EventHandler<MouseEvent> createDragDetectedHandler(final ImageView pImageView, final Card pCard)
    {//当你从一个Node上进行拖动的时候，会检测到拖动操作，将会执行这个EventHandler。
        return new EventHandler<MouseEvent>()

        {
            @Override
            public void handle(MouseEvent pMouseEvent)
            {
                Dragboard db = pImageView.startDragAndDrop(TransferMode.ANY);
                ClipboardContent content = new ClipboardContent();
                content.putString(GameModel.instance().serialize(pCard,aIndex));
                db.setContent(content);
                pMouseEvent.consume();
                GameModel.instance().setFromIndex(aIndex);

            }
        };
    }

    private EventHandler<DragEvent> createDragOverHandler(final ImageView pImageView, final Card pCard)
    {
        //当你拖动到目标上方的时候，会不停的执行。
        return new EventHandler<DragEvent>()
        {
            @Override
            public void handle(DragEvent pEvent)
            {
                // 如果拖动源不是当前的ImageView并且拖动板上有字符串
                if(pEvent.getGestureSource() != pImageView && pEvent.getDragboard().hasString())
                {
                    // 如果是合法移动
                    if( GameModel.instance().isLegalMove(GameModel.instance().getTop(pEvent.getDragboard().getString()), aIndex) )
                    {
                        // 接受移动
                        pEvent.acceptTransferModes(TransferMode.MOVE);
                    }
                }
                // 消耗事件
                pEvent.consume();
            }
        };
    }

    private EventHandler<DragEvent> createDragEnteredHandler(final ImageView pImageView, final Card pCard)
    {
        // 当你拖动到目标控件的时候，会执行这个事件回调。
        return new EventHandler<DragEvent>()
        {
            @Override
            public void handle(DragEvent pEvent)
            {
                if( GameModel.instance().isLegalMove(GameModel.instance().getTop(pEvent.getDragboard().getString()), aIndex) )
                {
                    // 设置阴影
                    pImageView.setEffect(new DropShadow());


                }
                pEvent.consume();
            }
        };
    }

    private EventHandler<DragEvent> createDragExitedHandler(final ImageView pImageView, final Card pCard)
    {
        //当你拖动移出目标控件的时候，执行这个操作。
        return new EventHandler<DragEvent>()
        {
            @Override
            public void handle(DragEvent pEvent)
            {
                // 移除阴影
                pImageView.setEffect(null);
                pEvent.consume();
            }
        };
    }

    private EventHandler<DragEvent> createDragDroppedHandler(final ImageView pImageView, final Card pCard)
    {
        //当你拖动到目标并松开鼠标的时候，执行这个DragDropped事件。
        return new EventHandler<DragEvent>()
        {
            @Override
            public void handle(DragEvent pEvent)
            {
                Dragboard db = pEvent.getDragboard();
                boolean success = false;
                if(db.hasString())
                {
                    GameModel.instance().store();
                    boolean a = GameModel.instance().moveCard(GameModel.instance().StringToStack(db.getString()), aIndex);
                    System.out.println(a);
                    success = true;
                    ClipboardContent content = new ClipboardContent();
                    content.putString(null);
                    db.setContent(content);
                    }
                pEvent.setDropCompleted(success);
                pEvent.consume();
            }
        };
    }
    //获取移动时最顶端的卡片

    public  void gameStateChanged()
    {
        buildLayout();
    }
}