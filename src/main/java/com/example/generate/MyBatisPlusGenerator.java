package com.example.generate;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.po.TableFill;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MyBatisPlusGenerator {

    // 数据库连接配置
    private static final String DB_URL = "jdbc:mysql://localhost:3306/xxx?characterEncoding=utf8&serverTimezone=UTC";
    private static final String DB_DRIVER_NAME = "com.mysql.cj.jdbc.Driver";
    private static final String DB_USERNAME = "root";
    private static final String DB_PASSWORD = "xxxxx";

    // 作者信息
    private static final String AUTHOR = "lemon";

    // 包信息
    private static final String PARENT_PACKAGE = "com.generate";
    private static final String MAPPER_PACKAGE = "mapper";
    private static final String XML_PACKAGE = "mapper";
    private static final String ENTITY_PACKAGE = "entity";
    private static final String SERVICE_PACKAGE = "service";
    private static final String SERVICE_IMPL_PACKAGE = "service.impl";
    private static final String CONTROLLER_PACKAGE = "controller";

    // 主键策略
    private static final IdType ID_TYPE = IdType.AUTO;

    // 时间类型
    private static final DateType DATE_TYPE = DateType.ONLY_DATE;

    // 是否生成基本 ResultMap 和 ColumnList
    private static final boolean BASE_RESULT_MAP = true;
    private static final boolean BASE_COLUMN_LIST = true;

    // 代码生成路径
    private static final String OUTPUT_DIR = System.getProperty("user.dir") + "/src/main/java";

    // 自动生成的服务默认前缀
    private static final String SERVICE_NAME_PREFIX = "I%sService";

    // 代码生成策略
    private static final NamingStrategy TABLE_NAMING_STRATEGY = NamingStrategy.underline_to_camel;
    private static final NamingStrategy COLUMN_NAMING_STRATEGY = NamingStrategy.underline_to_camel;

    // 是否使用 RestController 风格
    private static final boolean REST_CONTROLLER_STYLE = true;

    // 是否使用逻辑删除
    private static final String LOGIC_DELETE_FIELD_NAME = "deleted";

    // 是否使用乐观锁
    private static final String VERSION_FIELD_NAME = "version";

    // 自动填充配置
    private static final List<TableFill> TABLE_FILL_LIST = new ArrayList<>();

    static {
        TABLE_FILL_LIST.add(new TableFill("create_time", FieldFill.INSERT));
        TABLE_FILL_LIST.add(new TableFill("update_time", FieldFill.INSERT_UPDATE));
    }

    /**
     * 从控制台输入表名
     *
     * @param tip 输入提示信息
     * @return 输入的表名
     */
    public static String scanner(String tip) {
        Scanner scanner = new Scanner(System.in);
        StringBuilder help = new StringBuilder();
        help.append("请输入" + tip + "：");
        System.out.println(help.toString());
        if (scanner.hasNext()) {
            String ipt = scanner.next();
            if (StringUtils.isNotBlank(ipt)) {
                return ipt;
            }
        }
        throw new MybatisPlusException("请输入正确的" + tip + "！");
    }

    /**
     * 代码生成器主入口
     *
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        // 代码生成器
        AutoGenerator mpg = new AutoGenerator();

        // 全局配置
        GlobalConfig gc = new GlobalConfig();
        gc.setOutputDir(OUTPUT_DIR);
        gc.setFileOverride(true);
        gc.setOpen(false);
        gc.setAuthor(AUTHOR);
        gc.setIdType(ID_TYPE);
        gc.setBaseResultMap(BASE_RESULT_MAP);
        gc.setBaseColumnList(BASE_COLUMN_LIST);
        gc.setServiceName(SERVICE_NAME_PREFIX);
        gc.setDateType(DATE_TYPE);
        mpg.setGlobalConfig(gc);

        // 数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setUrl(DB_URL);
        dsc.setDriverName(DB_DRIVER_NAME);
        dsc.setUsername(DB_USERNAME);
        dsc.setPassword(DB_PASSWORD);
        mpg.setDataSource(dsc);

        // 包配置
        PackageConfig pc = new PackageConfig();
        pc.setParent(PARENT_PACKAGE);
        pc.setMapper(MAPPER_PACKAGE);
        pc.setXml(XML_PACKAGE);
        pc.setEntity(ENTITY_PACKAGE);
        pc.setService(SERVICE_PACKAGE);
        pc.setServiceImpl(SERVICE_IMPL_PACKAGE);
        pc.setController(CONTROLLER_PACKAGE);
        mpg.setPackageInfo(pc);

        // 策略配置
        StrategyConfig sc = new StrategyConfig();
        sc.setNaming(TABLE_NAMING_STRATEGY);
        sc.setColumnNaming(COLUMN_NAMING_STRATEGY);
        sc.setEntityLombokModel(true);
        sc.setRestControllerStyle(REST_CONTROLLER_STYLE);
        sc.setControllerMappingHyphenStyle(true);
        sc.setLogicDeleteFieldName(LOGIC_DELETE_FIELD_NAME);
        sc.setTableFillList(TABLE_FILL_LIST);
        sc.setVersionFieldName(VERSION_FIELD_NAME);
        sc.setInclude(scanner("表名，多个英文逗号分割").split(","));
        mpg.setStrategy(sc);

        // 生成代码
        mpg.execute();
    }
}