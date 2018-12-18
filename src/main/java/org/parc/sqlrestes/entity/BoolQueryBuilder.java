package org.parc.sqlrestes.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by xusiao on 2018/5/4.
 */
//public class BoolQueryBuilder extends AbstractQueryBuilder<BoolQueryBuilder> {
public class BoolQueryBuilder {
    public static final String NAME = "bool";
    public static final boolean ADJUST_PURE_NEGATIVE_DEFAULT = true;
    private static final String MUSTNOT = "mustNot";
    private static final String MUST_NOT = "must_not";
    private static final String FILTER = "filter";
    private static final String SHOULD = "should";
    private static final String MUST = "must";
    private static final ParseField DISABLE_COORD_FIELD = (new ParseField("disable_coord")).withAllDeprecated("disable_coord has been removed");
    private static final ParseField MINIMUM_SHOULD_MATCH = new ParseField("minimum_should_match");
    private static final ParseField ADJUST_PURE_NEGATIVE = new ParseField("adjust_pure_negative");
    private final List<QueryBuilder> mustClauses = new ArrayList();
    private final List<QueryBuilder> mustNotClauses = new ArrayList();
    private final List<QueryBuilder> filterClauses = new ArrayList();
    private final List<QueryBuilder> shouldClauses = new ArrayList();
    private boolean adjustPureNegative = true;
    private String minimumShouldMatch;

    public String toString() {
//        return "Script{type=" + this.type + ", lang=\'" + this.lang + '\'' + ", idOrCode=\'" + this.idOrCode + '\'' + ", options=" + this.options + ", params=" + this.params + '}';
        return "";
    }

    public BoolQueryBuilder( )   {
//        super(in);
//        this.mustClauses.addAll(readQueries(in));
//        this.mustNotClauses.addAll(readQueries(in));
//        this.shouldClauses.addAll(readQueries(in));
//        this.filterClauses.addAll(readQueries(in));
//        this.adjustPureNegative = in.readBoolean();
//        if(in.getVersion().before(Version.V_6_0_0_alpha1)) {
//            in.readBoolean();
//        }

//        this.minimumShouldMatch = in.readOptionalString();
    }

//    protected void doWriteTo(StreamOutput out) throws IOException {
//        writeQueries(out, this.mustClauses);
//        writeQueries(out, this.mustNotClauses);
//        writeQueries(out, this.shouldClauses);
//        writeQueries(out, this.filterClauses);
//        out.writeBoolean(this.adjustPureNegative);
//        if(out.getVersion().before(Version.V_6_0_0_alpha1)) {
//            out.writeBoolean(true);
//        }
//
//        out.writeOptionalString(this.minimumShouldMatch);
////    }
//
//    public BoolQueryBuilder must(QueryBuilder queryBuilder) {
//        if(queryBuilder == null) {
//            throw new IllegalArgumentException("inner bool query clause cannot be null");
//        } else {
//            this.mustClauses.add(queryBuilder);
//            return this;
//        }
//    }

    public List<QueryBuilder> must() {
        return this.mustClauses;
    }

    public BoolQueryBuilder filter(QueryBuilder queryBuilder) {
        if(queryBuilder == null) {
            throw new IllegalArgumentException("inner bool query clause cannot be null");
        } else {
            this.filterClauses.add(queryBuilder);
            return this;
        }
    }

    public List<QueryBuilder> filter() {
        return this.filterClauses;
    }

    public BoolQueryBuilder mustNot(QueryBuilder queryBuilder) {
        if(queryBuilder == null) {
            throw new IllegalArgumentException("inner bool query clause cannot be null");
        } else {
            this.mustNotClauses.add(queryBuilder);
            return this;
        }
    }

    public List<QueryBuilder> mustNot() {
        return this.mustNotClauses;
    }

    public BoolQueryBuilder should(QueryBuilder queryBuilder) {
        if(queryBuilder == null) {
            throw new IllegalArgumentException("inner bool query clause cannot be null");
        } else {
            this.shouldClauses.add(queryBuilder);
            return this;
        }
    }

    public List<QueryBuilder> should() {
        return this.shouldClauses;
    }

    public String minimumShouldMatch() {
        return this.minimumShouldMatch;
    }

    public BoolQueryBuilder minimumShouldMatch(String minimumShouldMatch) {
        this.minimumShouldMatch = minimumShouldMatch;
        return this;
    }

    public BoolQueryBuilder minimumShouldMatch(int minimumShouldMatch) {
        this.minimumShouldMatch = Integer.toString(minimumShouldMatch);
        return this;
    }

    public boolean hasClauses() {
        return !this.mustClauses.isEmpty() || !this.shouldClauses.isEmpty() || !this.mustNotClauses.isEmpty() || !this.filterClauses.isEmpty();
    }

    public BoolQueryBuilder adjustPureNegative(boolean adjustPureNegative) {
        this.adjustPureNegative = adjustPureNegative;
        return this;
    }

    public boolean adjustPureNegative() {
        return this.adjustPureNegative;
    }

//    protected void doXContent(XContentBuilder builder, Params params) throws IOException {
//        builder.startObject("bool");
//        doXArrayContent("must", this.mustClauses, builder, params);
//        doXArrayContent("filter", this.filterClauses, builder, params);
//        doXArrayContent("must_not", this.mustNotClauses, builder, params);
//        doXArrayContent("should", this.shouldClauses, builder, params);
//        builder.field(ADJUST_PURE_NEGATIVE.getPreferredName(), this.adjustPureNegative);
//        if(this.minimumShouldMatch != null) {
//            builder.field(MINIMUM_SHOULD_MATCH.getPreferredName(), this.minimumShouldMatch);
//        }
//
//        this.printBoostAndQueryName(builder);
//        builder.endObject();
//    }

//    private static void doXArrayContent(String field, List<QueryBuilder> clauses, XContentBuilder builder, Params params) throws IOException {
//        if(!clauses.isEmpty()) {
//            builder.startArray(field);
//            Iterator var4 = clauses.iterator();
//
//            while(var4.hasNext()) {
//                QueryBuilder clause = (QueryBuilder)var4.next();
//                clause.toXContent(builder, params);
//            }
//
//            builder.endArray();
//        }
//    }

//    public static BoolQueryBuilder fromXContent(XContentParser parser) throws IOException, ParsingException {
//        boolean adjustPureNegative = true;
//        float boost = 1.0F;
//        String minimumShouldMatch = null;
//        ArrayList mustClauses = new ArrayList();
//        ArrayList mustNotClauses = new ArrayList();
//        ArrayList shouldClauses = new ArrayList();
//        ArrayList filterClauses = new ArrayList();
//        String queryName = null;
//        String currentFieldName = null;
//
//        while(true) {
//            Token token;
//            while((token = parser.nextToken()) != Token.END_OBJECT) {
//                if(token == Token.FIELD_NAME) {
//                    currentFieldName = parser.currentName();
//                } else {
//                    byte var12;
//                    if(token == Token.START_OBJECT) {
//                        var12 = -1;
//                        switch(currentFieldName.hashCode()) {
//                            case -1274492040:
//                                if(currentFieldName.equals("filter")) {
//                                    var12 = 2;
//                                }
//                                break;
//                            case -903146061:
//                                if(currentFieldName.equals("should")) {
//                                    var12 = 1;
//                                }
//                                break;
//                            case 3363337:
//                                if(currentFieldName.equals("must")) {
//                                    var12 = 0;
//                                }
//                                break;
//                            case 853933981:
//                                if(currentFieldName.equals("must_not")) {
//                                    var12 = 3;
//                                }
//                                break;
//                            case 1413003274:
//                                if(currentFieldName.equals("mustNot")) {
//                                    var12 = 4;
//                                }
//                        }
//
//                        switch(var12) {
//                            case 0:
//                                mustClauses.add(parseInnerQueryBuilder(parser));
//                                break;
//                            case 1:
//                                shouldClauses.add(parseInnerQueryBuilder(parser));
//                                break;
//                            case 2:
//                                filterClauses.add(parseInnerQueryBuilder(parser));
//                                break;
//                            case 3:
//                            case 4:
//                                mustNotClauses.add(parseInnerQueryBuilder(parser));
//                                break;
//                            default:
//                                throw new ParsingException(parser.getTokenLocation(), "[bool] query does not support [" + currentFieldName + "]", new Object[0]);
//                        }
//                    } else if(token == Token.START_ARRAY) {
//                        while(parser.nextToken() != Token.END_ARRAY) {
//                            var12 = -1;
//                            switch(currentFieldName.hashCode()) {
//                                case -1274492040:
//                                    if(currentFieldName.equals("filter")) {
//                                        var12 = 2;
//                                    }
//                                    break;
//                                case -903146061:
//                                    if(currentFieldName.equals("should")) {
//                                        var12 = 1;
//                                    }
//                                    break;
//                                case 3363337:
//                                    if(currentFieldName.equals("must")) {
//                                        var12 = 0;
//                                    }
//                                    break;
//                                case 853933981:
//                                    if(currentFieldName.equals("must_not")) {
//                                        var12 = 3;
//                                    }
//                                    break;
//                                case 1413003274:
//                                    if(currentFieldName.equals("mustNot")) {
//                                        var12 = 4;
//                                    }
//                            }
//
//                            switch(var12) {
//                                case 0:
//                                    mustClauses.add(parseInnerQueryBuilder(parser));
//                                    break;
//                                case 1:
//                                    shouldClauses.add(parseInnerQueryBuilder(parser));
//                                    break;
//                                case 2:
//                                    filterClauses.add(parseInnerQueryBuilder(parser));
//                                    break;
//                                case 3:
//                                case 4:
//                                    mustNotClauses.add(parseInnerQueryBuilder(parser));
//                                    break;
//                                default:
//                                    throw new ParsingException(parser.getTokenLocation(), "bool query does not support [" + currentFieldName + "]", new Object[0]);
//                            }
//                        }
//                    } else if(token.isValue() && !DISABLE_COORD_FIELD.match(currentFieldName)) {
//                        if(MINIMUM_SHOULD_MATCH.match(currentFieldName)) {
//                            minimumShouldMatch = parser.textOrNull();
//                        } else if(BOOST_FIELD.match(currentFieldName)) {
//                            boost = parser.floatValue();
//                        } else if(ADJUST_PURE_NEGATIVE.match(currentFieldName)) {
//                            adjustPureNegative = parser.booleanValue();
//                        } else {
//                            if(!NAME_FIELD.match(currentFieldName)) {
//                                throw new ParsingException(parser.getTokenLocation(), "[bool] query does not support [" + currentFieldName + "]", new Object[0]);
//                            }
//
//                            queryName = parser.text();
//                        }
//                    }
//                }
//            }
//
//            BoolQueryBuilder boolQuery = new BoolQueryBuilder();
//            Iterator var14 = mustClauses.iterator();
//
//            QueryBuilder queryBuilder;
//            while(var14.hasNext()) {
//                queryBuilder = (QueryBuilder)var14.next();
//                boolQuery.must(queryBuilder);
//            }
//
//            var14 = mustNotClauses.iterator();
//
//            while(var14.hasNext()) {
//                queryBuilder = (QueryBuilder)var14.next();
//                boolQuery.mustNot(queryBuilder);
//            }
//
//            var14 = shouldClauses.iterator();
//
//            while(var14.hasNext()) {
//                queryBuilder = (QueryBuilder)var14.next();
//                boolQuery.should(queryBuilder);
//            }
//
//            var14 = filterClauses.iterator();
//
//            while(var14.hasNext()) {
//                queryBuilder = (QueryBuilder)var14.next();
//                boolQuery.filter(queryBuilder);
//            }
//
//            boolQuery.boost(boost);
//            boolQuery.adjustPureNegative(adjustPureNegative);
//            boolQuery.minimumShouldMatch(minimumShouldMatch);
//            boolQuery.queryName(queryName);
//            return boolQuery;
//        }
//    }

    public String getWriteableName() {
        return "bool";
    }

//    protected Query doToQuery(QueryShardContext context) throws IOException {
//        Builder booleanQueryBuilder = new Builder();
//        addBooleanClauses(context, booleanQueryBuilder, this.mustClauses, Occur.MUST);
//        addBooleanClauses(context, booleanQueryBuilder, this.mustNotClauses, Occur.MUST_NOT);
//        addBooleanClauses(context, booleanQueryBuilder, this.shouldClauses, Occur.SHOULD);
//        addBooleanClauses(context, booleanQueryBuilder, this.filterClauses, Occur.FILTER);
//        BooleanQuery booleanQuery = booleanQueryBuilder.build();
//        if(booleanQuery.clauses().isEmpty()) {
//            return new MatchAllDocsQuery();
//        } else {
//            String minimumShouldMatch;
//            if(context.isFilter() && this.minimumShouldMatch == null && this.shouldClauses.size() > 0) {
//                minimumShouldMatch = "1";
//            } else {
//                minimumShouldMatch = this.minimumShouldMatch;
//            }
//
//            Query query = Queries.applyMinimumShouldMatch(booleanQuery, minimumShouldMatch);
//            return this.adjustPureNegative?Queries.fixNegativeQueryIfNeeded(query):query;
//        }
////    }
//
//    private static void addBooleanClauses(QueryShardContext context, Builder booleanQueryBuilder, List<QueryBuilder> clauses, Occur occurs) throws IOException {
//        Query luceneQuery;
//        for(Iterator var4 = clauses.iterator(); var4.hasNext(); booleanQueryBuilder.add(new BooleanClause(luceneQuery, occurs))) {
//            QueryBuilder query = (QueryBuilder)var4.next();
//            luceneQuery = null;
//            switch(null.$SwitchMap$org$apache$lucene$search$BooleanClause$Occur[occurs.ordinal()]) {
//                case 1:
//                case 2:
//                    luceneQuery = query.toQuery(context);
//                    break;
//                case 3:
//                case 4:
//                    luceneQuery = query.toFilter(context);
//            }
//        }
//
//    }

    protected int doHashCode() {
        return Objects.hash(Boolean.valueOf(this.adjustPureNegative), this.minimumShouldMatch, this.mustClauses, this.shouldClauses, this.mustNotClauses, this.filterClauses);
    }

    protected boolean doEquals(BoolQueryBuilder other) {
        return Objects.equals(Boolean.valueOf(this.adjustPureNegative), Boolean.valueOf(other.adjustPureNegative)) && Objects.equals(this.minimumShouldMatch, other.minimumShouldMatch) && Objects.equals(this.mustClauses, other.mustClauses) && Objects.equals(this.shouldClauses, other.shouldClauses) && Objects.equals(this.mustNotClauses, other.mustNotClauses) && Objects.equals(this.filterClauses, other.filterClauses);
    }

//    protected QueryBuilder doRewrite(QueryRewriteContext queryRewriteContext) throws IOException {
//        BoolQueryBuilder newBuilder = new BoolQueryBuilder();
//        boolean changed = false;
//        int clauses = this.mustClauses.size() + this.mustNotClauses.size() + this.filterClauses.size() + this.shouldClauses.size();
//        if(clauses == 0) {
//            return ((MatchAllQueryBuilder)(new MatchAllQueryBuilder()).boost(this.boost())).queryName(this.queryName());
//        } else {
//            List var10002 = this.mustClauses;
//            Objects.requireNonNull(newBuilder);
//            changed |= rewriteClauses(queryRewriteContext, var10002, newBuilder::must);
//            var10002 = this.mustNotClauses;
//            Objects.requireNonNull(newBuilder);
//            changed |= rewriteClauses(queryRewriteContext, var10002, newBuilder::mustNot);
//            var10002 = this.filterClauses;
//            Objects.requireNonNull(newBuilder);
//            changed |= rewriteClauses(queryRewriteContext, var10002, newBuilder::filter);
//            var10002 = this.shouldClauses;
//            Objects.requireNonNull(newBuilder);
//            changed |= rewriteClauses(queryRewriteContext, var10002, newBuilder::should);
//            Optional any = Stream.concat(newBuilder.mustClauses.stream(), newBuilder.filterClauses.stream()).filter((b) -> {
//                return b instanceof MatchNoneQueryBuilder;
//            }).findAny();
//            if(any.isPresent()) {
//                return (QueryBuilder)any.get();
//            } else if(changed) {
//                newBuilder.adjustPureNegative = this.adjustPureNegative;
//                newBuilder.minimumShouldMatch = this.minimumShouldMatch;
//                newBuilder.boost(this.boost());
//                newBuilder.queryName(this.queryName());
//                return newBuilder;
//            } else {
//                return this;
//            }
//        }
//    }

//    protected void extractInnerHitBuilders(Map<String, InnerHitContextBuilder> innerHits) {
//        ArrayList clauses = new ArrayList(this.filter());
//        clauses.addAll(this.must());
//        clauses.addAll(this.should());
//        Iterator var3 = clauses.iterator();
//
//        while(var3.hasNext()) {
//            QueryBuilder clause = (QueryBuilder)var3.next();
//            InnerHitContextBuilder.extractInnerHits(clause, innerHits);
//        }
//
//    }

//    private static boolean rewriteClauses(QueryRewriteContext queryRewriteContext, List<QueryBuilder> builders, Consumer<QueryBuilder> consumer) throws IOException {
//        boolean changed = false;
//
//        QueryBuilder result;
//        for(Iterator var4 = builders.iterator(); var4.hasNext(); consumer.accept(result)) {
//            QueryBuilder builder = (QueryBuilder)var4.next();
//            result = builder.rewrite(queryRewriteContext);
//            if(result != builder) {
//                changed = true;
//            }
//        }
//
//        return changed;
//    }
}

