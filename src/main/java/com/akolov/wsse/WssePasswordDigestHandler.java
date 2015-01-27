package com.akolov.wsse;

import org.apache.ws.security.WSConstants;
import org.apache.ws.security.message.WSSecHeader;
import org.apache.ws.security.message.WSSecUsernameToken;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import java.util.HashSet;
import java.util.Set;

public class WssePasswordDigestHandler implements SOAPHandler<SOAPMessageContext> {


    @Override
    public Set<QName> getHeaders() {
        Set<QName> HEADERS = new HashSet<QName>();

        HEADERS.add(new QName(WSConstants.WSSE_NS, "Security"));
        HEADERS.add(new QName(WSConstants.WSSE11_NS, "Security"));

        return HEADERS;
    }

    @Override
    public boolean handleMessage(SOAPMessageContext context) {
        try {
            SOAPMessage msg = context.getMessage();

            Boolean isOutGoing = (Boolean) context
                .get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);

            if (isOutGoing) {
                sign(msg);
            }

        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        return true;
    }

    @Override
    public boolean handleFault(SOAPMessageContext context) {
        return true;
    }


    @Override
    public void close(MessageContext context) {

    }

    private void sign(SOAPMessage message) throws Exception {
        SOAPPart soappart = message.getSOAPPart();
        WSSecHeader wsheader = new WSSecHeader();
        wsheader.insertSecurityHeader(soappart);
        WSSecUsernameToken token = new WSSecUsernameToken();
        token.setPasswordType(WSConstants.PASSWORD_DIGEST);
        token.setUserInfo("myuser", "mypass");
        token.build(soappart, wsheader);
    }

}
