package com.syscom.beans;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Builder
@ToString(exclude = { "password" })
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true, exclude = { "password" })
@Entity
@Table(name = "T_USER")
public class User extends BaseBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "USER_SEQ_GENERATOR", sequenceName = "USER_SEQ", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "USER_SEQ_GENERATOR")
	@Column(name = "U_ID")
	private Long id;

	@NotEmpty
	@Column(name = "U_NAME")
	private String name;

	@NotEmpty
	@Column(name = "U_FIRST_NAME")
	private String firstName;

	@NotEmpty
	@Email
	@Column(name = "U_MAIL")
	private String mail;

	@NotEmpty
	@Column(name = "U_PASSWORD")
	private String password;

	@NotNull
	@Past
	@Column(name = "U_BIRTHDAY")
	private LocalDate birthDay;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "U_ROLE_ID", nullable = false)
	private Role role;

}
