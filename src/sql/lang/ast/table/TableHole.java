package sql.lang.ast.table;

import enumerator.parameterized.InstantiateEnv;
import util.Pair;
import sql.lang.DataType.ValType;
import sql.lang.Table;
import sql.lang.ast.Environment;
import sql.lang.ast.Hole;
import sql.lang.exception.SQLEvalException;
import sql.lang.trans.ValNodeSubstBinding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by clwang on 1/7/16.
 * TODO: Currently not used
 */
public class TableHole implements TableNode, Hole {

    String holeID;

    public TableHole(String ID) {

    }

    @Override
    public Table eval(Environment env) throws SQLEvalException {
        return null;
    }

    @Override
    public List<String> getSchema() {
        return null;
    }

    @Override
    public String getTableName() {
        return null;
    }

    @Override
    public List<ValType> getSchemaType() {
        return null;
    }

    @Override
    public int getNestedQueryLevel() {
        return 0;
    }

    @Override
    public String prettyPrint(int indentLv) {
        return null;
    }

    @Override
    public List<Hole> getAllHoles() {
        return Arrays.asList(this);
    }

    @Override
    public TableNode instantiate(InstantiateEnv env) {
        return this;
    }

    @Override
    public TableNode substNamedVal(ValNodeSubstBinding vnsb) {
        return this;
    }

    @Override
    public List<NamedTable> namedTableInvolved() {
        return new ArrayList<>();
    }

    @Override
    public TableNode tableSubst(List<Pair<TableNode,TableNode>> pairs) {
        return this;
    }
}