package solitaire.game;

import javax.swing.*;

public class Main extends JFrame{
    private Solitaire sp;
	public Main(){
		setSize(1366, 805); // 设置窗口大小为1366x735
		setTitle("Solitaire Game"); // 设置窗口标题为"Solitaire Game"
		setLayout(null); // 设置布局管理器为null，即绝对布局
		setDefaultCloseOperation(DISPOSE_ON_CLOSE); // 设置窗口关闭操作为DISPOSE_ON_CLOSE，即关闭窗口时销毁窗口并释放内存

		setResizable(false); // 设置窗口为不可调整大小

		sp = new Solitaire(); // 创建一个Solitaire对象
		add(sp); // 将Solitaire对象添加到窗口中
		// 将菜单栏设置为JFrame的菜单栏
		JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(sp);
		frame.setJMenuBar(sp.menuBar);
		setVisible(true); // 设置窗口为可见

	}
	public static void main(String[] args) {
		Main main = new Main();
	}

}
