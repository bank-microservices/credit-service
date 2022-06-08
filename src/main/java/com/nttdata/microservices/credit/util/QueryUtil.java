package com.nttdata.microservices.credit.util;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.Fields;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.UnwindOperation;
import org.springframework.data.mongodb.core.query.Criteria;

public class QueryUtil {

    public static Aggregation getAggregationSum(String creditId) {
        LookupOperation lookup = Aggregation.lookup("credit", "creditId", "_id", "credit");
        UnwindOperation unwindOperation = new UnwindOperation(Fields.field("$credit"));
        MatchOperation matchStage = Aggregation.match(Criteria.where("creditId").is(new ObjectId(creditId)));
        GroupOperation sumOperation = Aggregation.group().sum("amount").as("total");
        Aggregation aggregation = Aggregation.newAggregation(matchStage, lookup, unwindOperation, sumOperation);
        return aggregation;
    }
}
