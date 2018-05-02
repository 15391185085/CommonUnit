package com.lidroid.xutils.draw;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.lidroid.xutils.util.StringUtil;

/**
 * 
 * @author 李昊翔
 * 
 */
public class MiddleCircle {
	private static class Circle {

		private float y;
		private float x;

		public Circle(float x, float y) {
			super();
			this.y = y;
			this.x = x;
		}

		public float getY() {
			return y;
		}

		public float getX() {
			return x;
		}

		@Override
		public String toString() {
			return x + " " + y + ",";
		}

	}
	/**
	 * 中点画圆法
	 * 
	 * @author 李昊翔
	 * @param xc
	 *            中点x坐标
	 * @param yc
	 *            中点y坐标
	 * @param r
	 *            半径
	 * @return
	 */
	public static void drawCircle(float xc, float yc, float r) {
		List<Circle> l = MiddleCircle.MP_Circle(r);
		List<Circle> l1 = new ArrayList<Circle>();
		List<Circle> l2 = new ArrayList<Circle>();
		List<Circle> l3 = new ArrayList<Circle>();
		List<Circle> l4 = new ArrayList<Circle>();
		List<Circle> l5 = new ArrayList<Circle>();
		List<Circle> l6 = new ArrayList<Circle>();
		List<Circle> l7 = new ArrayList<Circle>();
		List<Circle> l8 = new ArrayList<Circle>();

		int m = l.size();
		for (int i = 0; i < m-1; i++) {
			float x = l.get(i).getX();
			float y = l.get(i).getY();
			l1.add(new Circle(x + xc, y + yc));
		}
		for (int i = m - 2; i >= 0; i--) {
			float x = l.get(i).getX();
			float y = l.get(i).getY();
			l2.add(new Circle(y + xc, x + yc));
		}
		for (int i = 0; i < m-1; i++) {
			float x = l.get(i).getX();
			float y = l.get(i).getY();
			l3.add(new Circle(y + xc, -x + yc));
		}
		for (int i = m - 2; i >= 0; i--) {
			float x = l.get(i).getX();
			float y = l.get(i).getY();
			l4.add(new Circle(x + xc, -y + yc));
		}
		for (int i = 0; i < m-1; i++) {
			float x = l.get(i).getX();
			float y = l.get(i).getY();
			l5.add(new Circle(-x + xc, -y + yc));
		}
		for (int i = m - 2; i >= 0; i--) {
			float x = l.get(i).getX();
			float y = l.get(i).getY();
			l6.add(new Circle(-y + xc, -x + yc));
		}
		for (int i = 0; i < m-1; i++) {
			float x = l.get(i).getX();
			float y = l.get(i).getY();
			l7.add(new Circle(-y + xc, x + yc));
		}
		for (int i = m - 2; i >= 0; i--) {
			float x = l.get(i).getX();
			float y = l.get(i).getY();
			l8.add(new Circle(-x + xc, y + yc));
		}

		String circle = MiddleCircle.toListCircle(l1) + ", \n"
				+ MiddleCircle.toListCircle(l2) + ", \n"
				+ MiddleCircle.toListCircle(l3) + ", \n"
				+ MiddleCircle.toListCircle(l4) + ", \n"
				+ MiddleCircle.toListCircle(l5) + ", \n"
				+ MiddleCircle.toListCircle(l6) + ", \n"
				+ MiddleCircle.toListCircle(l7) + ", \n"
				+ MiddleCircle.toListCircle(l8);
		System.out.println(circle);
	}

	private static List<Circle> MP_Circle(float r) {
		List<Circle> l = new ArrayList<Circle>();
		float x, y;
		float d;
		float m = 1;
		x = 0;
		y = r;
		d = (float) (5.0 / 4 - r);
		while (y > x) {
			if (d < 0) {
				d += x * 2.0 + 3;
				x= x+m;
			} else {
				d += (x - y) * 2.0 + 5;
				x= x+m;
				y= y-m;
			}
			l.add(new Circle(x, y));

		}
		return l;
	}

	private static String toListCircle(List<Circle> l) {
		StringBuffer sb = new StringBuffer();
		for (Iterator iterator = l.iterator(); iterator.hasNext();) {
			Circle circle = (Circle) iterator.next();
			sb.append(circle.toString());
		}
		return StringUtil.deleteLastCharacter(sb);
	}


}
