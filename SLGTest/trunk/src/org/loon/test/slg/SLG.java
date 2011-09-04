package org.loon.test.slg;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import org.loon.framework.android.game.core.EmulatorButtons;
import org.loon.framework.android.game.core.EmulatorListener;
import org.loon.framework.android.game.core.graphics.LColor;
import org.loon.framework.android.game.core.graphics.LFont;
import org.loon.framework.android.game.core.graphics.LImage;
import org.loon.framework.android.game.core.graphics.Screen;
import org.loon.framework.android.game.core.graphics.device.LGraphics;

import android.view.KeyEvent;
import android.view.MotionEvent;

/**
 * Copyright 2008 - 2009
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 * 
 * @project loonframework
 * @author chenpeng
 * @email：ceponline@yahoo.com.cn
 * @version 0.1
 */
public class SLG extends Screen implements EmulatorListener {
	// 地图
	private Map map = null;

	// 菜单
	private Menu menu = null;

	// 地图图片
	private LImage mapImage;

	private String state;

	private int lastX;

	private int lastY;

	private int curX;

	private int curY;

	private int turn = 1;

	private int actionUnit = -1;

	private int moveCount = 0;

	private int[][] moveList;

	private int[][] movingList;

	private int[][] attackList;

	private int maxX;

	private int maxY;

	private ArrayList<Role> unitList = new ArrayList<Role>(10);

	// 战斗个体图
	private LImage[] unitImages = Map
			.getSplitImages("res/unit.png", tile, tile);

	private LImage[] iconImages = Map
			.getSplitImages("res/icon.png", tile, tile);

	private LImage[] listImages = Map
			.getSplitImages("res/list.png", tile, tile);

	final static int tile = 32;

	private EmulatorButtons ebs;

	/**
	 * PS:其实这是一个很早以前发过的J2SE版LGame示例，很多地方已经可以用现成的LGame精灵和组件取代。
	 * 比如角色移动部分可以直接上精灵，Menu类可以直接用LSelect取代。
	 * 
	 */
	public SLG() {
		actionUnit = -1;
		state = "战斗开始";
		turn = 1;
		this.map = new Map("res/map.txt", tile);
		// 地图
		this.mapImage = this.map.getMapImage();
		this.maxX = map.getMaxX();
		this.maxY = map.getMaxY();
		this.moveList = new int[maxX][maxY];
		this.movingList = new int[maxX][maxY];
		this.attackList = new int[maxX][maxY];
		// int width = maxX * tile;
		// int height = maxY * tile;
		// 菜单
		this.menu = new Menu(maxX - 1);
		// 创建角色:name=空罐少女,team=0(我军),imageindex=3,x=7,y=1,以下雷同
		createRole("空罐少女", 0, 0, 3, 7, 1);
		createRole("猫猫1", 0, 1, 6, 1, 2);
		createRole("猫猫2", 0, 0, 3, 2, 6);
		createRole("猫猫3", 0, 0, 3, 12, 6);
		// 创建角色:name=躲猫兵团1,team=1(敌军),imageindex=6,x=4,y=5,以下雷同
		createRole("躲猫兵团1", 1, 2, 4, 4, 5);
		createRole("躲猫兵团2", 1, 2, 4, 8, 5);
		createRole("躲猫兵团3", 1, 2, 4, 5, 7);
		createRole("躲猫兵团4", 1, 2, 4, 7, 2);
		createRole("躲猫兵团5", 1, 2, 4, 7, 7);
		// 初始化
		this.initRange();
	}

	public void onLoad() {
		// 直接使用LGame提供的模拟按钮，来处理确定及取消事件
		ebs = getEmulatorButtons();
		if (ebs != null) {
			// 隐藏全部方向选择按钮
			ebs.hideLeft();
			// 隐藏三角和方块键
			ebs.getTriangle().disable(true);
			ebs.getSquare().disable(true);
			// 重新配置圆圈及取消键位置
			ebs.getCircle().setX(getWidth() - 180);
			ebs.getCircle().setY(getHeight() - 80);
			ebs.getCancel().setX(getWidth() - 90);
			ebs.getCancel().setY(getHeight() - 80);
		}
	}

	public void draw(LGraphics g) {
		int count = 0;
		// 绘制地图
		g.drawImage(mapImage, 0, 0);

		// 移动范围绘制
		if ((state.equalsIgnoreCase("角色移动"))
				|| (state.equalsIgnoreCase("移动范围"))) {
			for (int j = 0; j <= maxY - 1; j++) {
				for (int i = 0; i <= maxX - 1; i++) {
					if (moveList[i][j] > -1) {
						g.drawImage(iconImages[2], i * tile, j * tile);
					} else if (attackList[i][j] > 0) {
						g.drawImage(iconImages[3], i * tile, j * tile);
					}
				}
			}
		}
		// 角色绘制
		for (int index = 0; index < unitList.size(); index++) {
			Role role = (Role) unitList.get(index);
			if (index == actionUnit) {
				// 当前控制角色处理（此示例未加入特殊处理）
				g.drawImage(role.getImage(), role.getX() * tile, role.getY()
						* tile);
			} else {
				g.drawImage(role.getImage(), role.getX() * tile, role.getY()
						* tile);
			}
			// 已行动完毕
			if (role.action == 1) {
				g.drawImage(unitImages[3], role.getX() * tile, role.getY()
						* tile);
			}
		}
		// 攻击范围绘制
		if (state.equalsIgnoreCase("进行攻击")) {
			for (int j = 0; j <= maxY - 1; j++) {
				for (int i = 0; i <= maxX - 1; i++) {
					int result = attackList[i][j];
					if (result == 2) {
						g.drawImage(iconImages[3], i * tile, j * tile);
					}
					// 标注选中的攻击对象
					if (result == 2 && getRoleIdx(1, i, j) > -1 && curX == i
							&& curY == j) {
						g.drawImage(iconImages[4], i * tile, j * tile);
					}
				}
			}
		}
		// 绘制移动路线
		if (state.equalsIgnoreCase("角色移动")) {
			for (int j = 0; j <= maxY - 1; j++) {
				for (int i = 0; i <= maxX - 1; i++) {
					if (movingList[i][j] == -1) {
						continue;
					}
					count = 0;
					if ((movingList[i][j] == 0)
							|| (movingList[i][j] == moveCount)) {
						if ((i > 0)
								&& (movingList[i - 1][j] > -1)
								&& ((movingList[i - 1][j]
										- map.getMapCost(i - 1, j) == movingList[i][j]) || (movingList[i][j]
										- map.getMapCost(i, j) == movingList[i - 1][j]))) {
							count = 1;
						}
						if ((j > 0)
								&& (movingList[i][j - 1] > -1)
								&& ((movingList[i][j - 1]
										- map.getMapCost(i, j - 1) == movingList[i][j]) || (movingList[i][j]
										- map.getMapCost(i, j) == movingList[i][j - 1]))) {
							count = 2;
						}
						if ((i < maxX - 1)
								&& (movingList[i + 1][j] > -1)
								&& ((movingList[i + 1][j]
										- map.getMapCost(i + 1, j) == movingList[i][j]) || (movingList[i][j]
										- map.getMapCost(i, j) == movingList[i + 1][j]))) {
							count = 3;
						}
						if ((j < maxY - 1)
								&& (movingList[i][j + 1] > -1)
								&& ((movingList[i][j + 1]
										- map.getMapCost(i, j + 1) == movingList[i][j]) || (movingList[i][j]
										- map.getMapCost(i, j) == movingList[i][j + 1]))) {
							count = 4;
						}
						if (movingList[i][j] != 0) {
							count = count + 4;
						}
					} else {
						count = 6;
						if ((i > 0)
								&& (movingList[i - 1][j] > -1)
								&& ((movingList[i - 1][j]
										- map.getMapCost(i - 1, j) == movingList[i][j]) || (movingList[i][j]
										- map.getMapCost(i, j) == movingList[i - 1][j]))) {
							count = count + 1;
						}
						if ((j > 0)
								&& (movingList[i][j - 1] > -1)
								&& ((movingList[i][j - 1]
										- map.getMapCost(i, j - 1) == movingList[i][j]) || (movingList[i][j]
										- map.getMapCost(i, j) == movingList[i][j - 1]))) {
							count = count + 2;
						}
						if ((i < maxX - 1)
								&& (movingList[i + 1][j] > -1)
								&& ((movingList[i + 1][j]
										- map.getMapCost(i + 1, j) == movingList[i][j]) || (movingList[i][j]
										- map.getMapCost(i, j) == movingList[i + 1][j]))) {
							count = count + 3;
						}
						if ((j < maxY - 1)
								&& (movingList[i][j + 1] > -1)
								&& ((movingList[i][j + 1]
										- map.getMapCost(i, j + 1) == movingList[i][j]) || (movingList[i][j]
										- map.getMapCost(i, j) == movingList[i][j + 1]))) {
							count = count + 5;
						}
					}
					if (count > 0) {
						g.drawImage(iconImages[count + 4], i * tile, j * tile);
					}
				}
			}
		}
		// 菜单
		if (menu.visible) {
			g.setAlpha(0.50f);
			g.drawImage(listImages[0], menu.getLeft(curX) * tile, 0);
			for (int i = 1; i <= menu.width; i++) {
				g.drawImage(listImages[1], (menu.getLeft(curX) + i) * tile, 0);
			}
			g.drawImage(listImages[2], (menu.getLeft(curX) + menu.width + 1)
					* tile, 0);
			for (int j = 1; j <= menu.height; j++) {
				g.drawImage(listImages[3], menu.getLeft(curX) * tile, j * tile);
				for (int i = 1; i <= menu.width; i++) {
					g.drawImage(listImages[4], (menu.getLeft(curX) + i) * tile,
							j * tile);
				}
				g.drawImage(listImages[5],
						(menu.getLeft(curX) + menu.width + 1) * tile, j * tile);
			}
			g.drawImage(listImages[6], menu.getLeft(curX) * tile,
					(menu.height + 1) * tile);
			for (int i = 1; i <= menu.width; i++) {
				g.drawImage(listImages[7], (menu.getLeft(curX) + i) * tile,
						(menu.height + 1) * tile);
			}
			g.drawImage(listImages[8], (menu.getLeft(curX) + menu.width + 1)
					* tile, (menu.height + 1) * tile);
			g.setAlpha(1.0f);
			// 写入文字
			g.drawImage(iconImages[1], (menu.getLeft(curX) + 1) * tile,
					(menu.cur + 1) * tile);

			for (int j = 1; j <= menu.height; j++) {
				g.setColor(LColor.white);
				g.drawString(menu.getMenuItem(j - 1), (menu.getLeft(curX) + 2)
						* tile, ((j * tile)) + 24);
			}

		}
		// 显示状态
		if (state.equalsIgnoreCase("状态显示")) {
			int i = getRoleIdx(0, curX, curY);
			if (i == -1) {
				i = getRoleIdx(1, curX, curY);
			}
			if (i > -1) {
				Role role = (Role) unitList.get(i);
				g.setAlpha(0.75f);
				g.drawImage(listImages[0], menu.getLeft(curX) * tile, 0);
				g.drawImage(listImages[1], (menu.getLeft(curX) + 1) * tile, 0);
				g.drawImage(listImages[1], (menu.getLeft(curX) + 2) * tile, 0);
				g.drawImage(listImages[2], (menu.getLeft(curX) + 3) * tile, 0);

				g.drawImage(listImages[3], (menu.getLeft(curX)) * tile, tile);
				g.drawImage(listImages[4], (menu.getLeft(curX) + 1) * tile,
						tile);
				g.drawImage(listImages[4], (menu.getLeft(curX) + 2) * tile,
						tile);
				g.drawImage(listImages[5], (menu.getLeft(curX) + 3) * tile,
						tile);

				g.drawImage(listImages[3], menu.getLeft(curX) * tile, 64);
				g.drawImage(listImages[4], (menu.getLeft(curX) + 1) * tile, 64);
				g.drawImage(listImages[4], (menu.getLeft(curX) + 2) * tile, 64);
				g.drawImage(listImages[5], (menu.getLeft(curX) + 3) * tile, 64);

				g.drawImage(listImages[6], (menu.getLeft(curX)) * tile, 96);
				g.drawImage(listImages[7], (menu.getLeft(curX) + 1) * tile, 96);
				g.drawImage(listImages[7], (menu.getLeft(curX) + 2) * tile, 96);
				g.drawImage(listImages[8], (menu.getLeft(curX) + 3) * tile, 96);
				g.setAlpha(1.0f);
				// 显示角色数据
				g.drawImage(role.getImage(), (menu.getLeft(curX) + 1) * tile
						+ 16, tile);
				g.setColor(LColor.white);
				LFont old = g.getFont();
				g.setFont(LFont.getFont(12));
				g.drawString("HP:" + role.getHp(), (menu.getLeft(curX) + 1)
						* tile + 12, 75);
				g.drawString("MV:" + role.getMove(), (menu.getLeft(curX) + 1)
						* tile + 12, 88);
				g.setFont(old);
			}
		}
		// 战斗回合
		if (state.equalsIgnoreCase("战斗开始") || state.equalsIgnoreCase("战斗结束")) {
			g.setAlpha(0.5f);
			g.setColor(0, 0, 0, 125);
			g.fillRect(0, 90, 480, 140);
			g.setColor(LColor.white);
			g.setAlpha(1.0f);
			g.drawString("第" + turn + "回合", 220, 160);
		}
		// 我方移动
		else if (state.equalsIgnoreCase("开始移动")) {
			// 未添加处理
		} else if (state.equalsIgnoreCase("敌方行动")) {
			for (int i = unitList.size() - 1; i > -1; i--) {
				Role role = (Role) unitList.get(i);
				// 敌军，且无法再次移动和攻击
				if (role.team == 1 && role.action == 1) {
					int x = role.x;
					int y = role.y;
					int index = 0;
					// 当敌军移动地点附近才能在我方人物时, 直接删除List中我方角色(实际开发中应加入相应判定)
					if ((index = getRoleIdx(0, x, y + 1)) > -1
							&& !role.isAttack()) {
						unitList.remove(index);
					} else if ((index = getRoleIdx(0, x, y - 1)) > -1
							&& !role.isAttack()) {
						unitList.remove(index);
					} else if ((index = getRoleIdx(0, x + 1, y)) > -1
							&& !role.isAttack()) {
						unitList.remove(index);
					} else if ((index = getRoleIdx(0, x - 1, y)) > -1
							&& !role.isAttack()) {
						unitList.remove(index);
					}
					role.setAttack(true);
				}
			}

		} else {
			// 绘制光标
			g.drawImage(iconImages[0], curX * tile, curY * tile);

		}
	}

	/**
	 * 初始化各项范围参数
	 * 
	 */
	public synchronized void initRange() {
		for (int y = 0; y <= maxY - 1; y++) {
			for (int x = 0; x <= maxX - 1; x++) {
				moveCount = 0;
				moveList[x][y] = -1;
				movingList[x][y] = -1;
				attackList[x][y] = 0;
			}
		}
	}

	/**
	 * 获得移动到指定地点所需步数
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	public synchronized int getMoveCount(int x, int y) {
		if ((x < 0) || (x > maxX - 1) || (y < 0) || (y > maxY - 1)) {
			// 无法移动返回-1
			return -1;
		}
		return moveList[x][y];
	}

	/**
	 * 设定移动步数
	 * 
	 * @param x
	 * @param y
	 * @param count
	 */
	public synchronized void setMoveCount(int x, int y, int count) {
		Role role = getRole(actionUnit);
		// 当为我军时
		if (role.team == 0) {
			if (getRoleIdx(1, x, y) > -1) {
				return;
			}
		} else {
			if (getRoleIdx(0, x, y) > -1) {
				return;
			}
		}
		int cost = map.getMapCost(x, y);
		// 指定位置无法进入
		if (cost < 0) {
			return;
		}
		count = count + cost;
		// 移动步数超过移动能力
		if (count > role.move) {
			return;
		}
		// 获得移动所需步数
		if ((moveList[x][y] == -1) || (count < moveList[x][y])) {
			moveList[x][y] = count;
		}
	}

	/**
	 * 设定攻击范围
	 * 
	 * @param isAttack
	 */
	public synchronized void setAttackRange(final boolean isAttack) {
		try {
			int x, y, point;
			if (isAttack == true) {
				point = 2;
			} else {
				point = 1;
			}
			Role role = getRole(actionUnit);
			x = role.x;
			y = role.y;
			// 判定攻击点
			if (x > 0) {
				attackList[x - 1][y] = point;
			}
			if (y > 0) {
				attackList[x][y - 1] = point;
			}
			if (x < maxX - 1) {
				attackList[x + 1][y] = point;
			}
			if (y < maxY - 1) {
				attackList[x][y + 1] = point;
			}
		} catch (Exception e) {
		}
	}

	/**
	 * 判断是否能做出攻击
	 * 
	 * @return
	 */
	public synchronized boolean isAttackCheck() {
		for (int i = 0; i < unitList.size(); i++) {
			Role role = getRole(i);
			if (role.team != 1) {
				continue;
			}
			if (attackList[role.x][role.y] == 2) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 设定菜单
	 * 
	 * @param menuType
	 */
	public synchronized void openMenu(int menuType) {
		menu.visible = true;
		menu.setMenuType(menuType);
		menu.cur = 0;
	}

	/**
	 * 关闭菜单
	 * 
	 */
	public synchronized void closeMenu() {
		menu.visible = false;
	}

	/**
	 * 设定所有角色参与行动
	 * 
	 */
	public synchronized void setBeforeAction() {
		for (Iterator<Role> it = unitList.iterator(); it.hasNext();) {
			Role role = (Role) it.next();
			role.setAction(0);
		}
	}

	/**
	 * 返回指定索引下角色
	 * 
	 * @param index
	 * @return
	 */
	public synchronized Role getRole(final int index) {
		if (unitList != null && index > -1) {
			return (Role) unitList.get(index);
		}
		return null;
	}

	/**
	 * 矫正x坐标
	 * 
	 * @param x
	 * @return
	 */
	public synchronized int redressX(int x) {
		if (x < 0)
			x = 0;
		if (x > maxX - 1)
			x = maxX - 1;
		return x;
	}

	/**
	 * 矫正y坐标
	 * 
	 * @param y
	 * @return
	 */
	public synchronized int redressY(int y) {
		if (y < 0)
			y = 0;
		if (y > maxY - 1)
			y = maxY - 1;
		return y;
	}

	/**
	 * 敌军行动
	 * 
	 */
	public synchronized void enemyAction() {
		for (int index = 0; index < unitList.size(); index++) {
			Role role = (Role) unitList.get(index);
			if (role.team != 1) {
				continue;
			}
			actionUnit = index;
			setMoveRange();
			// 随机选择敌方移动地点
			int x = role.move - new Random().nextInt(role.move * 2 + 1);
			int y = (role.move - Math.abs(x))
					- new Random().nextInt((role.move - Math.abs(x)) * 2 + 1);
			x = redressX(role.x + x);
			y = redressY(role.y + y);
			if ((moveList[x][y] > 0) && (getRoleIdx(0, x, y) == -1)
					&& (getRoleIdx(1, x, y) == -1)) {
				// 记录角色最后的移动位置
				lastX = role.x;
				lastY = role.y;
				curX = x;
				curY = y;
				moveCount = moveList[x][y];
				movingList[x][y] = moveCount;
				for (int i = 0; i < moveCount; i++) {
					switch (setMoveCouse(x, y)) {
					case 0:
						x = x - 1;
						break;
					case 1:
						y = y - 1;
						break;
					case 2:
						x = x + 1;
						break;
					case 3:
						y = y + 1;
						break;
					default:
						break;
					}
				}
				moveCount = moveList[curX][curY];
				movingList[x][y] = 0;
				moveRole();
			}
			state = "敌方行动";
			curX = 0;
			curY = 0;
			role.setAction(1);
			role.setAttack(false);
			actionUnit = -1;
			initRange();
		}
	}

	/**
	 * 设定移动路线
	 * 
	 */
	public synchronized void setMoveCourse() {
		if (moveList[curX][curY] == -1) {
			return;
		}
		if (movingList[curX][curY] == moveCount) {
			return;
		}

		// 选择可行的最短路径
		if ((movingList[redressX(curX - 1)][curY] != moveCount)
				&& (movingList[curX][redressY(curY - 1)] != moveCount)
				&& (movingList[redressX(curX + 1)][curY] != moveCount)
				&& (movingList[curX][redressY(curY + 1)] != moveCount)
				|| (moveCount + map.getMapCost(curX, curY) > getRole(actionUnit).move)) {

			for (int j = 0; j <= maxY - 1; j++) {
				for (int i = 0; i <= maxX - 1; i++) {
					movingList[i][j] = -1;
				}
			}
			int x = curX;
			int y = curY;
			moveCount = moveList[curX][curY];
			movingList[x][y] = moveCount;
			// 获得移动路径
			for (int i = moveCount; i > 0; i--) {
				switch (setMoveCouse(x, y)) {
				case 0:
					x = x - 1;
					break;
				case 1:
					y = y - 1;
					break;
				case 2:
					x = x + 1;
					break;
				case 3:
					y = y + 1;
					break;
				case 4:
					break;
				}

			}
			moveCount = moveList[curX][curY];
			movingList[x][y] = 0;
			return;
		}
		// 获得矫正的移动步数
		moveCount = moveCount + map.getMapCost(curX, curY);

		if (movingList[curX][curY] > -1) {
			moveCount = movingList[curX][curY];
			for (int j = 0; j <= maxY - 1; j++) {
				for (int i = 0; i <= maxX - 1; i++) {
					if (movingList[i][j] > movingList[curX][curY]) {
						movingList[i][j] = -1;
					}
				}
			}
		}
		movingList[curX][curY] = moveCount;
	}

	/**
	 * 设定最短移动路径
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	public synchronized int setMoveCouse(int x, int y) {
		// 判定左方最短路径
		if ((x > 0) && (moveList[x - 1][y] > -1)
				&& (moveList[x - 1][y] < moveList[x][y])
				&& (moveList[x - 1][y] == moveCount - map.getMapCost(x, y))) {

			moveCount = moveCount - map.getMapCost(x, y);
			movingList[x - 1][y] = moveCount;
			return 0;
		}
		// 判定上方最短路径
		if ((y > 0) && (moveList[x][y - 1] > -1)
				&& (moveList[x][y - 1] < moveList[x][y])
				&& (moveList[x][y - 1] == moveCount - map.getMapCost(x, y))) {
			moveCount = moveCount - map.getMapCost(x, y);
			movingList[x][y - 1] = moveCount;
			return 1;
		}

		// 判定右方最短路径
		if ((x < maxX - 1) && (moveList[x + 1][y] > -1)
				&& (moveList[x + 1][y] < moveList[x][y])
				&& (moveList[x + 1][y] == moveCount - map.getMapCost(x, y))) {
			moveCount = moveCount - map.getMapCost(x, y);
			movingList[x + 1][y] = moveCount;
			return 2;

		}

		// 判定下方最短路径
		if ((y < maxY - 1) && (moveList[x][y + 1] > -1)
				&& (moveList[x][y + 1] < moveList[x][y])
				&& (moveList[x][y + 1] == moveCount - map.getMapCost(x, y))) {

			moveCount = moveCount - map.getMapCost(x, y);
			movingList[x][y + 1] = moveCount;
			return 3;
		}
		return 4;
	}

	/**
	 * 移动角色
	 * 
	 */
	public synchronized void moveRole() {
		state = "开始移动";
		int x = lastX;
		int y = lastY;
		int direction;
		// 移动方向判定
		for (int i = 0; i <= moveCount; i++) {
			direction = 4;
			if ((x > 0)
					&& (moveList[x - 1][y] > -1)
					&& (movingList[x - 1][y] - map.getMapCost(x - 1, y) == movingList[x][y]))
				direction = 0; // 左
			if ((y > 0)
					&& (moveList[x][y - 1] > -1)
					&& (movingList[x][y - 1] - map.getMapCost(x, y - 1) == movingList[x][y]))
				direction = 1; // 上
			if ((x < maxX - 1)
					&& (moveList[x + 1][y] > -1)
					&& (movingList[x + 1][y] - map.getMapCost(x + 1, y) == movingList[x][y]))
				direction = 2; // 右
			if ((y < maxY - 1)
					&& (moveList[x][y + 1] > -1)
					&& (movingList[x][y + 1] - map.getMapCost(x, y + 1) == movingList[x][y]))
				direction = 3; // 下
			switch (direction) {
			case 0:
				x = x - 1;
				break;
			case 1:
				y = y - 1;
				break;
			case 2:
				x = x + 1;
				break;
			case 3:
				y = y + 1;
				break;
			case 4:
				break;
			}
			Role role = getRole(actionUnit);
			role.setX(x);
			role.setY(y);
		}
		getRole(actionUnit).x = curX;
		getRole(actionUnit).y = curY;
	}

	/**
	 * 设定移动范围
	 */
	public synchronized void setMoveRange() {
		Role role = getRole(actionUnit);
		int x = role.x;
		int y = role.y;
		int area = role.move; // 有效范围

		moveList[x][y] = 0; // 设定现在为移动0步

		for (int count = 0; count <= area - 1; count++) {
			for (int j = redressY(y - area); j < redressY(y + area); j++) {
				for (int i = redressX(x - (area - Math.abs(y - j))); i <= redressX(x
						+ (area - Math.abs(y - j))); i++) {
					// 如果能够移动指定步数
					if ((getMoveCount(i - 1, j) == count)
							|| (getMoveCount(i, j - 1) == count)
							|| (getMoveCount(i + 1, j) == count)
							|| (getMoveCount(i, j + 1) == count)) {
						setMoveCount(i, j, count);
					}
				}
			}
		}

		area = area + 1; // 射程
		for (int j = redressY(y - area); j <= redressY(y + area); j++) {
			for (int i = redressX(x - (area - Math.abs(y - j))); i <= redressX(x
					+ (area - Math.abs(y - j))); i++) {
				// 远程攻击
				if ((getMoveCount(i - 1, j) > -1)
						|| (getMoveCount(i, j - 1) > -1)
						|| (getMoveCount(i + 1, j) > -1)
						|| (getMoveCount(i, j + 1) > -1)) {
					attackList[i][j] = 1;
				}
			}
		}
	}

	/**
	 * 获得指定索引及分组下角色
	 * 
	 * @param team
	 * @param x
	 * @param y
	 * @return
	 */
	public synchronized int getRoleIdx(final int team, final int x, final int y) {
		int index = 0;
		for (Iterator<Role> it = unitList.iterator(); it.hasNext();) {
			Role role = (Role) it.next();
			if (x == role.x && y == role.y && team == role.team) {
				return index;
			}
			index++;
		}
		return -1;
	}

	/**
	 * 创建角色
	 * 
	 * @param name
	 * @param team
	 * @param imageIndex
	 * @param move
	 * @param x
	 * @param y
	 */
	private synchronized void createRole(String name, int team, int imageIndex,
			int move, int x, int y) {
		unitList.add(new Role(name, team, unitImages[imageIndex], move, x, y));
	}

	public void onKey(KeyEvent e) {

	}

	public void onKeyUp(KeyEvent e) {
	}

	public void onCancelClick() {
		if (state.equalsIgnoreCase("角色移动")) // 移动
		{
			state = "状态显示";
			Role role = (Role) unitList.get(actionUnit);
			curX = role.x;
			curY = role.y;
			actionUnit = -1;
			initRange();

		} else if (state.equalsIgnoreCase("行动菜单")) // 移动后
		{
			state = "角色移动";
			closeMenu();
			setAttackRange(false); // 不显示攻击范围
			Role role = (Role) unitList.get(actionUnit);
			role.x = lastX;
			role.y = lastY;

		} else if (state.equalsIgnoreCase("进行攻击")) // 攻击状态
		{
			state = "行动菜单";
			Role role = (Role) unitList.get(actionUnit);
			curX = role.x;
			curY = role.y;
			openMenu(menu.menuType);

		} else if (state.equalsIgnoreCase("移动范围")) { // 移动范围

			state = "状态显示";
			Role role = (Role) unitList.get(actionUnit);
			curX = role.x;
			curY = role.y;
			actionUnit = -1;
			initRange();

		}

		else if (state.equalsIgnoreCase("情报查看")) // 角色情报
		{
			state = "状态显示";
			closeMenu();
		}

		else if (state.equalsIgnoreCase("战斗开始")) // 我军行动
		{
			state = "状态显示";
		} else if (state.equalsIgnoreCase("战斗结束")) // 敌军行动
		{
			state = "敌方行动";
			enemyAction();
			setBeforeAction();
			turn = turn + 1;
			state = "战斗开始";

		}

	}

	public void onCircleClick() {
		int index = 0;
		// 当游戏状态为[状态显示]下
		if (state.equalsIgnoreCase("状态显示")) {
			// 光标指向我方未行动角色
			index = getRoleIdx(0, curX, curY);
			if ((index > -1) && (getRole(index).action == 0)) {
				state = "角色移动";
				actionUnit = getRoleIdx(0, curX, curY);
				// 绘制移动范围
				setMoveRange();
				movingList[curX][curY] = moveCount;

				// 光标指向敌方未行动角色
			} else if (getRoleIdx(1, curX, curY) > -1) {
				state = "移动范围";
				actionUnit = getRoleIdx(1, curX, curY);
				setMoveRange();

				// 查看角色情报
			} else {
				state = "情报查看";
				openMenu(0);

			}
		}
		// 选择移动
		else if (state.equalsIgnoreCase("角色移动")) {
			// 无法移动的区域
			if (moveList[curX][curY] < 0) {
				return;
			}
			// 监测移动地点
			if ((getRoleIdx(0, curX, curY) == -1)
					|| (moveList[curX][curY] == 0)) {
				lastX = getRole(actionUnit).x;
				lastY = getRole(actionUnit).y;
				moveRole();
				state = "行动菜单";
				// 绘制攻击范围
				setAttackRange(true);
				// 判定菜单项
				if (isAttackCheck()) {
					openMenu(2);
				} else {
					openMenu(1);
				}

			}
		}
		// 当角色移动后
		else if (state.equalsIgnoreCase("行动菜单")) {
			if (menu.getMenuItem(menu.cur).equalsIgnoreCase("攻击")) {
				state = "进行攻击";
				closeMenu();

			} else if (menu.getMenuItem(menu.cur).equalsIgnoreCase("待机")) {
				state = "状态显示";
				closeMenu();
				getRole(actionUnit).action = 1;
				actionUnit = -1;
				initRange();

			}
		}
		// 攻击时
		else if (state.equalsIgnoreCase("进行攻击")) {
			// 无法攻击
			if (attackList[curX][curY] < 2) {
				return;
			}
			// 当指定地点敌方存在时
			if ((index = getRoleIdx(1, curX, curY)) > -1) {
				// 删除List中敌方角色（此处可设定减血规范）
				unitList.remove(index);
				state = "状态显示";
				// 改变行动状态
				getRole(actionUnit).action = 1;
				actionUnit = -1;
				initRange();

			}
		}
		// 查看角色移动范围
		else if (state.equalsIgnoreCase("移动范围")) {
			state = "状态显示";
			Role role = getRole(actionUnit);
			curX = role.x;
			curY = role.y;
			actionUnit = -1;
			initRange();

		}
		// 查看角色情报
		else if (state.equalsIgnoreCase("情报查看")) {
			// 本回合战斗结束
			if (menu.getMenuItem(menu.cur).equalsIgnoreCase("结束")) {
				closeMenu();
				curX = 0;
				curY = 0;
				setBeforeAction();
				state = "战斗结束";

			}
		}
		// 我军开始行动
		else if (state.equalsIgnoreCase("战斗开始")) {
			state = "状态显示";

		}
		// 敌军开始行动
		else if (state.equalsIgnoreCase("战斗结束")) {
			state = "敌方行动";
			enemyAction();
			setBeforeAction();
			turn = turn + 1;
			state = "战斗开始";

		}

	}

	public void onDownClick() {

	}

	public void onLeftClick() {

	}

	public void onRightClick() {

	}

	public void onSquareClick() {

	}

	public void onTriangleClick() {

	}

	public void onUpClick() {

	}

	public void unCancelClick() {

	}

	public void unCircleClick() {

	}

	public void unDownClick() {

	}

	public void unLeftClick() {

	}

	public void unRightClick() {

	}

	public void unSquareClick() {

	}

	public void unTriangleClick() {

	}

	public void unUpClick() {

	}

	@Override
	public void onTouch(float arg0, float arg1, MotionEvent arg2, int arg3,
			int arg4) {
		// TODO Auto-generated method stub

	}

	public boolean onKeyDown(int arg0, KeyEvent e) {
		int eventCode = e.getKeyCode();
		switch (eventCode) {
		// 按下Enter，开始触发游戏事件
		case KeyEvent.KEYCODE_ENTER:
			int index = 0;
			// 当游戏状态为[状态显示]下
			if (state.equalsIgnoreCase("状态显示")) {
				// 光标指向我方未行动角色
				index = getRoleIdx(0, curX, curY);
				if ((index > -1) && (getRole(index).action == 0)) {
					state = "角色移动";
					actionUnit = getRoleIdx(0, curX, curY);
					// 绘制移动范围
					setMoveRange();
					movingList[curX][curY] = moveCount;

					// 光标指向敌方未行动角色
				} else if (getRoleIdx(1, curX, curY) > -1) {
					state = "移动范围";
					actionUnit = getRoleIdx(1, curX, curY);
					setMoveRange();

					// 查看角色情报
				} else {
					state = "情报查看";
					openMenu(0);

				}
			}
			// 选择移动
			else if (state.equalsIgnoreCase("角色移动")) {
				// 无法移动的区域
				if (moveList[curX][curY] < 0) {
					return false;
				}
				// 监测移动地点
				if ((getRoleIdx(0, curX, curY) == -1)
						|| (moveList[curX][curY] == 0)) {
					lastX = getRole(actionUnit).x;
					lastY = getRole(actionUnit).y;
					moveRole();
					state = "行动菜单";
					// 绘制攻击范围
					setAttackRange(true);
					// 判定菜单项
					if (isAttackCheck()) {
						openMenu(2);
					} else {
						openMenu(1);
					}

				}
			}
			// 当角色移动后
			else if (state.equalsIgnoreCase("行动菜单")) {
				if (menu.getMenuItem(menu.cur).equalsIgnoreCase("攻击")) {
					state = "进行攻击";
					closeMenu();

				} else if (menu.getMenuItem(menu.cur).equalsIgnoreCase("待机")) {
					state = "状态显示";
					closeMenu();
					getRole(actionUnit).action = 1;
					actionUnit = -1;
					initRange();

				}
			}
			// 攻击时
			else if (state.equalsIgnoreCase("进行攻击")) {
				// 无法攻击
				if (attackList[curX][curY] < 2) {
					return false;
				}
				// 当指定地点敌方存在时
				if ((index = getRoleIdx(1, curX, curY)) > -1) {
					// 删除List中敌方角色（此处可设定减血规范）
					unitList.remove(index);
					state = "状态显示";
					// 改变行动状态
					getRole(actionUnit).action = 1;
					actionUnit = -1;
					initRange();

				}
			}
			// 查看角色移动范围
			else if (state.equalsIgnoreCase("移动范围")) {
				state = "状态显示";
				Role role = getRole(actionUnit);
				curX = role.x;
				curY = role.y;
				actionUnit = -1;
				initRange();

			}
			// 查看角色情报
			else if (state.equalsIgnoreCase("情报查看")) {
				// 本回合战斗结束
				if (menu.getMenuItem(menu.cur).equalsIgnoreCase("结束")) {
					closeMenu();
					curX = 0;
					curY = 0;
					setBeforeAction();
					state = "战斗结束";

				}
			}
			// 我军开始行动
			else if (state.equalsIgnoreCase("战斗开始")) {
				state = "状态显示";

			}
			// 敌军开始行动
			else if (state.equalsIgnoreCase("战斗结束")) {
				state = "敌方行动";
				enemyAction();
				setBeforeAction();
				turn = turn + 1;
				state = "战斗开始";

			}
			break;
		// 按下R，取消已做选择
		case KeyEvent.KEYCODE_R:
			if (state.equalsIgnoreCase("角色移动")) // 移动
			{
				state = "状态显示";
				Role role = (Role) unitList.get(actionUnit);
				curX = role.x;
				curY = role.y;
				actionUnit = -1;
				initRange();

			} else if (state.equalsIgnoreCase("行动菜单")) // 移动后
			{
				state = "角色移动";
				closeMenu();
				setAttackRange(false); // 不显示攻击范围
				Role role = (Role) unitList.get(actionUnit);
				role.x = lastX;
				role.y = lastY;

			} else if (state.equalsIgnoreCase("进行攻击")) // 攻击状态
			{
				state = "行动菜单";
				Role role = (Role) unitList.get(actionUnit);
				curX = role.x;
				curY = role.y;
				openMenu(menu.menuType);

			} else if (state.equalsIgnoreCase("移动范围")) { // 移动范围

				state = "状态显示";
				Role role = (Role) unitList.get(actionUnit);
				curX = role.x;
				curY = role.y;
				actionUnit = -1;
				initRange();

			}

			else if (state.equalsIgnoreCase("情报查看")) // 角色情报
			{
				state = "状态显示";
				closeMenu();

			}

			else if (state.equalsIgnoreCase("战斗开始")) // 我军行动
			{
				state = "状态显示";

			}

			else if (state.equalsIgnoreCase("战斗结束")) // 敌军行动
			{
				state = "敌方行动";
				enemyAction();
				setBeforeAction();
				turn = turn + 1;
				state = "战斗开始";

			}
			break;
		}
		if (eventCode > -1) {
			eventCode = -1;
		}
		if (state.equalsIgnoreCase("战斗开始"))
			return false;
		if (state.equalsIgnoreCase("战斗结束"))
			return false;
		if (state.equalsIgnoreCase("敌方行动"))
			return false;
		// 菜单可见
		if (menu.visible) {
			switch (e.getKeyCode()) {
			case KeyEvent.KEYCODE_DPAD_UP:
				if (menu.cur > 0) {
					menu.cur = menu.cur - 1;
				}
				break;
			case KeyEvent.KEYCODE_DPAD_DOWN:
				if (menu.cur < menu.height - 1) {
					menu.cur = menu.cur + 1;
				}
				break;
			}

		}
		// 菜单不可见
		else {
			switch (e.getKeyCode()) {
			case KeyEvent.KEYCODE_DPAD_LEFT:
				curX = redressX(curX - 1);
				break;
			case KeyEvent.KEYCODE_DPAD_UP:
				curY = redressY(curY - 1);
				break;
			case KeyEvent.KEYCODE_DPAD_RIGHT:
				curX = redressX(curX + 1);
				break;
			case KeyEvent.KEYCODE_DPAD_DOWN:
				curY = redressY(curY + 1);
				break;
			}
			System.out.println(curX + "," + curY);
		}
		if (state.equalsIgnoreCase("角色移动")) {
			setMoveCourse();
		}
		return false;
	}

	public boolean onKeyUp(int arg0, KeyEvent arg1) {

		return false;
	}

	public boolean onTouchDown(MotionEvent e) {
		int x = getTouchX();
		int y = getTouchY();
		if (ebs != null) {
			if (ebs.getCircle().getBounds().contains(x, y)) {
				return false;
			}
			if (ebs.getCancel().getBounds().contains(x, y)) {
				return false;
			}
		}
		curX = redressX(x / 32);

		curY = redressY(y / 32);

		if (state.equalsIgnoreCase("角色移动")) {
			setMoveCourse();
		}
		return true;
	}

	public boolean onTouchMove(MotionEvent arg0) {

		return true;
	}

	public boolean onTouchUp(MotionEvent arg0) {

		return true;
	}
}
