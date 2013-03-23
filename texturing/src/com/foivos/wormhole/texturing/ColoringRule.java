package com.foivos.wormhole.texturing;

import java.awt.Color;

public class ColoringRule {
	public Color[] from;
	public Color[][] to;
	
	public ColoringRule(Color from[], Color[] ... to) {
		this.from = from;
		this.to = to;
	}
}
