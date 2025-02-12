
package digital.inception.codes.ws.client;

import javax.xml.namespace.QName;
import digital.inception.core.ws.client.ServiceError;
import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.annotation.XmlElementDecl;
import jakarta.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the digital.inception.codes.ws.client package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _Code_QNAME = new QName("http://inception.digital/codes", "Code");
    private final static QName _CodeCategory_QNAME = new QName("http://inception.digital/codes", "CodeCategory");
    private final static QName _CodeCategorySummary_QNAME = new QName("http://inception.digital/codes", "CodeCategorySummary");
    private final static QName _CreateCode_QNAME = new QName("http://inception.digital/codes", "CreateCode");
    private final static QName _CreateCodeCategory_QNAME = new QName("http://inception.digital/codes", "CreateCodeCategory");
    private final static QName _CreateCodeCategoryResponse_QNAME = new QName("http://inception.digital/codes", "CreateCodeCategoryResponse");
    private final static QName _CreateCodeResponse_QNAME = new QName("http://inception.digital/codes", "CreateCodeResponse");
    private final static QName _DeleteCode_QNAME = new QName("http://inception.digital/codes", "DeleteCode");
    private final static QName _DeleteCodeCategory_QNAME = new QName("http://inception.digital/codes", "DeleteCodeCategory");
    private final static QName _DeleteCodeCategoryResponse_QNAME = new QName("http://inception.digital/codes", "DeleteCodeCategoryResponse");
    private final static QName _DeleteCodeResponse_QNAME = new QName("http://inception.digital/codes", "DeleteCodeResponse");
    private final static QName _GetCode_QNAME = new QName("http://inception.digital/codes", "GetCode");
    private final static QName _GetCodeCategories_QNAME = new QName("http://inception.digital/codes", "GetCodeCategories");
    private final static QName _GetCodeCategoriesResponse_QNAME = new QName("http://inception.digital/codes", "GetCodeCategoriesResponse");
    private final static QName _GetCodeCategory_QNAME = new QName("http://inception.digital/codes", "GetCodeCategory");
    private final static QName _GetCodeCategoryData_QNAME = new QName("http://inception.digital/codes", "GetCodeCategoryData");
    private final static QName _GetCodeCategoryDataResponse_QNAME = new QName("http://inception.digital/codes", "GetCodeCategoryDataResponse");
    private final static QName _GetCodeCategoryLastModified_QNAME = new QName("http://inception.digital/codes", "GetCodeCategoryLastModified");
    private final static QName _GetCodeCategoryLastModifiedResponse_QNAME = new QName("http://inception.digital/codes", "GetCodeCategoryLastModifiedResponse");
    private final static QName _GetCodeCategoryName_QNAME = new QName("http://inception.digital/codes", "GetCodeCategoryName");
    private final static QName _GetCodeCategoryNameResponse_QNAME = new QName("http://inception.digital/codes", "GetCodeCategoryNameResponse");
    private final static QName _GetCodeCategoryResponse_QNAME = new QName("http://inception.digital/codes", "GetCodeCategoryResponse");
    private final static QName _GetCodeCategorySummaries_QNAME = new QName("http://inception.digital/codes", "GetCodeCategorySummaries");
    private final static QName _GetCodeCategorySummariesResponse_QNAME = new QName("http://inception.digital/codes", "GetCodeCategorySummariesResponse");
    private final static QName _GetCodeName_QNAME = new QName("http://inception.digital/codes", "GetCodeName");
    private final static QName _GetCodeNameResponse_QNAME = new QName("http://inception.digital/codes", "GetCodeNameResponse");
    private final static QName _GetCodeResponse_QNAME = new QName("http://inception.digital/codes", "GetCodeResponse");
    private final static QName _GetCodes_QNAME = new QName("http://inception.digital/codes", "GetCodes");
    private final static QName _GetCodesResponse_QNAME = new QName("http://inception.digital/codes", "GetCodesResponse");
    private final static QName _UpdateCode_QNAME = new QName("http://inception.digital/codes", "UpdateCode");
    private final static QName _UpdateCodeCategory_QNAME = new QName("http://inception.digital/codes", "UpdateCodeCategory");
    private final static QName _UpdateCodeCategoryResponse_QNAME = new QName("http://inception.digital/codes", "UpdateCodeCategoryResponse");
    private final static QName _UpdateCodeResponse_QNAME = new QName("http://inception.digital/codes", "UpdateCodeResponse");
    private final static QName _CodeCategoryNotFoundException_QNAME = new QName("http://inception.digital/codes", "CodeCategoryNotFoundException");
    private final static QName _DuplicateCodeException_QNAME = new QName("http://inception.digital/codes", "DuplicateCodeException");
    private final static QName _DuplicateCodeCategoryException_QNAME = new QName("http://inception.digital/codes", "DuplicateCodeCategoryException");
    private final static QName _CodeNotFoundException_QNAME = new QName("http://inception.digital/codes", "CodeNotFoundException");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: digital.inception.codes.ws.client
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Code }
     * 
     */
    public Code createCode() {
        return new Code();
    }

    /**
     * Create an instance of {@link CodeCategory }
     * 
     */
    public CodeCategory createCodeCategory() {
        return new CodeCategory();
    }

    /**
     * Create an instance of {@link CodeCategorySummary }
     * 
     */
    public CodeCategorySummary createCodeCategorySummary() {
        return new CodeCategorySummary();
    }

    /**
     * Create an instance of {@link CreateCode }
     * 
     */
    public CreateCode createCreateCode() {
        return new CreateCode();
    }

    /**
     * Create an instance of {@link CreateCodeCategory }
     * 
     */
    public CreateCodeCategory createCreateCodeCategory() {
        return new CreateCodeCategory();
    }

    /**
     * Create an instance of {@link CreateCodeCategoryResponse }
     * 
     */
    public CreateCodeCategoryResponse createCreateCodeCategoryResponse() {
        return new CreateCodeCategoryResponse();
    }

    /**
     * Create an instance of {@link CreateCodeResponse }
     * 
     */
    public CreateCodeResponse createCreateCodeResponse() {
        return new CreateCodeResponse();
    }

    /**
     * Create an instance of {@link DeleteCode }
     * 
     */
    public DeleteCode createDeleteCode() {
        return new DeleteCode();
    }

    /**
     * Create an instance of {@link DeleteCodeCategory }
     * 
     */
    public DeleteCodeCategory createDeleteCodeCategory() {
        return new DeleteCodeCategory();
    }

    /**
     * Create an instance of {@link DeleteCodeCategoryResponse }
     * 
     */
    public DeleteCodeCategoryResponse createDeleteCodeCategoryResponse() {
        return new DeleteCodeCategoryResponse();
    }

    /**
     * Create an instance of {@link DeleteCodeResponse }
     * 
     */
    public DeleteCodeResponse createDeleteCodeResponse() {
        return new DeleteCodeResponse();
    }

    /**
     * Create an instance of {@link GetCode }
     * 
     */
    public GetCode createGetCode() {
        return new GetCode();
    }

    /**
     * Create an instance of {@link GetCodeCategories }
     * 
     */
    public GetCodeCategories createGetCodeCategories() {
        return new GetCodeCategories();
    }

    /**
     * Create an instance of {@link GetCodeCategoriesResponse }
     * 
     */
    public GetCodeCategoriesResponse createGetCodeCategoriesResponse() {
        return new GetCodeCategoriesResponse();
    }

    /**
     * Create an instance of {@link GetCodeCategory }
     * 
     */
    public GetCodeCategory createGetCodeCategory() {
        return new GetCodeCategory();
    }

    /**
     * Create an instance of {@link GetCodeCategoryData }
     * 
     */
    public GetCodeCategoryData createGetCodeCategoryData() {
        return new GetCodeCategoryData();
    }

    /**
     * Create an instance of {@link GetCodeCategoryDataResponse }
     * 
     */
    public GetCodeCategoryDataResponse createGetCodeCategoryDataResponse() {
        return new GetCodeCategoryDataResponse();
    }

    /**
     * Create an instance of {@link GetCodeCategoryLastModified }
     * 
     */
    public GetCodeCategoryLastModified createGetCodeCategoryLastModified() {
        return new GetCodeCategoryLastModified();
    }

    /**
     * Create an instance of {@link GetCodeCategoryLastModifiedResponse }
     * 
     */
    public GetCodeCategoryLastModifiedResponse createGetCodeCategoryLastModifiedResponse() {
        return new GetCodeCategoryLastModifiedResponse();
    }

    /**
     * Create an instance of {@link GetCodeCategoryName }
     * 
     */
    public GetCodeCategoryName createGetCodeCategoryName() {
        return new GetCodeCategoryName();
    }

    /**
     * Create an instance of {@link GetCodeCategoryNameResponse }
     * 
     */
    public GetCodeCategoryNameResponse createGetCodeCategoryNameResponse() {
        return new GetCodeCategoryNameResponse();
    }

    /**
     * Create an instance of {@link GetCodeCategoryResponse }
     * 
     */
    public GetCodeCategoryResponse createGetCodeCategoryResponse() {
        return new GetCodeCategoryResponse();
    }

    /**
     * Create an instance of {@link GetCodeCategorySummaries }
     * 
     */
    public GetCodeCategorySummaries createGetCodeCategorySummaries() {
        return new GetCodeCategorySummaries();
    }

    /**
     * Create an instance of {@link GetCodeCategorySummariesResponse }
     * 
     */
    public GetCodeCategorySummariesResponse createGetCodeCategorySummariesResponse() {
        return new GetCodeCategorySummariesResponse();
    }

    /**
     * Create an instance of {@link GetCodeName }
     * 
     */
    public GetCodeName createGetCodeName() {
        return new GetCodeName();
    }

    /**
     * Create an instance of {@link GetCodeNameResponse }
     * 
     */
    public GetCodeNameResponse createGetCodeNameResponse() {
        return new GetCodeNameResponse();
    }

    /**
     * Create an instance of {@link GetCodeResponse }
     * 
     */
    public GetCodeResponse createGetCodeResponse() {
        return new GetCodeResponse();
    }

    /**
     * Create an instance of {@link GetCodes }
     * 
     */
    public GetCodes createGetCodes() {
        return new GetCodes();
    }

    /**
     * Create an instance of {@link GetCodesResponse }
     * 
     */
    public GetCodesResponse createGetCodesResponse() {
        return new GetCodesResponse();
    }

    /**
     * Create an instance of {@link UpdateCode }
     * 
     */
    public UpdateCode createUpdateCode() {
        return new UpdateCode();
    }

    /**
     * Create an instance of {@link UpdateCodeCategory }
     * 
     */
    public UpdateCodeCategory createUpdateCodeCategory() {
        return new UpdateCodeCategory();
    }

    /**
     * Create an instance of {@link UpdateCodeCategoryResponse }
     * 
     */
    public UpdateCodeCategoryResponse createUpdateCodeCategoryResponse() {
        return new UpdateCodeCategoryResponse();
    }

    /**
     * Create an instance of {@link UpdateCodeResponse }
     * 
     */
    public UpdateCodeResponse createUpdateCodeResponse() {
        return new UpdateCodeResponse();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Code }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link Code }{@code >}
     */
    @XmlElementDecl(namespace = "http://inception.digital/codes", name = "Code")
    public JAXBElement<Code> createCode(Code value) {
        return new JAXBElement<Code>(_Code_QNAME, Code.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CodeCategory }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link CodeCategory }{@code >}
     */
    @XmlElementDecl(namespace = "http://inception.digital/codes", name = "CodeCategory")
    public JAXBElement<CodeCategory> createCodeCategory(CodeCategory value) {
        return new JAXBElement<CodeCategory>(_CodeCategory_QNAME, CodeCategory.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CodeCategorySummary }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link CodeCategorySummary }{@code >}
     */
    @XmlElementDecl(namespace = "http://inception.digital/codes", name = "CodeCategorySummary")
    public JAXBElement<CodeCategorySummary> createCodeCategorySummary(CodeCategorySummary value) {
        return new JAXBElement<CodeCategorySummary>(_CodeCategorySummary_QNAME, CodeCategorySummary.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CreateCode }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link CreateCode }{@code >}
     */
    @XmlElementDecl(namespace = "http://inception.digital/codes", name = "CreateCode")
    public JAXBElement<CreateCode> createCreateCode(CreateCode value) {
        return new JAXBElement<CreateCode>(_CreateCode_QNAME, CreateCode.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CreateCodeCategory }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link CreateCodeCategory }{@code >}
     */
    @XmlElementDecl(namespace = "http://inception.digital/codes", name = "CreateCodeCategory")
    public JAXBElement<CreateCodeCategory> createCreateCodeCategory(CreateCodeCategory value) {
        return new JAXBElement<CreateCodeCategory>(_CreateCodeCategory_QNAME, CreateCodeCategory.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CreateCodeCategoryResponse }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link CreateCodeCategoryResponse }{@code >}
     */
    @XmlElementDecl(namespace = "http://inception.digital/codes", name = "CreateCodeCategoryResponse")
    public JAXBElement<CreateCodeCategoryResponse> createCreateCodeCategoryResponse(CreateCodeCategoryResponse value) {
        return new JAXBElement<CreateCodeCategoryResponse>(_CreateCodeCategoryResponse_QNAME, CreateCodeCategoryResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CreateCodeResponse }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link CreateCodeResponse }{@code >}
     */
    @XmlElementDecl(namespace = "http://inception.digital/codes", name = "CreateCodeResponse")
    public JAXBElement<CreateCodeResponse> createCreateCodeResponse(CreateCodeResponse value) {
        return new JAXBElement<CreateCodeResponse>(_CreateCodeResponse_QNAME, CreateCodeResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DeleteCode }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link DeleteCode }{@code >}
     */
    @XmlElementDecl(namespace = "http://inception.digital/codes", name = "DeleteCode")
    public JAXBElement<DeleteCode> createDeleteCode(DeleteCode value) {
        return new JAXBElement<DeleteCode>(_DeleteCode_QNAME, DeleteCode.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DeleteCodeCategory }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link DeleteCodeCategory }{@code >}
     */
    @XmlElementDecl(namespace = "http://inception.digital/codes", name = "DeleteCodeCategory")
    public JAXBElement<DeleteCodeCategory> createDeleteCodeCategory(DeleteCodeCategory value) {
        return new JAXBElement<DeleteCodeCategory>(_DeleteCodeCategory_QNAME, DeleteCodeCategory.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DeleteCodeCategoryResponse }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link DeleteCodeCategoryResponse }{@code >}
     */
    @XmlElementDecl(namespace = "http://inception.digital/codes", name = "DeleteCodeCategoryResponse")
    public JAXBElement<DeleteCodeCategoryResponse> createDeleteCodeCategoryResponse(DeleteCodeCategoryResponse value) {
        return new JAXBElement<DeleteCodeCategoryResponse>(_DeleteCodeCategoryResponse_QNAME, DeleteCodeCategoryResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DeleteCodeResponse }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link DeleteCodeResponse }{@code >}
     */
    @XmlElementDecl(namespace = "http://inception.digital/codes", name = "DeleteCodeResponse")
    public JAXBElement<DeleteCodeResponse> createDeleteCodeResponse(DeleteCodeResponse value) {
        return new JAXBElement<DeleteCodeResponse>(_DeleteCodeResponse_QNAME, DeleteCodeResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetCode }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link GetCode }{@code >}
     */
    @XmlElementDecl(namespace = "http://inception.digital/codes", name = "GetCode")
    public JAXBElement<GetCode> createGetCode(GetCode value) {
        return new JAXBElement<GetCode>(_GetCode_QNAME, GetCode.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetCodeCategories }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link GetCodeCategories }{@code >}
     */
    @XmlElementDecl(namespace = "http://inception.digital/codes", name = "GetCodeCategories")
    public JAXBElement<GetCodeCategories> createGetCodeCategories(GetCodeCategories value) {
        return new JAXBElement<GetCodeCategories>(_GetCodeCategories_QNAME, GetCodeCategories.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetCodeCategoriesResponse }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link GetCodeCategoriesResponse }{@code >}
     */
    @XmlElementDecl(namespace = "http://inception.digital/codes", name = "GetCodeCategoriesResponse")
    public JAXBElement<GetCodeCategoriesResponse> createGetCodeCategoriesResponse(GetCodeCategoriesResponse value) {
        return new JAXBElement<GetCodeCategoriesResponse>(_GetCodeCategoriesResponse_QNAME, GetCodeCategoriesResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetCodeCategory }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link GetCodeCategory }{@code >}
     */
    @XmlElementDecl(namespace = "http://inception.digital/codes", name = "GetCodeCategory")
    public JAXBElement<GetCodeCategory> createGetCodeCategory(GetCodeCategory value) {
        return new JAXBElement<GetCodeCategory>(_GetCodeCategory_QNAME, GetCodeCategory.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetCodeCategoryData }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link GetCodeCategoryData }{@code >}
     */
    @XmlElementDecl(namespace = "http://inception.digital/codes", name = "GetCodeCategoryData")
    public JAXBElement<GetCodeCategoryData> createGetCodeCategoryData(GetCodeCategoryData value) {
        return new JAXBElement<GetCodeCategoryData>(_GetCodeCategoryData_QNAME, GetCodeCategoryData.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetCodeCategoryDataResponse }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link GetCodeCategoryDataResponse }{@code >}
     */
    @XmlElementDecl(namespace = "http://inception.digital/codes", name = "GetCodeCategoryDataResponse")
    public JAXBElement<GetCodeCategoryDataResponse> createGetCodeCategoryDataResponse(GetCodeCategoryDataResponse value) {
        return new JAXBElement<GetCodeCategoryDataResponse>(_GetCodeCategoryDataResponse_QNAME, GetCodeCategoryDataResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetCodeCategoryLastModified }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link GetCodeCategoryLastModified }{@code >}
     */
    @XmlElementDecl(namespace = "http://inception.digital/codes", name = "GetCodeCategoryLastModified")
    public JAXBElement<GetCodeCategoryLastModified> createGetCodeCategoryLastModified(GetCodeCategoryLastModified value) {
        return new JAXBElement<GetCodeCategoryLastModified>(_GetCodeCategoryLastModified_QNAME, GetCodeCategoryLastModified.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetCodeCategoryLastModifiedResponse }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link GetCodeCategoryLastModifiedResponse }{@code >}
     */
    @XmlElementDecl(namespace = "http://inception.digital/codes", name = "GetCodeCategoryLastModifiedResponse")
    public JAXBElement<GetCodeCategoryLastModifiedResponse> createGetCodeCategoryLastModifiedResponse(GetCodeCategoryLastModifiedResponse value) {
        return new JAXBElement<GetCodeCategoryLastModifiedResponse>(_GetCodeCategoryLastModifiedResponse_QNAME, GetCodeCategoryLastModifiedResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetCodeCategoryName }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link GetCodeCategoryName }{@code >}
     */
    @XmlElementDecl(namespace = "http://inception.digital/codes", name = "GetCodeCategoryName")
    public JAXBElement<GetCodeCategoryName> createGetCodeCategoryName(GetCodeCategoryName value) {
        return new JAXBElement<GetCodeCategoryName>(_GetCodeCategoryName_QNAME, GetCodeCategoryName.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetCodeCategoryNameResponse }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link GetCodeCategoryNameResponse }{@code >}
     */
    @XmlElementDecl(namespace = "http://inception.digital/codes", name = "GetCodeCategoryNameResponse")
    public JAXBElement<GetCodeCategoryNameResponse> createGetCodeCategoryNameResponse(GetCodeCategoryNameResponse value) {
        return new JAXBElement<GetCodeCategoryNameResponse>(_GetCodeCategoryNameResponse_QNAME, GetCodeCategoryNameResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetCodeCategoryResponse }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link GetCodeCategoryResponse }{@code >}
     */
    @XmlElementDecl(namespace = "http://inception.digital/codes", name = "GetCodeCategoryResponse")
    public JAXBElement<GetCodeCategoryResponse> createGetCodeCategoryResponse(GetCodeCategoryResponse value) {
        return new JAXBElement<GetCodeCategoryResponse>(_GetCodeCategoryResponse_QNAME, GetCodeCategoryResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetCodeCategorySummaries }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link GetCodeCategorySummaries }{@code >}
     */
    @XmlElementDecl(namespace = "http://inception.digital/codes", name = "GetCodeCategorySummaries")
    public JAXBElement<GetCodeCategorySummaries> createGetCodeCategorySummaries(GetCodeCategorySummaries value) {
        return new JAXBElement<GetCodeCategorySummaries>(_GetCodeCategorySummaries_QNAME, GetCodeCategorySummaries.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetCodeCategorySummariesResponse }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link GetCodeCategorySummariesResponse }{@code >}
     */
    @XmlElementDecl(namespace = "http://inception.digital/codes", name = "GetCodeCategorySummariesResponse")
    public JAXBElement<GetCodeCategorySummariesResponse> createGetCodeCategorySummariesResponse(GetCodeCategorySummariesResponse value) {
        return new JAXBElement<GetCodeCategorySummariesResponse>(_GetCodeCategorySummariesResponse_QNAME, GetCodeCategorySummariesResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetCodeName }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link GetCodeName }{@code >}
     */
    @XmlElementDecl(namespace = "http://inception.digital/codes", name = "GetCodeName")
    public JAXBElement<GetCodeName> createGetCodeName(GetCodeName value) {
        return new JAXBElement<GetCodeName>(_GetCodeName_QNAME, GetCodeName.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetCodeNameResponse }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link GetCodeNameResponse }{@code >}
     */
    @XmlElementDecl(namespace = "http://inception.digital/codes", name = "GetCodeNameResponse")
    public JAXBElement<GetCodeNameResponse> createGetCodeNameResponse(GetCodeNameResponse value) {
        return new JAXBElement<GetCodeNameResponse>(_GetCodeNameResponse_QNAME, GetCodeNameResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetCodeResponse }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link GetCodeResponse }{@code >}
     */
    @XmlElementDecl(namespace = "http://inception.digital/codes", name = "GetCodeResponse")
    public JAXBElement<GetCodeResponse> createGetCodeResponse(GetCodeResponse value) {
        return new JAXBElement<GetCodeResponse>(_GetCodeResponse_QNAME, GetCodeResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetCodes }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link GetCodes }{@code >}
     */
    @XmlElementDecl(namespace = "http://inception.digital/codes", name = "GetCodes")
    public JAXBElement<GetCodes> createGetCodes(GetCodes value) {
        return new JAXBElement<GetCodes>(_GetCodes_QNAME, GetCodes.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetCodesResponse }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link GetCodesResponse }{@code >}
     */
    @XmlElementDecl(namespace = "http://inception.digital/codes", name = "GetCodesResponse")
    public JAXBElement<GetCodesResponse> createGetCodesResponse(GetCodesResponse value) {
        return new JAXBElement<GetCodesResponse>(_GetCodesResponse_QNAME, GetCodesResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UpdateCode }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link UpdateCode }{@code >}
     */
    @XmlElementDecl(namespace = "http://inception.digital/codes", name = "UpdateCode")
    public JAXBElement<UpdateCode> createUpdateCode(UpdateCode value) {
        return new JAXBElement<UpdateCode>(_UpdateCode_QNAME, UpdateCode.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UpdateCodeCategory }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link UpdateCodeCategory }{@code >}
     */
    @XmlElementDecl(namespace = "http://inception.digital/codes", name = "UpdateCodeCategory")
    public JAXBElement<UpdateCodeCategory> createUpdateCodeCategory(UpdateCodeCategory value) {
        return new JAXBElement<UpdateCodeCategory>(_UpdateCodeCategory_QNAME, UpdateCodeCategory.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UpdateCodeCategoryResponse }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link UpdateCodeCategoryResponse }{@code >}
     */
    @XmlElementDecl(namespace = "http://inception.digital/codes", name = "UpdateCodeCategoryResponse")
    public JAXBElement<UpdateCodeCategoryResponse> createUpdateCodeCategoryResponse(UpdateCodeCategoryResponse value) {
        return new JAXBElement<UpdateCodeCategoryResponse>(_UpdateCodeCategoryResponse_QNAME, UpdateCodeCategoryResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UpdateCodeResponse }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link UpdateCodeResponse }{@code >}
     */
    @XmlElementDecl(namespace = "http://inception.digital/codes", name = "UpdateCodeResponse")
    public JAXBElement<UpdateCodeResponse> createUpdateCodeResponse(UpdateCodeResponse value) {
        return new JAXBElement<UpdateCodeResponse>(_UpdateCodeResponse_QNAME, UpdateCodeResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ServiceError }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link ServiceError }{@code >}
     */
    @XmlElementDecl(namespace = "http://inception.digital/codes", name = "CodeCategoryNotFoundException")
    public JAXBElement<ServiceError> createCodeCategoryNotFoundException(ServiceError value) {
        return new JAXBElement<ServiceError>(_CodeCategoryNotFoundException_QNAME, ServiceError.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ServiceError }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link ServiceError }{@code >}
     */
    @XmlElementDecl(namespace = "http://inception.digital/codes", name = "DuplicateCodeException")
    public JAXBElement<ServiceError> createDuplicateCodeException(ServiceError value) {
        return new JAXBElement<ServiceError>(_DuplicateCodeException_QNAME, ServiceError.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ServiceError }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link ServiceError }{@code >}
     */
    @XmlElementDecl(namespace = "http://inception.digital/codes", name = "DuplicateCodeCategoryException")
    public JAXBElement<ServiceError> createDuplicateCodeCategoryException(ServiceError value) {
        return new JAXBElement<ServiceError>(_DuplicateCodeCategoryException_QNAME, ServiceError.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ServiceError }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link ServiceError }{@code >}
     */
    @XmlElementDecl(namespace = "http://inception.digital/codes", name = "CodeNotFoundException")
    public JAXBElement<ServiceError> createCodeNotFoundException(ServiceError value) {
        return new JAXBElement<ServiceError>(_CodeNotFoundException_QNAME, ServiceError.class, null, value);
    }

}
