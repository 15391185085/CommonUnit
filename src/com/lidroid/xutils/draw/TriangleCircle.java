package com.lidroid.xutils.draw;
import com.lidroid.xutils.util.StringUtil;

public class TriangleCircle {


	public static String drawCircle(float xc, float yc, float r) {
		int angle = 10;
		float m = 360/angle;
		StringBuffer sb = new StringBuffer();
		
		for (int i = 0; i < m; i++) {
			float x = xc + (float) (r*Math.sin(Math.toRadians(angle*i)));
			float y = yc + (float) (r*Math.cos(Math.toRadians(angle*i)));
			sb.append(x + " " + y + ",");
		}
		return StringUtil.deleteLastCharacter(sb);
	}

}
