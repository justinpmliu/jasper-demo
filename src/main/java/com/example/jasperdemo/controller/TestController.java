package com.example.jasperdemo.controller;

import com.example.jasperdemo.mapper.TaskExecutionMapper;
import com.example.jasperdemo.model.TaskExecution;
import com.example.jasperdemo.model.User;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.InputStream;
import java.util.*;

@RestController
@RequestMapping("/exportPdf")
public class TestController {

    @Autowired
    private DataSource jdbcDataSource;

    @Autowired
    private TaskExecutionMapper taskExecutionMapper;

    /**
     * 2）带参数的导出
     * @throws Exception
     */
    @RequestMapping("/map")
    public void exportPdfWithMap(HttpServletResponse response) throws Exception{

        //1.读取.japser文件，构建输入流
        InputStream in = new ClassPathResource("jasper/test02_map.jasper").getInputStream();

        //2.构建Print对象，用于让模块结合数据
        //第二个参数就是用来填充模板中的parameters
        Map<String, Object> map = new HashMap<>();
        map.put("username","Justin");
        map.put("email","justin@qq.com");
        map.put("companyName","my company");
        map.put("deptName","my department");

        JasperPrint print = JasperFillManager.fillReport(in, map, new JREmptyDataSource());

        //3.使用Exporter导出PDF
        this.exportPdf(print, response);
    }

    /**
     * 3)使用JDBC数据源导出
     * @throws Exception
     */
    @RequestMapping("/jdbc")
    public void exportPdfWithJdbc(HttpServletResponse response) throws Exception{

        //1.读取.japser文件，构建输入流
        InputStream in = new ClassPathResource("jasper/test03_jdbc.jasper").getInputStream();

        //2.构建Print对象，用于让模块结合数据
        //第三个参数：如果是JDBC数据源，应该设置Connection对象
        JasperPrint print = JasperFillManager.fillReport(in, new HashMap<>(), jdbcDataSource.getConnection());

        //3.使用Exporter导出PDF
        this.exportPdf(print, response);

    }

    @RequestMapping("/list")
    public void exportPdfWithList(HttpServletResponse response) throws Exception {
        InputStream in = new ClassPathResource("jasper/test04_list.jasper").getInputStream();

        List<TaskExecution> taskExecutions = taskExecutionMapper.findAll();
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(taskExecutions);

        JasperPrint print = JasperFillManager.fillReport(in, new HashMap<>(), dataSource);

        this.exportPdf(print, response);
    }

    /**
     * 5)分组导出
     * @throws Exception
     */
    @RequestMapping("/group")
    public void exportPdfWithGroup(HttpServletResponse response) throws Exception{

        //1.读取.japser文件，构建输入流
        InputStream in = new ClassPathResource("jasper/test05_group.jasper").getInputStream();

        //2.构建Print对象，用于让模块结合数据
        //注意：JavaBean的属性名称和模版的Fileds的名称一致的
        List<User> list = new ArrayList<>();
        for(int i=1; i<5; i++) {
            for (int j = 1; j <= 10; j++) {
                User user = new User();
                user.setUserName("Zhangsan-" + j);
                user.setEmail("zhangsan-" + j + "@qq.com");
                user.setCompanyName("Tech-" + i);
                user.setDeptName("Dev.");
                list.add(user);
            }
        }

        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(list);

        //第三个参数：JavaBean作为数据源，使用JRBeanCollectionDataSource对象来填充
        JasperPrint print = JasperFillManager.fillReport(in,new HashMap<>(),dataSource);

        //3.使用Exporter导出PDF
        this.exportPdf(print, response);

    }

    /**
     * 6)图标导出
     * @throws Exception
     */
    @RequestMapping("/chart")
    public void exportPdfWithChart(HttpServletResponse response) throws Exception{

        //1.读取.japser文件，构建输入流
        InputStream in = new ClassPathResource("jasper/test06_chart.jasper").getInputStream();

        //2.构建Print对象，用于让模块结合数据
        //注意：JavaBean的属性名称和模版的Fileds的名称一致的
        List<Map> list = new ArrayList<>();
        for (int i = 1; i <= 6; i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("title","title-"+i);
            map.put("value", new Random().nextInt(100));
            list.add(map);
        }

        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(list);

        //第三个参数：JavaBean作为数据源，使用JRBeanCollectionDataSource对象来填充
        JasperPrint print = JasperFillManager.fillReport(in,new HashMap<>(),dataSource);

        //3.使用Exporter导出PDF
        this.exportPdf(print, response);

    }

    private void exportPdf(JasperPrint print, HttpServletResponse response) throws Exception {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/pdf");
        JasperExportManager.exportReportToPdfStream(print,response.getOutputStream());
    }

}
