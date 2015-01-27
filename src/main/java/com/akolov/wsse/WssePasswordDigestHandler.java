package com.akolov.wsse;

import org.apache.ws.security.WSConstants;
import org.apache.ws.security.message.WSSecHeader;
import org.apache.ws.security.message.WSSecUsernameToken;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import java.util.Set;

public class WssePasswordDigestHandler implements SOAPHandler<SOAPMessageContext> {


    public void sign(SOAPMessage message) throws Exception {
        SOAPPart soappart = message.getSOAPPart();
        SOAPEnvelope envelope = soappart.getEnvelope();
        SOAPHeader header = envelope.getHeader();
        WSSecHeader wsheader = new WSSecHeader();
        wsheader.insertSecurityHeader(soappart);
        WSSecUsernameToken token = new WSSecUsernameToken();
        token.setPasswordType(WSConstants.PASSWORD_DIGEST);
        token.setUserInfo("myuser", "mypass");
        token.build(soappart, wsheader);
    }

    @Override
    public Set<QName> getHeaders() {
        return null;
    }

    @Override
    public boolean handleMessage(SOAPMessageContext context) {
        try {
            // got the message from the context
            SOAPMessage msg = context.getMessage();

            // is outgoing?
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

}
