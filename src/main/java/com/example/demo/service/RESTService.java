package com.example.demo.service;

import com.example.demo.domain.ExporterTask;
import io.camunda.common.auth.*;
import io.camunda.operate.CamundaOperateClient;
import io.camunda.operate.exception.OperateException;
import io.camunda.operate.model.Variable;
import io.camunda.operate.search.Filter;
import io.camunda.operate.search.SearchQuery;
import io.camunda.operate.search.VariableFilter;
import io.camunda.tasklist.CamundaTaskListClient;
import io.camunda.tasklist.dto.*;
import io.camunda.tasklist.exception.TaskListException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class RESTService {
    public static void main(String args[]) throws TaskListException, OperateException {
        System.out.println("DemoExporter configure ");
        String dbURL = "jdbc:sqlserver://localhost:1433;databaseName=camundadb;encrypt=true;trustServerCertificate=true";
        ;

        String username = "camundadb";
        String password = "camunda";


        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            Connection conn = DriverManager.getConnection(dbURL, username, password);

            if (conn != null) {
                System.out.println("Connected");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }
    public static ExporterTask getTaskInformation(Long processInstanceKey) throws TaskListException, OperateException {
        ExporterTask exporterTask=new ExporterTask();
        SimpleConfig simpleConf = new SimpleConfig();
        simpleConf.addProduct(Product.TASKLIST, new SimpleCredential("http://localhost:7082", "demo", "demo"));
        Authentication auth = SimpleAuthentication.builder().withSimpleConfig(simpleConf).build();

        CamundaTaskListClient client = CamundaTaskListClient.builder()
                .taskListUrl("http://localhost:7082")
                .authentication(auth)
                .cookieExpiration(Duration.ofSeconds(5))
                .build();
        System.out.println("client::"+client);
        TaskSearch taskSearch =new TaskSearch();
        taskSearch.setState(TaskState.CREATED);
        taskSearch.setProcessInstanceKey(String.valueOf(processInstanceKey));
        TaskList taskList=client.getTasks(taskSearch);

        for (Task task:taskList){
            System.out.println("task Id::"+task.getId());
            System.out.println("process Instance Key::"+task.getProcessInstanceKey());
            System.out.println("task Name::"+task.getName());
            System.out.println("task Definition Id::"+task.getTaskDefinitionId());
            System.out.println("task Creation Date::"+task.getCreationDate());
            System.out.println("task Completion  Date::"+task.getCompletionDate());
            System.out.println("task State::"+task.getTaskState());
            System.out.println("assignee user::"+task.getAssignee());
            exporterTask.setTaskId(Long.valueOf(task.getId()));
            exporterTask.setProcessInstanceKey(Long.valueOf(task.getProcessInstanceKey()));
            exporterTask.setName(task.getName());
            exporterTask.setTaskDefinitionId(task.getTaskDefinitionId());
            OffsetDateTime offsetCreatedDateTime = OffsetDateTime.parse(task.getCreationDate(),
                    DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ"));
            exporterTask.setCreatedDate(offsetCreatedDateTime.toLocalDate());
            OffsetDateTime offsetCompletedDateTime = OffsetDateTime.parse(task.getCreationDate(),
                    DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ"));
            exporterTask.setCompletedDate(offsetCompletedDateTime.toLocalDate());
            exporterTask.setTaskState(TaskState.CREATED.getRawValue());
            exporterTask.setAssigneeUser(task.getAssignee());

        }
        return exporterTask;
    }
    public static void getOperateInformation(String processInstanceKey) throws OperateException {
        System.out.println("Inside getOperateInformation");
        SimpleConfig simpleConf = new SimpleConfig();
        simpleConf.addProduct(Product.OPERATE, new SimpleCredential("http://localhost:7081", "demo", "demo"));
        Authentication auth = SimpleAuthentication.builder().withSimpleConfig(simpleConf).build();

        CamundaOperateClient operateClient = CamundaOperateClient.builder()
                .operateUrl("http://localhost:7081")
                .authentication(auth)
                .build();
        SearchQuery searchQuery=new SearchQuery();
        VariableFilter filter=new VariableFilter();
        filter.setProcessInstanceKey(Long.valueOf(processInstanceKey));
        searchQuery.setFilter(filter);
        List<Variable> variable=operateClient.searchVariables(searchQuery);
        for(Variable variable1:variable) {
            System.out.println(variable1.getName() + "::" + variable1.getValue());
        }
    }
}
