package org.loon.test.slg;

import org.loon.framework.android.game.core.graphics.LImage;


/**
 * Copyright 2008 - 2009
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 *
 * @project loonframework
 * @author chenpeng  
 * @email：ceponline@yahoo.com.cn 
 * @version 0.1
 */
public class Role {

	//名称
	String name;
    //分队（0:我军 1:敌军）
	int team; 
    //hp
	int hp;
    //角色图像
	LImage image; 
    //移动力
	int move;
	//行动状态(0:未行动 1:已行动)
	int action; 
	//x坐标
	int x; 
	//y坐标
	int y; 
	//是否已进行攻击
	boolean isAttack = false;
	/**
	 * 设定角色参数
	 * 
	 * @param name
	 * @param team
	 * @param image
	 * @param move
	 * @param x
	 * @param y
	 */
	public Role(String name, int team, LImage image, int move, int x, int y) {
		this.name = name;
		this.team = team;
		this.hp = 10;
		this.image = image;
		this.move = move;
		this.x = x;
		this.y = y;
	}

	public int getAction() {
		return action;
	}

	public void setAction(int action) {
		this.action = action;
	}

	public int getHp() {
		return hp;
	}

	public void setHp(int hp) {
		this.hp = hp;
	}

	public LImage getImage() {
		return image;
	}

	public void setImage(LImage image) {
		this.image = image;
	}

	public int getMove() {
		return move;
	}

	public void setMove(int move) {
		this.move = move;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getTeam() {
		return team;
	}

	public void setTeam(int team) {
		this.team = team;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public boolean isAttack() {
		return isAttack;
	}

	public void setAttack(boolean isAttack) {
		this.isAttack = isAttack;
	}
}

