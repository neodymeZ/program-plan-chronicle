package com.iza.ppc.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.iza.ppc.model.Program;
import com.iza.ppc.model.ChronicleSnapshot;

import java.io.IOException;
import java.util.List;

/**
 * JSON deserializer for chronicle snapshots ({@link ChronicleSnapshot})
 *
 * @author Zakhar Izverov
 * created on 04.10.2021
 */

@SuppressWarnings("unused")
public class ChronicleSnapshotDeserializer extends StdDeserializer<ChronicleSnapshot> {

    public ChronicleSnapshotDeserializer() {
        this(null);
    }

    public ChronicleSnapshotDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public ChronicleSnapshot deserialize(JsonParser jp, DeserializationContext ctxt)
            throws IOException {

        JsonNode snapshotNode = jp.getCodec().readTree(jp);
        ChronicleSnapshot chronicleSnapshot = new ChronicleSnapshot();

        ObjectMapper mapper = new ObjectMapper();
        ObjectReader reader = mapper.readerFor(new TypeReference<List<Program>>() {
        });

        chronicleSnapshot.setPrograms(reader.readValue(snapshotNode.get("data").get("site").get("epg").get("items")));

        return chronicleSnapshot;
    }
}
