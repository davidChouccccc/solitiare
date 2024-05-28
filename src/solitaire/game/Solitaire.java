package solitaire.game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
//操作控制
public class Solitaire extends JPanel implements MouseListener, ActionListener,MouseMotionListener {

	private boolean isDrag = false;
	private int x;//mouse
	private int y;//mouse
    PopupMenu menu;
	JMenuBar menuBar;

	/**
	 * Solitaire的构造函数，初始化游戏界面和右键菜单选项。
	 */
	public Solitaire() {
		setSize(1366, 805); // 设置面板大小为1366x700
		setLayout(null); // 设置布局管理器为null，即绝对布局
		addMouseListener (this); // 添加鼠标监听器
		addMouseMotionListener(this); // 添加鼠标移动监听器

		menu=new PopupMenu(); // 创建一个新的弹出菜单
		MenuItem item=new MenuItem("undo"); // 创建一个新的菜单项，标签为"undo"
		MenuItem item2=new MenuItem("New game"); // 创建一个新的菜单项，标签为"New game"
		item.addActionListener(new ActionListener() { // 为"undo"菜单项添加动作监听器
			@Override
			public void actionPerformed(ActionEvent e) {
				Game.undo(); // 当点击"undo"菜单项时，执行Game类的undo方法
				repaint(); // 重绘面板
			}
		});

		item2.addActionListener(new ActionListener() { // 为"New game"菜单项添加动作监听器
			@Override
			public void actionPerformed(ActionEvent e) {
				Game.init(); // 当点击"New game"菜单项时，执行Game类的init方法
				repaint(); // 重绘面板
			}
		});
		menu.add(item); // 将"undo"菜单项添加到弹出菜单中
		menu.add(item2); // 将"New game"菜单项添加到弹出菜单中
		add(menu); // 将弹出菜单添加到面板中


		// 创建一个新的JPanel对象，用于存放按钮
		JPanel buttonPanel = new JPanel();
		// 设置JPanel的布局管理器为BoxLayout，使得按钮可以从左到右排列
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));

		// 创建一个固定宽度的不可见组件，用于设置按钮之间的间距
		Component horizontalStrut = Box.createHorizontalStrut(10);
		buttonPanel.add(horizontalStrut); // 将不可见组件添加到JPanel中

		// 创建"撤销"按钮
		JButton undoButton = new JButton("Undo");
		undoButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Game.undo(); // 当点击"Undo"按钮时，执行Game类的undo方法
				repaint(); // 重绘面板
			}
		});
		undoButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				undoButton.setBackground(Color.GRAY); // 当鼠标hover时，改变按钮的背景色
			}

			@Override
			public void mouseExited(MouseEvent e) {
				undoButton.setBackground(UIManager.getColor("control")); // 当鼠标离开时，恢复按钮的背景色
			}
		});
		buttonPanel.add(undoButton); // 将"Undo"按钮添加到JPanel中

		// 创建一个固定宽度的不可见组件，用于设置按钮之间的间距
		Component horizontalStrut2 = Box.createHorizontalStrut(10);
		buttonPanel.add(horizontalStrut2); // 将不可见组件添加到JPanel中

		// 创建"重新开始"按钮
		JButton newGameButton = new JButton("New Game");
		newGameButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Game.init(); // 当点击"New Game"按钮时，执行Game类的init方法
				repaint(); // 重绘面板
			}
		});
		newGameButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				newGameButton.setBackground(Color.GRAY); // 当鼠标hover时，改变按钮的背景色
			}

			@Override
			public void mouseExited(MouseEvent e) {
				newGameButton.setBackground(UIManager.getColor("control")); // 当鼠标离开时，恢复按钮的背景色
			}
		});
		buttonPanel.add(newGameButton); // 将"New Game"按钮添加到JPanel中

		// 创建一个固定宽度的不可见组件，用于设置按钮之间的间距
		Component horizontalStrut3 = Box.createHorizontalStrut(10);
		buttonPanel.add(horizontalStrut3); // 将不可见组件添加到JPanel中

		// 创建"Cheat"按钮
		JButton cheatButton = new JButton("Cheat");
		cheatButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Game.cheat();
				repaint(); // 重绘面板
			}
		});
		cheatButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				cheatButton.setBackground(Color.GRAY); // 当鼠标hover时，改变按钮的背景色
			}

			@Override
			public void mouseExited(MouseEvent e) {
				cheatButton.setBackground(UIManager.getColor("control")); // 当鼠标离开时，恢复按钮的背景色
			}
		});
		buttonPanel.add(cheatButton); // 将"Cheat"按钮添加到JPanel中


		// 创建一个新的菜单栏
		menuBar = new JMenuBar();
		menuBar.add(buttonPanel); // 将JPanel添加到JMenuBar中


	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;

		int panelWidth = getWidth();
		int panelHeight = getHeight();

		// 创建一个径向渐变，中心在面板中心，半径为面板宽度的一半
		// 颜色从深绿色渐变到黑色
		RadialGradientPaint rgp = new RadialGradientPaint(
				panelWidth * 2 / 3, panelHeight * 2 / 3, panelWidth * 2 / 3,
				new float[]{0.0f, 1.0f},
//				new Color[]{new Color(0xbb7ff3), new Color(0xDE6C92)}
				new Color[]{new Color(0xbb7ff3), new Color(0x6a60a9)}
//				new Color[]{new Color(0x20b152), new Color(0x095925)}
		);

		g2d.setPaint(rgp);
		g2d.fillRect(0, 0, panelWidth, panelHeight);

		for (int i = 0; i < 13; i++) // 遍历所有的牌堆
			Game.allPiles[i].display(g); // 调用每个牌堆的display方法，将其绘制到面板上
		Game.moveCard.display(g, x, y); // 调用moveCard的display方法，将其绘制到面板上，位置为鼠标当前位置
	}

	@Override
	public void actionPerformed(ActionEvent e) {}

	@Override
	public void mouseClicked(MouseEvent e) {
	    this_mousePressed(e);

    }

    void this_mousePressed(MouseEvent e) {
        int mods=e.getModifiers();//鼠标右键
        if((mods&InputEvent.BUTTON3_MASK)!=0){//弹出菜单
            menu.show(this,e.getX(),e.getY());
        }
    }

	/**
	 * 实现的mousePressed方法，用于处理鼠标按下事件。
	 * @param e 鼠标事件对象。
	 */
    @Override
	public void mousePressed(MouseEvent e) {

		x = e.getX(); // 获取鼠标点击的x坐标
		y = e.getY(); // 获取鼠标点击的y坐标
		isDrag = false; // 初始化拖动状态为false
		boolean isSelect = false; // 初始化选择状态为false
		isSelect = Game.testDeckPile(x,y); // 检查是否选择了发牌堆
		if(!isSelect){ // 如果没有选择牌堆
			isSelect = Game.testDisCardPile(x, y); // 检查是否选择了弃牌堆
			if(isSelect){ // 如果选择了弃牌堆
				isDrag = true; // 设置拖动状态为true
			}
			if(!isSelect){ // 如果没有选择弃牌堆
				isSelect = Game.testTablePile(x, y); // 检查是否选择了桌面牌堆
				if(isSelect) { // 如果选择了桌面牌堆
					isDrag = true; // 设置拖动状态为true
				}
//				else{ // 如果没有选择桌面牌堆
//					Game.testSuitPile(x,y); // 检查是否选择了结果牌堆
//					isDrag=true; // 设置拖动状态为true
//				}
			}
		}
		isDrag = false; // 重置拖动状态为false
		repaint(); // 重绘面板

	   }

	/**
	 * 实现的mouseReleased方法，用于处理鼠标释放事件。
	 * @param e 鼠标事件对象。
	 */
	@Override
	public void mouseReleased(MouseEvent e) {

		if(isDrag &&  Game.moveCard.size() > 0 ){ // 如果正在拖动并且移动的牌的数量大于0
			boolean isCanAdd =false; // 初始化是否可以添加的标志为false
			isCanAdd = Game.isCanAddToSuitPile(x,y); // 检查是否可以将牌添加到结果牌堆
			if(!isCanAdd) // 如果不能添加到结果牌堆
				isCanAdd = Game.isCanAddtoTablePile(x, y); // 检查是否可以将牌添加到桌面牌堆

			if(!isCanAdd ){ // 如果不能添加到任何牌堆
				Game.usedPile.pop(); // 移除最后一次的操作记录
				Game.returnToFromPile(); // 将牌返回到原来的位置
			}
			else // 如果可以添加
				Game.refreshTablePile(); // 刷新桌面牌堆
			isDrag = false; // 重置拖动状态为false
			repaint(); // 重绘面板
		}
		else{ // 如果没有在拖动
			if(Game.moveCard.size() > 0) { // 如果移动的牌的数量大于0
				Game.usedPile.pop(); // 清除无效记录
				Game.returnToFromPile(); // 将牌返回到原来的位置
			}
			repaint(); // 重绘面板

		}
		if (Game.isWin()){ // 如果游戏胜利
			Toolkit.getDefaultToolkit().beep(); // 发出提示音
			Object[] options = {"重新开始", "退出"};
			JOptionPane pane = new JOptionPane("You win !", JOptionPane.INFORMATION_MESSAGE, JOptionPane.YES_NO_OPTION, null, options, options[0]);
			JDialog dialog = pane.createDialog(this, "");
			dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
			dialog.addWindowListener(new WindowAdapter() {
				public void windowClosing(WindowEvent e) {
					dialog.dispose(); // 关闭对话框
					Game.init(); // 重新开始游戏
					repaint(); // 重绘面板
				}
			});
			dialog.setVisible(true);
			Object selectedValue = pane.getValue();
			if(selectedValue != null && selectedValue.equals("退出")) {
				System.exit(0); // 退出程序
			} else {
				Game.init(); // 重新开始游戏
				repaint(); // 重绘面板
			}
		}
	}

	/**
	 * 实现的mouseEntered方法，用于处理鼠标进入组件的事件。
	 * @param e 鼠标事件对象。
	 */
	@Override
	public void mouseEntered(MouseEvent e) {}

	/**
	 * 实现的mouseExited方法，用于处理鼠标离开组件的事件。
	 * @param e 鼠标事件对象。
	 */
	@Override
	public void mouseExited(MouseEvent e){}

	/**
	 * 实现的mouseDragged方法，用于处理鼠标拖动事件。
	 * @param e 鼠标事件对象。
	 */
	@Override
	public void mouseDragged(MouseEvent e){

		isDrag = true; // 设置拖动状态为true
		x = e.getX(); // 获取鼠标拖动时的x坐标
		y = e.getY(); // 获取鼠标拖动时的y坐标
		repaint(); // 重绘面板

	}

	/**
	 * 实现的mouseMoved方法，用于处理鼠标移动事件。
	 * @param e 鼠标事件对象。
	 */
	@Override
	public void mouseMoved(MouseEvent e) {
		x = e.getX(); // 获取鼠标移动时的x坐标
		y = e.getY(); // 获取鼠标移动时的y坐标
		if(Game.isCanChoose(x,y)) // 如果可以选择
			setCursor(new Cursor(Cursor.HAND_CURSOR)); // 设置鼠标样式为手型
		else // 如果不能选择
			setCursor(new Cursor(Cursor.DEFAULT_CURSOR)); // 设置鼠标样式为默认样式
	}


}
