package com.jsontoll.validation;

import com.fasterxml.jackson.databind.JsonNode;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;
import com.networknt.schema.ValidationMessage;

import java.util.Set;

public class Draft202012Strategy {

    public static class Report {
        public final boolean valid;
        public final Set<ValidationMessage> messages;

        public Report(boolean valid, Set<ValidationMessage> messages) {
            this.valid = valid;
            this.messages = messages;
        }
    }

    private static final JsonSchemaFactory factory = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V202012);

    public static Report validate(JsonNode schemaNode, JsonNode instanceNode) {
        try {
            JsonSchema schema = factory.getSchema(schemaNode);
            Set<ValidationMessage> messages = schema.validate(instanceNode);
            return new Report(messages.isEmpty(), messages);
        } catch (Exception e) {
            return new Report(false, Set.of());
        }
    }
}