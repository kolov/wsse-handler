# wsse-handler
SoapHandler to att Password digest as specified by WS-Security

Running the test delivers:

     <?xml version="1.0" encoding="UTF-8" standalone="no"?>
    <env:Envelope xmlns:env="http://www.w3.org/2003/05/soap-envelope">
     <env:Header>
      <wsse:Security xmlns:wsse="http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd" 
            xmlns:wsu="http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd"
            env:mustUnderstand="true">
       <wsse:UsernameToken wsu:Id="UsernameToken-7BE37D598A83061F2E14223700511711">
        <wsse:Username>myuser</wsse:Username>
        <wsse:Password Type="http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#PasswordDigest">epfO7o0xVFCPDy5ST7ZBT99uMqk=</wsse:Password>
        <wsse:Nonce EncodingType="http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-soap-message-security-1.0#Base64Binary">yKal3NlH/8zJgK6oNQekvw==</wsse:Nonce>
        <wsu:Created>2015-01-27T14:47:31.166Z</wsu:Created>
       </wsse:UsernameToken>
     </wsse:Security>
    </env:Header>
    <env:Body>
     <test/>
    </env:Body>
    </env:Envelope>
