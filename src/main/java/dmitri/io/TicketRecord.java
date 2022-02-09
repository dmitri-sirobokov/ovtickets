package dmitri.io;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class TicketRecord {
	@Id
	private String id;

	private String token;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
}
