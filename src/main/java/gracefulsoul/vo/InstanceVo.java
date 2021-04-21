package gracefulsoul.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class InstanceVo {

	// Server's information.
	private String host;
	private int port;

	// User's information in server.
	private String userName;
	private String password;

	// Using SSH private key.
	private String privateKey;
	private String passphrase;

}
