package com.syscom.service.impl;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.syscom.beans.Fonction;
import com.syscom.beans.Role;
import com.syscom.beans.Token;
import com.syscom.beans.User;
import com.syscom.exceptions.BusinessException;
import com.syscom.repository.UserRepository;
import com.syscom.service.ResourceBundleService;
import com.syscom.service.TokenService;
import com.syscom.utils.DateUtils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.CompressionCodecs;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.TextCodec;

/**
 * Implémentation du contrat d'interface des services métiers des tokens
 * d'authentification. http://www.baeldung.com/java-json-web-tokens-jjwt
 *
 */
@Service
@Transactional
public class TokenServiceImpl implements TokenService {

	private static final String FONCTIONS = "FONCTIONS";
	private static final String ISSUER = "SYSCOM";
	private static final String NAME = "NAME";

	@Autowired
	private ResourceBundleService resourceBundleService;

	@Autowired
	private UserRepository userRepository;

	@Value("${token.jwt.secret}")
	private String secret;

	@Value("${token.jwt.duration}")
	private Integer duration;

	@Override
	public Token retrieveToken(String tokenValue) {
		Assert.notNull(tokenValue, resourceBundleService.getMessage("token.empty"));
		return checkToken(tokenValue);
	}

	@Override
	public Token createToken(String mail) throws BusinessException {
		Assert.notNull(mail, resourceBundleService.getMessage("token.mail.empty"));
		Optional<User> optionalUser = userRepository.findOptionalByMail(mail);
		if (!optionalUser.isPresent()) {
			throw new BusinessException("Unknown user");
		}
		return generateToken(optionalUser.get());
	}

	/**
	 * Création d'un token.
	 * 
	 * @param userDTO l'utilisateur pour lequel le token est créé.
	 * @return le token {@link Token}
	 */
	private Token generateToken(User user) {
		Date now = new Date();
		LocalDateTime expirationDate = LocalDateTime.now().plusMinutes(duration);
		Date dateConvert = DateUtils.convertLocalDateTimeToDate(expirationDate);
		List<String> fonctions = user.getRole().getFonctions().stream().map(fonction -> fonction.getCode())
				.collect(Collectors.toList());
		String jws = Jwts.builder().setIssuer(ISSUER).setSubject(user.getMail()).claim(NAME, user.getName())
				.claim(FONCTIONS, fonctions).setIssuedAt(now).setNotBefore(now).setExpiration(dateConvert)
				.compressWith(CompressionCodecs.DEFLATE)
				.signWith(SignatureAlgorithm.HS512, TextCodec.BASE64.decode(secret)).compact();
		return Token.builder().dateExpiration(expirationDate).user(user).value(jws).build();
	}

	/**
	 * Vérifie si un token est valide.
	 * 
	 * @param tokenValue le token à vérifier.
	 * @return
	 */
	private Token checkToken(String tokenValue) {
		Token token = null;
		try {
			Jws<Claims> jws = Jwts.parser().setSigningKey(TextCodec.BASE64.decode(secret)).requireIssuer(ISSUER)
					.parseClaimsJws(tokenValue);
			String mail = jws.getBody().getSubject();
			String name = jws.getBody().get(NAME, String.class);
			LocalDateTime dateExpiration = DateUtils.convertDateToLocalDateTime(jws.getBody().getExpiration());
			List<String> fonctions = jws.getBody().get(FONCTIONS, List.class);
			List<Fonction> fonctionUtilisateurs = fonctions.stream()
					.map(fonction -> Fonction.builder().code(fonction).build()).collect(Collectors.toList());
			Role role = Role.builder().fonctions(fonctionUtilisateurs).build();
			User user = User.builder().name(name).mail(mail).role(role).build();
			token = Token.builder().user(user).dateExpiration(dateExpiration).value(tokenValue).build();
		} catch (JwtException e) {
			throw new BadCredentialsException("Error while checking token", e);
		}
		return token;
	}

}
