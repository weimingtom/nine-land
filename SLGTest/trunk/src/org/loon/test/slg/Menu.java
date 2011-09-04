package org.loon.test.slg;

import java.util.ArrayList;

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
public class Menu {
	// 是否可见
	 boolean visible;

	// 菜单纵幅
	 int height;

	// 菜单横幅
	 int width;

	// 光标位置
	 int cur;

	// 菜单类型
	 int menuType;

	// 菜单选项
	 ArrayList<String> menuItem;
	 
	 int size=0;

	public Menu(int size) {
		this.menuItem = new ArrayList<String>(10);
		this.setVisible(false);
		this.setMenuType(0);
		this.setCur(0);
		this.size=size;
	}

	public void free() {
		this.menuItem.clear();
	}

	public int getCur() {
		return cur;
	}

	/**
	 * 设定光标
	 * @param cur
	 */
	public void setCur(int cur) {
		if (cur < 0) {
			cur = this.height - 1;
		}
		if (cur > this.height - 1) {
			cur = 0;
		}
		this.cur = cur;
	}

	/**
	 * 获得指定索引位置菜单
	 * 
	 * @param index
	 * @return
	 */
	public String getMenuItem(int index) {
		return (String) menuItem.get(index);

	}

	/**
	 * 获得在地图上相对位置
	 * 
	 * @param size
	 * @param x
	 * @return
	 */
	public int getLeft(int x) {
		int result = -1;
		// 如果x点位置大�?size / 2，则在左侧显示菜�?
		if (x > this.size / 2) {
			result = 0;
		} else {
			// 在右侧显示菜�?
			result = this.size - width - 1;
		}
		return result;
	}

	/**
	 * 设定菜单�?
	 *
	 */
	public void setMenuItem() {
		switch (this.menuType) {
		case 0:
			this.width = 3;
			this.height = 1;
			menuItem.add("结束");
			break;
		case 1:
			this.width = 3;
			this.height = 1;
			menuItem.add("待机");
			break;
		case 2:
			this.width = 3;
			this.height = 2;
			menuItem.add("攻击");
			menuItem.add("待机");
			break;
		}
	}

	public int getMenuType() {
		return menuType;
	}

	public void setMenuType(int menuType) {
		this.free();
		this.menuType = menuType;
		this.setMenuItem();
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

}

