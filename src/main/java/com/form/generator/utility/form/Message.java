package com.form.generator.utility.form;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Message {

	private String from;

	private String subject;

	private String message;
}
