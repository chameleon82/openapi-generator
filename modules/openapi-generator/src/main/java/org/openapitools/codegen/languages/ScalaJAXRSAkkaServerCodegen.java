package org.openapitools.codegen.languages;

import org.openapitools.codegen.*;

import java.io.File;
import java.util.List;
import java.util.Map;

public class ScalaJAXRSAkkaServerCodegen extends ScalaJAXRSSpecCodegen {

    /**
     * Entrypoint to generate Scala akka-http JAX-RS specification based server
     */

    public ScalaJAXRSAkkaServerCodegen() {
        super("generated-code/akka-http-server");

        //invokerPackage = ""
        apiPackage = "org.openapitools.server.api";
        modelPackage = "org.openapitools.server.model";

        invokerPackage = apiPackage;

        embeddedTemplateDir = templateDir = "scala-jaxrs";

        apiTemplateFiles.put("akka-http-server/api.mustache", ".scala");
        apiTemplateFiles.put("akka-http-server/apiController.mustache", "Controller.scala");

        String apiPackageFolder = sourceFolder + File.separator + apiPackage().replace('.', File.separatorChar);

        supportingFiles.add(new SupportingFile("akka-http-server/build.sbt.mustache", "", "build.sbt"));
        supportingFiles.add(new SupportingFile("akka-http-server/abstractApi.mustache", apiPackageFolder, "AbstractApi.scala"));
        supportingFiles.add(new SupportingFile("akka-http-server/defaultJsonFormats.mustache", apiPackageFolder, "DefaultJsonFormats.scala"));

    }

    private String dataTypeToMatcher(String dataType) {
       if (dataType.equals("String")) return "Segment";
       if (dataType.equals("Integer")) return "IntNumber";
       if (dataType.equals("Long")) return "LongNumber";
       return "Segment";
    }

    private String formatAkkaPath(String path, List<CodegenParameter> pathParams) {
        if (path.isEmpty()) return "pathSingleSlash";
        String[] splittedPath = path.split("/");
        StringBuilder sb = new StringBuilder();
        boolean leadSlash = false;
        for (int x = 0; x < splittedPath.length; x++) {
            if (splittedPath[x].isEmpty()) continue;
            if (leadSlash) {sb.append(" / ");} else {leadSlash = true;};
            if (splittedPath[x].startsWith("{")) {
                for (CodegenParameter pathParam : pathParams) {
                    if (splittedPath[x].equals("{"+pathParam.paramName+"}")){

                        sb.append(dataTypeToMatcher(pathParam.dataType) + " ");
                    }
                }
            } else {
                sb.append("\"").append(splittedPath[x]).append("\"");
            }
        }
        //path = path.replaceAll("/", "\" / \"");
//        for (CodegenParameter pathParam : pathParams) {
//            path = path.replace(
//                    "\"{" + pathParam.paramName + "}",
//                    " " + pathParam.dataType + "Number / \""
//            );
//        }
//        path = path.replaceAll("\" /","+").replaceAll("/ \"","-");
        return "path( " + sb.toString() + ")";
    }

    @Override
    public Map<String, Object> postProcessOperationsWithModels(Map<String, Object> objs, List<Object> allModels) {
        super.postProcessOperationsWithModels(objs, allModels);

        @SuppressWarnings("unchecked")
        Map<String, Object> operations = (Map<String, Object>) objs.get("operations");
        if (operations != null) {
            String commonBaseName = null;
            boolean baseNameEquals = true;
            @SuppressWarnings("unchecked")
            List<CodegenOperation> ops = (List<CodegenOperation>) operations.get("operation");
            for (CodegenOperation operation : ops) {
                operation.vendorExtensions.put("x-path", formatAkkaPath( operation.path, operation.pathParams));
                operation.vendorExtensions.put("x-akka-httpMethod-directive", operation.httpMethod.toLowerCase());
               // operation.has
            }
        }
        return objs;
    }

    @Override
    public CodegenType getTag() {
        return CodegenType.SERVER;
    }

    @Override
    public String getName() {
        return "scala-jaxrs-akka-http-server";
    }

    @Override
    public String getHelp() {
        return "Generates a Scala Akka-Http JAX-RS Specification based server";
    }
}
