package vip.zhouxin.ureport.console.controller;

import vip.zhouxin.ureport.core.Utils;
import vip.zhouxin.ureport.core.build.Context;
import vip.zhouxin.ureport.console.designer.DataResult;
import vip.zhouxin.ureport.console.dto.BuildFieldsReq;
import vip.zhouxin.ureport.console.dto.DataSourceReq;
import vip.zhouxin.ureport.console.exception.ReportDesignException;
import vip.zhouxin.ureport.core.definition.dataset.Field;
import vip.zhouxin.ureport.core.definition.dataset.SqlDatasetDefinition;
import vip.zhouxin.ureport.core.definition.datasource.BuildinDatasource;
import vip.zhouxin.ureport.core.definition.datasource.DataType;
import vip.zhouxin.ureport.core.expression.ExpressionUtils;
import vip.zhouxin.ureport.core.expression.model.Expression;
import vip.zhouxin.ureport.core.utils.ProcedureUtils;
import vip.zhouxin.ureport.core.utils.SQLUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.*;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.sql.DataSource;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.Date;
import java.util.*;

/**
 * @author xinxingzhou
 * @since 2022/1/21
 */
@RestController
public class DatasourceController extends AbstractController {

    public static final String PREFIX = CONTENT + "/datasource";

    private final ApplicationContext applicationContext;


    public DatasourceController(ApplicationContext applicationContext, ObjectMapper objectMapper) {
        super(objectMapper);
        this.applicationContext = applicationContext;
    }

    @RequestMapping(value = PREFIX + "/loadBuildInDataSources", method = RequestMethod.GET)
    public List<String> loadBuildInDataSources() {
        List<String> dataSources = new ArrayList<>();
        for (BuildinDatasource datasource : Utils.getBuildinDatasources()) {
            dataSources.add(datasource.name());
        }
        return dataSources;
    }
    @RequestMapping(value = PREFIX + "/resetBuildInDataSources", method = RequestMethod.GET)
    public void resetBuildInDataSources() {
        Utils.resetBuildinDatasources();
    }

    @RequestMapping(value = PREFIX + "/loadMethods", method = RequestMethod.GET)
    public List<String> loadMethods(@RequestParam("beanId") String beanId) {
        Object obj = applicationContext.getBean(beanId);
        Class<?> clazz = obj.getClass();
        Method[] methods = clazz.getMethods();
        List<String> result = new ArrayList<>();
        for (Method method : methods) {
            Class<?>[] types = method.getParameterTypes();
            if (types.length != 3) {
                continue;
            }
            Class<?> typeClass1 = types[0];
            Class<?> typeClass2 = types[1];
            Class<?> typeClass3 = types[2];
            if (!String.class.isAssignableFrom(typeClass1)) {
                continue;
            }
            if (!String.class.isAssignableFrom(typeClass2)) {
                continue;
            }
            if (!Map.class.isAssignableFrom(typeClass3)) {
                continue;
            }
            result.add(method.getName());
        }
        return result;
    }

    @RequestMapping(value = PREFIX + "/buildClass", method = RequestMethod.GET)
    public List<Field> buildClass(@RequestParam("clazz") String clazz) {
        List<Field> result = new ArrayList<>();
        try {
            Class<?> targetClass = Class.forName(clazz);
            PropertyDescriptor[] propertyDescriptors = PropertyUtils.getPropertyDescriptors(targetClass);
            for (PropertyDescriptor pd : propertyDescriptors) {
                String name = pd.getName();
                if ("class".equals(name)) {
                    continue;
                }
                result.add(new Field(name));
            }
            return result;
        } catch (Exception ex) {
            throw new ReportDesignException(ex);
        }
    }

    @RequestMapping(value = PREFIX + "/buildDatabaseTables", method = RequestMethod.POST)
    public List<Map<String, String>> buildDatabaseTables(@RequestBody DataSourceReq req) throws ServletException {
        Connection conn = null;
        ResultSet rs = null;
        try {
            conn = buildConnection(req);
            DatabaseMetaData metaData = conn.getMetaData();
            String url = metaData.getURL();
            String schema = null;
            if (url.toLowerCase().contains("oracle")) {
                schema = metaData.getUserName();
            }
            List<Map<String, String>> tables = new ArrayList<>();
            rs = metaData.getTables(null, schema, "%", new String[]{"TABLE", "VIEW"});
            while (rs.next()) {
                Map<String, String> table = new HashMap<>();
                table.put("name", rs.getString("TABLE_NAME"));
                table.put("type", rs.getString("TABLE_TYPE"));
                tables.add(table);
            }
            return tables;
        } catch (Exception ex) {
            throw new ServletException(ex);
        } finally {
            JdbcUtils.closeResultSet(rs);
            JdbcUtils.closeConnection(conn);
        }
    }


    @RequestMapping(value = PREFIX + "/buildFields", method = RequestMethod.POST)
    public List<Field> buildFields(@RequestBody BuildFieldsReq req) {
        String sql = req.getSql();
        Connection conn = null;
        final List<Field> fields = new ArrayList<>();
        try {
            conn = buildConnection(req);
            Map<String, Object> map = buildParameters(req.getParameters());
            sql = parseSql(sql, map);
            if (ProcedureUtils.isProcedure(sql)) {
                List<Field> fieldsList = ProcedureUtils.procedureColumnsQuery(sql, map, conn);
                fields.addAll(fieldsList);
            } else {
                DataSource dataSource = new SingleConnectionDataSource(conn, false);
                NamedParameterJdbcTemplate jdbc = new NamedParameterJdbcTemplate(dataSource);
                PreparedStatementCreator statementCreator = getPreparedStatementCreator(sql, new MapSqlParameterSource(map));
                jdbc.getJdbcOperations().execute(statementCreator, ps -> {
                    ResultSet rs = null;
                    try {
                        rs = ps.executeQuery();
                        ResultSetMetaData metadata = rs.getMetaData();
                        int columnCount = metadata.getColumnCount();
                        for (int i = 0; i < columnCount; i++) {
                            String columnName = metadata.getColumnLabel(i + 1);
                            fields.add(new Field(columnName));
                        }
                        return null;
                    } finally {
                        JdbcUtils.closeResultSet(rs);
                    }
                });
            }
            return fields;
        } catch (Exception ex) {
            throw new ReportDesignException(ex);
        } finally {
            JdbcUtils.closeConnection(conn);
        }
    }


    @RequestMapping(value = PREFIX + "/previewData", method = RequestMethod.POST)
    public DataResult previewData(@RequestBody BuildFieldsReq req) throws ServletException, IOException {
        String sql = req.getSql();
        Map<String, Object> map = buildParameters(req.getParameters());
        sql = parseSql(sql, map);
        Connection conn = null;
        try {
            conn = buildConnection(req);
            List<Map<String, Object>> list;
            if (ProcedureUtils.isProcedure(sql)) {
                list = ProcedureUtils.procedureQuery(sql, map, conn);
            } else {
                DataSource dataSource = new SingleConnectionDataSource(conn, false);
                NamedParameterJdbcTemplate jdbc = new NamedParameterJdbcTemplate(dataSource);
                list = jdbc.queryForList(sql, map);
            }
            int size = list.size();
            int currentTotal = size;
            if (currentTotal > 500) {
                currentTotal = 500;
            }
            List<Map<String, Object>> ls = new ArrayList<>();
            for (int i = 0; i < currentTotal; i++) {
                ls.add(list.get(i));
            }
            DataResult result = new DataResult();
            List<String> fields = new ArrayList<>();
            if (size > 0) {
                Map<String, Object> item = list.get(0);
                fields.addAll(item.keySet());
            }
            result.setFields(fields);
            result.setCurrentTotal(currentTotal);
            result.setData(ls);
            result.setTotal(size);
            return result;
        } catch (Exception ex) {
            throw new ServletException(ex);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    @RequestMapping(value = PREFIX + "/testConnection", method = RequestMethod.POST)
    public Map<String, Object> testConnection(@RequestBody DataSourceReq req) {
        Connection conn = null;
        Map<String, Object> map = new HashMap<>();
        try {
            Class.forName(req.getDriver());
            conn = DriverManager.getConnection(req.getUrl(), req.getUsername(), req.getPassword());
            map.put("result", true);
        } catch (Exception ex) {
            map.put("error", ex.toString());
            map.put("result", false);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return map;
    }

    private Connection buildConnection(DataSourceReq req) throws Exception {
        if ("jdbc".equals(req.getType())) {

            Class.forName(req.getDriver());
            return DriverManager.getConnection(req.getUrl(), req.getUsername(), req.getPassword());
        } else {
            Connection conn = Utils.getBuildinConnection(req.getName());
            if (conn == null) {
                throw new ReportDesignException("Buildin datasource [" + req.getName() + "] not exist.");
            }
            return conn;
        }
    }

    protected PreparedStatementCreator getPreparedStatementCreator(String sql, SqlParameterSource paramSource) {
        ParsedSql parsedSql = NamedParameterUtils.parseSqlStatement(sql);
        String sqlToUse = NamedParameterUtils.substituteNamedParameters(parsedSql, paramSource);
        Object[] params = NamedParameterUtils.buildValueArray(parsedSql, paramSource, null);
        List<SqlParameter> declaredParameters = NamedParameterUtils.buildSqlParameterList(parsedSql, paramSource);
        PreparedStatementCreatorFactory pscf = new PreparedStatementCreatorFactory(sqlToUse, declaredParameters);
        return pscf.newPreparedStatementCreator(params);
    }

    private String parseSql(String sql, Map<String, Object> parameters) {
        sql = sql.trim();
        Context context = new Context(applicationContext, parameters);
        if (sql.startsWith(ExpressionUtils.EXPR_PREFIX) && sql.endsWith(ExpressionUtils.EXPR_SUFFIX)) {
            sql = sql.substring(2, sql.length() - 1);
            Expression expr = ExpressionUtils.parseExpression(sql);
            sql = SQLUtils.executeSqlExpr(expr, context);
            return sql;
        } else {
            String sqlForUse = sql;
            sqlForUse = SqlDatasetDefinition.sqlHandler(context, sqlForUse);
            Utils.logToConsole("DESIGN SQL:" + sqlForUse);
            return sqlForUse;
        }
    }


    private Map<String, Object> buildParameters(String parameters) throws JsonProcessingException {
        Map<String, Object> map = new HashMap<>();
        if (StringUtils.isBlank(parameters)) {
            return map;
        }
        List<Map<String, Object>> list = objectMapper.readValue(parameters, new TypeReference<List<Map<String, Object>>>() {
        });
        for (Map<String, Object> param : list) {
            String name = param.get("name").toString();
            DataType type = DataType.valueOf(param.get("type").toString());
            String defaultValue = (String) param.get("defaultValue");
            Object ob;
            if (StringUtils.isBlank(defaultValue)) {
                switch (type) {
                    case Boolean:
                        ob = false;
                        break;
                    case Date:
                        ob = new Date();
                        break;
                    case Float:
                        ob = 0f;
                        break;
                    case Integer:
                        ob = 0;
                        break;
                    case String:
                        ob = "";
                        break;
                    case List:
                        ob = Collections.emptyList();
                        break;
                    default:
                        ob = "null";
                }
            } else {
                ob = type.parse(defaultValue);
            }
            map.put(name, ob);
        }
        return map;
    }


}
