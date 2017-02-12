package pl.edu.agh.iosr.service;

import java.awt.image.BufferedImage;

public interface ImageEditorService {
	public BufferedImage editImage(BufferedImage image, float hue, float saturation, float brightness);
}
