package pl.edu.agh.iosr;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import pl.edu.agh.iosr.service.ImageEditorService;
import pl.edu.agh.iosr.utils.GifSequenceWriter;

@Controller
public class LogicController {

	private final static Logger log = Logger.getLogger(LogicController.class.getName());

	@Autowired
	private ImageEditorService imageEditorService;

	@PostMapping(value = "/")
	@ResponseBody
	public byte[] handleRequest(@RequestHeader HttpHeaders headers, @RequestBody byte[] rawByteImage,
			@RequestParam("hue") float hue, @RequestParam("saturation") float saturation,
			@RequestParam("brightness") float brightness, @RequestParam("format") String format) throws IOException {

		log.info("Received: type=" + headers.getContentType() + ", size=" + rawByteImage.length + ", hue= " + hue
				+ ", saturation=" + saturation + ", brightness=" + brightness + ", format=" + format);

		ByteArrayInputStream bais = new ByteArrayInputStream(rawByteImage);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		if (headers.getContentType().toString().equals("image/gif") && format.equals("gif")) {
			ImageInputStream iis = ImageIO.createImageInputStream(bais);
			ImageOutputStream ios = ImageIO.createImageOutputStream(baos);
			GifSequenceWriter gifWriter = new GifSequenceWriter(ios, BufferedImage.TYPE_INT_ARGB, 100, true);
			ImageReader gifReader = ImageIO.getImageReadersBySuffix("gif").next();
			gifReader.setInput(iis);
			for (int i = 0; i < gifReader.getNumImages(true); i++) {
				BufferedImage frame = gifReader.read(i);
				gifWriter.writeToSequence(imageEditorService.editImage(frame, hue, saturation, brightness));
			}
			gifWriter.close();
			return getBytes(ios);
		} else {
			BufferedImage processedImage = imageEditorService.editImage(ImageIO.read(bais), hue, saturation,
					brightness);
			ImageIO.write(processedImage, format, baos);
			return baos.toByteArray();
		}
	}

	private byte[] getBytes(ImageOutputStream ios) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream(255);
		ios.seek(0);
		while (true) {
			try {
				baos.write(ios.readByte());
			} catch (EOFException e) {
				break;
			}
		}
		return baos.toByteArray();
	}
}
