package com.foivos.wormhole.texturing;

import java.awt.Color;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.awt.image.ImageProducer;
import java.awt.image.RGBImageFilter;
import java.awt.image.Raster;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

import javax.imageio.ImageIO;

public class Texturing {
	
	public static final Color black = new Color(0, 0, 0, 255);
	public static final Color white = new Color(255, 255, 255, 255);
	public static final Color red = new Color(255, 0, 0, 255);
	public static final Color blue = new Color(0, 0, 255, 255);
	public static final Color green = new Color(0, 255, 0, 255);
	public static final Color yellow = new Color(255, 255, 0, 255);
	public static final Color purple = new Color(255, 0, 255, 255);
	public static final Color cyan = new Color(0, 255, 255, 255);
	public static final Color orange = new Color(255, 128, 0, 255);
	public static final Color gray = new Color(128, 128, 128, 255);
	public static final Color lightGray = new Color(207, 207, 207, 255);
	public static final Color transp = new Color(255,255,255,0);
	
	public static final ColoringRule colors = new ColoringRule(new Color[]{red}, new Color[][]{new Color[]{black}, new Color[]{red}, new Color[]{blue}, new Color[]{green}, new Color[]{yellow}, new Color[]{purple}, new Color[]{cyan}, new Color[]{orange}, new Color[]{gray}});
	//public static final ColoringRule temp = new ColoringRule(white, new Color[]{new Color(0, 0, 0, 0)});
	public static final ColoringRule bottom  = new ColoringRule(new Color[]{green, new Color(0,128,0,255), new Color(0,64,0,255)}, new Color[][]{new Color[]{red,red,lightGray}, new Color[]{lightGray, transp, transp}});
	public static final ColoringRule top  = new ColoringRule(new Color[]{blue, new Color(0,0,128,255), new Color(0,0,64,255)}, new Color[][]{new Color[]{red,red,lightGray}, new Color[]{lightGray, transp, transp}});
	public static final ColoringRule left  = new ColoringRule(new Color[]{yellow, new Color(128,128,0,255), new Color(64,64,0,255)}, new Color[][]{new Color[]{red,red,lightGray}, new Color[]{lightGray, transp, transp}});
	public static final ColoringRule right = new ColoringRule(new Color[]{purple, new Color(128,0,128,255), new Color(64,0,64,255)}, new Color[][]{new Color[]{red,red,lightGray}, new Color[]{lightGray, transp, transp}});
	public static final ColoringRule sides = new ColoringRule(new Color[]{blue}, new Color[][]{new Color[]{blue}, new Color[]{red}, new Color[]{green}, new Color[]{yellow}, new Color[]{purple}, new Color[]{orange}});
	
	

	public static void main(String[] args) throws Exception {
		String path = new String();
		for(int i=0;i<args.length;i++) {
			path = path + args[i] + " ";
		}
		path = path.substring(0, path.length()-1);
		/*for(int i=0;i<16;i++) {
			BufferedImage image = ImageIO.read(new File(path + "/original/tube"+i+".png"));
			applyRulesTo(image, path + "/result/tube", 9*i, colors);
		}*/
		BufferedImage image = ImageIO.read(new File(path + "/original/manipulator.png"));
		applyRulesTo(image, path + "/result/manipulator", 0, colors, sides);
		
	}
	
	public static void applyRulesTo(BufferedImage image, String name, int index, ColoringRule... rules) throws IOException {
		int[][][] toChange = new int[rules.length][][];
		int[][] sizes = new int[rules.length][];
		int width = image.getWidth(), height = image.getHeight(), size = width * height;
		for(int i=0; i<rules.length; i++) {
			toChange[i] = new int[rules[i].from.length][size];
			sizes[i] = new int[rules[i].from.length];
			for(int j=0;j<rules[i].from.length;j++) {
				for(int x=0; x<width; x++) {
					for(int y=0; y<height; y++) {
						if(image.getRGB(x, y) == rules[i].from[j].hashCode()) {
							toChange[i][j][sizes[i][j]++] = x + y * image.getWidth();
						}
					}
				}
			}
		}
		
		Queue<Integer[]> q = new LinkedList<Integer[]>();
		q.add(new Integer[rules.length]);
		for(int i=0; i<rules.length; i++) {
			int length = q.size();
			for(int j=0; j<length; j++) {
				Integer[] top = q.poll();
				for(int l=0; l<rules[i].to.length; l++) {
					top[i] = l;
					q.add(top.clone());
				}
			}
			
		}
		while(!q.isEmpty()) {
			Integer[] coloring = q.poll();
			for(int i=0; i<rules.length; i++) {
				for(int j=0; j<rules[i].from.length; j++) {
					for(int l=0;l<sizes[i][j]; l++) {
						image.setRGB(toChange[i][j][l] % width, toChange[i][j][l] / width, rules[i].to[coloring[i]][j].darker().darker().hashCode());

					
					}
				}
			}

			
			File output = new File(name+index+++".png");
			output.createNewFile();
			ImageIO.write(image, "png", output);
		}
				
	}

}
