package edu.bits.wilp.ir_assignment.api;

import edu.bits.wilp.ir_assignment.index.Documents;
import edu.bits.wilp.ir_assignment.index.TfIdf;
import edu.bits.wilp.ir_assignment.search.SearchResult;
import edu.bits.wilp.ir_assignment.search.Searcher;
import edu.bits.wilp.ir_assignment.utils.JsonSerDe;
import edu.bits.wilp.ir_assignment.utils.KryoSerDe;
import edu.bits.wilp.ir_assignment.utils.SystemUtil;
import io.javalin.Javalin;
import io.javalin.http.ContentType;
import org.apache.commons.lang3.math.NumberUtils;

import java.io.FileNotFoundException;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        SystemUtil.disableWarning();

        String modelFile = "output.bin";
        String documentFile = "documents.bin";
        TfIdf tfIdf = KryoSerDe.readFromFile(modelFile, TfIdf.class);
        Documents documents = KryoSerDe.readFromFile(documentFile, Documents.class);
        Searcher searcher = new Searcher(tfIdf, documents.getDocuments());

        int port = NumberUtils.toInt(System.getenv("PORT"));
        Javalin app = Javalin.create(config -> {
            config.enableCorsForAllOrigins();
        }).start(port);
        app.get("/health", ctx -> ctx.result("Ok!"));
        app.get("/search", ctx -> {
            String q = ctx.queryParam("q");
            SearchResult result = searcher.search(10, q);
            ctx.result(JsonSerDe.toJson(result)).contentType(ContentType.APPLICATION_JSON);
        });
    }
}
