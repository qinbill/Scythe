package lang.sql.ast.contable;

import forward_enumeration.primitive.parameterized.InstantiateEnv;
import lang.sql.dataval.Value;
import util.Pair;
import lang.sql.dataval.ValType;
import lang.table.Table;
import lang.sql.ast.Environment;
import lang.sql.ast.Hole;
import lang.sql.ast.Node;
import lang.sql.exception.SQLEvalException;
import lang.sql.transformation.ValNodeSubstitution;
import util.RenameWrapper;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by clwang on 12/16/15.
 */
public abstract class TableNode implements Node {
    public abstract Table eval(Environment env) throws SQLEvalException;

    public abstract List<String> getSchema();
    public abstract String getTableName();

    public abstract List<ValType> getSchemaType();
    public abstract int getNestedQueryLevel();
    public abstract int getASTNodeCnt();

    public List<Pair<String, ValType>> getSchemaWithType() {
        List<Pair<String, ValType>> result = new ArrayList<>();
        List<String> schema = this.getSchema();
        List<ValType> schemaType = this.getSchemaType();
        for (int i = 0; i < schema.size(); i ++) {
            result.add(new Pair<>(schema.get(i), schemaType.get(i)));
        }
        return result;
    }

    public abstract String prettyPrint(int indentLv, boolean asSubquery);
    public String printQuery() {
        String queryStr = this.simplifyAST().prettyPrint(0, false) + ";";
        Set<String> generatedNames = RenameWrapper.findAllGeneratedNames(queryStr)
                .stream().collect(Collectors.toSet());

        int i = 1;
        for (String s : generatedNames) {
            while (queryStr.contains("t" + i))
                i ++;
            queryStr = queryStr.replace(s, "t" + i);
            i ++;
        }

        return queryStr;
    }
    // remove/rewrite queries with redundant components
    public abstract TableNode simplifyAST();

    public abstract List<Hole> getAllHoles();

    public abstract TableNode instantiate(InstantiateEnv env);
    public abstract TableNode substNamedVal(ValNodeSubstitution vnsb);
    public abstract List<NamedTableNode> namedTableInvolved();

    // substitute a named table based on the cores
    public abstract TableNode tableSubst(List<Pair<TableNode,TableNode>> pairs);

    // the function will return a map,
    // that maps each column of this schema to its src (which column of which table)
    public abstract List<String> originalColumnName();

    public static Map<String, String> nameToOriginMap(List<String> schema, List<String> originalNames) {
        Map<String, String> map = new HashMap<>();
        for (int i = 0; i < schema.size(); i ++) {
            map.put(schema.get(i), originalNames.get(i));
        }
        return map;
    }

    public abstract double estimateAllFilterCost();
    public abstract List<Value> getAllConstants();

    public double estimateTotalScore(List<Value> constants) {

        double penalty = 0;
        List<Value> valuesInTheQuery = this.getAllConstants();
        for (Value v : constants) {
            int cnt = 0;
            for (Value x : valuesInTheQuery) {
                if (x.equals(v)) cnt ++;
            }
            if (cnt == 0) penalty += 1;
            else {
                //penalty += cnt - 1;
            }
        }
        return penalty + estimateAllFilterCost();
    }

    // Obtain the skeleton of a SQL query, the skeleton describes what does the query looks like,
    // this string can be used to distinguish query structure information
    public abstract String getQuerySkeleton();
}