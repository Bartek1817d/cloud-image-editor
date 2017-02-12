package pl.edu.agh.iosr;

import java.io.FileInputStream;

import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@WebAppConfiguration
public class LogicControllerTest extends AbstractTest {

	protected MockMvc mvc;

	@Autowired
	protected WebApplicationContext webApplicationContext;

	@Before
	public void setUp() {
		mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
	}

	@Test
	public void testHandleRequest() throws Exception {
		String uri = "/?hue={hue}&saturation={saturation}&brightness={brightness}&format={format}";
		byte[] rawByteImage = IOUtils.toByteArray(new FileInputStream("src/test/resources/images/raw1.png"));
		MvcResult result = mvc.perform(MockMvcRequestBuilders.post(uri, 0.5, 0.5, 0.5, "png")
				.contentType(MediaType.IMAGE_PNG).content(rawByteImage)).andReturn();
		byte[] content = result.getResponse().getContentAsByteArray();
		int status = result.getResponse().getStatus();
		Assert.assertEquals("failure - expected HTTP status", 200, status);
		Assert.assertTrue("failure - expected HTTP response body to have a value", content.length > 0);
	}
}
