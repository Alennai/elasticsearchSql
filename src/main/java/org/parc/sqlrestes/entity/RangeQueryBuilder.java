package org.parc.sqlrestes.entity;

import org.apache.logging.log4j.util.Strings;
import org.joda.time.DateTimeZone;

import java.util.Objects;

/**
 * Created by xusiao on 2018/5/4.
 */
public class RangeQueryBuilder {
    public static final String NAME = "range";
    public static final boolean DEFAULT_INCLUDE_UPPER = true;
    public static final boolean DEFAULT_INCLUDE_LOWER = true;
    public static final ParseField LTE_FIELD = new ParseField("lte");
    public static final ParseField GTE_FIELD = new ParseField("gte");
    public static final ParseField FROM_FIELD = new ParseField("from");
    public static final ParseField TO_FIELD = new ParseField("to");
    private static final ParseField INCLUDE_LOWER_FIELD = new ParseField("include_lower");
    private static final ParseField INCLUDE_UPPER_FIELD = new ParseField("include_upper");
    public static final ParseField GT_FIELD = new ParseField("gt");
    public static final ParseField LT_FIELD = new ParseField("lt");
    private static final ParseField TIME_ZONE_FIELD = new ParseField("time_zone");
    private static final ParseField FORMAT_FIELD = new ParseField("format");
    private static final ParseField RELATION_FIELD = new ParseField("relation");
    private final String fieldName;
    private Object from;
    private Object to;
    private DateTimeZone timeZone;
    private boolean includeLower = true;
    private boolean includeUpper = true;
    private FormatDateTimeFormatter format;
    private ShapeRelation relation;

    public RangeQueryBuilder(String fieldName) {
        if(Strings.isEmpty(fieldName)) {
            throw new IllegalArgumentException("field name is null or empty");
        } else {
            this.fieldName = fieldName;
        }
    }

//    public RangeQueryBuilder(StreamInput in) throws IOException {
//        super(in);
//        this.fieldName = in.readString();
//        this.from = in.readGenericValue();
//        this.to = in.readGenericValue();
//        this.includeLower = in.readBoolean();
//        this.includeUpper = in.readBoolean();
//        this.timeZone = in.readOptionalTimeZone();
//        String formatString = in.readOptionalString();
//        if(formatString != null) {
//            this.format = Joda.forPattern(formatString);
//        }
//
//        if(in.getVersion().onOrAfter(Version.V_5_2_0)) {
//            String relationString = in.readOptionalString();
//            if(relationString != null) {
//                this.relation = ShapeRelation.getRelationByName(relationString);
//                if(this.relation != null && !this.isRelationAllowed(this.relation)) {
//                    throw new IllegalArgumentException("[range] query does not support relation [" + relationString + "]");
//                }
//            }
//        }
//
//    }

    private boolean isRelationAllowed(ShapeRelation relation) {
        return relation == ShapeRelation.INTERSECTS || relation == ShapeRelation.CONTAINS || relation == ShapeRelation.WITHIN;
    }

//    protected void doWriteTo(StreamOutput out) throws IOException {
//        out.writeString(this.fieldName);
//        out.writeGenericValue(this.from);
//        out.writeGenericValue(this.to);
//        out.writeBoolean(this.includeLower);
//        out.writeBoolean(this.includeUpper);
//        out.writeOptionalTimeZone(this.timeZone);
//        String formatString = null;
//        if(this.format != null) {
//            formatString = this.format.format();
//        }
//
//        out.writeOptionalString(formatString);
//        if(out.getVersion().onOrAfter(Version.V_5_2_0)) {
//            String relationString = null;
//            if(this.relation != null) {
//                relationString = this.relation.getRelationName();
//            }
//
//            out.writeOptionalString(relationString);
//        }
//
//    }

    public String fieldName() {
        return this.fieldName;
    }

    private RangeQueryBuilder from(Object from, boolean includeLower) {
//        this.from = convertToBytesRefIfString(from);
        this.includeLower = includeLower;
        return this;
    }

    public RangeQueryBuilder from(Object from) {
        return this.from(from, this.includeLower);
    }

//    public Object from() {
//        return convertToStringIfBytesRef(this.from);
//    }

    public RangeQueryBuilder gt(Object from) {
        return this.from(from, false);
    }

    public RangeQueryBuilder gte(Object from) {
        return this.from(from, true);
    }

    private RangeQueryBuilder to(Object to, boolean includeUpper) {
//        this.to = convertToBytesRefIfString(to);
        this.includeUpper = includeUpper;
        return this;
    }

    public RangeQueryBuilder to(Object to) {
        return this.to(to, this.includeUpper);
    }

//    public Object to() {
//        return convertToStringIfBytesRef(this.to);
//    }

    public RangeQueryBuilder lt(Object to) {
        return this.to(to, false);
    }

    public RangeQueryBuilder lte(Object to) {
        return this.to(to, true);
    }

    public RangeQueryBuilder includeLower(boolean includeLower) {
        this.includeLower = includeLower;
        return this;
    }

    public boolean includeLower() {
        return this.includeLower;
    }

    public RangeQueryBuilder includeUpper(boolean includeUpper) {
        this.includeUpper = includeUpper;
        return this;
    }

    public boolean includeUpper() {
        return this.includeUpper;
    }

    public RangeQueryBuilder timeZone(String timeZone) {
        if(timeZone == null) {
            throw new IllegalArgumentException("timezone cannot be null");
        } else {
            this.timeZone = DateTimeZone.forID(timeZone);
            return this;
        }
    }

    private String timeZone() {
        return this.timeZone == null?null:this.timeZone.getID();
    }

    DateTimeZone getDateTimeZone() {
        return this.timeZone;
    }

    public RangeQueryBuilder format(String format) {
        if(format == null) {
            throw new IllegalArgumentException("format cannot be null");
        } else {
            this.format = Joda.forPattern(format);
            return this;
        }
    }

    private String format() {
        return this.format == null?null:this.format.format();
    }

    DateMathParser getForceDateParser() {
        return this.format != null?new DateMathParser(this.format):null;
    }

    public ShapeRelation relation() {
        return this.relation;
    }

    public RangeQueryBuilder relation(String relation) {
        if(relation == null) {
            throw new IllegalArgumentException("relation cannot be null");
        } else {
            this.relation = ShapeRelation.getRelationByName(relation);
            if(this.relation == null) {
                throw new IllegalArgumentException(relation + " is not a valid relation");
            } else if(!this.isRelationAllowed(this.relation)) {
                throw new IllegalArgumentException("[range] query does not support relation [" + relation + "]");
            } else {
                return this;
            }
        }
    }

//    protected void doXContent(XContentBuilder builder, Params params) throws IOException {
//        builder.startObject("range");
//        builder.startObject(this.fieldName);
//        builder.field(FROM_FIELD.getPreferredName(), convertToStringIfBytesRef(this.from));
//        builder.field(TO_FIELD.getPreferredName(), convertToStringIfBytesRef(this.to));
//        builder.field(INCLUDE_LOWER_FIELD.getPreferredName(), this.includeLower);
//        builder.field(INCLUDE_UPPER_FIELD.getPreferredName(), this.includeUpper);
//        if(this.timeZone != null) {
//            builder.field(TIME_ZONE_FIELD.getPreferredName(), this.timeZone.getID());
//        }
//
//        if(this.format != null) {
//            builder.field(FORMAT_FIELD.getPreferredName(), this.format.format());
//        }
//
//        if(this.relation != null) {
//            builder.field(RELATION_FIELD.getPreferredName(), this.relation.getRelationName());
//        }
//
//        this.printBoostAndQueryName(builder);
//        builder.endObject();
//        builder.endObject();
//    }

//    public static RangeQueryBuilder fromXContent(XContentParser parser) throws IOException {
//        String fieldName = null;
//        Object from = null;
//        Object to = null;
//        boolean includeLower = true;
//        boolean includeUpper = true;
//        String timeZone = null;
//        float boost = 1.0F;
//        String queryName = null;
//        String format = null;
//        String relation = null;
//        String currentFieldName = null;
//
//        while(true) {
//            Token token;
//            while((token = parser.nextToken()) != Token.END_OBJECT) {
//                if(token == Token.FIELD_NAME) {
//                    currentFieldName = parser.currentName();
//                } else if(token == Token.START_OBJECT) {
//                    throwParsingExceptionOnMultipleFields("range", parser.getTokenLocation(), fieldName, currentFieldName);
//                    fieldName = currentFieldName;
//
//                    while((token = parser.nextToken()) != Token.END_OBJECT) {
//                        if(token == Token.FIELD_NAME) {
//                            currentFieldName = parser.currentName();
//                        } else if(FROM_FIELD.match(currentFieldName)) {
//                            from = parser.objectBytes();
//                        } else if(TO_FIELD.match(currentFieldName)) {
//                            to = parser.objectBytes();
//                        } else if(INCLUDE_LOWER_FIELD.match(currentFieldName)) {
//                            includeLower = parser.booleanValue();
//                        } else if(INCLUDE_UPPER_FIELD.match(currentFieldName)) {
//                            includeUpper = parser.booleanValue();
//                        } else if(AbstractQueryBuilder.BOOST_FIELD.match(currentFieldName)) {
//                            boost = parser.floatValue();
//                        } else if(GT_FIELD.match(currentFieldName)) {
//                            from = parser.objectBytes();
//                            includeLower = false;
//                        } else if(GTE_FIELD.match(currentFieldName)) {
//                            from = parser.objectBytes();
//                            includeLower = true;
//                        } else if(LT_FIELD.match(currentFieldName)) {
//                            to = parser.objectBytes();
//                            includeUpper = false;
//                        } else if(LTE_FIELD.match(currentFieldName)) {
//                            to = parser.objectBytes();
//                            includeUpper = true;
//                        } else if(TIME_ZONE_FIELD.match(currentFieldName)) {
//                            timeZone = parser.text();
//                        } else if(FORMAT_FIELD.match(currentFieldName)) {
//                            format = parser.text();
//                        } else if(RELATION_FIELD.match(currentFieldName)) {
//                            relation = parser.text();
//                        } else {
//                            if(!AbstractQueryBuilder.NAME_FIELD.match(currentFieldName)) {
//                                throw new ParsingException(parser.getTokenLocation(), "[range] query does not support [" + currentFieldName + "]", new Object[0]);
//                            }
//
//                            queryName = parser.text();
//                        }
//                    }
//                } else if(token.isValue()) {
//                    throw new ParsingException(parser.getTokenLocation(), "[range] query does not support [" + currentFieldName + "]", new Object[0]);
//                }
//            }
//
//            RangeQueryBuilder rangeQuery = new RangeQueryBuilder(fieldName);
//            rangeQuery.from(from);
//            rangeQuery.to(to);
//            rangeQuery.includeLower(includeLower);
//            rangeQuery.includeUpper(includeUpper);
//            if(timeZone != null) {
//                rangeQuery.timeZone(timeZone);
//            }
//
//            rangeQuery.boost(boost);
//            rangeQuery.queryName(queryName);
//            if(format != null) {
//                rangeQuery.format(format);
//            }
//
//            if(relation != null) {
//                rangeQuery.relation(relation);
//            }
//
//            return rangeQuery;
//        }
//    }

    public String getWriteableName() {
        return "range";
    }

//    protected Relation getRelation(QueryRewriteContext queryRewriteContext) throws IOException {
//        QueryShardContext shardContext = queryRewriteContext.convertToShardContext();
//        if(shardContext != null && shardContext.getIndexReader() != null) {
//            MapperService mapperService = shardContext.getMapperService();
//            MappedFieldType fieldType = mapperService.fullName(this.fieldName);
//            if(fieldType == null) {
//                return Relation.DISJOINT;
//            } else {
//                DateMathParser dateMathParser = this.getForceDateParser();
//                return fieldType.isFieldWithinQuery(shardContext.getIndexReader(), this.from, this.to, this.includeLower, this.includeUpper, this.timeZone, dateMathParser, queryRewriteContext);
//            }
//        } else {
//            return Relation.INTERSECTS;
//        }
//    }

//    protected QueryBuilder doRewrite(QueryRewriteContext queryRewriteContext) throws IOException {
//        Relation relation = this.getRelation(queryRewriteContext);
//        switch(null.$SwitchMap$org$elasticsearch$index$mapper$MappedFieldType$Relation[relation.ordinal()]) {
//            case 1:
//                return new MatchNoneQueryBuilder();
//            case 2:
//                if(this.from == null && this.to == null && this.format == null && this.timeZone == null) {
//                    return this;
//                }
//
//                RangeQueryBuilder newRangeQuery = new RangeQueryBuilder(this.fieldName);
//                newRangeQuery.from((Object)null);
//                newRangeQuery.to((Object)null);
//                newRangeQuery.format = null;
//                newRangeQuery.timeZone = null;
//                return newRangeQuery;
//            case 3:
//                return this;
//            default:
//                throw new AssertionError();
//        }
//    }

//    protected Query doToQuery(QueryShardContext context) throws IOException {
//        if(this.from == null && this.to == null) {
//            FieldNamesFieldType query = (FieldNamesFieldType)context.getMapperService().fullName("_field_names");
//            if(query == null) {
//                return new MatchNoDocsQuery("No mappings yet");
//            }
//
//            if(query.isEnabled()) {
//                return ExistsQueryBuilder.newFilter(context, this.fieldName);
//            }
//        }
//
//        Object query1 = null;
//        MappedFieldType mapper = context.fieldMapper(this.fieldName);
//        if(mapper != null) {
//            DateMathParser forcedDateParser = this.getForceDateParser();
//            query1 = mapper.rangeQuery(this.from, this.to, this.includeLower, this.includeUpper, this.relation, this.timeZone, forcedDateParser, context);
//        } else if(this.timeZone != null) {
//            throw new QueryShardException(context, "[range] time_zone can not be applied to non unmapped field [" + this.fieldName + "]", new Object[0]);
//        }
//
//        if(query1 == null) {
//            query1 = new TermRangeQuery(this.fieldName, BytesRefs.toBytesRef(this.from), BytesRefs.toBytesRef(this.to), this.includeLower, this.includeUpper);
//        }
//
//        return (Query)query1;
//    }

    protected int doHashCode() {
        String timeZoneId = this.timeZone == null?null:this.timeZone.getID();
        String formatString = this.format == null?null:this.format.format();
        return Objects.hash(this.fieldName, this.from, this.to, timeZoneId, this.includeLower, this.includeUpper, formatString);
    }

    protected boolean doEquals(RangeQueryBuilder other) {
        String timeZoneId = this.timeZone == null?null:this.timeZone.getID();
        String formatString = this.format == null?null:this.format.format();
        return Objects.equals(this.fieldName, other.fieldName) && Objects.equals(this.from, other.from) && Objects.equals(this.to, other.to) && Objects.equals(timeZoneId, other.timeZone()) && Objects.equals(this.includeLower, other.includeLower) && Objects.equals(this.includeUpper, other.includeUpper) && Objects.equals(formatString, other.format());
    }
}
