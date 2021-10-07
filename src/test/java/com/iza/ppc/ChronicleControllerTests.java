package com.iza.ppc;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ChronicleControllerTests {

	@Autowired
	private MockMvc mockMvc;

	@Test
	public void shouldReturnOkMessage() throws Exception {
		String planDate = LocalDate.now().format(DateTimeFormatter.ISO_DATE);
		String urlTemplate = "/chronicle?date=" + planDate + "&domain=prosieben.de";
		this.mockMvc.perform(get(urlTemplate)).andExpect(status().is2xxSuccessful())
				.andExpect(content().string(containsString("\"domain\":\"prosieben.de\"")))
				.andExpect(content().string(containsString("\"planDate\":\"" + planDate + "\"")))
				.andExpect(content().string(containsString("\"snapshotDate\":\"" + planDate + "\"")));
	}

	@Test
	public void shouldReturnBadRequestMessage() throws Exception {
		this.mockMvc.perform(get("/chronicle?date=2021-01-02")).andExpect(status().is4xxClientError());
		this.mockMvc.perform(get("/chronicle?date=2021-01-02&domain=prosieben.de")).andExpect(status().is4xxClientError());
		this.mockMvc.perform(get("/chronicle?domain=prosieben.de")).andExpect(status().is4xxClientError());
	}
}
