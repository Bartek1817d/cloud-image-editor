package pl.edu.agh.iosr;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URLConnection;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class ViewController {

	@GetMapping("/")
	public String showForm() {
		return "uploadForm";
	}

	@PostMapping(value = "/")
	public void handleFileUpload(@RequestParam("file") MultipartFile file, HttpServletResponse response) throws IOException {

		BufferedImage image = ImageIO.read(file.getInputStream());
		
		// image edition
		Graphics graphics = image.createGraphics();
		graphics.setColor(Color.white);
		graphics.drawString("some text", 20, 20);
		graphics.fillRect(30, 30, 100, 100);
		
		String fileName = file.getOriginalFilename();
		String mimeType = URLConnection.guessContentTypeFromName(fileName);
		String extension = FilenameUtils.getExtension(fileName);
		
		response.setContentType(mimeType);
		response.setHeader("Content-Disposition", String.format("attachment; filename=\"" + fileName +"\""));
		response.setContentLength((int)file.getSize());
		ImageIO.write(image, extension, response.getOutputStream());
	}
}
