package pl.edu.agh.iosr.service;

import java.awt.Color;
import java.awt.image.BufferedImage;

import org.springframework.stereotype.Service;

@Service
public class ImageEditorServiceBean implements ImageEditorService {

	@Override
	public BufferedImage editImage(BufferedImage image, float hue, float saturation, float brightness) {
		int width = image.getWidth();
		int height = image.getHeight();
		BufferedImage processedImage = new BufferedImage(width, height, image.getType());
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				int rgb = image.getRGB(x, y);
				int r = (rgb >> 16) & 0xff;
				int g = (rgb >> 8) & 0xff;
				int b = (rgb) & 0xff;
				float hsb[] = new float[3];
				Color.RGBtoHSB(r, g, b, hsb);
				processedImage.setRGB(x, y, Color.HSBtoRGB(hue == -1 ? hsb[0] : hsb[0] + hue,
						saturation == -1 ? hsb[1] : saturation, brightness == -1 ? hsb[2] : brightness));
			}
		}
		return processedImage;
	}

}
