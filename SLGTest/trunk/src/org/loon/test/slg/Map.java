package org.loon.test.slg;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import org.loon.framework.android.game.core.graphics.LColor;
import org.loon.framework.android.game.core.graphics.LImage;
import org.loon.framework.android.game.core.graphics.device.LGraphics;
import org.loon.framework.android.game.core.resource.Resources;
import org.loon.framework.android.game.utils.GraphicsUtils;

import android.graphics.Bitmap.Config;

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
 * @email ceponline@yahoo.com.cn
 * @version 0.1
 */
public class Map {

	private int[][] mapArray = null;

	private int size = 0;

	private int maxX = 0;

	private int maxY = 0;

	private int tile = 32;

	private LImage[] mapImages;

	public static LImage[] getSplitImages(String fileName, int row, int col) {
		return getSplitImages(GraphicsUtils.loadImage(fileName), row, col);
	}

	public static LImage[] getSplitImages(LImage image, int row, int col) {
		int frame = 0;
		int wlength = image.getWidth() / row;
		int hlength = image.getHeight() / col;
		int total = wlength * hlength;
		LImage[] images = new LImage[total];
		for (int y = 0; y < hlength; y++) {
			for (int x = 0; x < wlength; x++) {
				images[frame] = new LImage(row, col, Config.ARGB_4444);
				LGraphics g = images[frame].getLGraphics();
				g.drawImage(image, 0, 0, row, col, (x * row), (y * col), row
						+ (x * row), col + (y * col));
				g.dispose();
				int[] pixels = images[frame].getPixels();
				// 循环像素
				for (int i = 0; i < pixels.length; i++) {
					// 去色255,0,255
					int r = LColor.getRed(pixels[i]);
					int gr = LColor.getGreen(pixels[i]);
					int b = LColor.getBlue(pixels[i]);
					if (r >= 250 && gr <= 10 && b >= 250) {
						// 透明化
						pixels[i] = 0xffffff;
					}
				}
				images[frame].setPixels(pixels, row, col);
				frame++;
			}
		}
		return images;
	}

	private static ArrayList<int[]> loadList(final String fileName)
			throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				Resources.getResourceAsStream(fileName)));
		ArrayList<int[]> records = new ArrayList<int[]>(10);
		String result = null;
		try {
			while ((result = reader.readLine()) != null) {
				char[] charArray = result.toCharArray();
				int size = charArray.length;
				int[] intArray = new int[size];
				for (int i = 0; i < size; i++) {
					intArray[i] = Character.getNumericValue(charArray[i]);
				}
				records.add(intArray);
			}
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
				}
			}
		}
		return records;
	}

	public static int[][] loadArray(final String fileName) throws IOException {

		ArrayList<int[]> list = loadList(fileName);
		int row = list.size();

		int[][] mapArray = new int[row][];
		for (int i = 0; i < row; i++) {
			mapArray[i] = (int[]) list.get(i);
		}
		int col = (((int[]) mapArray[row > 0 ? row - 1 : 0]).length);

		int[][] result = new int[col][row];
		for (int j = 0; j < col; j++) {
			for (int i = 0; i < row; i++) {
				result[j][i] = mapArray[i][j];
			}
		}
		return result;
	}

	public Map(final String fileName, final int tile) {
		try {
			this.mapArray = Map.loadArray(fileName);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		this.size = this.mapArray.length;
		this.tile = tile;
		this.maxX = this.size;
		this.maxY = (((int[]) this.mapArray[this.maxX > 0 ? this.maxX - 1 : 0]).length);
		this.mapImages = Map.getSplitImages("res/map.png", tile, tile);
	}

	public LImage getMapImage() {
		LImage image = LImage.createImage(maxX * tile, maxY * tile, true);
		LGraphics graphics = image.getLGraphics();
		for (int y = 0; y <= maxY - 1; y++) {
			for (int x = 0; x <= maxX - 1; x++) {
				int type = getMapType(x, y);
				graphics.drawImage(mapImages[type], x * tile, y * tile);
			}
		}
		graphics.dispose();
		graphics = null;
		return image;
	}

	public int[][] getMaps() {
		return mapArray;
	}

	public int getMaxX() {
		return maxX;
	}

	public int getMaxY() {
		return maxY;
	}

	public int getMapType(int x, int y) {
		return mapArray[x][y];
	}

	public int getMapCost(int x, int y) {
		int type = getMapType(x, y);

		switch (type) {
		case 0:
			type = 1;
			break;
		case 1:
			type = 2;
			break;
		case 2:
			type = 3;
			break;
		case 3:
			type = -1;
			break;
		}
		return type;
	}

}
