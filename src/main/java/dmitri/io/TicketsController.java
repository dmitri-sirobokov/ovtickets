package dmitri.io;

import java.security.PrivateKey;
import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import io.jsonwebtoken.Jwts;

@RestController
public class TicketsController {

	@Autowired
	private TicketRepository ticketRepository;

	private PrivateKey privateKey;

	TicketsController() {
		// todo: initialize private key(s) from configuration
		this.privateKey = null;
	}

	/**
	 * @return jwt token, which grants access to check-in for public transport.
	 */
	@PostMapping("tickets")
	public String createTicket(Date validDate, Double deposit) {
		// user model in security context should be well defined, this is just an example
		String userid = getUserId();

		String ticketId = UUID.randomUUID().toString();
		String ticketToken = Jwts.builder()
				.setId(ticketId)
				.setSubject(userid)
				.claim("deposit", deposit)
				.setIssuer("tickets_issuer")
				.setIssuedAt(validDate)
				.setExpiration(new Date(validDate.getTime() + 24 * 60 * 60 * 1000))
				.signWith(this.privateKey)
				.compact();
		TicketRecord ticketRecord = new TicketRecord();
		ticketRecord.setId(ticketId);
		ticketRecord.setToken(ticketToken);
		this.ticketRepository.save(ticketRecord);
		return ticketToken;
	}

	private String getUserId() {
		SecurityContext securityContext = SecurityContextHolder.getContext();
		Authentication authentication = securityContext.getAuthentication();
		String userid = (String)authentication.getPrincipal();
		return userid;
	}
}
