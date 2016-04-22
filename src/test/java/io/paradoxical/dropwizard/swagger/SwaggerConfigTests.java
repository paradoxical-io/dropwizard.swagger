package io.paradoxical.dropwizard.swagger;


import io.paradoxical.dropwizard.swagger.sample.App;
import org.junit.Test;

public class SwaggerConfigTests extends TestBase {
    @Test
	public void test_app_start() throws Exception {
		App.main(new String[]{ "server", "src/test/resources/conf.yml" });
	}
}
