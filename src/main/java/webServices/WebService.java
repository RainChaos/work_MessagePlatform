
package webServices;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.WebServiceFeature;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.9-b130926.1035
 * Generated source version: 2.2
 * 
 */
@WebServiceClient(name = "WebService", targetNamespace = "http://www.139130.net", wsdlLocation = "http://218.65.241.26:9081/Service/WebService.asmx?wsdl")
public class WebService
    extends Service
{

    private final static URL WEBSERVICE_WSDL_LOCATION;
    private final static WebServiceException WEBSERVICE_EXCEPTION;
    private final static QName WEBSERVICE_QNAME = new QName("http://www.139130.net", "WebService");

    static {
        URL url = null;
        WebServiceException e = null;
        try {
            url = new URL("http://218.65.241.26:9081/Service/WebService.asmx?wsdl");
        } catch (MalformedURLException ex) {
            e = new WebServiceException(ex);
        }
        WEBSERVICE_WSDL_LOCATION = url;
        WEBSERVICE_EXCEPTION = e;
    }

    public WebService() {
        super(__getWsdlLocation(), WEBSERVICE_QNAME);
    }

    public WebService(WebServiceFeature... features) {
        super(__getWsdlLocation(), WEBSERVICE_QNAME, features);
    }

    public WebService(URL wsdlLocation) {
        super(wsdlLocation, WEBSERVICE_QNAME);
    }

    public WebService(URL wsdlLocation, WebServiceFeature... features) {
        super(wsdlLocation, WEBSERVICE_QNAME, features);
    }

    public WebService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public WebService(URL wsdlLocation, QName serviceName, WebServiceFeature... features) {
        super(wsdlLocation, serviceName, features);
    }

    /**
     * 
     * @return
     *     returns WebServiceSoap
     */
    @WebEndpoint(name = "WebServiceSoap")
    public WebServiceSoap getWebServiceSoap() {
        return super.getPort(new QName("http://www.139130.net", "WebServiceSoap"), WebServiceSoap.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns WebServiceSoap
     */
    @WebEndpoint(name = "WebServiceSoap")
    public WebServiceSoap getWebServiceSoap(WebServiceFeature... features) {
        return super.getPort(new QName("http://www.139130.net", "WebServiceSoap"), WebServiceSoap.class, features);
    }

    private static URL __getWsdlLocation() {
        if (WEBSERVICE_EXCEPTION!= null) {
            throw WEBSERVICE_EXCEPTION;
        }
        return WEBSERVICE_WSDL_LOCATION;
    }

}
