package com.xxw.admin;

import com.github.springtestdbunit.DatabaseConnections;
import com.github.springtestdbunit.DbUnitRunner;
import com.github.springtestdbunit.DbUnitTestContext;
import com.github.springtestdbunit.annotation.DbUnitConfiguration;
import com.github.springtestdbunit.dataset.DataSetLoader;
import com.github.springtestdbunit.dataset.FlatXmlDataSetLoader;
import com.github.springtestdbunit.operation.DatabaseOperationLookup;
import com.github.springtestdbunit.operation.DefaultDatabaseOperationLookup;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.ext.mysql.MySqlConnection;
import org.springframework.context.ApplicationContext;
import org.springframework.core.Conventions;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.AbstractTestExecutionListener;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author xxw
 * @date 2018/11/24
 */
public class MysqlDbUnitTestExecutionListener extends AbstractTestExecutionListener {

    private static final Log logger = LogFactory.getLog(MysqlDbUnitTestExecutionListener.class);
    private static final String[] COMMON_DATABASE_CONNECTION_BEAN_NAMES = new String[]{"dbUnitDatabaseConnection", "dataSource"};
    private static final String DATA_SET_LOADER_BEAN_NAME = "dbUnitDataSetLoader";
    protected static final String CONNECTION_ATTRIBUTE = Conventions.getQualifiedAttributeName(MysqlDbUnitTestExecutionListener.class, "connection");
    protected static final String DATA_SET_LOADER_ATTRIBUTE = Conventions.getQualifiedAttributeName(MysqlDbUnitTestExecutionListener.class, "dataSetLoader");
    protected static final String DATABASE_OPERATION_LOOKUP_ATTRIBUTE = Conventions.getQualifiedAttributeName(MysqlDbUnitTestExecutionListener.class, "databseOperationLookup");
    private static DbUnitRunner runner = new DbUnitRunner();
    private static final Pattern schemaPattern = Pattern.compile("/([^.]+)\\?");

    public MysqlDbUnitTestExecutionListener() {
    }

    public void prepareTestInstance(TestContext testContext) throws Exception {
        this.prepareTestInstance(new MysqlDbUnitTestExecutionListener.DbUnitTestContextAdapter(testContext));
    }

    public void prepareTestInstance(MysqlDbUnitTestExecutionListener.DbUnitTestContextAdapter testContext) throws Exception {
        if(logger.isDebugEnabled()) {
            logger.debug("Preparing test instance " + testContext.getTestClass() + " for DBUnit");
        }

        String[] databaseConnectionBeanNames = null;
        String dataSetLoaderBeanName = null;
        Class<? extends DataSetLoader> dataSetLoaderClass = FlatXmlDataSetLoader.class;
        Class<? extends DatabaseOperationLookup> databaseOperationLookupClass = DefaultDatabaseOperationLookup.class;
        DbUnitConfiguration configuration = (DbUnitConfiguration)testContext.getTestClass().getAnnotation(DbUnitConfiguration.class);
        if(configuration != null) {
            if(logger.isDebugEnabled()) {
                logger.debug("Using @DbUnitConfiguration configuration");
            }

            databaseConnectionBeanNames = configuration.databaseConnection();
            dataSetLoaderClass = configuration.dataSetLoader();
            dataSetLoaderBeanName = configuration.dataSetLoaderBean();
            databaseOperationLookupClass = configuration.databaseOperationLookup();
        }

        if(ObjectUtils.isEmpty(databaseConnectionBeanNames) || databaseConnectionBeanNames.length == 1 && StringUtils.isEmpty(databaseConnectionBeanNames[0])) {
            databaseConnectionBeanNames = new String[]{this.getDatabaseConnectionUsingCommonBeanNames(testContext)};
        }

        if(!StringUtils.hasLength(dataSetLoaderBeanName) && testContext.getApplicationContext().containsBean("dbUnitDataSetLoader")) {
            dataSetLoaderBeanName = "dbUnitDataSetLoader";
        }

        if(logger.isDebugEnabled()) {
            logger.debug("DBUnit tests will run using databaseConnection \"" + StringUtils.arrayToCommaDelimitedString(databaseConnectionBeanNames) + "\", datasets will be loaded using " + (StringUtils.hasLength(dataSetLoaderBeanName)?"'" + dataSetLoaderBeanName + "'":dataSetLoaderClass));
        }

        this.prepareDatabaseConnection(testContext, databaseConnectionBeanNames);
        this.prepareDataSetLoader(testContext, dataSetLoaderBeanName, dataSetLoaderClass);
        this.prepareDatabaseOperationLookup(testContext, databaseOperationLookupClass);
    }

    private String getDatabaseConnectionUsingCommonBeanNames(MysqlDbUnitTestExecutionListener.DbUnitTestContextAdapter testContext) {
        String[] var2 = COMMON_DATABASE_CONNECTION_BEAN_NAMES;
        int var3 = var2.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            String beanName = var2[var4];
            if(testContext.getApplicationContext().containsBean(beanName)) {
                return beanName;
            }
        }

        throw new IllegalStateException("Unable to find a DB Unit database connection, missing one the following beans: " + Arrays.asList(COMMON_DATABASE_CONNECTION_BEAN_NAMES));
    }

    private void prepareDatabaseConnection(MysqlDbUnitTestExecutionListener.DbUnitTestContextAdapter testContext, String[] connectionBeanNames) throws Exception {
        IDatabaseConnection[] connections = new IDatabaseConnection[connectionBeanNames.length];

        for(int i = 0; i < connectionBeanNames.length; ++i) {
            Object databaseConnection = testContext.getApplicationContext().getBean(connectionBeanNames[i]);
            if(databaseConnection instanceof DataSource) {
                //修改此处将IDatabaseConnection换成MySqlConnection实例
                String jdbcUrl = ((HikariDataSource) databaseConnection).getJdbcUrl();
                Matcher matcher = schemaPattern.matcher(jdbcUrl);
                if (matcher.find()) {
                    databaseConnection = new MySqlConnection(((DataSource) databaseConnection).getConnection(), matcher.group(1));
                }
                else {
                    throw new IllegalArgumentException("spring.datasource.url未包含库名");
                }
            }

            Assert.isInstanceOf(IDatabaseConnection.class, databaseConnection);
            connections[i] = (IDatabaseConnection)databaseConnection;
        }

        testContext.setAttribute(CONNECTION_ATTRIBUTE, new DatabaseConnections(connectionBeanNames, connections));
    }

    private void prepareDataSetLoader(MysqlDbUnitTestExecutionListener.DbUnitTestContextAdapter testContext, String beanName, Class<? extends DataSetLoader> dataSetLoaderClass) {
        if(StringUtils.hasLength(beanName)) {
            testContext.setAttribute(DATA_SET_LOADER_ATTRIBUTE, testContext.getApplicationContext().getBean(beanName, DataSetLoader.class));
        } else {
            try {
                testContext.setAttribute(DATA_SET_LOADER_ATTRIBUTE, dataSetLoaderClass.newInstance());
            } catch (Exception var5) {
                throw new IllegalArgumentException("Unable to create data set loader instance for " + dataSetLoaderClass, var5);
            }
        }

    }

    private void prepareDatabaseOperationLookup(MysqlDbUnitTestExecutionListener.DbUnitTestContextAdapter testContext, Class<? extends DatabaseOperationLookup> databaseOperationLookupClass) {
        try {
            testContext.setAttribute(DATABASE_OPERATION_LOOKUP_ATTRIBUTE, databaseOperationLookupClass.newInstance());
        } catch (Exception var4) {
            throw new IllegalArgumentException("Unable to create database operation lookup instance for " + databaseOperationLookupClass, var4);
        }
    }

    public void beforeTestMethod(TestContext testContext) throws Exception {
        runner.beforeTestMethod(new MysqlDbUnitTestExecutionListener.DbUnitTestContextAdapter(testContext));
    }

    public void afterTestMethod(TestContext testContext) throws Exception {
        runner.afterTestMethod(new MysqlDbUnitTestExecutionListener.DbUnitTestContextAdapter(testContext));
    }

    private static class DbUnitTestContextAdapter implements DbUnitTestContext {
        private static final Method GET_TEST_CLASS;
        private static final Method GET_TEST_INSTANCE;
        private static final Method GET_TEST_METHOD;
        private static final Method GET_TEST_EXCEPTION;
        private static final Method GET_APPLICATION_CONTEXT;
        private static final Method GET_ATTRIBUTE;
        private static final Method SET_ATTRIBUTE;
        private TestContext testContext;

        public DbUnitTestContextAdapter(TestContext testContext) {
            this.testContext = testContext;
        }

        public DatabaseConnections getConnections() {
            return (DatabaseConnections)this.getAttribute(MysqlDbUnitTestExecutionListener.CONNECTION_ATTRIBUTE);
        }

        public DataSetLoader getDataSetLoader() {
            return (DataSetLoader)this.getAttribute(MysqlDbUnitTestExecutionListener.DATA_SET_LOADER_ATTRIBUTE);
        }

        public DatabaseOperationLookup getDatbaseOperationLookup() {
            return (DatabaseOperationLookup)this.getAttribute(MysqlDbUnitTestExecutionListener.DATABASE_OPERATION_LOOKUP_ATTRIBUTE);
        }

        public Class<?> getTestClass() {
            return (Class)ReflectionUtils.invokeMethod(GET_TEST_CLASS, this.testContext);
        }

        public Method getTestMethod() {
            return (Method)ReflectionUtils.invokeMethod(GET_TEST_METHOD, this.testContext);
        }

        public Object getTestInstance() {
            return ReflectionUtils.invokeMethod(GET_TEST_INSTANCE, this.testContext);
        }

        public Throwable getTestException() {
            return (Throwable)ReflectionUtils.invokeMethod(GET_TEST_EXCEPTION, this.testContext);
        }

        public ApplicationContext getApplicationContext() {
            return (ApplicationContext)ReflectionUtils.invokeMethod(GET_APPLICATION_CONTEXT, this.testContext);
        }

        public Object getAttribute(String name) {
            return ReflectionUtils.invokeMethod(GET_ATTRIBUTE, this.testContext, new Object[]{name});
        }

        public void setAttribute(String name, Object value) {
            ReflectionUtils.invokeMethod(SET_ATTRIBUTE, this.testContext, new Object[]{name, value});
        }

        static {
            try {
                GET_TEST_CLASS = TestContext.class.getMethod("getTestClass", new Class[0]);
                GET_TEST_INSTANCE = TestContext.class.getMethod("getTestInstance", new Class[0]);
                GET_TEST_METHOD = TestContext.class.getMethod("getTestMethod", new Class[0]);
                GET_TEST_EXCEPTION = TestContext.class.getMethod("getTestException", new Class[0]);
                GET_APPLICATION_CONTEXT = TestContext.class.getMethod("getApplicationContext", new Class[0]);
                GET_ATTRIBUTE = TestContext.class.getMethod("getAttribute", new Class[]{String.class});
                SET_ATTRIBUTE = TestContext.class.getMethod("setAttribute", new Class[]{String.class, Object.class});
            } catch (Exception var1) {
                throw new IllegalStateException(var1);
            }
        }
    }

    /**
     * UPDATE：将数据集中的内容更新到数据库中，它假设数据库中已经有对应的记录，否则将失败
     * INSERT：将数据集中的内容插入到数据库中，它假设数据库中没有对应的记录，否则将失败
     * REFRESH：将数据集中的内容刷新到数据库中，如果数据库有对应的记录，则更新，没有则插入
     * DELETE：删除数据库中与数据集对应的记录
     * DELETE_ALL：删除表中所有的记录，如果没有对应的表，则不受影响
     * TRUNCATE_TABLE：与DELETE_ALL类似，更轻量级，不能rollback
     * CLEAN_INSERT：是一个组合操作，是DELETE_ALL和INSERT的组合
     */
}
