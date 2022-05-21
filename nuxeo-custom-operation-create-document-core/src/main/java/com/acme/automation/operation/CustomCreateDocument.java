package com.acme.automation.operation;

import static org.nuxeo.ecm.core.api.validation.DocumentValidationService.CTX_MAP_KEY;
import static org.nuxeo.ecm.core.api.validation.DocumentValidationService.Forcing;

import java.io.IOException;

import org.nuxeo.ecm.automation.core.Constants;
import org.nuxeo.ecm.automation.core.annotations.Context;
import org.nuxeo.ecm.automation.core.annotations.Operation;
import org.nuxeo.ecm.automation.core.annotations.OperationMethod;
import org.nuxeo.ecm.automation.core.annotations.Param;
import org.nuxeo.ecm.automation.core.collectors.DocumentModelCollector;
import org.nuxeo.ecm.automation.core.util.DocumentHelper;
import org.nuxeo.ecm.automation.core.util.Properties;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentRef;

/**
 *
 */
@Operation(id=CustomCreateDocument.ID, category=Constants.CAT_DOCUMENT, label="Document.CustomCreate", description="Describe here what your operation does.")
public class CustomCreateDocument {

    public static final String ID = "Document.CustomCreate";

    @Context
    protected CoreSession session;

    @Param(name = "type")
    protected String type;

    @Param(name = "name", required = false)
    protected String name;

    @Param(name = "properties", required = false)
    protected Properties content;

    @Param(name = "validation", required = false)
    protected boolean validation = true;

    @OperationMethod(collector = DocumentModelCollector.class)
    public DocumentModel run(DocumentModel doc) throws IOException {
        if (name == null) {
            name = "Untitled";
        }
        DocumentModel newDoc = session.createDocumentModel(doc.getPathAsString(), name, type);
        if (content != null) {
            DocumentHelper.setProperties(session, newDoc, content);
        }
        if (!validation) {
            newDoc.putContextData(CTX_MAP_KEY, Forcing.TURN_OFF);
        }
        return session.createDocument(newDoc);
    }

    @OperationMethod(collector = DocumentModelCollector.class)
    public DocumentModel run(DocumentRef doc) throws IOException {
        return run(session.getDocument(doc));
    }
}
