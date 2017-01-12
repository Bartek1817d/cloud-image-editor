package pl.edu.agh.iosr;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import javax.imageio.ImageIO;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LogicController {

	@PostMapping(value = "/")
	public ResponseEntity<byte[]> handleRequest(RequestEntity<byte[]> request)
			throws IOException {

		
		byte[] media = request.getBody();
		BufferedImage image = ImageIO.read(new ByteArrayInputStream(media));

		// image edition
		Graphics graphics = image.createGraphics();
		graphics.setColor(Color.white);
		graphics.drawString("some text", 20, 20);
		graphics.fillRect(30, 30, 100, 100);
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageIO.write(image, "jpg", baos);
		
		return ResponseEntity.ok().body(baos.toByteArray());

	}
}
