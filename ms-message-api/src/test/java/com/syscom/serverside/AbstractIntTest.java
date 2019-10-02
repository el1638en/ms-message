package com.syscom.serverside;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;

import org.apache.commons.lang3.StringUtils;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

import com.syscom.MessageApp;
import com.syscom.rest.LoginController;

@Transactional
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = MessageApp.class)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.yml")
public abstract class AbstractIntTest {

	protected static final String MAIL = "MAIL@gmail.com";
	protected static final String PASSWORD = "PASSWORD";
	protected static final String NAME = "NAME";
	protected static final String FIRST_NAME = "FIRST_NAME";
	protected static final LocalDate BIRTH_DAY = LocalDate.now().minusDays(1);

	@Autowired
	protected MockMvc mockMvc;

	protected String getAccessToken(String username, String password) throws Exception {
		ResultActions result = mockMvc
				.perform(MockMvcRequestBuilders.get(LoginController.PATH).header(HttpHeaders.AUTHORIZATION,
						"Basic " + Base64Utils.encodeToString(StringUtils.join(username, ":", password).getBytes())))
				.andExpect(status().isOk());

		String resultString = result.andReturn().getResponse().getContentAsString();

		JacksonJsonParser jsonParser = new JacksonJsonParser();
		return jsonParser.parseMap(resultString).get("value").toString();
	}

}
