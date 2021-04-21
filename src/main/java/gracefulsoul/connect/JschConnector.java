package gracefulsoul.connect;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

import gracefulsoul.logger.JschLogger;
import gracefulsoul.util.FileUtils;
import gracefulsoul.util.StringUtils;
import gracefulsoul.vo.ActionVo;
import gracefulsoul.vo.InstanceVo;

public class JschConnector {

	private Session session;
	private ChannelSftp channel;
	
	public JschConnector() {
		JSch.setLogger(new JschLogger());
	}
	
	public JschConnector(int logLevel) {
		JSch.setLogger(new JschLogger(logLevel));
	}

	public void connect(InstanceVo instanceVo) {
		JSch jsch = new JSch();
		try {
			// If you use privateKey. It is safer than using password.
			if (StringUtils.isNotBlank(instanceVo.getPrivateKey())) {
				this.initJschIdentity(jsch, instanceVo);
			}
			this.connectSession(jsch, instanceVo);
			this.connectChannel();
		} catch (JSchException jschException) {
			jschException.printStackTrace();
			this.disconnect();
		}
	}

	private void initJschIdentity(JSch jsch, InstanceVo instanceVo) throws JSchException {
		if (StringUtils.isNotBlank(instanceVo.getPassphrase())) {
			// If you used passphrase to create a privateKey.
			jsch.addIdentity(instanceVo.getPrivateKey(), instanceVo.getPassphrase());
		} else {
			// If you did not use passphrase when creating a privateKey.
			jsch.addIdentity(instanceVo.getPrivateKey());
		}
	}
	
	private void connectSession(JSch jsch, InstanceVo instanceVo) throws JSchException {
		this.session = jsch.getSession(instanceVo.getUserName(), instanceVo.getHost(), instanceVo.getPort());
		// If privateKey is not entered, use it as a password-based login.
		if (StringUtils.isBlank(instanceVo.getPrivateKey())) {
			this.session.setPassword(instanceVo.getPassword());
		}
		this.session.setConfig("StrictHostKeyChecking", "no");
		this.session.connect();
	}
	
	private void connectChannel() throws JSchException {
		this.channel = (ChannelSftp) this.session.openChannel("sftp");
		this.channel.connect();
	}
	
	public boolean check(ActionVo actionVo) {
		try {
			return null != this.channel.stat(String.join(File.separator, actionVo.getTargetDir(), actionVo.getFileName()));
		} catch (SftpException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public void upload(ActionVo actionVo) {
		File file = new File(actionVo.getTargetDir(), actionVo.getFileName());
		try (InputStream inputStream = new FileInputStream(file)) {
			FileUtils.mkdirIfNotExists(actionVo.getDestinationDir());
			this.channel.cd(actionVo.getDestinationDir());
			this.channel.put(inputStream, actionVo.getFileName());
		} catch (IOException | SftpException e) {
			e.printStackTrace();
		}
	}
	
	public void download(ActionVo actionVo) {
		try {
			this.channel.cd(actionVo.getTargetDir());
			FileUtils.mkdirIfNotExists(actionVo.getDestinationDir());
			this.copy(actionVo);
		} catch (SftpException e) {
			e.printStackTrace();
		}
	}
	
	private void copy(ActionVo actionVo) throws SftpException {
		try (InputStream inputStream = this.channel.get(actionVo.getFileName());
			OutputStream outputStream = new FileOutputStream(new File(actionVo.getDestinationDir(), actionVo.getFileName()))) {
			FileUtils.copy(inputStream, outputStream);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void disconnect() {
		this.disconnectSession();
		this.disconnectChannel();
	}
	
	private void disconnectSession() {
		if (this.session != null && this.session.isConnected()) {
			this.session.disconnect();
		}
	}
	
	private void disconnectChannel() {
		if (this.channel != null && this.channel.isConnected()) {
			this.channel.disconnect();
		}
	}

}
