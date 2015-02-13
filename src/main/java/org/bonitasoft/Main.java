/*
 * Copyright (C) 2015 BonitaSoft S.A.
 * BonitaSoft, 32 rue Gustave Eiffel - 38000 Grenoble
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2.0 of the License, or
 * (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package org.bonitasoft;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.bonitasoft.engine.api.ApiAccessType;
import org.bonitasoft.engine.api.TenantAPIAccessor;
import org.bonitasoft.engine.expression.Expression;
import org.bonitasoft.engine.expression.ExpressionBuilder;
import org.bonitasoft.engine.session.APISession;
import org.bonitasoft.engine.util.APITypeManager;

/**
 * @author Baptiste Mesta
 */
public class Main {

    public static void main(String[] args) throws Exception {

        String script = "import org.bonitasoft.accountupdateinfo.AccountUpdateInfo;\n"
                +
                "import org.bonitasoft.contactupdateinfo.ContactUpdateInfo;\n"
                +
                "\n"
                +
                "/*return \"\"\"<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:cus=\"http://webservices.cscinfo.com/etp/ECCMS/CustomersContacts\" xmlns:cus1=\"http://schemas.cscinfo.com/etp/ECCMS/CustomersContacts\"><soapenv:Header/><soapenv:Body><cus:UpdateContactRequest><cus:ContactID>${contactUpdateInfo.getContactPartyId()}</cus:ContactID><cus:ContactName><cus1:Prefix>${contactUpdateInfo.getContactSalutation().toUpperCase()}</cus1:Prefix><cus1:Given>${contactUpdateInfo.getContactFirstName()}</cus1:Given><cus1:Middle>${contactUpdateInfo.getContactMiddleName()}</cus1:Middle><cus1:Last>${contactUpdateInfo.getContactLastName()}</cus1:Last><cus1:Suffix>${contactUpdateInfo.getContactSuffix().toUpperCase()}</cus1:Suffix></cus:ContactName><cus:Phone isActive=\"Y\" isPrimary=\"Y\"><cus1:RefKeyId>${if(contactUpdateInfo.getPhoneRefKeyId()==null || contactUpdateInfo.getPhoneRefKeyId().isEmpty()){return 0}else{return contactUpdateInfo.getPhoneRefKeyId()}}</cus1:RefKeyId><cus1:CountryCode>1</cus1:CountryCode><cus1:AreaCode>301</cus1:AreaCode><cus1:Number>${contactUpdateInfo.getContactPhone()}</cus1:Number><cus1:Extension>63526</cus1:Extension><cus1:PrimaryType>TELEPHONE</cus1:PrimaryType></cus:Phone><cus:Email isPrimary=\"Y\"><cus1:RefKeyId>${if(contactUpdateInfo.getEmailRefKeyId()==null || contactUpdateInfo.getEmailRefKeyId().isEmpty()){return 0}else{return contactUpdateInfo.getEmailRefKeyId()}}</cus1:RefKeyId><cus1:Address>${contactUpdateInfo.getContactEmail()}</cus1:Address></cus:Email><cus:CSCBusinessUnit>${if(accountUpdateInfo.getBusinessUnit()==null || accountUpdateInfo.getBusinessUnit().isEmpty()){return 0}else{return accountUpdateInfo.getBusinessUnit()}}</cus:CSCBusinessUnit><cus:SystemUserId>${contactUpdateInfo.getUpdateUser()}</cus:SystemUserId><cus:SourceOfChange>SF</cus:SourceOfChange></cus:UpdateContactRequest></soapenv:Body></soapenv:Envelope>\"\"\".toString()*/\n"
                +
                "\n"
                +
                "String bodyMiddle=\"\"\n"
                +
                "if (contactUpdateInfo.getContactPartyId()!=null){\n"
                +
                "\tbodyMiddle= bodyMiddle +\"<cus:ContactID>${contactUpdateInfo.getContactPartyId()}</cus:ContactID>\"\n"
                +
                "}\n"
                +
                "if(contactUpdateInfo.getContactSalutation() !=null ||\n"
                +
                "\t\tcontactUpdateInfo.getContactFirstName() != null ||\n"
                +
                "\t\tcontactUpdateInfo.getContactMiddleName() !=null ||\n"
                +
                "\t\tcontactUpdateInfo.getContactLastName() !=null ||\n"
                +
                "\t\tcontactUpdateInfo.getContactSuffix() !=null){\n"
                +
                "\tbodyMiddle = bodyMiddle+\"<cus:ContactName>\"\n"
                +
                "\n"
                +
                "\tif(contactUpdateInfo.getContactSalutation()!=null){\n"
                +
                "\t\tbodyMiddle= bodyMiddle+\"<cus1:Prefix>${contactUpdateInfo.getContactSalutation().toUpperCase()}</cus1:Prefix>\"\n"
                +
                "\t}\n"
                +
                "\tif (contactUpdateInfo.getContactFirstName()!=null){\n"
                +
                "\t\tbodyMiddle=bodyMiddle+\"<cus1:Given>${contactUpdateInfo.getContactFirstName()}</cus1:Given>\"\n"
                +
                "\t}\n"
                +
                "\tif(contactUpdateInfo.getContactMiddleName() !=null){\n"
                +
                "\t\tbodyMiddle=bodyMiddle+\"<cus1:Middle>${contactUpdateInfo.getContactMiddleName()}</cus1:Middle>\"\n"
                +
                "\t}\n"
                +
                "\tif(contactUpdateInfo.getContactLastName() !=null){\n"
                +
                "\t\tbodyMiddle=bodyMiddle+\"<cus1:Last>${contactUpdateInfo.getContactLastName()}</cus1:Last>\"\n"
                +
                "\t}\n"
                +
                "\tif(contactUpdateInfo.getContactSuffix() !=null){\n"
                +
                "\t\tbodyMiddle=bodyMiddle+\"<cus1:Suffix>${contactUpdateInfo.getContactSuffix().toUpperCase()}</cus1:Suffix>\"\n"
                +
                "\t}\n"
                +
                "\tbodyMiddle=bodyMiddle+\"</cus:ContactName>\"\n"
                +
                "}\n"
                +
                "if(contactUpdateInfo.getContactPhoneAreaCode()!=null||\n"
                +
                "\t\tcontactUpdateInfo.getContactPhoneCountryCode()!=null ||\n"
                +
                "\t\tcontactUpdateInfo.getContactPhoneNumber()!=null ||\n"
                +
                "\t\tcontactUpdateInfo.getContactPhoneExtension()!=null){\n"
                +
                "\tbodyMiddle=bodyMiddle+\"\"\" <cus:Phone isActive=\"Y\" isPrimary=\"Y\"><cus1:RefKeyId>${if(contactUpdateInfo.getPhoneRefKeyId()==null || contactUpdateInfo.getPhoneRefKeyId().isEmpty()){return 0}else{return contactUpdateInfo.getPhoneRefKeyId()}}</cus1:RefKeyId>\"\"\"\n"
                +
                "\n"
                +
                "\tif(contactUpdateInfo.getContactPhoneAreaCode()!=null){\n"
                +
                "\t\tbodyMiddle=bodyMiddle+\"<cus1:AreaCode>${contactUpdateInfo.getContactPhoneAreaCode()}</cus1:AreaCode>\"\n"
                +
                "\t}\n"
                +
                "\tif(contactUpdateInfo.getContactPhoneCountryCode()!=null){\n"
                +
                "\t\tbodyMiddle=bodyMiddle+\"<cus1:CountryCode>${contactUpdateInfo.getContactPhoneCountryCode()}</cus1:CountryCode>\"\n"
                +
                "\t}\n"
                +
                "\tif(contactUpdateInfo.getContactPhoneNumber()!=null){\n"
                +
                "\t\tbodyMiddle=bodyMiddle+\"<cus1:Number>${contactUpdateInfo.getContactPhoneNumber()}</cus1:Number>\"\n"
                +
                "\t}\n"
                +
                "\tif(contactUpdateInfo.getContactPhoneExtension()!=null){\n"
                +
                "\t\tbodyMiddle=bodyMiddle+\"<cus1:Extension>${contactUpdateInfo.getContactPhoneExtension()}</cus1:Extension><cus1:PrimaryType>TELEPHONE</cus1:PrimaryType>\"\n"
                +
                "\t}\n"
                +
                "\tbodyMiddle=bodyMiddle+\"</cus:Phone>\"\n"
                +
                "}\n"
                +
                "if(contactUpdateInfo.getContactEmail()!=null){\n"
                +
                "\tbodyMiddle=bodyMiddle+\"\"\"<cus:Email isPrimary=\"Y\"><cus1:RefKeyId>${if(contactUpdateInfo.getEmailRefKeyId()==null || contactUpdateInfo.getEmailRefKeyId().isEmpty()){return 0}else{return contactUpdateInfo.getEmailRefKeyId()}}</cus1:RefKeyId>\"\"\"\n"
                +
                "\n"
                +
                "\tif(contactUpdateInfo.getContactEmail()!=null){\n"
                +
                "\t\tbodyMiddle=bodyMiddle+\"<cus1:Address>${contactUpdateInfo.getContactEmail()}</cus1:Address></cus:Email>\"\n"
                +
                "\t}\n"
                +
                "\t//bodyMiddle=bodyMiddle+\"</cus:Email>\"\n"
                +
                "}\n"
                +
                "/* //commented out address by Sonu\n"
                +
                "if(contactUpdateInfo.getContactAddress1()!=null||\n"
                +
                "    contactUpdateInfo.getContactAddress2()!=null ||\n"
                +
                "    contactUpdateInfo.getContactAddress3()!=null ||\n"
                +
                "    contactUpdateInfo.getContactAddress4()!=null||\n"
                +
                "    contactUpdateInfo.getContactAddressCity()!=null ||\n"
                +
                "    contactUpdateInfo.getContactAddressState()!=null ||\n"
                +
                "    contactUpdateInfo.getContactAddressZip()!=null ||\n"
                +
                "    contactUpdateInfo.getContactAddressPostalCountryCode()!=null){\n"
                +
                "    bodyMiddle=bodyMiddle+\"\"\"<cus:Address isStreetAddress=\"Y\"><cus1:RefKeyID>${if(contactUpdateInfo.getAddressRefKeyId()==null || contactUpdateInfo.getAddressRefKeyId().isEmpty()){return 0}else{return contactUpdateInfo.getAddressRefKeyId()}}</cus1:RefKeyID>\"\"\"\n"
                +
                "    if(contactUpdateInfo.getContactAddress1()!=null){\n"
                +
                "        bodyMiddle=bodyMiddle+\"<cus1:AddressLine>${contactUpdateInfo.getContactAddress1()}</cus1:AddressLine>\"\n"
                +
                "    }\n"
                +
                "    if(contactUpdateInfo.getContactAddress2()!=null){\n"
                +
                "        bodyMiddle=bodyMiddle+\"<cus1:AddressLine>${contactUpdateInfo.getContactAddress2()}</cus1:AddressLine>\"\n"
                +
                "    }\n"
                +
                "    if(contactUpdateInfo.getContactAddress3()!=null){\n"
                +
                "        bodyMiddle=bodyMiddle+\"<cus1:AddressLine>${contactUpdateInfo.getContactAddress3()}</cus1:AddressLine>\"\n"
                +
                "    }\n"
                +
                "    if(contactUpdateInfo.getContactAddress4()!=null){\n"
                +
                "        bodyMiddle=bodyMiddle+\"<cus1:AddressLine>${contactUpdateInfo.getContactAddress4()}</cus1:AddressLine>\"\n"
                +
                "    }\n"
                +
                "    if(contactUpdateInfo.getContactAddressCity()!=null){\n"
                +
                "        bodyMiddle=bodyMiddle+\"<cus1:City>${contactUpdateInfo.getContactAddressCity()}</cus1:City>\"\n"
                +
                "    }\n"
                +
                "    if(contactUpdateInfo.getContactAddressState()!=null){\n"
                +
                "        bodyMiddle=bodyMiddle+\"<cus1:State>${contactUpdateInfo.getContactAddressState()}</cus1:State>\"\n"
                +
                "    }\n"
                +
                "    if(contactUpdateInfo.getContactAddressZip()!=null){\n"
                +
                "        bodyMiddle=bodyMiddle+\"<cus1:Zip>${contactUpdateInfo.getContactAddressZip()}</cus1:Zip>\"\n"
                +
                "    }\n"
                +
                "    if(contactUpdateInfo.getContactAddressPostalCountryCode()!=null){\n"
                +
                "        bodyMiddle=bodyMiddle+\"<cus1:CountryCode>${contactUpdateInfo.getContactAddressPostalCountryCode()}</cus1:CountryCode>\"\n"
                +
                "    }\n"
                +
                "    bodyMiddle=bodyMiddle+\"</cus:Address>\"\n"
                +
                "    }\n"
                +
                "*/\n"
                +
                "\n"
                +
                "bodyMiddle=bodyMiddle+\"\"\"<cus:CSCBusinessUnit>${if(businessUnit==null || businessUnit.isEmpty()){return \"\"}else{return businessUnit}}</cus:CSCBusinessUnit><cus:SystemUserId>${requestParameters.get(\"updateUser\")}</cus:SystemUserId><cus:SourceOfChange>Salesforce</cus:SourceOfChange>\"\"\"\n"
                +
                "def begin=\"\"\"<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:cus=\"http://webservices.cscinfo.com/etp/ECCMS/CustomersContacts\" xmlns:cus1=\"http://schemas.cscinfo.com/etp/ECCMS/CustomersContacts\"><soapenv:Header/>\"\"\"\n"
                +
                "def bodyBegin=\"<soapenv:Body><cus:UpdateContactRequest>\"\n" +
                "def bodyEnd = \"   </cus:UpdateContactRequest></soapenv:Body>\"\n" +
                "def body = bodyBegin + bodyMiddle + bodyEnd\n" +
                "\n" +
                "def end =\"</soapenv:Envelope>\"\n" +
                "\n" +
                "def request = begin + body + end\n" +
                "return request";
        System.out.println("Execute script with expression on process definition");
        Expression groovyExpression = new ExpressionBuilder().createGroovyScriptExpression("groovy", script, String.class.getName());
        String property = System.getProperty("process.definition.id");
        long processDefinitionId = Long.valueOf(property);

        HashMap<String, String> parameters = new HashMap<String, String>();
        parameters.put("server.url", System.getProperty("server.url"));
        parameters.put("application.name", System.getProperty("application.name"));
        APITypeManager.setAPITypeAndParams(ApiAccessType.HTTP, parameters);

        APISession apiSession = TenantAPIAccessor.getLoginAPI().login(System.getProperty("username"), System.getProperty("password"));
        try {
            Map<String, Serializable> context = new HashMap<String, Serializable>();
            context.put("contactUpdateInfo", null);
            Serializable groovy = TenantAPIAccessor.getProcessAPI(apiSession).evaluateExpressionOnProcessDefinition(
                    groovyExpression, context, processDefinitionId);

            System.out.println("result=" + groovy);
        } catch (Throwable t) {
            boolean compilation = t.getMessage().toLowerCase().contains("compilation");
            t.printStackTrace();
            System.err.println("=============");
            System.err.println("=============");
            System.err.println("=============");
            if (compilation) {
                System.err.println("Issue is reproduced");
                    System.err.println("=============");
            } else {
                    System.err.println("Issue not reproduced");
                    System.err.println("=============");
            }
        }
        TenantAPIAccessor.getLoginAPI().logout(apiSession);
    }
}
