package org.sonatype.nexus.blobstore.file;

import java.io.File;
import java.util.Map;

import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.HTreeMap;
import org.mapdb.TxMaker;
import org.sonatype.nexus.blobstore.api.BlobId;
import org.sonatype.nexus.blobstore.file.MapdbBlobMetadataStore.MetadataRecord;

public class TestNexusMapDB {

    public static void main(String[] args) {
        TxMaker db = DBMaker.newFileDB(new File("/tmp/metadata-mapdb/metadata.db")).checksumEnable().makeTxMaker();
        HTreeMap<BlobId, MetadataRecord> treeMap = db.makeTx().getHashMap("entries");

        for (Map.Entry<BlobId, MetadataRecord> entry : treeMap.entrySet()) {
            System.out.println(entry.getKey() + ":" + entry.getValue());
        }

        db.close();
    }
}
