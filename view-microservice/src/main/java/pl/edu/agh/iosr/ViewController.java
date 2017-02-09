package pl.edu.agh.iosr;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.apache.log4j.Logger;

@Controller
public class ViewController {

	Logger log = Logger.getLogger(ViewController.class.getName());

	@Autowired
	RestTemplate restTemplate;

	@GetMapping("/")
	public String showForm() {
		return "uploadForm";
	}

	@PostMapping(value = "/")
	public void handleRequest(@RequestParam(name = "image", required = true) MultipartFile image,
			@RequestParam(name = "hue", defaultValue = "-1") float hue,
			@RequestParam(name = "saturation", defaultValue = "-1") float saturation,
			@RequestParam(value = "brightness", defaultValue = "-1") float brightness,
			@RequestParam(name = "format", required = true) String format, HttpServletResponse response)
			throws IOException, URISyntaxException {

		log.info("Received: type=" + image.getContentType() + ", size=" + image.getSize() + ", hue= " + hue
				+ ", saturation=" + saturation + ", brightness=" + brightness + ", format=" + format);

		byte[] rawByteImage = IOUtils.toByteArray(image.getInputStream());
		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", image.getContentType());
		HttpEntity<byte[]> entity = new HttpEntity<byte[]>(rawByteImage, headers);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("hue", hue);
		map.put("saturation", saturation);
		map.put("brightness", brightness);
		map.put("format", format);
		byte[] editedByteImage = restTemplate.postForObject(
				"http://controller-microservice:2222?hue={hue}&saturation={saturation}&brightness={brightness}&format={format}",
				entity, byte[].class, map);

		response.setContentType(String.format("image/%s", format));
		response.setHeader("Content-Disposition", String.format("attachment; filename=obrazek.%s", format));
		response.setContentLength(editedByteImage.length);
		response.getOutputStream().write(editedByteImage);
	}
}
