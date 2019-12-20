<?xml version='1.0' encoding='UTF-8'?><wsdl:definitions name="WebService" targetNamespace="http://www.139130.net" xmlns:ns1="http://schemas.xmlsoap.org/wsdl/soap/http" xmlns:soap="http://schemas.xmlsoap.org/wsdl/soap/" xmlns:tns="http://www.139130.net" xmlns:wsdl="http://schemas.xmlsoap.org/wsdl/" xmlns:xsd="http://www.w3.org/2001/XMLSchema">
  <wsdl:types>
<xs:schema elementFormDefault="qualified" targetNamespace="http://www.139130.net" version="1.0" xmlns:tns="http://www.139130.net" xmlns:xs="http://www.w3.org/2001/XMLSchema">
<xs:element name="FindReport">
    <xs:complexType>
      <xs:sequence>
        <xs:element minOccurs="0" name="account" type="xs:string" />
        <xs:element minOccurs="0" name="password" type="xs:string" />
        <xs:element minOccurs="0" name="batchid" type="xs:string" />
        <xs:element minOccurs="0" name="mobile" type="xs:string" />
        <xs:element name="pageindex" type="xs:int" />
        <xs:element name="flag" type="xs:int" />
      </xs:sequence>
    </xs:complexType>
  </xs:element>
<xs:element name="FindReportResponse">
    <xs:complexType>
      <xs:sequence>
        <xs:element minOccurs="0" name="FindReportResult" type="tns:ArrayOfMTReport" />
      </xs:sequence>
    </xs:complexType>
  </xs:element>
<xs:element name="FindResponse">
    <xs:complexType>
      <xs:sequence>
        <xs:element minOccurs="0" name="account" type="xs:string" />
        <xs:element minOccurs="0" name="password" type="xs:string" />
        <xs:element minOccurs="0" name="batchid" type="xs:string" />
        <xs:element minOccurs="0" name="mobile" type="xs:string" />
        <xs:element name="pageindex" type="xs:int" />
        <xs:element name="flag" type="xs:int" />
      </xs:sequence>
    </xs:complexType>
  </xs:element>
<xs:element name="FindResponseResponse">
    <xs:complexType>
      <xs:sequence>
        <xs:element minOccurs="0" name="FindResponseResult" type="tns:ArrayOfMTResponse" />
      </xs:sequence>
    </xs:complexType>
  </xs:element>
<xs:element name="GetAccountInfo">
    <xs:complexType>
      <xs:sequence>
        <xs:element minOccurs="0" name="account" type="xs:string" />
        <xs:element minOccurs="0" name="password" type="xs:string" />
      </xs:sequence>
    </xs:complexType>
  </xs:element>
<xs:element name="GetAccountInfoResponse">
    <xs:complexType>
      <xs:sequence>
        <xs:element minOccurs="0" name="GetAccountInfoResult" type="tns:AccountInfo" />
      </xs:sequence>
    </xs:complexType>
  </xs:element>
<xs:element name="GetBusinessType">
    <xs:complexType>
      <xs:sequence>
        <xs:element minOccurs="0" name="account" type="xs:string" />
        <xs:element minOccurs="0" name="password" type="xs:string" />
      </xs:sequence>
    </xs:complexType>
  </xs:element>
<xs:element name="GetBusinessTypeResponse">
    <xs:complexType>
      <xs:sequence>
        <xs:element minOccurs="0" name="GetBusinessTypeResult" type="tns:ArrayOfBusinessType" />
      </xs:sequence>
    </xs:complexType>
  </xs:element>
<xs:element name="GetMOMessage">
    <xs:complexType>
      <xs:sequence>
        <xs:element minOccurs="0" name="account" type="xs:string" />
        <xs:element minOccurs="0" name="password" type="xs:string" />
        <xs:element name="pagesize" type="xs:int" />
      </xs:sequence>
    </xs:complexType>
  </xs:element>
<xs:element name="GetMOMessageResponse">
    <xs:complexType>
      <xs:sequence>
        <xs:element minOccurs="0" name="GetMOMessageResult" type="tns:ArrayOfMOMsg" />
      </xs:sequence>
    </xs:complexType>
  </xs:element>
<xs:element name="GetReport">
    <xs:complexType>
      <xs:sequence>
        <xs:element minOccurs="0" name="account" type="xs:string" />
        <xs:element minOccurs="0" name="password" type="xs:string" />
        <xs:element name="PageSize" type="xs:int" />
      </xs:sequence>
    </xs:complexType>
  </xs:element>
<xs:element name="GetReportResponse">
    <xs:complexType>
      <xs:sequence>
        <xs:element minOccurs="0" name="GetReportResult" type="tns:ArrayOfMTReport" />
      </xs:sequence>
    </xs:complexType>
  </xs:element>
<xs:element name="GetResponse">
    <xs:complexType>
      <xs:sequence>
        <xs:element minOccurs="0" name="account" type="xs:string" />
        <xs:element minOccurs="0" name="password" type="xs:string" />
        <xs:element name="PageSize" type="xs:int" />
      </xs:sequence>
    </xs:complexType>
  </xs:element>
<xs:element name="GetResponseResponse">
    <xs:complexType>
      <xs:sequence>
        <xs:element minOccurs="0" name="GetResponseResult" type="tns:ArrayOfMTResponse" />
      </xs:sequence>
    </xs:complexType>
  </xs:element>
<xs:element name="ModifyPassword">
    <xs:complexType>
      <xs:sequence>
        <xs:element minOccurs="0" name="account" type="xs:string" />
        <xs:element minOccurs="0" name="old_password" type="xs:string" />
        <xs:element minOccurs="0" name="new_password" type="xs:string" />
      </xs:sequence>
    </xs:complexType>
  </xs:element>
<xs:element name="ModifyPasswordResponse">
    <xs:complexType>
      <xs:sequence>
        <xs:element name="ModifyPasswordResult" type="xs:int" />
      </xs:sequence>
    </xs:complexType>
  </xs:element>
<xs:element name="Post">
    <xs:complexType>
      <xs:sequence>
        <xs:element minOccurs="0" name="account" type="xs:string" />
        <xs:element minOccurs="0" name="password" type="xs:string" />
        <xs:element minOccurs="0" name="mtpack" type="tns:MTPacks" />
      </xs:sequence>
    </xs:complexType>
  </xs:element>
<xs:element name="PostGroup">
    <xs:complexType>
      <xs:sequence>
        <xs:element minOccurs="0" name="account" type="xs:string" />
        <xs:element minOccurs="0" name="password" type="xs:string" />
        <xs:element minOccurs="0" name="mssages" type="tns:ArrayOfMessageData" />
        <xs:element minOccurs="0" name="subid" type="xs:string" />
      </xs:sequence>
    </xs:complexType>
  </xs:element>
<xs:element name="PostGroupResponse">
    <xs:complexType>
      <xs:sequence>
        <xs:element name="PostGroupResult" type="xs:int" />
      </xs:sequence>
    </xs:complexType>
  </xs:element>
<xs:element name="PostMass">
    <xs:complexType>
      <xs:sequence>
        <xs:element minOccurs="0" name="account" type="xs:string" />
        <xs:element minOccurs="0" name="password" type="xs:string" />
        <xs:element minOccurs="0" name="mobiles" type="tns:ArrayOfString" />
        <xs:element minOccurs="0" name="content" type="xs:string" />
        <xs:element minOccurs="0" name="subid" type="xs:string" />
        <xs:element minOccurs="0" name="orgCode" type="xs:string" />
        <xs:element name="msgFmt" type="xs:int" />
      </xs:sequence>
    </xs:complexType>
  </xs:element>
<xs:element name="PostMassResponse">
    <xs:complexType>
      <xs:sequence>
        <xs:element name="PostMassResult" type="xs:int" />
      </xs:sequence>
    </xs:complexType>
  </xs:element>
<xs:element name="PostResponse">
    <xs:complexType>
      <xs:sequence>
        <xs:element minOccurs="0" name="PostResult" type="tns:GsmsResponse" />
      </xs:sequence>
    </xs:complexType>
  </xs:element>
<xs:element name="PostSingle">
    <xs:complexType>
      <xs:sequence>
        <xs:element minOccurs="0" name="account" type="xs:string" />
        <xs:element minOccurs="0" name="password" type="xs:string" />
        <xs:element minOccurs="0" name="mobile" type="xs:string" />
        <xs:element minOccurs="0" name="content" type="xs:string" />
        <xs:element minOccurs="0" name="subid" type="xs:string" />
        <xs:element minOccurs="0" name="orgCode" type="xs:string" />
        <xs:element name="msgFmt" type="xs:int" />
      </xs:sequence>
    </xs:complexType>
  </xs:element>
<xs:element name="PostSingleResponse">
    <xs:complexType>
      <xs:sequence>
        <xs:element name="PostSingleResult" type="xs:int" />
      </xs:sequence>
    </xs:complexType>
  </xs:element>
<xs:element name="SetMedias">
    <xs:complexType>
      <xs:sequence>
        <xs:element minOccurs="0" name="fullPath" type="xs:string" />
      </xs:sequence>
    </xs:complexType>
  </xs:element>
<xs:element name="SetMediasResponse">
    <xs:complexType>
      <xs:sequence>
        <xs:element minOccurs="0" name="SetMediasResult" type="tns:ArrayOfMediaItems" />
      </xs:sequence>
    </xs:complexType>
  </xs:element>
<xs:complexType name="ArrayOfMessageData">
    <xs:sequence>
      <xs:element maxOccurs="unbounded" minOccurs="0" name="MessageData" nillable="true" type="tns:MessageData" />
    </xs:sequence>
  </xs:complexType>
<xs:complexType name="MessageData">
    <xs:sequence>
      <xs:element minOccurs="0" name="Phone" type="xs:string" />
      <xs:element minOccurs="0" name="Content" type="xs:string" />
      <xs:element name="vipFlag" type="xs:boolean" />
      <xs:element minOccurs="0" name="customMsgID" type="xs:string" />
      <xs:element minOccurs="0" name="medias" type="tns:ArrayOfMediaItems" />
      <xs:element minOccurs="0" name="orgCode" type="xs:string" />
      <xs:element name="msgFmt" type="xs:int" />
    </xs:sequence>
  </xs:complexType>
<xs:complexType name="ArrayOfMediaItems">
    <xs:sequence>
      <xs:element maxOccurs="unbounded" minOccurs="0" name="MediaItems" nillable="true" type="tns:MediaItems" />
    </xs:sequence>
  </xs:complexType>
<xs:complexType name="MediaItems">
    <xs:sequence>
      <xs:element minOccurs="0" name="meta" type="xs:string" />
      <xs:element minOccurs="0" name="data" type="xs:base64Binary" />
    </xs:sequence>
  </xs:complexType>
<xs:complexType name="ArrayOfMTResponse">
    <xs:sequence>
      <xs:element maxOccurs="unbounded" minOccurs="0" name="MTResponse" nillable="true" type="tns:MTResponse" />
    </xs:sequence>
  </xs:complexType>
<xs:complexType name="MTResponse">
    <xs:sequence>
      <xs:element minOccurs="0" name="batchID" type="xs:string" />
      <xs:element minOccurs="0" name="msgID" type="xs:string" />
      <xs:element minOccurs="0" name="customMsgID" type="xs:string" />
      <xs:element name="state" type="xs:int" />
      <xs:element minOccurs="0" name="phone" type="xs:string" />
      <xs:element name="total" nillable="true" type="xs:int" />
      <xs:element name="number" nillable="true" type="xs:int" />
      <xs:element name="submitRespTime" nillable="true" type="xs:dateTime" />
      <xs:element minOccurs="0" name="originResult" type="xs:string" />
      <xs:element minOccurs="0" name="reserve" type="xs:string" />
      <xs:element name="id" type="xs:long" />
    </xs:sequence>
  </xs:complexType>
<xs:complexType name="ArrayOfBusinessType">
    <xs:sequence>
      <xs:element maxOccurs="unbounded" minOccurs="0" name="BusinessType" nillable="true" type="tns:BusinessType" />
    </xs:sequence>
  </xs:complexType>
<xs:complexType name="BusinessType">
    <xs:sequence>
      <xs:element name="Id" type="xs:int" />
      <xs:element minOccurs="0" name="Name" type="xs:string" />
      <xs:element name="Priority" type="xs:int" />
      <xs:element minOccurs="0" name="StartTime" type="xs:string" />
      <xs:element minOccurs="0" name="EndTime" type="xs:string" />
      <xs:element name="ExtendFlag" type="xs:boolean" />
      <xs:element name="state" type="xs:int" />
      <xs:element minOccurs="0" name="bindChs" type="tns:ArrayOfBindChannel" />
    </xs:sequence>
  </xs:complexType>
<xs:complexType name="ArrayOfBindChannel">
    <xs:sequence>
      <xs:element maxOccurs="unbounded" minOccurs="0" name="BindChannel" nillable="true" type="tns:BindChannel" />
    </xs:sequence>
  </xs:complexType>
<xs:complexType name="BindChannel">
    <xs:sequence>
      <xs:element minOccurs="0" name="ChannelNum" type="xs:string" />
      <xs:element minOccurs="0" name="Carrier" type="xs:string" />
      <xs:element minOccurs="0" name="SendType" type="xs:string" />
    </xs:sequence>
  </xs:complexType>
<xs:complexType name="AccountInfo">
    <xs:sequence>
      <xs:element minOccurs="0" name="Account" type="xs:string" />
      <xs:element minOccurs="0" name="Name" type="xs:string" />
      <xs:element minOccurs="0" name="Identify" type="xs:string" />
      <xs:element minOccurs="0" name="BizNames" type="tns:ArrayOfString" />
      <xs:element minOccurs="0" name="Userbrief" type="xs:string" />
      <xs:element name="Balance" type="xs:decimal" />
      <xs:element minOccurs="0" name="Reserve" type="xs:string" />
    </xs:sequence>
  </xs:complexType>
<xs:complexType name="ArrayOfString">
    <xs:sequence>
      <xs:element maxOccurs="unbounded" minOccurs="0" name="string" nillable="true" type="xs:string" />
    </xs:sequence>
  </xs:complexType>
<xs:complexType name="MTPacks">
    <xs:sequence>
      <xs:element name="uuid" type="xs:string" />
      <xs:element name="batchID" type="xs:string" />
      <xs:element minOccurs="0" name="batchName" type="xs:string" />
      <xs:element name="sendType" type="xs:int" />
      <xs:element name="msgType" type="xs:int" />
      <xs:element minOccurs="0" name="medias" type="tns:ArrayOfMediaItems" />
      <xs:element minOccurs="0" name="msgs" type="tns:ArrayOfMessageData" />
      <xs:element name="bizType" type="xs:int" />
      <xs:element name="distinctFlag" type="xs:boolean" />
      <xs:element minOccurs="0" name="scheduleTime" type="xs:string" />
      <xs:element minOccurs="0" name="remark" type="xs:string" />
      <xs:element minOccurs="0" name="customNum" type="xs:string" />
      <xs:element minOccurs="0" name="deadline" type="xs:string" />
      <xs:element minOccurs="0" name="templateNo" type="xs:string" />
    </xs:sequence>
  </xs:complexType>
<xs:complexType name="GsmsResponse">
    <xs:sequence>
      <xs:element name="result" type="xs:int" />
      <xs:element name="uuid" type="xs:string" />
      <xs:element minOccurs="0" name="message" type="xs:string" />
      <xs:element minOccurs="0" name="attributes" type="xs:string" />
    </xs:sequence>
  </xs:complexType>
<xs:complexType name="ArrayOfMOMsg">
    <xs:sequence>
      <xs:element maxOccurs="unbounded" minOccurs="0" name="MOMsg" nillable="true" type="tns:MOMsg" />
    </xs:sequence>
  </xs:complexType>
<xs:complexType name="MOMsg">
    <xs:sequence>
      <xs:element minOccurs="0" name="Phone" type="xs:string" />
      <xs:element minOccurs="0" name="Content" type="xs:string" />
      <xs:element name="MsgType" type="xs:int" />
      <xs:element minOccurs="0" name="SpecNumber" type="xs:string" />
      <xs:element minOccurs="0" name="ServiceType" type="xs:string" />
      <xs:element name="ReceiveTime" nillable="true" type="xs:dateTime" />
      <xs:element minOccurs="0" name="Reserve" type="xs:string" />
    </xs:sequence>
  </xs:complexType>
<xs:complexType name="ArrayOfMTReport">
    <xs:sequence>
      <xs:element maxOccurs="unbounded" minOccurs="0" name="MTReport" nillable="true" type="tns:MTReport" />
    </xs:sequence>
  </xs:complexType>
<xs:complexType name="MTReport">
    <xs:sequence>
      <xs:element name="id" type="xs:long" />
      <xs:element minOccurs="0" name="batchID" type="xs:string" />
      <xs:element minOccurs="0" name="phone" type="xs:string" />
      <xs:element minOccurs="0" name="msgID" type="xs:string" />
      <xs:element minOccurs="0" name="customMsgID" type="xs:string" />
      <xs:element name="state" type="xs:int" />
      <xs:element name="total" nillable="true" type="xs:int" />
      <xs:element name="number" nillable="true" type="xs:int" />
      <xs:element name="submitTime" nillable="true" type="xs:dateTime" />
      <xs:element name="doneTime" nillable="true" type="xs:dateTime" />
      <xs:element minOccurs="0" name="originResult" type="xs:string" />
      <xs:element minOccurs="0" name="reserve" type="xs:string" />
    </xs:sequence>
  </xs:complexType>
</xs:schema>
  </wsdl:types>
  <wsdl:message name="PostGroupResponse">
    <wsdl:part element="tns:PostGroupResponse" name="parameters">
    </wsdl:part>
  </wsdl:message>
  <wsdl:message name="ModifyPassword">
    <wsdl:part element="tns:ModifyPassword" name="parameters">
    </wsdl:part>
  </wsdl:message>
  <wsdl:message name="GetAccountInfoResponse">
    <wsdl:part element="tns:GetAccountInfoResponse" name="parameters">
    </wsdl:part>
  </wsdl:message>
  <wsdl:message name="SetMediasResponse">
    <wsdl:part element="tns:SetMediasResponse" name="parameters">
    </wsdl:part>
  </wsdl:message>
  <wsdl:message name="PostMassResponse">
    <wsdl:part element="tns:PostMassResponse" name="parameters">
    </wsdl:part>
  </wsdl:message>
  <wsdl:message name="FindReport">
    <wsdl:part element="tns:FindReport" name="parameters">
    </wsdl:part>
  </wsdl:message>
  <wsdl:message name="PostGroup">
    <wsdl:part element="tns:PostGroup" name="parameters">
    </wsdl:part>
  </wsdl:message>
  <wsdl:message name="SetMedias">
    <wsdl:part element="tns:SetMedias" name="parameters">
    </wsdl:part>
  </wsdl:message>
  <wsdl:message name="ModifyPasswordResponse">
    <wsdl:part element="tns:ModifyPasswordResponse" name="parameters">
    </wsdl:part>
  </wsdl:message>
  <wsdl:message name="GetAccountInfo">
    <wsdl:part element="tns:GetAccountInfo" name="parameters">
    </wsdl:part>
  </wsdl:message>
  <wsdl:message name="Post">
    <wsdl:part element="tns:Post" name="parameters">
    </wsdl:part>
  </wsdl:message>
  <wsdl:message name="PostMass">
    <wsdl:part element="tns:PostMass" name="parameters">
    </wsdl:part>
  </wsdl:message>
  <wsdl:message name="GetReport">
    <wsdl:part element="tns:GetReport" name="parameters">
    </wsdl:part>
  </wsdl:message>
  <wsdl:message name="GetBusinessTypeResponse">
    <wsdl:part element="tns:GetBusinessTypeResponse" name="parameters">
    </wsdl:part>
  </wsdl:message>
  <wsdl:message name="GetResponseResponse">
    <wsdl:part element="tns:GetResponseResponse" name="parameters">
    </wsdl:part>
  </wsdl:message>
  <wsdl:message name="GetReportResponse">
    <wsdl:part element="tns:GetReportResponse" name="parameters">
    </wsdl:part>
  </wsdl:message>
  <wsdl:message name="FindResponse">
    <wsdl:part element="tns:FindResponse" name="parameters">
    </wsdl:part>
  </wsdl:message>
  <wsdl:message name="PostResponse">
    <wsdl:part element="tns:PostResponse" name="parameters">
    </wsdl:part>
  </wsdl:message>
  <wsdl:message name="FindResponseResponse">
    <wsdl:part element="tns:FindResponseResponse" name="parameters">
    </wsdl:part>
  </wsdl:message>
  <wsdl:message name="PostSingleResponse">
    <wsdl:part element="tns:PostSingleResponse" name="parameters">
    </wsdl:part>
  </wsdl:message>
  <wsdl:message name="GetMOMessage">
    <wsdl:part element="tns:GetMOMessage" name="parameters">
    </wsdl:part>
  </wsdl:message>
  <wsdl:message name="GetResponse">
    <wsdl:part element="tns:GetResponse" name="parameters">
    </wsdl:part>
  </wsdl:message>
  <wsdl:message name="GetBusinessType">
    <wsdl:part element="tns:GetBusinessType" name="parameters">
    </wsdl:part>
  </wsdl:message>
  <wsdl:message name="GetMOMessageResponse">
    <wsdl:part element="tns:GetMOMessageResponse" name="parameters">
    </wsdl:part>
  </wsdl:message>
  <wsdl:message name="PostSingle">
    <wsdl:part element="tns:PostSingle" name="parameters">
    </wsdl:part>
  </wsdl:message>
  <wsdl:message name="FindReportResponse">
    <wsdl:part element="tns:FindReportResponse" name="parameters">
    </wsdl:part>
  </wsdl:message>
  <wsdl:portType name="WebServiceSoap">
    <wsdl:operation name="PostSingle">
      <wsdl:input message="tns:PostSingle" name="PostSingle">
    </wsdl:input>
      <wsdl:output message="tns:PostSingleResponse" name="PostSingleResponse">
    </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="PostGroup">
      <wsdl:input message="tns:PostGroup" name="PostGroup">
    </wsdl:input>
      <wsdl:output message="tns:PostGroupResponse" name="PostGroupResponse">
    </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="FindResponse">
      <wsdl:input message="tns:FindResponse" name="FindResponse">
    </wsdl:input>
      <wsdl:output message="tns:FindResponseResponse" name="FindResponseResponse">
    </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="GetBusinessType">
      <wsdl:input message="tns:GetBusinessType" name="GetBusinessType">
    </wsdl:input>
      <wsdl:output message="tns:GetBusinessTypeResponse" name="GetBusinessTypeResponse">
    </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="SetMedias">
      <wsdl:input message="tns:SetMedias" name="SetMedias">
    </wsdl:input>
      <wsdl:output message="tns:SetMediasResponse" name="SetMediasResponse">
    </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="GetAccountInfo">
      <wsdl:input message="tns:GetAccountInfo" name="GetAccountInfo">
    </wsdl:input>
      <wsdl:output message="tns:GetAccountInfoResponse" name="GetAccountInfoResponse">
    </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="Post">
      <wsdl:input message="tns:Post" name="Post">
    </wsdl:input>
      <wsdl:output message="tns:PostResponse" name="PostResponse">
    </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="ModifyPassword">
      <wsdl:input message="tns:ModifyPassword" name="ModifyPassword">
    </wsdl:input>
      <wsdl:output message="tns:ModifyPasswordResponse" name="ModifyPasswordResponse">
    </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="GetResponse">
      <wsdl:input message="tns:GetResponse" name="GetResponse">
    </wsdl:input>
      <wsdl:output message="tns:GetResponseResponse" name="GetResponseResponse">
    </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="GetMOMessage">
      <wsdl:input message="tns:GetMOMessage" name="GetMOMessage">
    </wsdl:input>
      <wsdl:output message="tns:GetMOMessageResponse" name="GetMOMessageResponse">
    </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="GetReport">
      <wsdl:input message="tns:GetReport" name="GetReport">
    </wsdl:input>
      <wsdl:output message="tns:GetReportResponse" name="GetReportResponse">
    </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="FindReport">
      <wsdl:input message="tns:FindReport" name="FindReport">
    </wsdl:input>
      <wsdl:output message="tns:FindReportResponse" name="FindReportResponse">
    </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="PostMass">
      <wsdl:input message="tns:PostMass" name="PostMass">
    </wsdl:input>
      <wsdl:output message="tns:PostMassResponse" name="PostMassResponse">
    </wsdl:output>
    </wsdl:operation>
  </wsdl:portType>
  <wsdl:binding name="WebServiceSoapBinding" type="tns:WebServiceSoap">
    <soap:binding style="document" transport="http://schemas.xmlsoap.org/soap/http" />
    <wsdl:operation name="PostSingle">
      <soap:operation soapAction="http://www.139130.net/PostSingle" style="document" />
      <wsdl:input name="PostSingle">
        <soap:body use="literal" />
      </wsdl:input>
      <wsdl:output name="PostSingleResponse">
        <soap:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="PostGroup">
      <soap:operation soapAction="http://www.139130.net/PostGroup" style="document" />
      <wsdl:input name="PostGroup">
        <soap:body use="literal" />
      </wsdl:input>
      <wsdl:output name="PostGroupResponse">
        <soap:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="FindResponse">
      <soap:operation soapAction="http://www.139130.net/FindResponse" style="document" />
      <wsdl:input name="FindResponse">
        <soap:body use="literal" />
      </wsdl:input>
      <wsdl:output name="FindResponseResponse">
        <soap:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="GetBusinessType">
      <soap:operation soapAction="http://www.139130.net/GetBusinessType" style="document" />
      <wsdl:input name="GetBusinessType">
        <soap:body use="literal" />
      </wsdl:input>
      <wsdl:output name="GetBusinessTypeResponse">
        <soap:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="SetMedias">
      <soap:operation soapAction="http://www.139130.net/SetMedias" style="document" />
      <wsdl:input name="SetMedias">
        <soap:body use="literal" />
      </wsdl:input>
      <wsdl:output name="SetMediasResponse">
        <soap:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="GetAccountInfo">
      <soap:operation soapAction="http://www.139130.net/GetAccountInfo" style="document" />
      <wsdl:input name="GetAccountInfo">
        <soap:body use="literal" />
      </wsdl:input>
      <wsdl:output name="GetAccountInfoResponse">
        <soap:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="ModifyPassword">
      <soap:operation soapAction="http://www.139130.net/ModifyPassword" style="document" />
      <wsdl:input name="ModifyPassword">
        <soap:body use="literal" />
      </wsdl:input>
      <wsdl:output name="ModifyPasswordResponse">
        <soap:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="Post">
      <soap:operation soapAction="http://www.139130.net/Post" style="document" />
      <wsdl:input name="Post">
        <soap:body use="literal" />
      </wsdl:input>
      <wsdl:output name="PostResponse">
        <soap:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="GetResponse">
      <soap:operation soapAction="http://www.139130.net/GetResponse" style="document" />
      <wsdl:input name="GetResponse">
        <soap:body use="literal" />
      </wsdl:input>
      <wsdl:output name="GetResponseResponse">
        <soap:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="GetMOMessage">
      <soap:operation soapAction="http://www.139130.net/GetMOMessage" style="document" />
      <wsdl:input name="GetMOMessage">
        <soap:body use="literal" />
      </wsdl:input>
      <wsdl:output name="GetMOMessageResponse">
        <soap:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="FindReport">
      <soap:operation soapAction="http://www.139130.net/FindReport" style="document" />
      <wsdl:input name="FindReport">
        <soap:body use="literal" />
      </wsdl:input>
      <wsdl:output name="FindReportResponse">
        <soap:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="GetReport">
      <soap:operation soapAction="http://www.139130.net/GetReport" style="document" />
      <wsdl:input name="GetReport">
        <soap:body use="literal" />
      </wsdl:input>
      <wsdl:output name="GetReportResponse">
        <soap:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="PostMass">
      <soap:operation soapAction="http://www.139130.net/PostMass" style="document" />
      <wsdl:input name="PostMass">
        <soap:body use="literal" />
      </wsdl:input>
      <wsdl:output name="PostMassResponse">
        <soap:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
  </wsdl:binding>
  <wsdl:service name="WebService">
    <wsdl:port binding="tns:WebServiceSoapBinding" name="WebServiceSoap">
      <soap:address location="http://218.65.241.26:9081/Service/WebService.asmx" />
    </wsdl:port>
  </wsdl:service>
</wsdl:definitions>