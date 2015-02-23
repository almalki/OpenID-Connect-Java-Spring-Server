/*******************************************************************************
 * Copyright 2015 The MITRE Corporation
 *   and the MIT Kerberos and Internet Trust Consortium
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
/**
 * 
 */
package org.mitre.oauth2.service.impl;

import org.mitre.openid.connect.service.BlacklistedSiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.exceptions.InvalidRequestException;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.endpoint.DefaultRedirectResolver;
import org.springframework.stereotype.Component;

/**
 * @author jricher
 *
 */
@Component("blacklistAwareRedirectResolver")
public class BlacklistAwareRedirectResolver extends DefaultRedirectResolver {

	@Autowired
	private BlacklistedSiteService blacklistService;

	/* (non-Javadoc)
	 * @see org.springframework.security.oauth2.provider.endpoint.RedirectResolver#resolveRedirect(java.lang.String, org.springframework.security.oauth2.provider.ClientDetails)
	 */
	@Override
	public String resolveRedirect(String requestedRedirect, ClientDetails client) throws OAuth2Exception {
		String redirect = super.resolveRedirect(requestedRedirect, client);
		if (blacklistService.isBlacklisted(redirect)) {
			// don't let it go through
			throw new InvalidRequestException("The supplied redirect_uri is not allowed on this server.");
		} else {
			// not blacklisted, passed the parent test, we're fine
			return redirect;
		}
	}

}