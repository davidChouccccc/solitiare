package solitaire.pile;


import solitaire.card.Card;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class DeckPile extends CardPile {

	public DeckPile (int x, int y) {
			
		super(x, y);
		this.setType(DECK_PILE);
		
	}

	@Override
	public void display(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;

		// 开启抗锯齿
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		// 提高渲染质量
		g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		if (isEmpty()) {
			Image image = null; // 初始化图像为空
			try {
				String picture = "poker/return.png"; // 设置图片路径
				image = ImageIO.read(new File(picture)); // 从文件中读取图片
			} catch (IOException e) { // 捕获可能的输入输出异常
				e.printStackTrace(); // 打印异常堆栈信息
				System.out.println("class-CardPile-display-if(isEmpty())"); // 打印错误信息
			}
			g2d.drawImage(image, this.x, this.y, Card.width, Card.height, null); // 在指定位置绘制图片
		} else {
			super.display(g);
		}
	}
	

}
