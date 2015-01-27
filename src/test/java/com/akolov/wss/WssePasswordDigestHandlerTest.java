package com.akolov.wss;

import com.akolov.wsse.WssePasswordDigestHandler;
import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import javax.xml.namespace.QName;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPConstants;
import javax.xml.soap.SOAPMessage;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import java.io.ByteArrayOutputStream;

public class WssePasswordDigestHandlerTest extends TestCase {

    @Test
    public void testHandleMessage() throws Exception {
        SOAPMessageContext ctx = Mockito.mock(SOAPMessageContext.class, Mockito.RETURNS_DEEP_STUBS);


        Mockito.when(ctx.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY)).thenReturn(Boolean.TRUE);

        SOAPMessage soapMessage = MessageFactory.newInstance(
            SOAPConstants.SOAP_1_2_PROTOCOL).createMessage();

        SOAPBody soapBody = soapMessage.getSOAPBody();
        soapBody.addBodyElement(new QName("test"));

        Mockito.when(ctx.getMessage()).thenReturn(soapMessage);
        WssePasswordDigestHandler handler = new WssePasswordDigestHandler();
        handler.handleMessage(ctx);

        String messageAsString = getSOAPMessageAsString(soapMessage);
        System.out.println(messageAsString);
        Assert.assertTrue(messageAsString.contains(":Created>"));
        Assert.assertTrue(messageAsString.contains(":Password Type=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#PasswordDigest"));

    }

    public static String getSOAPMessageAsString(SOAPMessage soapMessage) throws Exception {


        TransformerFactory tff = TransformerFactory.newInstance();
        Transformer tf = tff.newTransformer();

        // Set formatting

        tf.setOutputProperty(OutputKeys.INDENT, "yes");
        tf.setOutputProperty("{http://xml.apache.org/xslt}indent-amount",
            "2");

        Source sc = soapMessage.getSOAPPart().getContent();

        ByteArrayOutputStream streamOut = new ByteArrayOutputStream();
        StreamResult result = new StreamResult(streamOut);
        tf.transform(sc, result);

        String strMessage = streamOut.toString();
        return strMessage;
    }
}