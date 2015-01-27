package com.akolov;

import org.apache.ws.security.WSConstants;
import org.apache.ws.security.message.WSSecHeader;
import org.apache.ws.security.message.WSSecUsernameToken;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import java.io.ByteArrayOutputStream;
import java.util.Set;

/**
 * Created by assen on 27/01/15.
 */
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

            throw new RuntimeException(ex.getMessage());
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

//    public void testUsernameTokenDigest() throws Exception {
//        WSSecUsernameToken builder = new WSSecUsernameToken();
//        builder.setUserInfo("wernerd", "verySecret");
//
//        Document doc = SOAPUtil.toSOAPPart(SOAPUtil.SAMPLE_SOAP_MSG);
//        WSSecHeader secHeader = new WSSecHeader();
//        secHeader.insertSecurityHeader(doc);
//        Document signedDoc = builder.build(doc, secHeader);
//
//        if (LOG.isDebugEnabled()) {
//            LOG.debug("Message with UserNameToken PW Digest:");
//            String outputString =
//                XMLUtils.PrettyDocumentToString(signedDoc);
//            LOG.debug(outputString);
//        }
//        LOG.info("After adding UsernameToken PW Digest....");
//
//        List<WSSecurityEngineResult> results = verify(signedDoc);
//        WSSecurityEngineResult actionResult =
//            WSSecurityUtil.fetchActionResult(results, WSConstants.UT);
//        UsernameToken receivedToken =
//            (UsernameToken) actionResult.get(WSSecurityEngineResult.TAG_USERNAME_TOKEN);
//        assertTrue(receivedToken != null);
//
//        UsernameToken clone =
//            new UsernameToken(receivedToken.getElement(), false, new BSPEnforcer());
//        assertTrue(clone.equals(receivedToken));
//        assertTrue(clone.hashCode() == receivedToken.hashCode());
//    }
}
