package cern.c2mon.server.eslog.structure.types;

import cern.c2mon.server.eslog.structure.mappings.TagBooleanMapping;
import cern.c2mon.server.eslog.structure.mappings.TagESMapping;
import org.elasticsearch.common.xcontent.XContentBuilder;

import java.io.IOException;

/**
 * Represents a Tag in ElasticSearch.
 * This type of TagES contains a boolean tagValue.
 * @author Alban Marguet.
 */
public class TagBoolean extends TagES implements TagESInterface {

    public String getMapping() {
        return mapping.getMapping();
    }

    @Override
    public void setTagValue(Object tagValue) {
        if (tagValue instanceof Boolean) {
            this.tagValue = tagValue;
        } else {
            throw new IllegalArgumentException("Must give a String object to TagString in ElasticSearch");
        }
    }

    @Override
    public void setMapping(String tagValueType, TagESMapping mapping) {
        this.mapping = mapping;
    }
}