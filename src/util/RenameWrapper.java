package util;

import lang.sql.ast.abstable.AbsRenameNode;
import lang.sql.ast.abstable.AbsTableNode;
import lang.sql.ast.contable.RenameTableNode;
import lang.sql.ast.contable.TableNode;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by clwang on 1/7/16.
 * Rename a table node into
 */
public class RenameWrapper {

    static int renamingIndex = 0;

    static final String tableNameTemplate = "[T#-id#]";
    static final String tableNameTemplateRE = "\\[T#-(\\d+)#\\]";

    /**
     * Given a string, find all automatically generated template name in the string
     * @param str the target string to check with
     * @return all automatically generated template strings
     */
    public static List<String> findAllGeneratedNames(String str) {
        List<String> result = new ArrayList<>();
        // Now create matcher object.
        Matcher m = Pattern.compile(tableNameTemplateRE).matcher(str);
        //System.out.println(name);
        while (m.find( )) {
            result.add(m.group());
        }
        return result;
    }

    private static String nameGenerator(Integer index) {
        String name = tableNameTemplate.replace("id", index.toString());
        return name;
    }

    /*****************************************************
     Renaming enumerated tables
     used for renaming anonymous table that will be
     used later enumeration process
     ****************************************************/

    private static TableNode genRenamedTable(TableNode tn, boolean forceRename) {
        if (forceRename || tn.getTableName().equals("anonymous")) {
            String newName = nameGenerator(renamingIndex);
            renamingIndex ++;
            List<String> newSchema = new ArrayList<String>();
            for (String s : tn.getSchema()) {
                String shortName = s.substring(s.lastIndexOf(".") + 1);
                if (newSchema.contains(shortName)) {
                    int i = 1;
                    while(newSchema.contains(shortName + i)) {
                        i ++;
                    }
                    newSchema.add(shortName + i);
                } else {
                    newSchema.add(shortName);
                }
            }
            return new RenameTableNode(newName, newSchema, tn);
        } else
            return tn;
    }

    public static TableNode tryRename(TableNode tn) {
        return genRenamedTable(tn, false);
    }

    public static TableNode forceRename(TableNode tn) {
        return genRenamedTable(tn, true);
    }


    private static AbsTableNode genRenamedTable(AbsTableNode tn, boolean forceRename) {
        if (forceRename || tn.getTableName().equals("anonymous")) {
            String newName = nameGenerator(renamingIndex);
            renamingIndex ++;
            List<String> newSchema = new ArrayList<String>();
            for (String s : tn.getSchema()) {
                String shortName = s.substring(s.lastIndexOf(".") + 1);
                if (newSchema.contains(shortName)) {
                    int i = 1;
                    while(newSchema.contains(shortName + i)) {
                        i ++;
                    }
                    newSchema.add(shortName + i);
                } else {
                    newSchema.add(shortName);
                }
            }
            return new AbsRenameNode(newName, newSchema, tn);
        } else
            return tn;
    }

    public static AbsTableNode tryRename(AbsTableNode tn) {
        return genRenamedTable(tn, false);
    }
    public static AbsTableNode forceRename(AbsTableNode tn) {
        return genRenamedTable(tn, true);
    }
}