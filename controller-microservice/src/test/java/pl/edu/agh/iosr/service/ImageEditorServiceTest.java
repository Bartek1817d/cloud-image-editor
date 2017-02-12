package pl.edu.agh.iosr.service;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.imageio.ImageIO;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import pl.edu.agh.iosr.AbstractTest;

public class ImageEditorServiceTest extends AbstractTest {

	@Autowired
	private ImageEditorService imageEditorService;

	private List<BufferedImage> rawImages, editedImages;

	@Before
	public void loadImages() throws IOException {
		rawImages = new LinkedList<BufferedImage>();
		editedImages = new LinkedList<BufferedImage>();
		rawImages.add(ImageIO.read(new File("src/test/resources/images/raw1.png")));
		editedImages.add(ImageIO.read(new File("src/test/resources/images/edited1.png")));
	}

	@Test
	public void testEditImage() {
		Iterator<BufferedImage> editedIter = editedImages.iterator();
		for (BufferedImage rawImage : rawImages) {
			Assert.assertTrue(
					compareImages(imageEditorService.editImage(rawImage, 0.5f, 0.5f, 0.5f), editedIter.next()));
		}
	}

	private static boolean compareImages(BufferedImage imgA, BufferedImage imgB) {
		// The images must be the same size.
		if (imgA.getWidth() == imgB.getWidth() && imgA.getHeight() == imgB.getHeight()) {
			int width = imgA.getWidth();
			int height = imgA.getHeight();

			// Loop over every pixel.
			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {
					// Compare the pixels for equality.
					if (imgA.getRGB(x, y) != imgB.getRGB(x, y)) {
						return false;
					}
				}
			}
		} else {
			return false;
		}

		return true;
	}

}
