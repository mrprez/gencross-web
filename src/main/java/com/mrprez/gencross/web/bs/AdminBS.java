package com.mrprez.gencross.web.bs;

import com.mrprez.gencross.web.bs.face.IAdminBS;
import com.mrprez.gencross.web.dao.face.IMailResource;

public class AdminBS implements IAdminBS {
	private IMailResource mailResource;
	

	@Override
	public void sendMail(String subject, String message) throws Exception {
		mailResource.sendAdminMail(subject, message);
	}

	public IMailResource getMailResource() {
		return mailResource;
	}

	public void setMailResource(IMailResource mailResource) {
		this.mailResource = mailResource;
	}

}
