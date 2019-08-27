package org.openapitools.codegen.scala.jaxrsakkahttpserver;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.media.DateTimeSchema;
import io.swagger.v3.oas.models.media.IntegerSchema;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.parser.util.SchemaTypeUtil;
import org.openapitools.codegen.CodegenModel;
import org.openapitools.codegen.TestUtils;
import org.openapitools.codegen.languages.*;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class ScalaJaxrsAkkaHttpServerCodegenModelTest {

    protected ScalaJAXRSSpecCodegen codegen;

    @BeforeMethod
    public void beforeMethod() {
        codegen = new ScalaJAXRSAkkaServerCodegen();
    }

    @Test(description = "convert a simple scala model")
    public void simpleModelTest() {
        final Schema model = new Schema()
                .description("a sample model")
                .addProperties("id", new IntegerSchema().format(SchemaTypeUtil.INTEGER64_FORMAT))
                .addProperties("name", new StringSchema())
                .addProperties("createdAt", new DateTimeSchema())
                .addRequiredItem("id")
                .addRequiredItem("name");


        OpenAPI openAPI = TestUtils.createOpenAPIWithOneSchema("sample", model);
        codegen.setOpenAPI(openAPI);
        final CodegenModel cm = codegen.fromModel("sample", model);

        assertEquals(cm.vars.get(0).baseType, "Long");
        assertTrue(cm.imports.contains("Date"));
    }

}
