package org.parc.plugin;

import org.elasticsearch.search.SearchHits;
import org.parc.sqlrestes.exception.SqlParseException;

import java.io.IOException;

/**
 * Created by xusiao on 2018/6/20.
 */
public interface ElasticHitsExecutor {
    void run() throws IOException, SqlParseException;

    SearchHits getHits();
}
