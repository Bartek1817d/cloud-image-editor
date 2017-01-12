package pl.edu.agh.iosr;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class ViewController {

	RestTemplate restTemplate = new RestTemplate();

	@GetMapping("/")
	public String showForm() {
		return "uploadForm";
	}

	@PostMapping(value = "/")
	public void handleRequest(@RequestParam("file") MultipartFile file, HttpServletResponse response)
			throws IOException, URISyntaxException {

		byte[] media = IOUtils.toByteArray(file.getInputStream());

		RequestEntity<byte[]> requestEntity = RequestEntity.post(new URI("http://controller-microservice:2222/"))
				.body(media);

		ResponseEntity<byte[]> responseEntity = restTemplate.exchange(requestEntity, byte[].class);

		media = responseEntity.getBody();
		BufferedImage image = ImageIO.read(new ByteArrayInputStream(media));

		response.setContentType("image/jpg");
		response.setHeader("Content-Disposition", String.format("attachment; filename=obrazek.jpg"));
		response.setContentLength(media.length);
		ImageIO.write(image, "jpg", response.getOutputStream());
	}
}
