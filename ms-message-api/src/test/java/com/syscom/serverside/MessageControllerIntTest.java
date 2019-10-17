package com.syscom.serverside;

import static com.syscom.enums.EnumRole.USERS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.syscom.TestUtil;
import com.syscom.beans.Message;
import com.syscom.beans.Role;
import com.syscom.beans.User;
import com.syscom.dto.MessageDTO;
import com.syscom.repository.RoleRepository;
import com.syscom.rest.MessageController;
import com.syscom.service.MessageService;
import com.syscom.service.UserService;

public class MessageControllerIntTest extends AbstractIntTest {

	private static final String TITLE = "TITLE";
	private static final String CONTENT = "CONTENT";
	private static final LocalDate BEGIN_DATE = LocalDate.now();
	private static final LocalDate END_DATE = LocalDate.now().plusDays(1);

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private UserService userService;

	@Autowired
	private MessageService messageService;

	private User user;

	@Before
	public void setup() {
		Role role = roleRepository.findByCode(USERS.name());
		user = User.builder().mail(MAIL).password(PASSWORD).name(NAME).firstName(FIRST_NAME).birthDay(BIRTH_DAY)
				.role(role).build();

	}

	@Test
	public void testCreateMessageWithoutValidToken() throws Exception {
		// GIVEN
		MessageDTO messageDTO = MessageDTO.builder().title(TITLE).content(CONTENT).beginDate(BEGIN_DATE)
				.endDate(END_DATE).build();

		// WHEN

		// THEN
		mockMvc.perform(MockMvcRequestBuilders.post(MessageController.PATH).contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(messageDTO))).andExpect(status().isUnauthorized());
	}

	@Test
	public void testCreateMessageWithWrongToken() throws Exception {
		// GIVEN
		MessageDTO messageDTO = MessageDTO.builder().title(TITLE).content(CONTENT).beginDate(BEGIN_DATE)
				.endDate(END_DATE).build();

		// WHEN

		// THEN
		mockMvc.perform(MockMvcRequestBuilders.post(MessageController.PATH)
				.header(HttpHeaders.AUTHORIZATION, "Bearer wrongTokendcqscsqcqsvsdvsdfv")
				.contentType(TestUtil.APPLICATION_JSON_UTF8).content(TestUtil.convertObjectToJsonBytes(messageDTO)))
				.andExpect(status().isUnauthorized());
	}

	@Test
	public void testCreateWrongMessage() throws Exception {
		// GIVEN
		userService.create(user);
		MessageDTO messageDTO = new MessageDTO();

		// WHEN

		// THEN
		mockMvc.perform(MockMvcRequestBuilders.post(MessageController.PATH)
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + getAccessToken(MAIL, PASSWORD))
				.contentType(TestUtil.APPLICATION_JSON_UTF8).content(TestUtil.convertObjectToJsonBytes(messageDTO)))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void testCreateMessage() throws Exception {
		// GIVEN
		userService.create(user);
		MessageDTO messageDTO = MessageDTO.builder().title(TITLE).content(CONTENT).beginDate(BEGIN_DATE)
				.endDate(END_DATE).build();

		// WHEN

		// THEN
		mockMvc.perform(MockMvcRequestBuilders.post(MessageController.PATH)
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + getAccessToken(MAIL, PASSWORD))
				.contentType(TestUtil.APPLICATION_JSON_UTF8).content(TestUtil.convertObjectToJsonBytes(messageDTO)))
				.andExpect(status().isOk());
	}

	@Test
	public void testFindAllMessages() throws Exception {
		// GIVEN
		userService.create(user);
		messageService.create(new Message(null, TITLE, CONTENT, BEGIN_DATE, END_DATE));

		// WHEN

		// THEN
		mockMvc.perform(MockMvcRequestBuilders.get(MessageController.PATH).header(HttpHeaders.AUTHORIZATION,
				"Bearer " + getAccessToken(MAIL, PASSWORD))).andExpect(status().isOk())
				.andExpect(jsonPath("$.length()").value(1)).andExpect(jsonPath("$.[0].title").value(TITLE))
				.andExpect(jsonPath("$.[0].content").value(CONTENT));
	}

	@Test
	public void testFindMessageById() throws Exception {
		// GIVEN
		userService.create(user);
		Message message = messageService.create(new Message(null, TITLE, CONTENT, BEGIN_DATE, END_DATE));

		// WHEN

		// THEN
		mockMvc.perform(MockMvcRequestBuilders.get(MessageController.PATH + "/" + message.getId())
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + getAccessToken(MAIL, PASSWORD)))
				.andExpect(status().isOk()).andExpect(jsonPath("$.title").value(TITLE))
				.andExpect(jsonPath("$.content").value(CONTENT));
	}

	@Test
	public void testUpdateMessage() throws Exception {
		// GIVEN
		userService.create(user);
		Message message = messageService.create(new Message(null, TITLE, CONTENT, BEGIN_DATE, END_DATE));

		MessageDTO messageDTO = MessageDTO.builder().title("NEW_TITLE").content("NEW_CONTENT").beginDate(BEGIN_DATE)
				.endDate(END_DATE).build();

		// WHEN

		// THEN
		mockMvc.perform(MockMvcRequestBuilders.put(MessageController.PATH + "/" + message.getId())
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + getAccessToken(MAIL, PASSWORD))
				.contentType(TestUtil.APPLICATION_JSON_UTF8).content(TestUtil.convertObjectToJsonBytes(messageDTO)))
				.andExpect(status().isOk()).andExpect(jsonPath("$.title").value("NEW_TITLE"))
				.andExpect(jsonPath("$.content").value("NEW_CONTENT"));
	}

	@Test
	public void testDeleteMessage() throws Exception {
		// GIVEN
		userService.create(user);
		Message message = messageService.create(new Message(null, TITLE, CONTENT, BEGIN_DATE, END_DATE));

		// WHEN

		// THEN
		mockMvc.perform(MockMvcRequestBuilders.delete(MessageController.PATH + "/" + message.getId())
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + getAccessToken(MAIL, PASSWORD))
				.contentType(TestUtil.APPLICATION_JSON_UTF8)).andExpect(status().isOk());

		assertThat(messageService.findAll().size()).isEqualTo(0);
	}
}
