
package webServices;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.9-b130926.1035
 * Generated source version: 2.2
 * 
 */
@WebService(name = "WebServiceSoap", targetNamespace = "http://www.139130.net")
@XmlSeeAlso({
    ObjectFactory.class
})
public interface WebServiceSoap {


    /**
     * 
     * @param subid
     * @param password
     * @param orgCode
     * @param mobile
     * @param msgFmt
     * @param account
     * @param content
     * @return
     *     returns int
     */
    @WebMethod(operationName = "PostSingle", action = "http://www.139130.net/PostSingle")
    @WebResult(name = "PostSingleResult", targetNamespace = "http://www.139130.net")
    @RequestWrapper(localName = "PostSingle", targetNamespace = "http://www.139130.net", className = "webServices.PostSingle")
    @ResponseWrapper(localName = "PostSingleResponse", targetNamespace = "http://www.139130.net", className = "webServices.PostSingleResponse")
    public int postSingle(
        @WebParam(name = "account", targetNamespace = "http://www.139130.net")
        String account,
        @WebParam(name = "password", targetNamespace = "http://www.139130.net")
        String password,
        @WebParam(name = "mobile", targetNamespace = "http://www.139130.net")
        String mobile,
        @WebParam(name = "content", targetNamespace = "http://www.139130.net")
        String content,
        @WebParam(name = "subid", targetNamespace = "http://www.139130.net")
        String subid,
        @WebParam(name = "orgCode", targetNamespace = "http://www.139130.net")
        String orgCode,
        @WebParam(name = "msgFmt", targetNamespace = "http://www.139130.net")
        int msgFmt);

    /**
     * 
     * @param subid
     * @param password
     * @param mssages
     * @param account
     * @return
     *     returns int
     */
    @WebMethod(operationName = "PostGroup", action = "http://www.139130.net/PostGroup")
    @WebResult(name = "PostGroupResult", targetNamespace = "http://www.139130.net")
    @RequestWrapper(localName = "PostGroup", targetNamespace = "http://www.139130.net", className = "webServices.PostGroup")
    @ResponseWrapper(localName = "PostGroupResponse", targetNamespace = "http://www.139130.net", className = "webServices.PostGroupResponse")
    public int postGroup(
        @WebParam(name = "account", targetNamespace = "http://www.139130.net")
        String account,
        @WebParam(name = "password", targetNamespace = "http://www.139130.net")
        String password,
        @WebParam(name = "mssages", targetNamespace = "http://www.139130.net")
        ArrayOfMessageData mssages,
        @WebParam(name = "subid", targetNamespace = "http://www.139130.net")
        String subid);

    /**
     * 
     * @param password
     * @param flag
     * @param mobile
     * @param pageindex
     * @param batchid
     * @param account
     * @return
     *     returns webServices.ArrayOfMTResponse
     */
    @WebMethod(operationName = "FindResponse", action = "http://www.139130.net/FindResponse")
    @WebResult(name = "FindResponseResult", targetNamespace = "http://www.139130.net")
    @RequestWrapper(localName = "FindResponse", targetNamespace = "http://www.139130.net", className = "webServices.FindResponse")
    @ResponseWrapper(localName = "FindResponseResponse", targetNamespace = "http://www.139130.net", className = "webServices.FindResponseResponse")
    public ArrayOfMTResponse findResponse(
        @WebParam(name = "account", targetNamespace = "http://www.139130.net")
        String account,
        @WebParam(name = "password", targetNamespace = "http://www.139130.net")
        String password,
        @WebParam(name = "batchid", targetNamespace = "http://www.139130.net")
        String batchid,
        @WebParam(name = "mobile", targetNamespace = "http://www.139130.net")
        String mobile,
        @WebParam(name = "pageindex", targetNamespace = "http://www.139130.net")
        int pageindex,
        @WebParam(name = "flag", targetNamespace = "http://www.139130.net")
        int flag);

    /**
     * 
     * @param password
     * @param account
     * @return
     *     returns webServices.ArrayOfBusinessType
     */
    @WebMethod(operationName = "GetBusinessType", action = "http://www.139130.net/GetBusinessType")
    @WebResult(name = "GetBusinessTypeResult", targetNamespace = "http://www.139130.net")
    @RequestWrapper(localName = "GetBusinessType", targetNamespace = "http://www.139130.net", className = "webServices.GetBusinessType")
    @ResponseWrapper(localName = "GetBusinessTypeResponse", targetNamespace = "http://www.139130.net", className = "webServices.GetBusinessTypeResponse")
    public ArrayOfBusinessType getBusinessType(
        @WebParam(name = "account", targetNamespace = "http://www.139130.net")
        String account,
        @WebParam(name = "password", targetNamespace = "http://www.139130.net")
        String password);

    /**
     * 
     * @param fullPath
     * @return
     *     returns webServices.ArrayOfMediaItems
     */
    @WebMethod(operationName = "SetMedias", action = "http://www.139130.net/SetMedias")
    @WebResult(name = "SetMediasResult", targetNamespace = "http://www.139130.net")
    @RequestWrapper(localName = "SetMedias", targetNamespace = "http://www.139130.net", className = "webServices.SetMedias")
    @ResponseWrapper(localName = "SetMediasResponse", targetNamespace = "http://www.139130.net", className = "webServices.SetMediasResponse")
    public ArrayOfMediaItems setMedias(
        @WebParam(name = "fullPath", targetNamespace = "http://www.139130.net")
        String fullPath);

    /**
     * 
     * @param password
     * @param account
     * @return
     *     returns webServices.AccountInfo
     */
    @WebMethod(operationName = "GetAccountInfo", action = "http://www.139130.net/GetAccountInfo")
    @WebResult(name = "GetAccountInfoResult", targetNamespace = "http://www.139130.net")
    @RequestWrapper(localName = "GetAccountInfo", targetNamespace = "http://www.139130.net", className = "webServices.GetAccountInfo")
    @ResponseWrapper(localName = "GetAccountInfoResponse", targetNamespace = "http://www.139130.net", className = "webServices.GetAccountInfoResponse")
    public AccountInfo getAccountInfo(
        @WebParam(name = "account", targetNamespace = "http://www.139130.net")
        String account,
        @WebParam(name = "password", targetNamespace = "http://www.139130.net")
        String password);

    /**
     * 
     * @param oldPassword
     * @param newPassword
     * @param account
     * @return
     *     returns int
     */
    @WebMethod(operationName = "ModifyPassword", action = "http://www.139130.net/ModifyPassword")
    @WebResult(name = "ModifyPasswordResult", targetNamespace = "http://www.139130.net")
    @RequestWrapper(localName = "ModifyPassword", targetNamespace = "http://www.139130.net", className = "webServices.ModifyPassword")
    @ResponseWrapper(localName = "ModifyPasswordResponse", targetNamespace = "http://www.139130.net", className = "webServices.ModifyPasswordResponse")
    public int modifyPassword(
        @WebParam(name = "account", targetNamespace = "http://www.139130.net")
        String account,
        @WebParam(name = "old_password", targetNamespace = "http://www.139130.net")
        String oldPassword,
        @WebParam(name = "new_password", targetNamespace = "http://www.139130.net")
        String newPassword);

    /**
     * 
     * @param password
     * @param mtpack
     * @param account
     * @return
     *     returns webServices.GsmsResponse
     */
    @WebMethod(operationName = "Post", action = "http://www.139130.net/Post")
    @WebResult(name = "PostResult", targetNamespace = "http://www.139130.net")
    @RequestWrapper(localName = "Post", targetNamespace = "http://www.139130.net", className = "webServices.Post")
    @ResponseWrapper(localName = "PostResponse", targetNamespace = "http://www.139130.net", className = "webServices.PostResponse")
    public GsmsResponse post(
        @WebParam(name = "account", targetNamespace = "http://www.139130.net")
        String account,
        @WebParam(name = "password", targetNamespace = "http://www.139130.net")
        String password,
        @WebParam(name = "mtpack", targetNamespace = "http://www.139130.net")
        MTPacks mtpack);

    /**
     * 
     * @param password
     * @param pageSize
     * @param account
     * @return
     *     returns webServices.ArrayOfMTResponse
     */
    @WebMethod(operationName = "GetResponse", action = "http://www.139130.net/GetResponse")
    @WebResult(name = "GetResponseResult", targetNamespace = "http://www.139130.net")
    @RequestWrapper(localName = "GetResponse", targetNamespace = "http://www.139130.net", className = "webServices.GetResponse")
    @ResponseWrapper(localName = "GetResponseResponse", targetNamespace = "http://www.139130.net", className = "webServices.GetResponseResponse")
    public ArrayOfMTResponse getResponse(
        @WebParam(name = "account", targetNamespace = "http://www.139130.net")
        String account,
        @WebParam(name = "password", targetNamespace = "http://www.139130.net")
        String password,
        @WebParam(name = "PageSize", targetNamespace = "http://www.139130.net")
        int pageSize);

    /**
     * 
     * @param password
     * @param pagesize
     * @param account
     * @return
     *     returns webServices.ArrayOfMOMsg
     */
    @WebMethod(operationName = "GetMOMessage", action = "http://www.139130.net/GetMOMessage")
    @WebResult(name = "GetMOMessageResult", targetNamespace = "http://www.139130.net")
    @RequestWrapper(localName = "GetMOMessage", targetNamespace = "http://www.139130.net", className = "webServices.GetMOMessage")
    @ResponseWrapper(localName = "GetMOMessageResponse", targetNamespace = "http://www.139130.net", className = "webServices.GetMOMessageResponse")
    public ArrayOfMOMsg getMOMessage(
        @WebParam(name = "account", targetNamespace = "http://www.139130.net")
        String account,
        @WebParam(name = "password", targetNamespace = "http://www.139130.net")
        String password,
        @WebParam(name = "pagesize", targetNamespace = "http://www.139130.net")
        int pagesize);

    /**
     * 
     * @param password
     * @param flag
     * @param mobile
     * @param pageindex
     * @param batchid
     * @param account
     * @return
     *     returns webServices.ArrayOfMTReport
     */
    @WebMethod(operationName = "FindReport", action = "http://www.139130.net/FindReport")
    @WebResult(name = "FindReportResult", targetNamespace = "http://www.139130.net")
    @RequestWrapper(localName = "FindReport", targetNamespace = "http://www.139130.net", className = "webServices.FindReport")
    @ResponseWrapper(localName = "FindReportResponse", targetNamespace = "http://www.139130.net", className = "webServices.FindReportResponse")
    public ArrayOfMTReport findReport(
        @WebParam(name = "account", targetNamespace = "http://www.139130.net")
        String account,
        @WebParam(name = "password", targetNamespace = "http://www.139130.net")
        String password,
        @WebParam(name = "batchid", targetNamespace = "http://www.139130.net")
        String batchid,
        @WebParam(name = "mobile", targetNamespace = "http://www.139130.net")
        String mobile,
        @WebParam(name = "pageindex", targetNamespace = "http://www.139130.net")
        int pageindex,
        @WebParam(name = "flag", targetNamespace = "http://www.139130.net")
        int flag);

    /**
     * 
     * @param password
     * @param pageSize
     * @param account
     * @return
     *     returns webServices.ArrayOfMTReport
     */
    @WebMethod(operationName = "GetReport", action = "http://www.139130.net/GetReport")
    @WebResult(name = "GetReportResult", targetNamespace = "http://www.139130.net")
    @RequestWrapper(localName = "GetReport", targetNamespace = "http://www.139130.net", className = "webServices.GetReport")
    @ResponseWrapper(localName = "GetReportResponse", targetNamespace = "http://www.139130.net", className = "webServices.GetReportResponse")
    public ArrayOfMTReport getReport(
        @WebParam(name = "account", targetNamespace = "http://www.139130.net")
        String account,
        @WebParam(name = "password", targetNamespace = "http://www.139130.net")
        String password,
        @WebParam(name = "PageSize", targetNamespace = "http://www.139130.net")
        int pageSize);

    /**
     * 
     * @param subid
     * @param password
     * @param mobiles
     * @param orgCode
     * @param msgFmt
     * @param account
     * @param content
     * @return
     *     returns int
     */
    @WebMethod(operationName = "PostMass", action = "http://www.139130.net/PostMass")
    @WebResult(name = "PostMassResult", targetNamespace = "http://www.139130.net")
    @RequestWrapper(localName = "PostMass", targetNamespace = "http://www.139130.net", className = "webServices.PostMass")
    @ResponseWrapper(localName = "PostMassResponse", targetNamespace = "http://www.139130.net", className = "webServices.PostMassResponse")
    public int postMass(
        @WebParam(name = "account", targetNamespace = "http://www.139130.net")
        String account,
        @WebParam(name = "password", targetNamespace = "http://www.139130.net")
        String password,
        @WebParam(name = "mobiles", targetNamespace = "http://www.139130.net")
        ArrayOfString mobiles,
        @WebParam(name = "content", targetNamespace = "http://www.139130.net")
        String content,
        @WebParam(name = "subid", targetNamespace = "http://www.139130.net")
        String subid,
        @WebParam(name = "orgCode", targetNamespace = "http://www.139130.net")
        String orgCode,
        @WebParam(name = "msgFmt", targetNamespace = "http://www.139130.net")
        int msgFmt);

}
