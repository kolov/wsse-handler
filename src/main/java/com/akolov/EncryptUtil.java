package com.akolov;


import org.apache.ws.security.WSSecurityEngine;
import org.apache.ws.security.WSSecurityException;
import org.apache.ws.security.components.crypto.Crypto;
import org.apache.ws.security.components.crypto.CryptoFactory;
import org.w3c.dom.Document;

import javax.security.auth.callback.CallbackHandler;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import java.io.IOException;
import java.util.Properties;
import java.util.Vector;

public class EncryptUtil {


    @SuppressWarnings({"unchecked", "deprecation"})
    static void CheckSignatureAndDecode(SOAPMessage msg, CallbackHandler cb, Properties prop) throws WSSecurityException, TransformerConfigurationException, TransformerException, SOAPException, IOException, Exception {

        WSSecurityEngine secEngine = new WSSecurityEngine();


        String alias = prop.getProperty("alias");// login of jks file
        String password = prop.getProperty("password");// password of jks file


        Crypto crypto = CryptoFactory.getInstance(prop);
        org.w3c.dom.Document doc = null;//toDocument(mensaje);

        //after we set the encrypt stuff the processsecurity does all the work

        Vector v = (Vector) secEngine.processSecurityHeader(doc, null, cb, crypto);

        if (v == null) {

            throw new Exception("Access not granted.");

        }

        //put the decoded message into the object
        updateSOAPMessage(doc, msg);
    }


    /**
     * Updates the message with the unencrypt form
     */


    private static SOAPMessage updateSOAPMessage(Document doc, SOAPMessage message) throws SOAPException {

        DOMSource domSource = new DOMSource(doc);
        message.getSOAPPart().setContent(domSource);

        return message;

    }


    /**
     * Changes the SOAPMessage to a dom.Document.
     */


    public static org.w3c.dom.Document toDocument(SOAPMessage soapMsg) throws SOAPException, TransformerException {

        Source src = soapMsg.getSOAPPart().getContent();

        TransformerFactory tf = TransformerFactory.newInstance();

        Transformer transformer = tf.newTransformer();

        DOMResult result = new DOMResult();
        transformer.transform(src, result);
        return (Document) result.getNode();

    }





}
