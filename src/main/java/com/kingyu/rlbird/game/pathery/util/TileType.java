package com.kingyu.rlbird.game.pathery.util;

import java.awt.Color;

public enum TileType {
	
	NORMAL(" ", Color.WHITE),
	MAZE_WALL("X", new Color(184, 85, 85)),
	USER_BLOCK("#", new Color(102, 102, 102)),
	START("S", Color.GREEN),
	WP_A("1", new Color(247, 119, 255)),
	WP_B("2", new Color(255, 255, 17)),
	FINISH("F", Color.LIGHT_GRAY);
	
	public final String string;
	public final Color color;
	
	private TileType(String string, Color color) {
		this.string = string;
		this.color = color;
	}

	public static TileType[] wps() {
		return new TileType[] {WP_A, WP_B};
	}
	
	public static boolean blocked(TileType t) {
		return t==MAZE_WALL && t==USER_BLOCK;
	}
	
}